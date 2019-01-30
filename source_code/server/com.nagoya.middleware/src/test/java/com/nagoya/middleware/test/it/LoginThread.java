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
package com.nagoya.middleware.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginThread implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(LoginThread.class);

	private int userId;
	private String targetUrl;

	public LoginThread(String targetUrl, int userId) {
		this.targetUrl = targetUrl;
		this.userId = userId;
	}

	@Override
	public void run() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUrl);

		com.nagoya.model.to.person.PersonLegalTO personTO = new com.nagoya.model.to.person.PersonLegalTO();
		personTO.setEmail("test@test.com" + userId);
		personTO.setPassword("test@test.com" + userId);
		Entity<com.nagoya.model.to.person.PersonLegalTO> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

		Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
		int status = response.getStatus();
		response.close();
		LOGGER.debug("Post sent. Response: " + status);
	}

}
