
package com.nagoya.dao.resource;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractResourceDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.AddressDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.dbo.resource.ResourceFileDBO;
import com.nagoya.model.dbo.resource.TaxonomyDBO;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

public class GeneticResourceDAOTest extends DAOTest {

    private Session session = null;

    @BeforeEach
    public void init() {
        session = super.getSession();
        initializeEnvironment(session);
    }

    @Test
    @DisplayName("Test genetic resource: insert and search")
    public void searchGeneticResourceTest()
        throws InvalidObjectException, ResourceOutOfDateException {
        AddressDBO address = new AddressDBO();
        address.setStreet("s");
        address.setNumber("132");
        address.setZip("132");
        address.setCountry("Bla");
        address.setCity("A");

        PersonLegalDBO legalPerson = new PersonLegalDBO();
        legalPerson.setEmail("legal@legal.com");
        legalPerson.setPassword("secret");
        legalPerson.setAddress(address);
        legalPerson.setName("Legal Corp.");
        legalPerson.setCommercialRegisterNumber("c123");
        legalPerson.setTaxNumber("tax123");
        legalPerson.setPersonType(PersonType.LEGAL);

        // save the legal person
        GeneticResourceDAO dao = new GeneticResourceDAOImpl(session);
        dao.insert(legalPerson, true);
        Assert.assertNotNull(address.getId());
        Assert.assertNotNull(legalPerson.getId());

        GeneticResourceDBO resource1 = new GeneticResourceDBO();
        resource1.setIdentifier("i123x");
        resource1.setDescription("sonneblume1");
        resource1.setOwner(legalPerson);
        resource1.setVisibilityType(VisibilityType.PRIVATE);
        resource1.setSource("Brasil");
        ResourceFileDBO rf = new ResourceFileDBO();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource1.getFiles().add(rf);
        dao.insert(resource1, true);

        GeneticResourceDBO resource2 = new GeneticResourceDBO();
        resource2.setIdentifier("a568x");
        resource2.setDescription("sonneblume2");
        resource2.setOwner(legalPerson);
        resource2.setVisibilityType(VisibilityType.PUBLIC);
        resource2.setSource("Brasil");

        TaxonomyDBO taxonomy = new TaxonomyDBO();
        taxonomy.setName("Plantae");
        TaxonomyDBO c1 = new TaxonomyDBO();
        c1.setName("Blumen");
        c1.setParent(taxonomy);
        TaxonomyDBO c2 = new TaxonomyDBO();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        resource2.setTaxonomy(c2);
        dao.insert(resource2, true);

        GeneticResourceFilter filter = new GeneticResourceFilter();
        filter.setIdentifier("5");
        List<GeneticResourceDBO> search = dao.search(filter, legalPerson, 20);
        Assert.assertEquals(1, search.size());

        GeneticResourceFilter filter2 = new GeneticResourceFilter();
        filter2.setIdentifier("x");
        List<GeneticResourceDBO> search2 = dao.search(filter2, legalPerson, 20);
        Assert.assertEquals(2, search2.size());

        GeneticResourceFilter filter3 = new GeneticResourceFilter();
        filter3.setIdentifier("3");
        filter3.setDescription("e");
        List<GeneticResourceDBO> search3 = dao.search(filter3, legalPerson, 20);
        Assert.assertEquals(1, search3.size());

        PersonLegalDBO legalPerson2 = new PersonLegalDBO();
        legalPerson2.setEmail("legal@legal.com");
        legalPerson2.setPassword("secret");
        legalPerson2.setAddress(address);
        legalPerson2.setName("Legal Corp.");
        legalPerson2.setCommercialRegisterNumber("c123");
        legalPerson2.setTaxNumber("tax123");
        legalPerson2.setPersonType(PersonType.LEGAL);
        dao.insert(legalPerson2, true);

        List<GeneticResourceDBO> search4 = dao.search(null, legalPerson2, 20);
        Assert.assertEquals(1, search4.size());

        // contract adding
        ContractDBO contract = new ContractDBO();
        contract.setSender(legalPerson);
        contract.setReceiver(legalPerson2);
        contract.setStatus(Status.CREATED);
        ContractResourceDBO contractResource = new ContractResourceDBO();
        contractResource.setGeneticResource(resource1);
        contractResource.setAmount(new BigDecimal(100));
        contractResource.setMeasuringUnit("kg");
        contract.getContractResources().add(contractResource);

        dao.insert(contract, true);
        Assert.assertNotNull(contract.getId());
    }

    @Test
    @DisplayName("Search taxonomy root level")
    public void testTaxonomyParentSearch() {
        GeneticResourceDAO dao = new GeneticResourceDAOImpl(session);

        // insert test data
        TaxonomyDBO taxonomy = new TaxonomyDBO();
        taxonomy.setName("Plantae");

        TaxonomyDBO c1 = new TaxonomyDBO();
        c1.setName("Blumen");
        c1.setParent(taxonomy);

        TaxonomyDBO c2 = new TaxonomyDBO();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        dao.insert(taxonomy, true);

        TaxonomyDBO taxonomy2 = new TaxonomyDBO();
        taxonomy2.setName("Algae");

        TaxonomyDBO c11 = new TaxonomyDBO();
        c11.setName("Blumen2");
        c11.setParent(taxonomy2);

        TaxonomyDBO c22 = new TaxonomyDBO();
        c22.setName("Sonnenblume2");
        c22.setParent(c11);

        dao.insert(taxonomy2, true);

        List<TaxonomyDBO> taxonomyRootLevel = dao.getTaxonomyRootLevel();
        Assert.assertEquals(2, taxonomyRootLevel.size());
    }

    @Test
    @DisplayName("Search taxonomy children for parent")
    public void testTaxonomyChildrenSearch() {
        GeneticResourceDAO dao = new GeneticResourceDAOImpl(session);

        // insert test data 1
        TaxonomyDBO taxonomy = new TaxonomyDBO();
        taxonomy.setName("Plantae");

        TaxonomyDBO c1 = new TaxonomyDBO();
        c1.setName("Blumen");
        c1.setParent(taxonomy);

        TaxonomyDBO c2 = new TaxonomyDBO();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        dao.insert(c2, true);

        // test data 2
        TaxonomyDBO taxonomy2 = new TaxonomyDBO();
        taxonomy2.setName("Algae");

        TaxonomyDBO c11 = new TaxonomyDBO();
        c11.setName("Blumen2");
        c11.setParent(taxonomy2);

        TaxonomyDBO c22 = new TaxonomyDBO();
        c22.setName("Sonnenblume2");
        c22.setParent(c11);
        dao.insert(c22, true);

        // verify
        List<TaxonomyDBO> taxonomyRootLevel = dao.getTaxonomyChildren(taxonomy.getId());
        Assert.assertEquals(1, taxonomyRootLevel.size());
        Assert.assertEquals(c1.getName(), taxonomyRootLevel.get(0).getName());

    }

}
