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
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;

/**
 * @author flba
 *
 */
public class GeneticResourceTaxonomySearchService extends GeneticResourceService {

    public GeneticResourceTaxonomySearchService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        List<com.nagoya.model.to.resource.TaxonomyTO> dtos = new ArrayList<>();
        List<TaxonomyDBO> taxonomyRootLevel = getGeneticResourceDAO().getTaxonomyRootLevel();
        for (TaxonomyDBO taxonomyDBO : taxonomyRootLevel) {
            com.nagoya.model.to.resource.TaxonomyTO dto = GeneticResourceTransformer.getDTO(null, taxonomyDBO);
            dtos.add(dto);
        }

        DefaultResponse result = new DefaultResponse();
        result.setEntity(dtos);
        return result;
    }

}
