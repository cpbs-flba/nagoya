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

package com.nagoya.dao.resource;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractResourceDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.VisibilityType;

public class ContractDAOTest extends DAOTest {

    private Session session = null;

    @BeforeEach
    public void init() {
        session = super.getSession();
        initializeEnvironment(session);
    }

    @Test
    @DisplayName("Test contract: insert and search")
    public void searchGeneticResourceTest() {
        PersonLegalDBO p1 = insertTestPersonLegal(session, "test1@test1.com");
        PersonLegalDBO p2 = insertTestPersonLegal(session, "test2@test2.com");
        GeneticResourceDBO gr = insertTestGeneticResource(session, p1, "sonnenblume123", VisibilityType.PRIVATE, getTaxonomyParent());

        Calendar cal = Calendar.getInstance();
        Date contractCreationDate = cal.getTime();

        ContractDBO c1 = new ContractDBO();
        c1.setSender(p1);
        c1.setReceiver(p2);
        c1.setStatus(Status.CREATED);
        c1.setCreationDate(contractCreationDate);

        cal.add(Calendar.HOUR, 1);
        Date contractCreationDate1hLater = cal.getTime();

        ContractResourceDBO cr1 = new ContractResourceDBO();
        cr1.setGeneticResource(gr);
        cr1.setAmount(BigDecimal.ONE);
        cr1.setMeasuringUnit("kg");
        c1.getContractResources().add(cr1);

        ContractDAO contractDAO = new ContractDAOImpl(session);
        contractDAO.insert(c1, true);

        List<ContractDBO> search1 = contractDAO.search(p1, null, contractCreationDate1hLater, Status.CREATED, 0);
        Assert.assertEquals(1, search1.size());

        List<ContractDBO> search2 = contractDAO.search(p2, null, null, Status.CREATED, 0);
        Assert.assertEquals(1, search2.size());

        List<ContractDBO> search3 = contractDAO.search(p2, null, null, Status.ACCEPTED, 0);
        Assert.assertEquals(0, search3.size());

    }

}
