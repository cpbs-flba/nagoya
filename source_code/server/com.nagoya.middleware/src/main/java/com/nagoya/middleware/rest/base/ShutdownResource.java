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

package com.nagoya.middleware.rest.base;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.glassfish.jersey.server.ManagedAsync;

/**
 * @author flba
 *
 */
@Path("/")
public interface ShutdownResource {

    public static final String SHUTDOWN_PATH = "shutdown";

    /**
     * @return
     *         <ul>
     *         <li>204 No content</li>
     *         <li>500 Internal server error - if something went terribly wrong.</li>
     *         </ul>
     */
    @GET
    @Path(SHUTDOWN_PATH)
    @ManagedAsync
    public void shutdown(@Suspended final AsyncResponse asyncResponse);
}
