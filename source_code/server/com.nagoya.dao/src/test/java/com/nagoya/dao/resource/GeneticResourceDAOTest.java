
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
import com.nagoya.model.dbo.contract.Contract;
import com.nagoya.model.dbo.contract.ContractResource;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.Address;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.ResourceFile;
import com.nagoya.model.dbo.resource.Taxonomy;
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
        Address address = new Address();
        address.setStreet("s");
        address.setNumber("132");
        address.setZip("132");
        address.setCountry("Bla");
        address.setCity("A");

        PersonLegal legalPerson = new PersonLegal();
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

        GeneticResource resource1 = new GeneticResource();
        resource1.setIdentifier("i123x");
        resource1.setDescription("sonneblume1");
        resource1.setOwner(legalPerson);
        resource1.setVisibilityType(VisibilityType.PRIVATE);
        resource1.setSource("Brasil");
        ResourceFile rf = new ResourceFile();
        rf.setContent("test".getBytes());
        rf.setName("sometext");
        rf.setType("txt");
        resource1.getFiles().add(rf);
        dao.insert(resource1, true);

        GeneticResource resource2 = new GeneticResource();
        resource2.setIdentifier("a568x");
        resource2.setDescription("sonneblume2");
        resource2.setOwner(legalPerson);
        resource2.setVisibilityType(VisibilityType.PUBLIC);
        resource2.setSource("Brasil");

        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName("Plantae");
        Taxonomy c1 = new Taxonomy();
        c1.setName("Blumen");
        c1.setParent(taxonomy);
        Taxonomy c2 = new Taxonomy();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        resource2.setTaxonomy(c2);
        dao.insert(resource2, true);

        GeneticResourceFilter filter = new GeneticResourceFilter();
        filter.setIdentifier("5");
        List<GeneticResource> search = dao.search(filter, legalPerson, 20);
        Assert.assertEquals(1, search.size());

        GeneticResourceFilter filter2 = new GeneticResourceFilter();
        filter2.setIdentifier("x");
        List<GeneticResource> search2 = dao.search(filter2, legalPerson, 20);
        Assert.assertEquals(2, search2.size());

        GeneticResourceFilter filter3 = new GeneticResourceFilter();
        filter3.setIdentifier("3");
        filter3.setDescription("e");
        List<GeneticResource> search3 = dao.search(filter3, legalPerson, 20);
        Assert.assertEquals(1, search3.size());

        PersonLegal legalPerson2 = new PersonLegal();
        legalPerson2.setEmail("legal@legal.com");
        legalPerson2.setPassword("secret");
        legalPerson2.setAddress(address);
        legalPerson2.setName("Legal Corp.");
        legalPerson2.setCommercialRegisterNumber("c123");
        legalPerson2.setTaxNumber("tax123");
        legalPerson2.setPersonType(PersonType.LEGAL);
        dao.insert(legalPerson2, true);

        List<GeneticResource> search4 = dao.search(null, legalPerson2, 20);
        Assert.assertEquals(1, search4.size());

        // contract adding
        Contract contract = new Contract();
        contract.setSender(legalPerson);
        contract.setReceiver(legalPerson2);
        contract.setStatus(Status.CREATED);
        ContractResource contractResource = new ContractResource();
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
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName("Plantae");

        Taxonomy c1 = new Taxonomy();
        c1.setName("Blumen");
        c1.setParent(taxonomy);

        Taxonomy c2 = new Taxonomy();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        dao.insert(taxonomy, true);

        Taxonomy taxonomy2 = new Taxonomy();
        taxonomy2.setName("Algae");

        Taxonomy c11 = new Taxonomy();
        c11.setName("Blumen2");
        c11.setParent(taxonomy2);

        Taxonomy c22 = new Taxonomy();
        c22.setName("Sonnenblume2");
        c22.setParent(c11);

        dao.insert(taxonomy2, true);

        List<Taxonomy> taxonomyRootLevel = dao.getTaxonomyRootLevel();
        Assert.assertEquals(2, taxonomyRootLevel.size());
    }

    @Test
    @DisplayName("Search taxonomy children for parent")
    public void testTaxonomyChildrenSearch() {
        GeneticResourceDAO dao = new GeneticResourceDAOImpl(session);

        // insert test data 1
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setName("Plantae");

        Taxonomy c1 = new Taxonomy();
        c1.setName("Blumen");
        c1.setParent(taxonomy);

        Taxonomy c2 = new Taxonomy();
        c2.setName("Sonnenblume");
        c2.setParent(c1);
        dao.insert(c2, true);

        // test data 2
        Taxonomy taxonomy2 = new Taxonomy();
        taxonomy2.setName("Algae");

        Taxonomy c11 = new Taxonomy();
        c11.setName("Blumen2");
        c11.setParent(taxonomy2);

        Taxonomy c22 = new Taxonomy();
        c22.setName("Sonnenblume2");
        c22.setParent(c11);
        dao.insert(c22, true);

        // verify
        List<Taxonomy> taxonomyRootLevel = dao.getTaxonomyChildren(taxonomy.getId());
        Assert.assertEquals(1, taxonomyRootLevel.size());
        Assert.assertEquals(c1.getName(), taxonomyRootLevel.get(0).getName());

    }

}
