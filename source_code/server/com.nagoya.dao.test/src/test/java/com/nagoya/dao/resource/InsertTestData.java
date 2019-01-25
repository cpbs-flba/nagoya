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
import com.nagoya.model.dbo.resource.VisibilityType;

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
        PersonNaturalDBO p1 = insertTestPersonNatural(session, "florin.balint@cpb-software.com", "Florin", "Balint");
        PersonLegalDBO pl1 = insertTestPersonLegal(session, "alexander.dimitrov@cpb-software.com");
        PersonLegalDBO pl2 = insertTestPersonLegal(session, "test2@test2.com");

        GeneticResourceDBO gr = insertTestGeneticResource(session, p1, "sonnenblume123", VisibilityType.PUBLIC);

        insertTestGeneticResource(session, p1, "tomato123", VisibilityType.PUBLIC);
        insertTestGeneticResource(session, p1, "potato123", VisibilityType.PRIVATE);

        insertTestGeneticResource(session, pl1, "banana123", VisibilityType.PUBLIC);
        insertTestGeneticResource(session, pl1, "lettuce123", VisibilityType.PRIVATE);

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

        ContractDAO contractDAO = new ContractDAOImpl(session);
        contractDAO.insert(c1, true);

    }

}
