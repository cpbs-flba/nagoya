/*
 * Copyright (c) 2018. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 * 
 * Author: Florin Bogdan Balint
 * 
 */
/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : flba
 */

package com.nagoya.middleware.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.jetty.servlets.CrossOriginFilter;

public class CustomContainerResponseFilter implements ContainerResponseFilter {

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Origin, Content-Type, Accept, Authorization");
        headers.add(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, "Origin, Content-type, Accept, Authorization");
        headers.add(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        headers.add(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With, Content-Type, Accept, Origin, Authorization");
        headers.add("x-responded-by", "cors-response-filter");
    }
}
