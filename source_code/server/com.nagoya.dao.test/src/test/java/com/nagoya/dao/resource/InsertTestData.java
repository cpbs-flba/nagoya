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

import org.hibernate.Session;
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
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;

/**
 * Use this test to insert test data.
 * 
 * @author flba
 *
 */
public class InsertTestData extends DAOTest {

    private Session session = null;

    @BeforeEach
    public void init() {
        session = super.getSession();
        initializeEnvironment(session);
    }

    @Test
    @DisplayName("Insert some test data")
    public void searchGeneticResourceTest() {
        ContractDAO contractDAO = new ContractDAOImpl(session);

        PersonNaturalDBO p1 = insertTestPersonNatural(session, "florin.balint@cpb-software.com", "Friedrich", "Schiller");
        PersonNaturalDBO pl1 = insertTestPersonNatural(session, "alexander.dimitrov@cpb-software.com", "Wolfgang", "Goethe");
        PersonLegalDBO pl2 = insertTestPersonLegal(session, "test2@test2.com");

        TaxonomyDBO tp = getTaxonomyParent();
        contractDAO.insert(tp, true);
        TaxonomyDBO t1 = getTaxonomyChild1(tp);
        contractDAO.insert(t1, true);
        TaxonomyDBO t2 = getTaxonomyChild2(tp);
        contractDAO.insert(t2, true);

        GeneticResourceDBO gr = insertTestGeneticResource(session, p1, "sonnenblume123", VisibilityType.PUBLIC, t1);

        insertTestGeneticResource(session, p1, "tomato123", VisibilityType.PUBLIC, t1);
        insertTestGeneticResource(session, p1, "potato123", VisibilityType.PRIVATE, t2);

        insertTestGeneticResource(session, pl1, "banana123", VisibilityType.PUBLIC, t1);
        insertTestGeneticResource(session, pl1, "lettuce123", VisibilityType.PRIVATE, t2);

        Calendar cal = Calendar.getInstance();
        Date contractCreationDate = cal.getTime();

        ContractDBO c1 = new ContractDBO();
        c1.setSender(p1);
        c1.setReceiver(pl2);
        c1.setStatus(Status.CREATED);
        c1.setCreationDate(contractCreationDate);

        ContractResourceDBO cr1 = new ContractResourceDBO();
        cr1.setGeneticResource(gr);
        cr1.setAmount(BigDecimal.ONE);
        cr1.setMeasuringUnit("kg");
        c1.getContractResources().add(cr1);

        contractDAO.insert(c1, true);

        UserRequestDBO ur = new UserRequestDBO();
        ur.setContract(c1);
        cal.add(Calendar.DAY_OF_YEAR, 3);
        ur.setExpirationDate(cal.getTime());
        ur.setToken("t12345");
        ur.setRequestType(RequestType.CONTRACT_ACCEPTANCE);
        ur.setPerson(pl2);
        contractDAO.insert(ur, true);
    }

}
