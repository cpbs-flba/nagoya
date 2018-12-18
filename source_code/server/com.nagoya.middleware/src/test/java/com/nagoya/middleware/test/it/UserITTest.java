
package com.nagoya.middleware.test.it;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.nagoya.middleware.test.base.RestBaseTest;

/**
 * @author flba
 *
 */
public class UserITTest extends RestBaseTest {

	private static final Logger LOGGER = LogManager.getLogger(UserITTest.class);

	@Test
	@DisplayName("simple login")
	public void simpleLoginTest() throws Exception {
		// insert some dummy data
		insertDummyLegalPerson();

		String targetUrl = serverURL + "/users/login";
		LOGGER.debug("Sending request POST: " + targetUrl);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUrl);

		com.nagoya.model.to.person.PersonLegal personTO = new com.nagoya.model.to.person.PersonLegal();
		personTO.setEmail("test@test.com1");
		personTO.setPassword("test@test.com1");
		Entity<com.nagoya.model.to.person.PersonLegal> entity = Entity.entity(personTO, MediaType.APPLICATION_JSON);

		Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
		int status = response.getStatus();
		response.close();
		Assert.assertEquals(200, status);
	}

	@Test
	@DisplayName("parallel login requests")
	public void parallelLoginTest() throws Exception {
		// insert some dummy data
		insertDummyLegalPerson();

		final String targetUrl = serverURL + "/users/login";

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 20; i++) {
			executorService.execute(new LoginThread(targetUrl, 1));
		}

		LOGGER.debug("Shutting down threads.");
		executorService.shutdown();
		while (!executorService.isTerminated()) {
			// wait
		}
	}
	
	@Test
	@DisplayName("sequential login requests")
	public void sequentialLoginTest() throws Exception {
		// insert some dummy data
		insertDummyLegalPerson();

		final String targetUrl = serverURL + "/users/login";

		for (int i = 0; i < 10; i++) {
			new LoginThread(targetUrl, i).run();
		}
	}

	private void insertDummyLegalPerson() {
		// save the legal person
		
		BasicDAO<com.nagoya.model.dbo.person.PersonLegal> personDAO = new BasicDAOImpl<com.nagoya.model.dbo.person.PersonLegal>(
				getSession());
		for (int i = 0; i < 20; i++) {
			// insert dummy data
			com.nagoya.model.dbo.person.PersonLegal pl = new com.nagoya.model.dbo.person.PersonLegal();
			pl.setEmail("test@test.com" + i);
			pl.setPassword(DefaultPasswordEncryptionProvider.encryptPassword("test@test.com" + i));
			pl.setEmailConfirmed(true);
			pl.setName("test");
			pl.setTaxNumber("test");
			pl.setCommercialRegisterNumber("test");
			personDAO.insert(pl, true);
		}
	}

}
