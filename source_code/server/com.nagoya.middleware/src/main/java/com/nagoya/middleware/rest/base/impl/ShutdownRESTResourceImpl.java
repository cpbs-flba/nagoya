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

package com.nagoya.middleware.rest.base.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.middleware.main.JettyStopper;
import com.nagoya.middleware.rest.base.ShutdownRESTResource;

/**
 * @author flba
 *
 */
public class ShutdownRESTResourceImpl implements ShutdownRESTResource {

    private static final Logger LOGGER = LogManager.getLogger(PingRESTResourceImpl.class);

    @Override
    public void shutdown(@Suspended final AsyncResponse asyncResponse) {
        LOGGER.info("Shutdown request received successfully.");
        Response response;
        try {
            new JettyStopper().start();
            response = Response.noContent().build();
        } catch (Exception e) {
            LOGGER.error(e, e.getCause());
            response = Response.serverError().build();
        } finally {
        }
        asyncResponse.resume(response);
    }
}
