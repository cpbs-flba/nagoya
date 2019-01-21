
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
import com.nagoya.model.dbo.resource.GeneticResourceDBO;

public class InertTestData extends DAOTest {

    private Session session = null;

    @BeforeEach
    public void init() {
        session = super.getSession();
        initializeEnvironment(session);
    }

    @Test
    @DisplayName("Test contract: insert and search")
    public void searchGeneticResourceTest() {
        PersonLegalDBO p1 = insertTestPersonLegal(session, "florin.balint@cpb-software.com");
        PersonLegalDBO p2 = insertTestPersonLegal(session, "test2@test2.com");
        GeneticResourceDBO gr = insertTestGeneticResource(session, p1, "sonnenblume123");

        Calendar cal = Calendar.getInstance();
        Date contractCreationDate = cal.getTime();

        ContractDBO c1 = new ContractDBO();
        c1.setSender(p1);
        c1.setReceiver(p2);
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
