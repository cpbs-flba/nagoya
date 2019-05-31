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

import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;

/**
 * @author flba
 *
 */
public class GeneticResourceRetrievalService extends GeneticResourceService {

    public GeneticResourceRetrievalService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        String resourceId = (String) params[0];
        // verify the file id
        Long resourceIdAsLong = null;
        try {
            resourceIdAsLong = Long.parseLong(resourceId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_RESOURCE_ID_INVALID", "Resource ID and/or File ID is invalid.");
        }

        GeneticResourceDBO dbo = null;
        try {
            dbo = (GeneticResourceDBO) getGeneticResourceDAO().find(resourceIdAsLong, com.nagoya.model.dbo.resource.GeneticResourceDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException(e);
        }

        if (dbo == null) {
            throw new BusinessLogicException(400, "E400_RESOURCE_ID_INVALID", "Resource ID and/or File ID is invalid.");
        }

        isUserAuthorizedForRessource(getOnlineUser(), dbo, false);

        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);

        DefaultResponse result = new DefaultResponse(200);
        result.setEntity(dto);
        return result;
    }

}
