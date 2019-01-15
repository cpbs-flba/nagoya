/**
 * 
 */

package com.nagoya.middleware.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.rest.UserResource;
import com.nagoya.middleware.util.DefaultDateProvider;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.user.OnlineUser;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * @author Florin Bogdan Balint
 *
 */
public abstract class ResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ResourceService.class);

    private PersonDAO           personDAO;

    public ResourceService(Session session) {
        this.personDAO = new PersonDAOImpl(session);
    }

    public OnlineUser validateSession(String jsonWebToken)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
        if (StringUtil.isNullOrBlank(jsonWebToken)) {
            throw new NotAuthorizedException();
        }

        String sessionToken = extractSessionToken(jsonWebToken);
        OnlineUser onlineUser = personDAO.getOnlineUser(sessionToken);
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
    public DefaultReturnObject refreshSession(OnlineUser onlineUser, Object responseEntity, Map<String, String> header)
        throws InvalidObjectException, ResourceOutOfDateException {

        DefaultReturnObject result = new DefaultReturnObject();

        // set the response object/entity
        result.setEntity(responseEntity);

        // add all the headers, in case there are any
        if (header != null) {
            result.getHeader().putAll(header);
        }

        // refresh the token and retrieve it
        String newJSONWebToken = updateSession(onlineUser);
        String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newJSONWebToken;
        result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);

        return result;
    }

    public DefaultReturnObject buildDefaultReturnObject(String authorization, Object responseEntity, Map<String, String> header)
        throws InvalidObjectException, ResourceOutOfDateException {
        DefaultReturnObject result = new DefaultReturnObject();

        // set the response object/entity
        result.setEntity(responseEntity);

        // add all the headers, in case there are any
        if (header != null) {
            result.getHeader().putAll(header);
        }

        // check the 'Bearer ' string...
        // and add if necessary
        if (!authorization.contains(UserResource.HEADER_AUTHORIZATION_BEARER)) {
            authorization = UserResource.HEADER_AUTHORIZATION_BEARER + authorization;
        }
        result.getHeader().put(UserResource.HEADER_AUTHORIZATION, authorization);

        return result;
    }

    /**
     * Refreshes the session of the current user and returns the new json web token, that has to be returned to the client.
     * 
     * @param onlineUser
     * @return
     * @throws InvalidObjectException
     * @throws ResourceOutOfDateException
     */
    public String updateSession(OnlineUser onlineUser)
        throws InvalidObjectException, ResourceOutOfDateException {
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
        onlineUser.setExpirationDate(deadline);
        onlineUser.setPrivateKey(privateKey);
        onlineUser.setSessionToken(sessionToken);
        personDAO.update(onlineUser, true);

        return jsonWebToken;
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
