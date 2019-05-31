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
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.TimeoutException;

/**
 * @author Florin Bogdan Balint
 *
 */
public abstract class DefaultResourceService {

    private static final Logger LOGGER = LogManager.getLogger(DefaultResourceService.class);

    private PersonDAO           personDAO;

    public DefaultResourceService(Session session) {
        this.personDAO = new PersonDAOImpl(session);
    }

    public OnlineUserDBO validateSession(String jsonWebToken)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
        if (StringUtil.isNullOrBlank(jsonWebToken)) {
            throw new NotAuthorizedException();
        }

        String sessionToken = extractSessionToken(jsonWebToken);
        OnlineUserDBO onlineUser = personDAO.getOnlineUser(sessionToken);
        if (onlineUser == null) {
            throw new NotAuthorizedException();
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
            throw new TimeoutException();
        }

        return onlineUser;
    }

    public static String extractSessionToken(String jsonWebToken)
        throws InvalidTokenException {
        if (StringUtil.isNullOrBlank(jsonWebToken)) {
            throw new InvalidTokenException("Invalid token");
        }
        if (jsonWebToken.indexOf(".") < 1 || jsonWebToken.lastIndexOf(".") < 1) {
            throw new InvalidTokenException("Invalid token");
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
            throw new InvalidTokenException("Invalid token");
        }
    }

    /**
     * @return the personDAO
     */
    public PersonDAO getPersonDAO() {
        return personDAO;
    }

}
