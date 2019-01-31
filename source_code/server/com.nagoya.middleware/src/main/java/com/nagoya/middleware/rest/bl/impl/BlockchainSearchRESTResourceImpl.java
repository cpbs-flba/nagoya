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

package com.nagoya.middleware.rest.bl.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.middleware.rest.bl.BlockchainSearchRESTResource;
import com.nagoya.middleware.service.BlockchainService;
import com.nagoya.middleware.util.DefaultReturnObject;

/**
 * @author flba
 *
 */
public class BlockchainSearchRESTResourceImpl implements BlockchainSearchRESTResource {

    private static final Logger LOGGER = LogManager.getLogger(BlockchainSearchRESTResourceImpl.class);

    @Override
    public void search(String query, AsyncResponse asyncResponse) {
        Response response = null;
        try {
            BlockchainService service = new BlockchainService(null);
            DefaultReturnObject result = service.search(query);
            ResponseBuilder responseBuilder = null;
            if (result.getEntity() == null) {
                responseBuilder = Response.noContent();
            } else {
                responseBuilder = Response.ok(result.getEntity());
            }
            response = responseBuilder.build();
        } catch (Exception e) {
            LOGGER.error(e);
            response = Response.serverError().build();
        }
        asyncResponse.resume(response);
    }

}
