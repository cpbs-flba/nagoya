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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.to.resource.GeneticResourceTransformer;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author flba
 *
 */
public class PublicGeneticResourceSearchService {

    private static final Logger LOGGER = LogManager.getLogger(PublicGeneticResourceSearchService.class);

    public Response search(GeneticResourceFilter geneticRessourceFilter) {
        Session session = null;
        try {
            session = ConnectionProvider.getInstance().getSession();
            GeneticResourceDAO geneticResourceDAO = new GeneticResourceDAOImpl(session);
            List<GeneticResourceDBO> dbos = geneticResourceDAO.search(geneticRessourceFilter, null, 50);

            List<com.nagoya.model.to.resource.GeneticResourceTO> dtos = new ArrayList<>();
            for (GeneticResourceDBO dbo : dbos) {
                com.nagoya.model.to.resource.GeneticResourceTO dto = GeneticResourceTransformer.getDTO(dbo);
                dto.getFiles().clear();
                dtos.add(dto);
            }

            return Response.ok(dtos, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            LOGGER.error(e, e);
        } finally {
            if (session != null) {
                ConnectionProvider.getInstance().closeSession(session);
            }
        }

        return Response.noContent().build();
    }

}
