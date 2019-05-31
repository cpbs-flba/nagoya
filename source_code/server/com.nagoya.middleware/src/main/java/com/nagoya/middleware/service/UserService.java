/*******************************************************************************
 * Copyright (c) 2004 - 2019 CPB Software AG
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 *
 * This software is published under the Apache License, Version 2.0, January 2004, 
 * http://www.apache.org/licenses/
 *  
 * Author: Florin Bogdan Balint
 *******************************************************************************/

package com.nagoya.middleware.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.common.crypto.AESEncryptionProvider;
import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.common.util.DefaultDateProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.DefaultDBOFiller;
import com.nagoya.middleware.rest.bl.UserRESTResource;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.person.PersonKeysDBO;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.person.PersonTO;
import com.nagoya.model.to.person.PersonTransformer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * @author flba
 *
 */
public class UserService extends DefaultResourceService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private PersonDAO           personDAO;

    public UserService(Session session) {
        super(session);
        this.personDAO = new PersonDAOImpl(session);
    }

    public DefaultResponse login(PersonTO personTO)
        throws NotAuthorizedException, BadRequestException, ConflictException, ForbiddenException, InvalidObjectException,
        ResourceOutOfDateException {
        validateCredentialsExist(personTO);

        String email = personTO.getEmail();

        com.nagoya.model.dbo.person.PersonDBO existentPerson = personDAO.findPersonForEmail(email.toLowerCase());
        if (existentPerson == null) {
            throw new NotAuthorizedException("Invalid email/password combination.");
        }

        String enteredPassword = personTO.getPassword();
        boolean passwordCorrect = DefaultPasswordEncryptionProvider.isPasswordCorrect(existentPerson.getPassword(), enteredPassword);
        if (!passwordCorrect) {
            throw new NotAuthorizedException("Invalid email/password combination.");
        }

        boolean emailConfirmed = existentPerson.isEmailConfirmed();
        if (!emailConfirmed) {
            throw new ForbiddenException();
        }

        // login was okay, create the session and the online user
        OnlineUserDBO onlineUserDBO = createSession(existentPerson);

        if (existentPerson.isStorePrivateKey()) {
            Set<PersonKeysDBO> keys = existentPerson.getKeys();
            for (PersonKeysDBO personKeysDBO : keys) {
                String privateKey = personKeysDBO.getPrivateKey();
                if (StringUtil.isNullOrBlank(privateKey)) {
                    continue;
                }

                String decryptedBlockchainPrivateKey = AESEncryptionProvider.decrypt(privateKey, enteredPassword);
                if (StringUtil.isNotNullOrBlank(decryptedBlockchainPrivateKey)) {
                    // this is the private key for the blockchain, we need to save it to the session
                    String encryptedBlockchainPrivateKey = AESEncryptionProvider.encrypt(decryptedBlockchainPrivateKey,
                        onlineUserDBO.getPrivateKey());
                    onlineUserDBO.setBlockchainKey(encryptedBlockchainPrivateKey);
                    personDAO.update(onlineUserDBO, true);
                }
            }
        }

        // login was okay, return the user object

        PersonTO dto = PersonTransformer.getDTO(existentPerson);
        // hide the password hash
        dto.setPassword(null);
        DefaultResponse result = new DefaultResponse();
        result.setEntity(dto);

        // also set the JSON web token in the bearer authorization
        String headerValue = UserRESTResource.HEADER_AUTHORIZATION_BEARER + onlineUserDBO.getJsonWebToken();
        result.getHeader().put(UserRESTResource.HEADER_AUTHORIZATION, headerValue);

        return result;
    }

    public void register(PersonTO person, String language)
        throws BadRequestException, ConflictException {
        validate(person);
        com.nagoya.model.dbo.person.PersonDBO toRegister = PersonTransformer.getDBO(null, person);

        // encrypt password
        String plainPassword = toRegister.getPassword();
        String encryptPassword = DefaultPasswordEncryptionProvider.encryptPassword(plainPassword);
        toRegister.setPassword(encryptPassword);

        // generate and store the keys only if the person wants it
        // otherwise they are generated upon account confirmation
        if (toRegister.isStorePrivateKey()) {
            BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
            Credentials credentials = blockchainDriver.createCredentials();

            PersonKeysDBO pk = new PersonKeysDBO();
            DefaultDBOFiller.fillDefaultDataObjectValues(pk);
            pk.setPublicKey(credentials.getPublicKey());
            String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), plainPassword);
            pk.setPrivateKey(privateKeyEncrypted);
            pk.setActive(true);
            toRegister.getKeys().add(pk);
        }

        // persist into the DB
        com.nagoya.model.dbo.person.PersonDBO registered = personDAO.register(toRegister);

        UserRequestDBO userRequest = new UserRequestDBO();
        userRequest.setPerson(registered);
        userRequest.setRequestType(RequestType.REGISTRATION);
        Date expirationDate = DefaultDateProvider.getDeadline24h();
        userRequest.setExpirationDate(expirationDate);
        String registrationToken = DefaultIDGenerator.generateRandomID();
        userRequest.setToken(registrationToken);
        personDAO.insert(userRequest, true);

        // send confirmation e-mail
        String email = person.getEmail();
        MailService mailService = new MailService(language);
        mailService.sendRegistratationMail(email, registrationToken, expirationDate);
    }

    public void resetPassword(PersonTO person, String language)
        throws BadRequestException, ConflictException {
        if (person == null) {
            throw new BadRequestException();
        }
        String providedEmail = person.getEmail();
        if (StringUtil.isNullOrBlank(providedEmail)) {
            throw new BadRequestException();
        }
        com.nagoya.model.dbo.person.PersonDBO personForMail = personDAO.findPersonForEmail(providedEmail);
        if (personForMail == null) {
            return;
        }

        // we have a valid user
        UserRequestDBO userRequest = new UserRequestDBO();
        userRequest.setPerson(personForMail);
        userRequest.setRequestType(RequestType.PASSWORD_RECOVERY);
        Date expirationDate = DefaultDateProvider.getDeadline24h();
        userRequest.setExpirationDate(expirationDate);
        String token = DefaultIDGenerator.generateRandomID();
        userRequest.setToken(token);
        personDAO.insert(userRequest, true);

        // send confirmation e-mail
        String email = person.getEmail();
        MailService mailService = new MailService(language);
        mailService.sendPasswordResetMail(email, token, expirationDate);
    }

    public DefaultResponse confirmRequest(String token, PersonTO personTO)
        throws TimeoutException, BadRequestException, ConflictException, InvalidObjectException, ResourceOutOfDateException {

        if (StringUtil.isNullOrBlank(token)) {
            throw new BadRequestException();
        }

        UserRequestDBO userRequest = personDAO.findUserRequest(token);
        if (userRequest == null) {
            throw new BadRequestException();
        }

        // we found the request, now we have to delete it
        com.nagoya.model.dbo.person.PersonDBO person = userRequest.getPerson();
        personDAO.delete(userRequest, true);

        Date expirationDate = userRequest.getExpirationDate();
        Date currentDate = Calendar.getInstance().getTime();

        // if the user request expired, then we are done here
        if (expirationDate.before(currentDate)) {
            throw new TimeoutException();
        }

        // handle user registration request
        if (userRequest.getRequestType().equals(RequestType.REGISTRATION)) {
            // set the email confirmation flag
            person.setEmailConfirmed(true);
            BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
            Credentials credentials = blockchainDriver.createCredentials();
            if (person.isStorePrivateKey() == false) {

                PersonKeysDBO pk = new PersonKeysDBO();
                DefaultDBOFiller.fillDefaultDataObjectValues(pk);
                pk.setActive(true);
                pk.setPublicKey(credentials.getPublicKey());
                person.getKeys().add(pk);
            }

            personDAO.update(person, true);

            PersonTO dto = PersonTransformer.getDTO(person);
            // hide the password hash
            dto.setPassword(null);
            if (person.isStorePrivateKey() == false) {
                dto.getKeys().iterator().next().setPrivateKey(credentials.getPrivateKey());
            } else {
                dto.getKeys().iterator().next().setPrivateKey(null);
            }
            DefaultResponse result = new DefaultResponse();
            result.setEntity(dto);

            return result;
        }

        // handle the account removal request
        if (userRequest.getRequestType().equals(RequestType.ACCOUNT_REMOVAL)) {
            personDAO.delete(person);
            return null;
        }

        // handle password recovery request
        if (userRequest.getRequestType().equals(RequestType.PASSWORD_RECOVERY)) {
            if (personTO == null) {
                throw new BadRequestException();
            }

            String newEnteredPassword = personTO.getPassword();
            String newEncryptedPassword = DefaultPasswordEncryptionProvider.encryptPassword(newEnteredPassword);
            person.setPassword(newEncryptedPassword);

            // we need new credentials for the blockchain and encrypt them with the new password
            if (person.isStorePrivateKey()) {
                // deactivate all old keys
                for (PersonKeysDBO keyPair : person.getKeys()) {
                    keyPair.setActive(false);
                }

                BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
                Credentials credentials = blockchainDriver.createCredentials();

                PersonKeysDBO pk = new PersonKeysDBO();
                DefaultDBOFiller.fillDefaultDataObjectValues(pk);
                pk.setActive(true);
                pk.setPublicKey(credentials.getPublicKey());
                String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), newEnteredPassword);
                pk.setPrivateKey(privateKeyEncrypted);
                person.getKeys().add(pk);
            }
            personDAO.update(person, true);

            // hide the password hash
            PersonTO dto = PersonTransformer.getDTO(person);
            dto.setPassword(null);
            DefaultResponse result = new DefaultResponse();
            result.setEntity(dto);
            return result;
        }

        personDAO.delete(userRequest, true);

        return null;
    }

    private OnlineUserDBO createSession(com.nagoya.model.dbo.person.PersonDBO person) {
        // step 1: create new session
        String sessionToken = DefaultIDGenerator.generateRandomID();
        LOGGER.debug("Created token: " + sessionToken);
        Key key = MacProvider.generateKey();
        String privateKey = new String(Base64.getEncoder().encode(key.getEncoded()), StandardCharsets.UTF_8);

        // append the deadline to the token
        Date deadline = DefaultDateProvider.getDeadline(10);
        long time = deadline.getTime();
        Map<String, Object> claims = new HashMap<>();
        claims.put("deadline", time);

        String jsonWebToken = Jwts.builder().setClaims(claims).setSubject(sessionToken).signWith(SignatureAlgorithm.HS512, key).compact();

        // persist
        OnlineUserDBO onlineUser = new OnlineUserDBO();
        onlineUser.setPerson(person);
        onlineUser.setExpirationDate(deadline);
        onlineUser.setPrivateKey(privateKey);
        onlineUser.setSessionToken(sessionToken);
        onlineUser.setJsonWebToken(jsonWebToken);

        personDAO.removeOldSessions(person);
        personDAO.insert(onlineUser, true);

        return onlineUser;
    }

    public static String checkSignature(String token, String privateKey)
        throws BadRequestException {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(privateKeyBytes).parseClaimsJws(token);
            String subject = parseClaimsJws.getBody().getSubject();
            LOGGER.debug("Extracted subject:\r\n" + subject);
            return subject;
        } catch (Exception e) {
            throw new BadRequestException("Signature not ok.");
        }
    }

    private void validate(PersonTO personTO)
        throws BadRequestException {
        validateCredentialsExist(personTO);

        if (personTO.getPersonType() == null) {
            throw new BadRequestException();
        }
    }

    private void validateCredentialsExist(PersonTO personTO)
        throws BadRequestException {
        if (personTO == null) {
            throw new BadRequestException();
        }

        String email = personTO.getEmail();
        if (StringUtil.isNullOrBlank(email)) {
            throw new BadRequestException();
        }

        String password = personTO.getPassword();
        if (StringUtil.isNullOrBlank(password)) {
            throw new BadRequestException();
        }
    }

    public void logout(String authorization)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
        OnlineUserDBO onlineUser = validateSession(authorization);
        try {
            personDAO.delete(onlineUser, true);
        } catch (InvalidObjectException e) {
            LOGGER.debug("Could not delete a user session - maybe it was already deleted.");
        }
    }

}
