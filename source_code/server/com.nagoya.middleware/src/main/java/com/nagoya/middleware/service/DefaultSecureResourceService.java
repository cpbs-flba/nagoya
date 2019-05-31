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

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagoya.common.util.DefaultDateProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.middleware.rest.bl.UserRESTResource;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonKeysDBO;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public abstract class DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(DefaultSecureResourceService.class);

    private String              authorization;
    private PersonDAO           personDAO;
    private GeneticResourceDAO  geneticResourceDAO;
    private Session             session;
    private OnlineUserDBO       onlineUser;
    private MailService         mailService;

    public DefaultSecureResourceService(String authorization, String language) {
        this.authorization = authorization;
        this.mailService = new MailService(language);
    }

    /**
     * Implement this method. This is called by the runService operation which provides the necessary framework.
     * 
     * @param params
     * @throws BusinessLogicException
     * @throws InternalException
     */
    protected abstract DefaultResponse runOperation(Object... params)
        throws BusinessLogicException;

    /**
     * This method is called on the upper side
     * 
     * @param params
     * @return
     */
    public Response runService(Object... params) {
        DefaultResponse defaultResponse = null;
        Response response = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            this.personDAO = new PersonDAOImpl(session);
            this.geneticResourceDAO = new GeneticResourceDAOImpl(session);

            // check the web token
            onlineUser = validateSession(authorization);

            // run operation
            defaultResponse = runOperation(params);
            if (defaultResponse == null) {
                // if we have no result at all, return 204 no content
                defaultResponse = new DefaultResponse(204);
            }

            // refresh the session
            refreshSession(onlineUser, defaultResponse);

            // set the header param
            String jsonWebToken = onlineUser.getJsonWebToken();
            defaultResponse.getHeader().put(UserRESTResource.HEADER_AUTHORIZATION, UserRESTResource.HEADER_AUTHORIZATION_BEARER + jsonWebToken);
        } catch (BusinessLogicException e) {
            LOGGER.debug(e, e);
            defaultResponse = new DefaultResponse(e.getStatusCode());
            defaultResponse.setEntity(e.getErrors());
        } catch (Exception e) {
            LOGGER.error(e, e);
            response = Response.serverError().build();
        } finally {
            if (session != null) {
                ConnectionProvider.getInstance().closeSession(session);
            }
        }

        response = DefaultResponseFactory.createResponse(defaultResponse);
        LOGGER.debug("Return code: " + response.getStatus());
        return response;
    }

    protected OnlineUserDBO validateSession(String jsonWebToken)
        throws BusinessLogicException, ConflictException {
        if (StringUtil.isNullOrBlank(jsonWebToken)) {
            throw new BusinessLogicException(400, "E400_TOKEN_MISSING", "JSON web token is missing");
        }

        String sessionToken = extractSessionToken(jsonWebToken);
        OnlineUserDBO onlineUser = personDAO.getOnlineUser(sessionToken);
        if (onlineUser == null) {
            throw new BusinessLogicException(401, "E401_UNAUTHORIZED", "Not authorized");
        }

        // if the session expired, remove the object
        Date expirationDate = onlineUser.getExpirationDate();
        Date currentDate = Calendar.getInstance().getTime();
        if (currentDate.after(expirationDate)) {
            try {
                // always delete the old/expired session
                personDAO.delete(onlineUser, true);
            } catch (InvalidObjectException e) {
                LOGGER.error(e, e);
            }
            // then throw an exception
            throw new BusinessLogicException(419, "E419_AUTHENTICATION_TIMEOUT", "Session timeout.");
        }

        return onlineUser;
    }

    public String extractSessionToken(String jsonWebToken)
        throws BusinessLogicException {
        if (StringUtil.isNullOrBlank(jsonWebToken)) {
            throw new BusinessLogicException(400, "E400_TOKEN_INVALID", "Invalid JSON web token.");
        }
        if (jsonWebToken.indexOf(".") < 1 || jsonWebToken.lastIndexOf(".") < 1) {
            throw new BusinessLogicException(400, "E400_TOKEN_INVALID", "Invalid JSON web token.");
        }
        String token = jsonWebToken.substring(jsonWebToken.indexOf(".") + 1, jsonWebToken.lastIndexOf("."));
        byte[] decode = Base64.getDecoder().decode(token);
        String json = new String(decode);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode actualObj = mapper.readTree(json);
            String sessionToken = actualObj.get("sub").asText();
            return sessionToken;
        } catch (IOException e) {
            throw new BusinessLogicException(400, "E400_TOKEN_INVALID", "Invalid JSON web token.");
        }
    }

    /**
     * Refreshes the session token for a logged in user.
     * 
     * @param onlineUser
     * @param responseEntity
     * @param header
     * @return
     * @throws InvalidObjectException
     * @throws ResourceOutOfDateException
     */
    protected void refreshSession(OnlineUserDBO onlineUser, DefaultResponse defaultResponse)
        throws InvalidObjectException, ResourceOutOfDateException {
        // refresh the token and retrieve it
        updateSession(onlineUser);
    }

    /**
     * Refreshes the session of the current user and returns the new json web token, that has to be returned to the client.
     * 
     * @param onlineUser
     * @return
     * @throws InvalidObjectException
     * @throws ResourceOutOfDateException
     */
    protected String updateSession(OnlineUserDBO onlineUser)
        throws InvalidObjectException, ResourceOutOfDateException {

        if (onlineUser == null) {
            return null;
        }
        if (onlineUser.getPerson() == null) {
            return null;
        }

        // step 1: get the secret key or create a new one
        Key secretKey = null;

        String privateKey = onlineUser.getPrivateKey();
        if (StringUtil.isNullOrBlank(privateKey)) {
            secretKey = MacProvider.generateKey();
            privateKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } else {
            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey);
            secretKey = new SecretKeySpec(decodedPrivateKey, SignatureAlgorithm.HS512.getJcaName());
        }

        // append the deadline to the token
        // default deadline is 30 minutes
        Date deadline = DefaultDateProvider.getDeadline(60);
        long time = deadline.getTime();
        Map<String, Object> claims = new HashMap<>();
        claims.put("deadline", time);

        // the session token is always random
        String sessionToken = DefaultIDGenerator.generateRandomID();
        LOGGER.debug("Created token: " + sessionToken);
        String jsonWebToken = Jwts.builder().setClaims(claims).setSubject(sessionToken).signWith(SignatureAlgorithm.HS512, secretKey).compact();

        // persist
        onlineUser.setExpirationDate(deadline);
        onlineUser.setPrivateKey(privateKey);
        onlineUser.setSessionToken(sessionToken);
        onlineUser.setJsonWebToken(jsonWebToken);
        personDAO.update(onlineUser, true);

        return jsonWebToken;
    }

    public Credentials getValidCredentials(PersonDBO person) {
        Credentials result = null;
        Set<PersonKeysDBO> keyPairs = person.getKeys();
        for (PersonKeysDBO keyPair : keyPairs) {
            if (Boolean.TRUE.equals(keyPair.getActive())) {
                result = new Credentials();
                result.setPublicKey(keyPair.getPublicKey());
                break;
            }
        }
        return result;
    }

    /**
     * @return the personDAO
     */
    public PersonDAO getPersonDAO() {
        return personDAO;
    }

    /**
     * @return the geneticResourceDAO
     */
    public GeneticResourceDAO getGeneticResourceDAO() {
        return geneticResourceDAO;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @return the onlineUser
     */
    public OnlineUserDBO getOnlineUser() {
        return onlineUser;
    }

    /**
     * @return the mailService
     */
    public MailService getMailService() {
        return mailService;
    }

}
