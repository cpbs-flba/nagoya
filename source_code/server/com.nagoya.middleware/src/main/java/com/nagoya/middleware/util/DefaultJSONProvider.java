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

package com.nagoya.middleware.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class that can transform a POJO (i.e. Plain Old Java Object) into a JSON (i.e. JavaScript Object Notation).
 * 
 * @author flba
 *
 */
public final class DefaultJSONProvider {

    private static final Logger LOGGER = LogManager.getLogger(DefaultJSONProvider.class);

    private DefaultJSONProvider() {
        // Utility classes should prevent instantiation
    }

    /**
     * Returns the POJO representation of a given object.
     * 
     * @param object
     * @return String containing the JSON
     */
    public static String getObjectAsJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String objectAsJson = objectMapper.writeValueAsString(object);
            return objectAsJson;
        } catch (IOException e) {
            LOGGER.error("Could not transform object to JSON", e);
        }
        return null;
    }

    /**
     * Transforms a given JSON object into the specified class. Note: in case of any error NULL is returned.
     * 
     * @param json
     * @param clazz
     * @return The specified object.
     */
    public static Object getObjectFromJson(String json, Class<?> clazz) {
        if (json == null) {
            return null;
        }
        if (json.trim().isEmpty()) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            LOGGER.error("Could not transform JSON to object", e);
        } catch (JsonMappingException e) {
            LOGGER.error("Could not transform JSON to object", e);
        } catch (IOException e) {
            LOGGER.error("Could not transform JSON to object", e);
        }
        return null;
    }

    /**
     * Transforms a given JSON object into the specified class. Note: in case of any error NULL is returned.
     * 
     * @param json
     * @param clazz
     * @return The specified object.
     */
    public static Object getObjectFromJson(String json, TypeReference<?> typeRef) {
        if (json == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, typeRef);
        } catch (JsonParseException e) {
            LOGGER.error("Could not transform JSON to object", e);
        } catch (JsonMappingException e) {
            LOGGER.error("Could not transform JSON to object", e);
        } catch (IOException e) {
            LOGGER.error("Could not transform JSON to object", e);
        }
        return null;
    }
}
