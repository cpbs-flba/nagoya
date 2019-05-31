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

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import com.nagoya.common.util.StringUtil;
import com.nagoya.middleware.rest.bl.GeneticResource;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.service.genetic.GeneticResourceCreationService;
import com.nagoya.middleware.service.genetic.GeneticResourceDeletionService;
import com.nagoya.middleware.service.genetic.GeneticResourceFileCreationService;
import com.nagoya.middleware.service.genetic.GeneticResourceFileDeletionService;
import com.nagoya.middleware.service.genetic.GeneticResourceRetrievalService;
import com.nagoya.middleware.service.genetic.GeneticResourceSearchService;
import com.nagoya.middleware.service.genetic.GeneticResourceTaxonomyParentSearchService;
import com.nagoya.middleware.service.genetic.GeneticResourceTaxonomySearchService;
import com.nagoya.middleware.service.genetic.GeneticResourceUpdateService;
import com.nagoya.middleware.service.genetic.PublicGeneticResourceSearchService;
import com.nagoya.model.to.resource.ResourceFileTO;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticRESTResourceImpl implements GeneticResource {

    @Override
    public void create(String authorization, String language, com.nagoya.model.to.resource.GeneticResourceTO geneticRessource,
        AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceCreationService(authorization, language);
        Response response = service.runService(geneticRessource);
        asyncResponse.resume(response);
    }

    @Override
    public void read(String authorization, String language, String resourceId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceRetrievalService(authorization, language);
        Response response = service.runService(resourceId);
        asyncResponse.resume(response);
    }

    @Override
    public void delete(String authorization, String language, String resourceId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceDeletionService(authorization, language);
        Response response = service.runService(resourceId);
        asyncResponse.resume(response);
    }

    @Override
    public void delete(String authorization, String language, String resourceId, String fileId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceFileDeletionService(authorization, language);
        Response response = service.runService(resourceId, fileId);
        asyncResponse.resume(response);
    }

    @Override
    public void create(String authorization, String language, String resourceId, ResourceFileTO ressourceFile, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceFileCreationService(authorization, language);
        Response response = service.runService(resourceId, ressourceFile);
        asyncResponse.resume(response);
    }

    @Override
    public void update(String authorization, String language, String resourceId, com.nagoya.model.to.resource.GeneticResourceTO geneticRessource,
        AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceUpdateService(authorization, language);
        Response response = service.runService(resourceId, geneticRessource);
        asyncResponse.resume(response);
    }

    @Override
    public void search(String authorization, String language, GeneticResourceFilter geneticRessourceFilter, AsyncResponse asyncResponse) {
        if (StringUtil.isNotNullOrBlank(authorization)) {
            DefaultSecureResourceService service = new GeneticResourceSearchService(authorization, language);
            Response response = service.runService(geneticRessourceFilter);
            asyncResponse.resume(response);
        } else {
            PublicGeneticResourceSearchService service = new PublicGeneticResourceSearchService();
            Response response = service.search(geneticRessourceFilter);
            asyncResponse.resume(response);
        }
    }

    @Override
    public void searchForTaxonomy(String authorization, String language, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceTaxonomySearchService(authorization, language);
        Response response = service.runService();
        asyncResponse.resume(response);
    }

    @Override
    public void searchForTaxonomyForParent(String authorization, String language, String parentId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new GeneticResourceTaxonomyParentSearchService(authorization, language);
        Response response = service.runService(parentId);
        asyncResponse.resume(response);
    }
}
