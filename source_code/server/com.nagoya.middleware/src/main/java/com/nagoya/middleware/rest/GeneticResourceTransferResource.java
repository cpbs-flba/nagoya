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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

/**
 * REST service for the transfer of genetic resources.
 * 
 * @author Florin Bogdan Balint
 *
 */
@Path("/transfers")
public interface GeneticResourceTransferResource {

	/**
	 * Used for the creation of a new transfer of genetic resources.
	 * 
	 * @param authorization
	 * @param geneticRessource
	 * @param asyncResponse
	 * @return
	 *         <ul>
	 *         <li>204 No Content - if everything was okay</li>
	 *         <li>400 Bad Request - in case the provided transfer object is missing or not okay</li>
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
			final com.nagoya.model.to.resource.GeneticResourceTransfer geneticRessourceTransfer, //
			@Suspended final AsyncResponse asyncResponse);
}
