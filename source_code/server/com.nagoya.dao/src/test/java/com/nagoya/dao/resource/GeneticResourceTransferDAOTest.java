package com.nagoya.dao.resource;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.model.dbo.person.PersonLegal;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.GeneticResourceTransfer;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * 
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceTransferDAOTest extends DAOTest {

	private Session session = null;

	@BeforeEach
	public void init() {
		session = super.getSession();
		initializeEnvironment(session);
	}

	@Test
	@DisplayName("Test genetic resource: insert and search")
	public void insertContractTest() throws InvalidObjectException, ResourceOutOfDateException {
		GeneticResourceDAO dao = new GeneticResourceDAOImpl(session);

		// insert test data
		PersonLegal sender = getLegalTestPerson("sender@sender.com");
		dao.insert(sender, true);
		PersonLegal receiver = getLegalTestPerson("receiver@recevier.com");
		dao.insert(receiver, true);

		GeneticResource resource = new GeneticResource();
		resource.setIdentifier("i123x");
		resource.setDescription("sonneblume1");
		resource.setOwner(sender);
		resource.setVisibilityType(VisibilityType.PRIVATE);
		resource.setSource("Brasil");
		dao.insert(resource, true);

		GeneticResourceTransfer geneticResourceTransfer = new GeneticResourceTransfer();
		geneticResourceTransfer.getGeneticResources().add(resource);
		geneticResourceTransfer.setSender(sender);
		geneticResourceTransfer.setReceiver(receiver);
		// persist
		dao.insert(geneticResourceTransfer, true);

		Assert.assertNotNull(geneticResourceTransfer.getId());
	}

	private PersonLegal getLegalTestPerson(String emailpw) {
		PersonLegal legalPerson = new PersonLegal();
		legalPerson.setEmail(emailpw);
		legalPerson.setPassword(emailpw);
		legalPerson.setName("Test");
		legalPerson.setCommercialRegisterNumber("c123");
		legalPerson.setTaxNumber("tax123");
		return legalPerson;
	}

}
