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

package com.nagoya.middleware.rest.bl;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

/**
 * REST service for contracts.
 * 
 * @author Florin Bogdan Balint
 *
 */
@Path("/contracts")
public interface ContractRESTResource {

    public static final String QUERY_PARAM_DATE_FROM   = "date-from";
    public static final String QUERY_PARAM_DATE_UNTIL  = "date-until";
    public static final String QUERY_PARAM_STATUS      = "status";
    public static final String QUERY_PARAM_ROLE        = "role";
    public static final String QUERY_PARAM_PRIVATE_KEY = "privatekey";

    /**
     * Creates a new contract.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @PUT
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void addContract(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @QueryParam(QUERY_PARAM_PRIVATE_KEY) String privateKey, //
        final com.nagoya.model.to.contract.ContractTO contractTO, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Adds a file to an existing contract
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @PUT
    @Path("/{contractId}/files")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void addFile(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("contractId") String contractId, //
        final com.nagoya.model.to.contract.ContractFileTO contractFileTO, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Deletes a created contract.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @DELETE
    @Path("/{contractId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void deleteContract(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("contractId") String contractId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Removes a file from an existing contract
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @DELETE
    @Path("/{contractId}/files/{fileId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void deleteFile(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("contractId") String contractId, //
        @PathParam("fileId") String fileId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Retrieves a file from an existing contract
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @GET
    @Path("/{contractId}/files/{fileId}")
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void retrieveFile(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("contractId") String contractId, //
        @PathParam("fileId") String fileId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Searches for a user's contract based on the provided authorization and the provided search parameters. <br>
     * <br>
     * If provided, the date parameters will be expected to be in the following <b>ISO-8601</b> format: <br>
     * yyyy-MM-dd'T'HH:mm:ssZ
     * 
     * @param authorization
     * @param language
     * @param contractStatus
     * @param dateFrom
     * @param dateUntil
     * @param asyncResponse
     * @return one of the following status codes:
     */
    @GET
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void search(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization,
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, @QueryParam(QUERY_PARAM_STATUS) String contractStatus,
        @QueryParam(QUERY_PARAM_DATE_FROM) String dateFrom, @QueryParam(QUERY_PARAM_DATE_UNTIL) String dateUntil,
        @QueryParam(QUERY_PARAM_ROLE) String role, @Suspended final AsyncResponse asyncResponse);

    /**
     * Accepts a contract
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @GET
    @Path("/accept/{tokenId}")
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void acceptContract(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("tokenId") String tokenId, //
        @QueryParam(QUERY_PARAM_PRIVATE_KEY) String privateKey, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Rejects a contract
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @GET
    @Path("/reject/{tokenId}")
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void rejectContract(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("tokenId") String tokenId, //
        @Suspended final AsyncResponse asyncResponse);
}
