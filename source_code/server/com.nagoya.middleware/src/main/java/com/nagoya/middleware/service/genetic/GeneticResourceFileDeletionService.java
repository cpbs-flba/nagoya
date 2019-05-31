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

package com.nagoya.middleware.service.genetic;

import java.util.Iterator;

import com.nagoya.common.util.StringUtil;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;

/**
 * @author flba
 *
 */
public class GeneticResourceFileDeletionService extends GeneticResourceService {

    public GeneticResourceFileDeletionService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        String resourceId = (String) params[0];
        String fileId = (String) params[1];

        if (StringUtil.isNullOrBlank(resourceId) || StringUtil.isNullOrBlank(fileId)) {
            throw new BusinessLogicException(400, "E400_IDS_MISSING", "Resource ID and/or File ID is missing.");
        }

        Long resourceIdAsLong = null;
        Long fileIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
            fileIdAsLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_IDS_MISSING", "Resource ID and/or File ID is missing.");
        }
        GeneticResourceDBO dbo;
        try {
            dbo = (GeneticResourceDBO) getGeneticResourceDAO().find(resourceIdAsLong, com.nagoya.model.dbo.resource.GeneticResourceDBO.class);
        } catch (NonUniqueResultException e1) {
            throw new InternalException(e1);
        }

        if (dbo == null) {
            throw new BusinessLogicException(400, "E400_IDS_INVALID", "E400_IDS_INVALID");
        }

        isUserAuthorizedForRessource(getOnlineUser(), dbo, true);

        // search through the files and delete it
        Iterator<ResourceFileDBO> iterator = dbo.getFiles().iterator();
        while (iterator.hasNext()) {
            ResourceFileDBO rf = iterator.next();
            if (rf.getId().equals(fileIdAsLong)) {
                iterator.remove();
            }
        }

        try {
            getGeneticResourceDAO().update(dbo, true);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new InternalException(e);
        }

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        DefaultResponse result = new DefaultResponse(200);
        result.setEntity(dto);
        return result;
    }

}
