package com.nagoya.dao.person;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.model.dbo.person.Address;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.person.group.PersonGroup;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.ResourceFile;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;

public class PersonDAOTest extends DAOTest {

	private Session session = null;

	@BeforeEach
	public void init() {
		session = super.getSession();
		initializeEnvironment(session);
	}

	@Test
	@DisplayName("Test person: legal person insert, update and delete")
	public void insertAndDeleteLegalPerson() throws InvalidObjectException, ResourceOutOfDateException {
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
		BasicDAO<Person> personDAO = new BasicDAOImpl<Person>(session);
		personDAO.insert(legalPerson, true);
		Assert.assertNotNull(address.getId());
		Assert.assertNotNull(legalPerson.getId());

		PersonGroup pg = new PersonGroup();
		pg.setName("testgroup");
		pg.getPersons().add(legalPerson);
		personDAO.insert(pg, true);
		
		GeneticResource resource = new GeneticResource();
		resource.setIdentifier("i123");
		resource.setDescription("sonneblume");
		resource.setOwner(legalPerson);
		resource.setVisibilityType(VisibilityType.PUBLIC);
		resource.setSource("Brasil");
		ResourceFile rf = new ResourceFile();
		rf.setContent("test".getBytes());
		rf.setName("sometext");
		rf.setType("txt");
		resource.getFiles().add(rf);
		personDAO.insert(resource, true);

		// update the legal person
		legalPerson.setName("Legal Corp. 2 ");
		personDAO.update(legalPerson, true);

		// delete the person
		// personDAO.delete(legalPerson, true);
	}

}
