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

import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.to.resource.GeneticResourceTransformer;
import com.nagoya.model.to.resource.TaxonomyTO;

/**
 * @author flba
 *
 */
public class GeneticResourceCreationService extends GeneticResourceService {

    public GeneticResourceCreationService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        com.nagoya.model.to.resource.GeneticResourceTO geneticRessource = (com.nagoya.model.to.resource.GeneticResourceTO) params[0];

        if (geneticRessource == null) {
            throw new BusinessLogicException(400, "E400_GENETIC_RESOURCE_MISSING", "E400_GENETIC_RESOURCE_MISSING");
        }

        // find the taxonomy based on the provided ID
        TaxonomyTO taxonomy = geneticRessource.getTaxonomy();
        if (taxonomy == null) {
            throw new BusinessLogicException(400, "E400_GENETIC_RESOURCE_TAXONOMY_MISSING", "E400_GENETIC_RESOURCE_TAXONOMY_MISSING");
        }
        String taxonomyId = taxonomy.getId();
        if (StringUtil.isNullOrBlank(taxonomyId)) {
            throw new BusinessLogicException(400, "E400_GENETIC_RESOURCE_TAXONOMY_MISSING", "E400_GENETIC_RESOURCE_TAXONOMY_MISSING");
        }
        TaxonomyDBO taxonomyDBO = null;
        try {
            long parseLong = Long.parseLong(taxonomyId);
            BasicDAO<TaxonomyDBO> taxonomyDAO = new BasicDAOImpl<TaxonomyDBO>(getSession());
            taxonomyDBO = (TaxonomyDBO) taxonomyDAO.find(parseLong, TaxonomyDBO.class);
        } catch (NumberFormatException | NonUniqueResultException e) {
            throw new BusinessLogicException(400, "E400_GENETIC_RESOURCE_TAXONOMY_INVALID", "E400_GENETIC_RESOURCE_TAXONOMY_INVALID");
        }
        GeneticResourceDBO dbo = GeneticResourceTransformer.getDBO(null, geneticRessource);
        dbo.setTaxonomy(taxonomyDBO);
        dbo.setOwner(getOnlineUser().getPerson());

        GeneticResourceDAO geneticResourceDAO = new GeneticResourceDAOImpl(getSession());
        geneticResourceDAO.insert(dbo, true);

        // get the transfer object, but do not send all the data
        com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
        dto.getFiles().clear();

        DefaultResponse result = new DefaultResponse(200);
        result.setEntity(dto);
        return result;
    }

}
