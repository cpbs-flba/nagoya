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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

/**
 * REST service for genetic resources.
 * 
 * @author Florin Bogdan Balint
 *
 */
@Path("/genetics")
public interface GeneticResource {

    /**
     * Used for the creation of new genetic resources.
     * 
     * @param authorization
     * @param language
     * @param geneticRessource
     * @param asyncResponse
     */
    @PUT
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void create(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        final com.nagoya.model.to.resource.GeneticResourceTO geneticRessource, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Returns all the details of a specific genetic resource.
     * 
     * @param authorization
     * @param language
     * @param id
     * @param asyncResponse
     */
    @GET
    @Path("/{resourceId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void read(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("resourceId") String resourceId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Deletes a specific genetic resource.
     * 
     * @param authorization
     * @param id
     * @param asyncResponse
     */
    @DELETE
    @Path("/{resourceId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void delete(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("resourceId") String resourceId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Deletes a specific genetic resource.
     * 
     * @param authorization
     * @param id
     * @param asyncResponse
     */
    @DELETE
    @Path("/{resourceId}/{fileId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void delete(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("resourceId") String resourceId, //
        @PathParam("fileId") String fileId, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Creates a new resource file.
     * 
     * @param authorization
     * @param geneticRessource
     * @param asyncResponse
     */
    @PUT
    @Path("/{resourceId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void create(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("resourceId") String resourceId, //
        final com.nagoya.model.to.resource.ResourceFileTO ressourceFile, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Updates a specific genetic resource. Note: The files are not updated. If there is a new file, then it is added to the existent files, however,
     * if an update request is send and no files are provided, then the existent files are not deleted in the DB.
     * 
     * @param authorization
     * @param id
     * @param asyncResponse
     */
    @POST
    @Path("/{resourceId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void update(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("resourceId") String resourceId, //
        final com.nagoya.model.to.resource.GeneticResourceTO geneticRessource, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Search for a genetic resources based on the specified filter.
     * 
     * @param authorization
     * @param geneticRessourceFilter
     * @param asyncResponse
     */
    @POST
    @Path("/search")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void search(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        final com.nagoya.model.to.resource.filter.GeneticResourceFilter geneticRessourceFilter, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Retrieves all root level taxonomies.
     * 
     * @param authorization
     * @param geneticRessourceFilter
     * @param asyncResponse
     */
    @GET
    @Path("/search/taxonomy")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void searchForTaxonomy(@HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @Suspended final AsyncResponse asyncResponse);

    /**
     * Search for a genetic resources based on the specified taxonomy.
     * 
     * @param authorization
     * @param geneticRessourceFilter
     * @param asyncResponse
     */
    @GET
    @Path("/search/taxonomy/{parentId}")
    @Consumes({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @Produces({ MediaType.APPLICATION_JSON + UserRESTResource.DEFAULT_RESPONSE_ENCODING })
    @ManagedAsync
    public void searchForTaxonomyForParent(//
        @HeaderParam(UserRESTResource.HEADER_AUTHORIZATION) String authorization, //
        @HeaderParam(UserRESTResource.HEADER_LANGUAGE) String language, //
        @PathParam("parentId") String parentId, //
        @Suspended final AsyncResponse asyncResponse);

}
