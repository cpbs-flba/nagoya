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

import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.nagoya.middleware.util.DefaultResponse;

public final class DefaultResponseFactory {

    public static Response createResponse(DefaultResponse defaultResponse) {
        if (defaultResponse == null) {
            throw new IllegalArgumentException();
        }

        Response response = null;

        int httpStatusCode = defaultResponse.getHttpStatusCode();
        Object entity = defaultResponse.getEntity();
        if (entity != null) {
            response = Response.status(httpStatusCode).entity(entity).build();
        } else {
            response = Response.status(httpStatusCode).build();
        }

        Set<Entry<String, String>> entrySet = defaultResponse.getHeader().entrySet();
        for (Entry<String, String> entry : entrySet) {
            response.getHeaders().add(entry.getKey(), entry.getValue());
        }

        return response;
    }

}