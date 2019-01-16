/**
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

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
