/*
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.middleware.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.blockchain.api.Credentials;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.common.crypto.AESEncryptionProvider;
import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.DefaultDBOFiller;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.rest.bl.UserRESTResource;
import com.nagoya.middleware.util.DefaultDateProvider;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.person.PersonKeysDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
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
public class UserService extends ResourceService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    // private BlockchainHelper blockchainHelper;
    private PersonDAO           personDAO;

    public UserService(Session session) {
        super(session);
        this.personDAO = new PersonDAOImpl(session);
        // this.blockchainHelper = new BlockchainHelper();
    }

    public DefaultReturnObject login(PersonTO personTO)
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
        DefaultReturnObject result = new DefaultReturnObject();
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
            BlockchainDriver bl = new BlockchainDriverImpl();
            Credentials credentials = bl.createCredentials();

            PersonKeysDBO pk = new PersonKeysDBO();
            DefaultDBOFiller.fillDefaultDataObjectValues(pk);
            pk.setPublicKey(credentials.getPublicKey());
            String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), plainPassword);
            pk.setPrivateKey(privateKeyEncrypted);
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

    public DefaultReturnObject confirmRequest(String token, PersonTO personTO)
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
            BlockchainDriver bl = new BlockchainDriverImpl();
            Credentials credentials = bl.createCredentials();
            if (person.isStorePrivateKey() == false) {

                PersonKeysDBO pk = new PersonKeysDBO();
                DefaultDBOFiller.fillDefaultDataObjectValues(pk);
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
            DefaultReturnObject result = new DefaultReturnObject();
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
                BlockchainDriver bl = new BlockchainDriverImpl();
                Credentials credentials = bl.createCredentials();

                PersonKeysDBO pk = new PersonKeysDBO();
                DefaultDBOFiller.fillDefaultDataObjectValues(pk);
                pk.setPublicKey(credentials.getPublicKey());
                String privateKeyEncrypted = AESEncryptionProvider.encrypt(credentials.getPrivateKey(), newEnteredPassword);
                pk.setPrivateKey(privateKeyEncrypted);
                person.getKeys().add(pk);
            }
            personDAO.update(person, true);

            // hide the password hash
            PersonTO dto = PersonTransformer.getDTO(person);
            dto.setPassword(null);
            DefaultReturnObject result = new DefaultReturnObject();
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

    public DefaultReturnObject delete(String authorization, String language)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
        ResourceOutOfDateException {
        OnlineUserDBO onlineUser = validateSession(authorization);
        com.nagoya.model.dbo.person.PersonDBO person = onlineUser.getPerson();

        // we have a valid user
        UserRequestDBO userRequest = new UserRequestDBO();
        userRequest.setPerson(person);
        userRequest.setRequestType(RequestType.ACCOUNT_REMOVAL);
        Date expirationDate = DefaultDateProvider.getDeadline24h();
        userRequest.setExpirationDate(expirationDate);
        String token = DefaultIDGenerator.generateRandomID();
        userRequest.setToken(token);
        personDAO.insert(userRequest, true);

        // send confirmation e-mail
        String email = person.getEmail();
        MailService mailService = new MailService(language);
        mailService.sendDeleteAccountMail(email, token, expirationDate);

        // also set the JSON web token in the bearer authorization
        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject update(String authorization, String language, PersonTO personTO)
        throws NotAuthorizedException, ConflictException, TimeoutException, ForbiddenException, InvalidObjectException, ResourceOutOfDateException,
        InvalidTokenException {
        // validate the session token
        OnlineUserDBO onlineUser = validateSession(authorization);
        com.nagoya.model.dbo.person.PersonDBO foundPerson = onlineUser.getPerson();

        // all updates must be confirmed via password confirmation
        String actualPassword = foundPerson.getPassword();
        String passwordConfirmation = personTO.getPasswordConfirmation();
        if (!DefaultPasswordEncryptionProvider.isPasswordCorrect(actualPassword, passwordConfirmation)) {
            throw new ForbiddenException();
        }

        String newPassword = personTO.getPassword();
        if (StringUtil.isNotNullOrBlank(newPassword)) {
            String encryptPassword = DefaultPasswordEncryptionProvider.encryptPassword(newPassword);
            personTO.setPassword(encryptPassword);
        } else {
            personTO.setPassword(foundPerson.getPassword());
        }
        foundPerson = PersonTransformer.getDBO(foundPerson, personTO);
        personDAO.update(foundPerson, true);

        DefaultReturnObject result = new DefaultReturnObject();
        PersonTO dto = PersonTransformer.getDTO(foundPerson);
        result.setEntity(dto);
        String newToken = updateSession(onlineUser);
        String headerValue = UserRESTResource.HEADER_AUTHORIZATION_BEARER + newToken;
        result.getHeader().put(UserRESTResource.HEADER_AUTHORIZATION, headerValue);
        return result;
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

    public DefaultReturnObject search(String authorization, String personFilter)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException, ResourceOutOfDateException,
        BadRequestException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(personFilter)) {
            throw new BadRequestException("the filter must contain at least one character.");
        }

        List<com.nagoya.model.to.person.PersonTO> resultsTO = new ArrayList<>();
        List<PersonNaturalDBO> searchNatural = personDAO.searchNatural(personFilter, 10);
        for (PersonNaturalDBO personDBO : searchNatural) {
            PersonTO dto = PersonTransformer.getDTO(personDBO);
            resultsTO.add(dto);
        }

        List<PersonLegalDBO> searchLegal = personDAO.searchLegal(personFilter, 10);
        for (PersonLegalDBO personDBO : searchLegal) {
            PersonTO dto = PersonTransformer.getDTO(personDBO);
            resultsTO.add(dto);
        }

        DefaultReturnObject result = refreshSession(onlineUser, resultsTO, null);
        return result;
    }

}
