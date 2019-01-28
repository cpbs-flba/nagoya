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

package com.nagoya.middleware.rest.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.middleware.rest.PingResource;

/**
 * @author flba
 *
 */
public class PingResourceImpl implements PingResource {

	private static final Logger LOGGER = LogManager.getLogger(PingResourceImpl.class);

	@Override
	public void ping(@Suspended final AsyncResponse asyncResponse) {
		LOGGER.debug("Ping request received successfully.");
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			LOGGER.debug("DB connection okay.");
			response = Response.noContent().build();
		} catch (Exception e) {
			LOGGER.error(e, e.getCause());
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}

}