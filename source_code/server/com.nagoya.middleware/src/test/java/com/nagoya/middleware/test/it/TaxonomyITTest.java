
package com.nagoya.middleware.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.middleware.test.base.RestBaseTest;
import com.nagoya.model.dbo.resource.TaxonomyDBO;

/**
 * @author flba
 *
 */
public class TaxonomyITTest extends RestBaseTest {

	private static final Logger LOGGER = LogManager.getLogger(TaxonomyITTest.class);

	@Test
	@DisplayName("taxonomy root level search")
	public void simpleTaxonomySearchTest() throws Exception {
		// insert some dummy data
		insertDummyLegalPerson();
		insertTestTaxonomy();

		// first login
		String targetUrl = serverURL + "/users/login";
		LOGGER.debug("Sending request POST: " + targetUrl);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUrl);

		com.nagoya.model.to.person.PersonLegalTO personTO = new com.nagoya.model.to.person.PersonLegalTO();
		personTO.setEmail("test@test.com1");
		personTO.setPassword("test@test.com1");
		Entity<com.nagoya.model.to.person.PersonLegalTO> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

		Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
		String authHeader = response.getHeaderString("Authorization");
		int status = response.getStatus();
		response.close();
		Assert.assertEquals(200, status);

		// now search for taxonomy
		targetUrl = serverURL + "/genetics/search/taxonomy";
		target = client.target(targetUrl);
		response = target.request(MediaType.APPLICATION_JSON).header("Authorization", authHeader).get();
		status = response.getStatus();
		Assert.assertEquals(200, status);

		String body = response.readEntity(String.class);
		LOGGER.debug("Received response: \r\n" + body);
		Assert.assertNotNull(body);

		response.close();
	}

	@Test
	@DisplayName("taxonomy 2nd level search")
	public void secondLevelTaxonomySearchTest() throws Exception {
		// insert some dummy data
		insertDummyLegalPerson();
		long insertTestTaxonomy = insertTestTaxonomy();

		// first login
		String targetUrl = serverURL + "/users/login";
		LOGGER.debug("Sending request POST: " + targetUrl);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUrl);

		com.nagoya.model.to.person.PersonLegalTO personTO = new com.nagoya.model.to.person.PersonLegalTO();
		personTO.setEmail("test@test.com1");
		personTO.setPassword("test@test.com1");
		Entity<com.nagoya.model.to.person.PersonLegalTO> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

		Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
		String authHeader = response.getHeaderString("Authorization");
		int status = response.getStatus();
		response.close();
		Assert.assertEquals(200, status);

		// now search for taxonomy
		targetUrl = serverURL + "/genetics/search/taxonomy/" + insertTestTaxonomy;
		target = client.target(targetUrl);
		response = target.request(MediaType.APPLICATION_JSON).header("Authorization", authHeader).get();
		status = response.getStatus();
		Assert.assertEquals(200, status);

		String body = response.readEntity(String.class);
		LOGGER.debug("Received response: \r\n" + body);
		Assert.assertNotNull(body);

		response.close();
	}

	private void insertDummyLegalPerson() {
		// save the legal person

		BasicDAO<com.nagoya.model.dbo.person.PersonLegalDBO> personDAO = new BasicDAOImpl<com.nagoya.model.dbo.person.PersonLegalDBO>(
				getSession());
		for (int i = 0; i < 20; i++) {
			// insert dummy data
			com.nagoya.model.dbo.person.PersonLegalDBO pl = new com.nagoya.model.dbo.person.PersonLegalDBO();
			pl.setEmail("test@test.com" + i);
			pl.setPassword(DefaultPasswordEncryptionProvider.encryptPassword("test@test.com" + i));
			pl.setEmailConfirmed(true);
			pl.setName("test");
			pl.setTaxNumber("test");
			pl.setCommercialRegisterNumber("test");
			personDAO.insert(pl, true);
		}
	}

	private long insertTestTaxonomy() {
		GeneticResourceDAO dao = new GeneticResourceDAOImpl(getSession());
		TaxonomyDBO taxonomy = new TaxonomyDBO();
		taxonomy.setName("Plantae");

		TaxonomyDBO c1 = new TaxonomyDBO();
		c1.setName("Blumen");
		c1.setParent(taxonomy);

		TaxonomyDBO c2 = new TaxonomyDBO();
		c2.setName("Sonnenblume");
		c2.setParent(c1);
		dao.insert(c2, true);

		TaxonomyDBO taxonomy2 = new TaxonomyDBO();
		taxonomy2.setName("Algae");

		TaxonomyDBO c11 = new TaxonomyDBO();
		c11.setName("Blumen2");
		c11.setParent(taxonomy2);

		TaxonomyDBO c22 = new TaxonomyDBO();
		c22.setName("Sonnenblume2");
		c22.setParent(c11);
		dao.insert(c22, true);

		return taxonomy.getId();
	}

}
