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

import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.exception.BusinessLogicException;

/**
 * @author flba
 *
 */
public abstract class GeneticResourceService extends DefaultSecureResourceService {

    public GeneticResourceService(String authorization, String language) {
        super(authorization, language);
    }

    protected void isUserAuthorizedForRessource(OnlineUserDBO onlineUser, GeneticResourceDBO dbo, boolean writeOperation)
        throws BusinessLogicException {
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
            throw new BusinessLogicException(403, "E403_ACCESS_DENIED", "Access denied");
        }
    }

}
