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

package com.nagoya.dao.geneticresource;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

public interface GeneticResourceDAO extends BasicDAO<com.nagoya.model.dbo.resource.GeneticResourceDBO> {

    /**
     * Searches for the genetic resources based on the specified filter.
     * 
     * @param filter
     * @param maxResults
     * @return
     */
    public java.util.List<com.nagoya.model.dbo.resource.GeneticResourceDBO> search(GeneticResourceFilter filter, PersonDBO owner, int maxResults);

    /**
     * Returns all taxonomy root levels.
     * 
     * @return
     */
    public java.util.List<TaxonomyDBO> getTaxonomyRootLevel();

    /**
     * Returns all taxonomy children for the provided parent. This goes only one level deep.
     * 
     * @param parent
     * @return
     */
    public java.util.List<TaxonomyDBO> getTaxonomyChildren(long parentId);
}
