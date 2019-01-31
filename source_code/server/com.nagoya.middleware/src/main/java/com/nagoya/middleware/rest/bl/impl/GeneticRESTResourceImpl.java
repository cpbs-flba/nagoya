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
import com.nagoya.middleware.rest.bl.GeneticResource;
import com.nagoya.middleware.service.GeneticResourceService;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.NotFoundException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.resource.ResourceFileTO;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticRESTResourceImpl implements GeneticResource {

    private static final Logger LOGGER = LogManager.getLogger(GeneticRESTResourceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.GeneticResource#create(java.lang.String, com.nagoya.model.to.resource.GeneticResource,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void create(String authorization, com.nagoya.model.to.resource.GeneticResourceTO geneticRessource, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.create(authorization, geneticRessource);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
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

    @Override
    public void read(String authorization, String resourceId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.read(authorization, resourceId);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
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

    @Override
    public void delete(String authorization, String resourceId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.delete(authorization, resourceId);
            ResponseBuilder responseBuilder = Response.noContent();
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
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

    @Override
    public void delete(String authorization, String resourceId, String fileId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.delete(authorization, resourceId, fileId);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
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

    @Override
    public void create(String authorization, String resourceId, ResourceFileTO ressourceFile, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.addResourceFile(authorization, resourceId, ressourceFile);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.NOT_FOUND).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
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

    @Override
    public void update(String authorization, String resourceId, com.nagoya.model.to.resource.GeneticResourceTO geneticRessource,
        AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.update(authorization, resourceId, geneticRessource);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (NotFoundException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.NOT_FOUND).build();
        } catch (TimeoutException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.REQUEST_TIMEOUT).build();
        } catch (NotAuthorizedException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.UNAUTHORIZED).build();
        } catch (ForbiddenException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.FORBIDDEN).build();
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

    @Override
    public void search(String authorization, GeneticResourceFilter geneticRessourceFilter, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.search(authorization, geneticRessourceFilter);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (Exception e) {
            LOGGER.error(e, e);
            response = Response.serverError().build();
        } finally {
            if (session != null) {
                ConnectionProvider.getInstance().closeSession(session);
            }
        }
        asyncResponse.resume(response);

    }

    @Override
    public void searchForTaxonomy(String authorization, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.searchForTaxonomyRootLevel(authorization);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
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
    public void searchForTaxonomyForParent(String authorization, String parentId, AsyncResponse asyncResponse) {
        Response response = null;
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceService service = new GeneticResourceService(session);
            DefaultReturnObject result = service.searchForTaxonomyLevel(authorization, parentId);
            ResponseBuilder responseBuilder = Response.ok(result.getEntity());
            Set<Entry<String, String>> entrySet = result.getHeader().entrySet();
            for (Entry<String, String> entry : entrySet) {
                responseBuilder.header(entry.getKey(), entry.getValue());
            }
            response = responseBuilder.build();
        } catch (BadRequestException e) {
            LOGGER.error(e, e);
            response = Response.status(Status.BAD_REQUEST).build();
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
}
