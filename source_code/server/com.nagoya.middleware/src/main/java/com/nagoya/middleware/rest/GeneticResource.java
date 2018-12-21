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

package com.nagoya.middleware.rest;

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
	 * @param geneticRessource
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>400 Bad Request - in case the provided transfer object is missing
	 *         or not okay</li>
	 *         <li>401 Unauthorized - in case of a bad session token</li>
	 *         <li>408 Request Timeout - if the session token has expired</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@PUT
	@Path("/")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void create(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			final com.nagoya.model.to.resource.GeneticResource geneticRessource, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * Returns all the details of a specific genetic resource.
	 * 
	 * @param authorization
	 * @param id
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>409 Conflict - if the e-mail address is already registered</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@GET
	@Path("/{resourceId}")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void read(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("resourceId") String resourceId, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * Deletes a specific genetic resource.
	 * 
	 * @param authorization
	 * @param id
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>409 Conflict - if the e-mail address is already registered</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@DELETE
	@Path("/{resourceId}")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void delete(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("resourceId") String resourceId, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * Deletes a specific genetic resource.
	 * 
	 * @param authorization
	 * @param id
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>409 Conflict - if the e-mail address is already registered</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@DELETE
	@Path("/{resourceId}/{fileId}")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void delete(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("resourceId") String resourceId, //
			@PathParam("fileId") String fileId, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * Creates a new resource file.
	 * 
	 * @param authorization
	 * @param geneticRessource
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>409 Conflict - if the e-mail address is already registered</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@PUT
	@Path("/{resourceId}")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void create(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("resourceId") String resourceId, //
			final com.nagoya.model.to.resource.ResourceFile ressourceFile, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * Updates a specific genetic resource. Note: The files are not updated. If
	 * there is a new file, then it is added to the existent files, however, if an
	 * update request is send and no files are provided, then the existent files are
	 * not deleted in the DB.
	 * 
	 * @param authorization
	 * @param id
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>409 Conflict - if the e-mail address is already registered</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("/{resourceId}")
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void update(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("resourceId") String resourceId, //
			final com.nagoya.model.to.resource.GeneticResource geneticRessource, //
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
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void search(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
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
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void searchForTaxonomy(@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
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
	@Consumes({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + UserResource.DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void searchForTaxonomyForParent(//
			@HeaderParam(UserResource.HEADER_AUTHORIZATION) String authorization, //
			@PathParam("parentId") String parentId, //
			@Suspended final AsyncResponse asyncResponse);

}
