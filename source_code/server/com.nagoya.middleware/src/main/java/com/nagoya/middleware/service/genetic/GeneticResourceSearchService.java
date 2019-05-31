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

import java.util.ArrayList;
import java.util.List;

import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author flba
 *
 */
public class GeneticResourceSearchService extends GeneticResourceService {

    public GeneticResourceSearchService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        GeneticResourceFilter geneticRessourceFilter = (GeneticResourceFilter) params[0];

        List<GeneticResourceDBO> dbos = getGeneticResourceDAO().search(geneticRessourceFilter, getOnlineUser().getPerson(), 50);

        List<com.nagoya.model.to.resource.GeneticResourceTO> dtos = new ArrayList<>();
        for (GeneticResourceDBO dbo : dbos) {
            com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
            dto.getFiles().clear();
            dtos.add(dto);
        }

        DefaultResponse result = new DefaultResponse();
        result.setEntity(dtos);
        return result;
    }

}
