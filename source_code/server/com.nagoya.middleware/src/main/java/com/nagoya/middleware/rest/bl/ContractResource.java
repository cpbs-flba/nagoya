/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

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
public interface ContractResource {

    public static final String QUERY_PARAM_STATUS     = "status";
    public static final String QUERY_PARAM_DATE_FROM  = "date-from";
    public static final String QUERY_PARAM_DATE_UNTIL = "date-until";

    /**
     * Creates a new contract.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     * @return
     *         <ul>
     *         <li>204 No Content - if everything was okay</li>
     *         <li>409 Conflict - if the e-mail address is already registered</li>
     *         <li>500 Internal server error - if something went terribly wrong.</li>
     *         </ul>
     */
    @PUT
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void create(//
        @HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserResource.HEADER_LANGUAGE) String language, //
        final com.nagoya.model.dto.contract.Contract contractTO, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Deletes a created contract.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     * @return
     *         <ul>
     *         <li>204 No Content - if everything was okay</li>
     *         <li>400 Bad Request - if the contract ID was not provided or the contract does not exist</li>
     *         <li>403 Forbidden - if the contract cannot be deleted (e.g., if a contract is already accepted, it cannot be deleted)</li>
     *         <li>500 Internal server error - if something went terribly wrong.</li>
     *         </ul>
     */
    @DELETE
    @Path("/{contractId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void delete(//
        @HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserResource.HEADER_LANGUAGE) String language, //
        @PathParam("contractId") String contractId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Searches for a user's contract based on the provided authorization and the provided search parameters.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     * @return
     *         <ul>
     *         <li>204 No Content - if everything was okay</li>
     *         <li>500 Internal server error - if something went terribly wrong.</li>
     *         </ul>
     */
    @GET
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void search(//
        @HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserResource.HEADER_LANGUAGE) String language, //
        @QueryParam(QUERY_PARAM_STATUS) String contractStatus, //
        @QueryParam(QUERY_PARAM_DATE_FROM) String dateFrom, //
        @QueryParam(QUERY_PARAM_DATE_UNTIL) String dateUntil, //
        @Suspended final AsyncResponse asyncResponse);
}
