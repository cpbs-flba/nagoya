
package com.nagoya.dao.resource;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.model.dbo.contract.Contract;
import com.nagoya.model.dbo.contract.ContractResource;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.resource.GeneticResource;

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
        PersonLegal p1 = insertTestPersonLegal(session, "t1@t1.com");
        PersonLegal p2 = insertTestPersonLegal(session, "t2@t2.com");
        GeneticResource gr = insertTestGeneticResource(session, p1, "sonnenblume123");

        Contract c1 = new Contract();
        c1.setSender(p1);
        c1.setReceiver(p2);
        c1.setStatus(Status.CREATED);

        ContractResource cr1 = new ContractResource();
        cr1.setGeneticResource(gr);
        cr1.setAmount(BigDecimal.ONE);
        cr1.setMeasuringUnit("kg");
        c1.getContractResources().add(cr1);

        ContractDAO contractDAO = new ContractDAOImpl(session);
        contractDAO.insert(c1, true);

        List<Contract> search1 = contractDAO.search(p1, null, null, null, 0);
        Assert.assertEquals(1, search1.size());

        List<Contract> search2 = contractDAO.search(p2, null, null, null, 0);
        Assert.assertEquals(1, search2.size());
    }

}
