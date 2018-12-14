package com.nagoya.dao.person;

import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.model.dbo.person.Address;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.ResourceFile;
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
	public void searchGeneticResourceTest() throws InvalidObjectException, ResourceOutOfDateException {
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

	}

}
