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

package com.nagoya.dao.contract;

import java.util.Date;
import java.util.List;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.person.PersonDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
public interface ContractDAO extends BasicDAO<com.nagoya.model.dbo.contract.ContractDBO> {

    /**
     * Searches for all contracts, where the specified person is involved. Additional query parameters like periodFrom, periodUntil and status can be
     * specified, but are not mandatory.
     * 
     * @param person
     * @param periodFrom
     * @param periodUntil
     * @param status
     * @return
     */
    public List<com.nagoya.model.dbo.contract.ContractDBO> search(PersonDBO person, Date periodFrom, Date periodUntil,
        com.nagoya.model.dbo.contract.Status status, int maxResults);

}
