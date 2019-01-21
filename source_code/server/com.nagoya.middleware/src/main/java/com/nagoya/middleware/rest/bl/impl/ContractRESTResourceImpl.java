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

package com.nagoya.middleware.rest.bl.impl;

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
import com.nagoya.middleware.rest.bl.ContractRESTResource;
import com.nagoya.middleware.service.ContractResourceService;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.NotFoundException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.contract.ContractFileTO;
import com.nagoya.model.to.contract.ContractTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractRESTResourceImpl implements ContractRESTResource {

    private static final Logger LOGGER = LogManager.getLogger(ContractRESTResourceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractResource#create(java.lang.String, java.lang.String, com.nagoya.model.dto.contract.Contract,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void addContract(String authorization, String language, ContractTO contractTO, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.create(authorization, language, contractTO);
            ResponseBuilder responseBuilder = Response.noContent();
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
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
        } catch (BadRequestException e) {
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

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractResource#delete(java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void deleteContract(String authorization, String language, String contractId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.delete(authorization, contractId);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (BadRequestException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractResource#search(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void search(String authorization, String language, String contractStatus, String dateFrom, String dateUntil, AsyncResponse asyncResponse) {
        LOGGER.debug("Received request.");
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.search(authorization, contractStatus, dateFrom, dateUntil);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
            LOGGER.debug("Request processed succesfully");
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#addFile(java.lang.String, java.lang.String, java.lang.String,
     * com.nagoya.model.to.contract.ContractFileTO, javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void addFile(String authorization, String language, String contractId, ContractFileTO contractFileTO, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.create(authorization, language, contractId, contractFileTO);
            ResponseBuilder responseBuilder = Response.noContent();
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (ForbiddenException e) {
            LOGGER.error(e.getMessage());
            response = Response.status(Status.FORBIDDEN).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (BadRequestException e) {
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

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#deleteFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void deleteFile(String authorization, String language, String contractId, String fileId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.deleteFile(authorization, language, contractId, fileId);
            ResponseBuilder responseBuilder = Response.noContent();
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            response = Response.status(Status.NOT_FOUND).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e.getMessage());
            response = Response.status(Status.FORBIDDEN).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (BadRequestException e) {
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

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#retrieveFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void retrieveFile(String authorization, String language, String contractId, String fileId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            ContractResourceService service = new ContractResourceService(session);
            DefaultReturnObject result = service.retrieveFile(authorization, language, contractId, fileId);
            ResponseBuilder responseBuilder = Response.noContent();
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (BadRequestException e) {
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
