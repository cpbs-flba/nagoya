/**
 * 
 */
package com.nagoya.middleware.rest.impl;

import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.middleware.rest.GeneticResourceTransferResource;
import com.nagoya.middleware.service.GeneticResourceTransferService;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.resource.GeneticResourceTransfer;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceTransferResourceImpl implements GeneticResourceTransferResource {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceTransferResourceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nagoya.middleware.rest.GeneticResourceTransferResource#create(java.lang.
	 * String, com.nagoya.model.to.resource.GeneticResourceTransfer,
	 * javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void create(String authorization, String language, GeneticResourceTransfer geneticRessourceTransfer,
			AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			GeneticResourceTransferService service = new GeneticResourceTransferService(session, language);
			DefaultReturnObject result = service.create(authorization, geneticRessourceTransfer);
			ResponseBuilder responseBuilder = Response.ok(result.getEntity());
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (BadRequestException | ConflictException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}

	@Override
	public void delete(String authorization, String language, String transferId, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			GeneticResourceTransferService service = new GeneticResourceTransferService(session, language);
			DefaultReturnObject result = service.delete(authorization, transferId);
			ResponseBuilder responseBuilder = Response.ok(result.getEntity());
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (ForbiddenException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (ConflictException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}

}
