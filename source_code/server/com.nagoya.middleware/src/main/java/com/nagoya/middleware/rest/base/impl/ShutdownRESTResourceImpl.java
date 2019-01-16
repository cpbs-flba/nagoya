/**
 * (c) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : flba
 */

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
