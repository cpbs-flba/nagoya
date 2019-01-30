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

package com.nagoya.middleware.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.NotFoundException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;
import com.nagoya.model.to.resource.TaxonomyTO;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceService extends ResourceService {

    private static final Logger LOGGER = LogManager.getLogger(GeneticResourceService.class);

    private GeneticResourceDAO  geneticResourceDAO;

    private Session             session;

    public GeneticResourceService(Session session) {
        super(session);
        this.session = session;
        this.geneticResourceDAO = new GeneticResourceDAOImpl(session);
    }

    public DefaultReturnObject create(String authorization, com.nagoya.model.to.resource.GeneticResourceTO geneticRessource)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException, ResourceOutOfDateException,
        BadRequestException, NonUniqueResultException {
        LOGGER.debug("Adding genetic resource");
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (geneticRessource == null) {
            throw new BadRequestException("Cannot execute create operation, when the object is null.");
        }

        // find the taxonomy based on the provided ID
        TaxonomyTO taxonomy = geneticRessource.getTaxonomy();
        if (taxonomy == null) {
            throw new BadRequestException("Taxonomy must be provided.");
        }
        String taxonomyId = taxonomy.getId();
        if (StringUtil.isNullOrBlank(taxonomyId)) {
            throw new BadRequestException("Taxonomy ID must be provided.");
        }
        TaxonomyDBO taxonomyDBO = null;
        try {
            long parseLong = Long.parseLong(taxonomyId);
            BasicDAO<TaxonomyDBO> taxonomyDAO = new BasicDAOImpl<TaxonomyDBO>(session);
            taxonomyDBO = (TaxonomyDBO) taxonomyDAO.find(parseLong, TaxonomyDBO.class);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid taxonomy ID provided.");
        }
        GeneticResourceDBO dbo = GeneticResourceTransformer.getDBO(null, geneticRessource);
        dbo.setTaxonomy(taxonomyDBO);
        dbo.setOwner(onlineUser.getPerson());
        geneticResourceDAO.insert(dbo, true);

        // get the transfer object, but do not send all the data
        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        dto.getFiles().clear();

        DefaultReturnObject result = refreshSession(onlineUser, dto, null);
        return result;
    }

    public DefaultReturnObject read(String authorization, String resourceId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(resourceId)) {
            throw new BadRequestException("Resource ID is missing.");
        }
        // verify the file id
        Long resourceIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Resource ID and/or File ID is invalid.");
        }

        GeneticResourceDBO dbo = (GeneticResourceDBO) geneticResourceDAO.find(resourceIdAsLong,
            com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

        if (dbo == null) {
            throw new NotFoundException();
        }

        isUserAuthorizedForRessource(onlineUser, dbo, false);

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);

        DefaultReturnObject result = refreshSession(onlineUser, dto, null);
        return result;
    }

    private void isUserAuthorizedForRessource(OnlineUserDBO onlineUser, GeneticResourceDBO dbo, boolean writeOperation)
        throws ForbiddenException {
        boolean authorized = false;
        // if the user is the owner
        if (dbo.getOwner().getId().equals(onlineUser.getPerson().getId())) {
            authorized = true;
        }
        // if the user is in the correct group
        if (dbo.getVisibilityType().equals(VisibilityType.PUBLIC)) {
            authorized = true;
        }
        // if the user is in the correct group
        if (dbo.getVisibilityType().equals(VisibilityType.GROUP) && !writeOperation) {
            // Verify if a user is in the group
            authorized = true;
        }
        if (!authorized) {
            throw new ForbiddenException("Access denied");
        }
    }

    public DefaultReturnObject delete(String authorization, String resourceId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(resourceId)) {
            throw new BadRequestException("Resource ID is missing.");
        }

        // verify the file id
        Long resourceIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Resource ID and/or File ID is invalid.");
        }
        GeneticResourceDBO dbo = (GeneticResourceDBO) geneticResourceDAO.find(resourceIdAsLong,
            com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

        if (dbo == null) {
            throw new NotFoundException();
        }

        isUserAuthorizedForRessource(onlineUser, dbo, true);

        geneticResourceDAO.delete(dbo, true);

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject delete(String authorization, String resourceId, String fileId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(resourceId) || StringUtil.isNullOrBlank(fileId)) {
            throw new BadRequestException("Resource ID and/or File ID is missing.");
        }
        Long resourceIdAsLong = null;
        Long fileIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
            fileIdAsLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Resource ID and/or File ID is invalid.");
        }
        GeneticResourceDBO dbo = (GeneticResourceDBO) geneticResourceDAO.find(resourceIdAsLong,
            com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

        if (dbo == null) {
            throw new NotFoundException();
        }

        isUserAuthorizedForRessource(onlineUser, dbo, true);

        // search through the files and delete it
        Iterator<ResourceFileDBO> iterator = dbo.getFiles().iterator();
        while (iterator.hasNext()) {
            ResourceFileDBO rf = iterator.next();
            if (rf.getId().equals(fileIdAsLong)) {
                iterator.remove();
            }
        }

        geneticResourceDAO.update(dbo, true);

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        DefaultReturnObject result = refreshSession(onlineUser, dto, null);
        return result;
    }

    public DefaultReturnObject addResourceFile(String authorization, String resourceId, com.nagoya.model.to.resource.ResourceFileTO resourceFile)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(resourceId) || resourceFile == null) {
            throw new BadRequestException("Resource ID and/or File ID is missing.");
        }
        Long resourceIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Resource ID and/or File ID is invalid.");
        }
        GeneticResourceDBO dbo = (GeneticResourceDBO) geneticResourceDAO.find(resourceIdAsLong,
            com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

        if (dbo == null) {
            throw new NotFoundException();
        }

        isUserAuthorizedForRessource(onlineUser, dbo, true);

        ResourceFileDBO dboResourceFile = GeneticResourceTransformer.getDBO(resourceFile);
        dbo.getFiles().add(dboResourceFile);
        geneticResourceDAO.update(dbo, true);

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        DefaultReturnObject result = refreshSession(onlineUser, dto, null);
        return result;
    }

    public DefaultReturnObject update(String authorization, String resourceId, com.nagoya.model.to.resource.GeneticResourceTO geneticResource)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(resourceId) || geneticResource == null) {
            throw new BadRequestException("Resource ID and/or File ID is missing.");
        }
        Long resourceIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Resource ID and/or File ID is invalid.");
        }
        GeneticResourceDBO dbo = (GeneticResourceDBO) geneticResourceDAO.find(resourceIdAsLong,
            com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

        if (dbo == null) {
            throw new NotFoundException();
        }

        isUserAuthorizedForRessource(onlineUser, dbo, true);

        GeneticResourceDBO dboGeneticResource = GeneticResourceTransformer.getDBO(dbo, geneticResource);
        geneticResourceDAO.update(dboGeneticResource, true);

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        DefaultReturnObject result = refreshSession(onlineUser, dto, null);
        return result;
    }

    public DefaultReturnObject search(String authorization, GeneticResourceFilter geneticRessourceFilter)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
        ResourceOutOfDateException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        List<GeneticResourceDBO> dbos = geneticResourceDAO.search(geneticRessourceFilter, onlineUser.getPerson(), 50);

        List<com.nagoya.model.to.resource.GeneticResourceTO> dtos = new ArrayList<>();
        for (GeneticResourceDBO dbo : dbos) {
            com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
            dto.getFiles().clear();
            dtos.add(dto);
        }

        DefaultReturnObject result = refreshSession(onlineUser, dtos, null);
        return result;
    }

    public DefaultReturnObject searchForTaxonomyRootLevel(String authorization)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
        ResourceOutOfDateException {
        validateSession(authorization);

        List<com.nagoya.model.to.resource.TaxonomyTO> dtos = new ArrayList<>();
        List<TaxonomyDBO> taxonomyRootLevel = geneticResourceDAO.getTaxonomyRootLevel();
        for (TaxonomyDBO taxonomyDBO : taxonomyRootLevel) {
            com.nagoya.model.to.resource.TaxonomyTO dto = GeneticResourceTransformer.getDTO(null, taxonomyDBO);
            dtos.add(dto);
        }

        DefaultReturnObject result = buildDefaultReturnObject(authorization, dtos, null);
        return result;
    }

    public DefaultReturnObject searchForTaxonomyLevel(String authorization, String parentId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException, ResourceOutOfDateException,
        BadRequestException {
        validateSession(authorization);

        if (StringUtil.isNullOrBlank(parentId)) {
            throw new BadRequestException("Parent ID must be specified");
        }

        long parsedId = 0;
        try {
            parsedId = Long.parseLong(parentId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Parent ID must be a valid long number");
        }

        List<com.nagoya.model.to.resource.TaxonomyTO> dtos = new ArrayList<>();
        List<TaxonomyDBO> taxonomyRootLevel = geneticResourceDAO.getTaxonomyChildren(parsedId);
        for (TaxonomyDBO taxonomyDBO : taxonomyRootLevel) {
            com.nagoya.model.to.resource.TaxonomyTO dto = GeneticResourceTransformer.getDTO(null, taxonomyDBO);
            dtos.add(dto);
        }

        DefaultReturnObject result = buildDefaultReturnObject(authorization, dtos, null);
        return result;
    }

}
