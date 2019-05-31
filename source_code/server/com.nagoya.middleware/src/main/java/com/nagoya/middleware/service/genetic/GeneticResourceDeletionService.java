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
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;

/**
 * @author flba
 *
 */
public class GeneticResourceDeletionService extends GeneticResourceService {

    public GeneticResourceDeletionService(String authorization, String language) {
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
            throw new BusinessLogicException(400, "E400_INVALID_ID", "E400_INVALID_ID");
        }
        GeneticResourceDBO dbo;
        try {
            dbo = (GeneticResourceDBO) getGeneticResourceDAO().find(resourceIdAsLong, com.nagoya.model.dbo.resource.GeneticResourceDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException(e);
        }

        if (dbo == null) {
            throw new BusinessLogicException(400, "E400_INVALID_ID", "E400_INVALID_ID");
        }

        isUserAuthorizedForRessource(getOnlineUser(), dbo, true);

        try {
            getGeneticResourceDAO().delete(dbo, true);
        } catch (InvalidObjectException e) {
            throw new InternalException(e);
        }
        return null;
    }

}
