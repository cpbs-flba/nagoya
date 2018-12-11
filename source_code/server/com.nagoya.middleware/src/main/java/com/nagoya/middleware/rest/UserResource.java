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

package com.nagoya.middleware.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

import com.nagoya.model.to.person.Person;
import com.nagoya.model.to.person.PersonLegal;
import com.nagoya.model.to.person.PersonNatural;

/**
 * Ping REST resource.
 * 
 * @author Florin Bogdan Balint
 *
 */
@Path("/users")
public interface UserResource {

	public static final String QUERY_PARAM_TOKEN = "token";

	public static final String HEADER_AUTHORIZATION = "Authorization";

	public static final String HEADER_LANGUAGE = "Accept-Language";

	public static final String HEADER_AUTHORIZATION_BEARER = "Bearer ";

	public static final String DEFAULT_RESPONSE_ENCODING = ";charset=utf-8";

	/**
	 * @return
	 *         <ul>
	 *         <li>200 OK - if everything was okay</li>
	 *         <li>400 Bad Request - if email/password are missing</li>
	 *         <li>401 Unauthorized - in case of bad email/password combination</li>
	 *         <li>403 Forbidden - if the user did not confirm his e-mail</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("login")
	@Consumes({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void login(final Person person, @Suspended final AsyncResponse asyncResponse);

	/**
	 * @return
	 *         <ul>
	 *         <li>204 No content - if the request was processed successfully</li>
	 *         <li>400 Bad request - if no email address is provided</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("resetpw")
	@Consumes({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void resetPassword(//
			final Person person, //
			@HeaderParam(HEADER_LANGUAGE) String language, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * @return
	 *         <ul>
	 *         <li>204 No content - if the request was processed successfully</li>
	 *         <li>401 Unauthorized - in case of a bad session token</li>
	 *         <li>408 Request Timeout - if the session token has expired</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@DELETE
	@Path("delete")
	@ManagedAsync
	public void delete( //
			@HeaderParam(HEADER_AUTHORIZATION) String authorization, //
			@HeaderParam(HEADER_LANGUAGE) String language, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * @return
	 *         <ul>
	 *         <li>200 OK - if the request was processed successfully</li>
	 *         <li>401 Unauthorized - in case of a bad session token</li>
	 *         <li>403 Forbidden - in case of a bad password confirmation</li>
	 *         <li>408 Request Timeout - if the session token has expired</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("update/natural")
	@ManagedAsync
	public void update( //
			@HeaderParam(HEADER_AUTHORIZATION) String authorization, //
			@HeaderParam(HEADER_LANGUAGE) String language, //
			final PersonNatural person, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * @return
	 *         <ul>
	 *         <li>200 OK - if the request was processed successfully</li>
	 *         <li>401 Unauthorized - in case of a bad session token</li>
	 *         <li>403 Forbidden - in case of a bad password confirmation</li>
	 *         <li>408 Request Timeout - if the session token has expired</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("update/legal")
	@ManagedAsync
	public void update( //
			@HeaderParam(HEADER_AUTHORIZATION) String authorization, //
			@HeaderParam(HEADER_LANGUAGE) String language, //
			final PersonLegal person, //
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * @param person
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
	@Path("register/legal")
	@Consumes({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void register(final PersonLegal person, @HeaderParam(HEADER_LANGUAGE) String language,
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * @param person
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
	@Path("register/natural")
	@Consumes({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void register(final PersonNatural person, @HeaderParam(HEADER_LANGUAGE) String language,
			@Suspended final AsyncResponse asyncResponse);

	/**
	 * REST service used when a user confirms an action (by clicking on a link in
	 * the received e-mail).
	 * 
	 * In all cases, a JSON web token (e.g., Authorization Bearer ...) is NOT
	 * transmitted here for security reasons.
	 * 
	 * @param person
	 * @param token
	 * @param asyncResponse
	 * 
	 * @return
	 *         <ul>
	 *         <li>200 OK - in the case of an account registration, if everything
	 *         was okay</li>
	 *         <li>204 No Content - in all other cases, if everything was okay</li>
	 *         <li>400 Bad Request - if the token is invalid</li>
	 *         <li>408 Request Timeout - if the token has expired</li>
	 *         <li>500 Internal server error - if something went terribly
	 *         wrong.</li>
	 *         </ul>
	 */
	@POST
	@Path("confirm")
	@Consumes({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@Produces({ MediaType.APPLICATION_JSON + DEFAULT_RESPONSE_ENCODING })
	@ManagedAsync
	public void confirm(final Person person, @QueryParam(QUERY_PARAM_TOKEN) String token,
			@Suspended final AsyncResponse asyncResponse);

}
