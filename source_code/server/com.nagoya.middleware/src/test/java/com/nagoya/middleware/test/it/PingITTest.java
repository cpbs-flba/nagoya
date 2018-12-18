
package com.nagoya.middleware.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.middleware.test.base.RestBaseTest;

/**
 * @author flba
 *
 */
public class PingITTest extends RestBaseTest {

	private static final Logger LOGGER = LogManager.getLogger(PingITTest.class);

	@Test
	@DisplayName("ping - single request")
	public void simplePingTest() throws Exception {
		// request
		String targetUrl = serverURL + "/ping";
		LOGGER.debug("Sending request GET: " + targetUrl);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUrl);
		Response response = target.request().get();

		// verify response
		int status = response.getStatus();
		response.close();
		Assert.assertEquals(204, status);
	}

	@Test
	@DisplayName("ping - 500 requests")
	public void multiplePingTest() throws Exception {
		// request
		String targetUrl = serverURL + "/ping";
		LOGGER.debug("Sending request GET: " + targetUrl);

		for (int i = 1; i < 500; i++) {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(targetUrl);
			Response response = target.request().get();
			
			// verify response
			int status = response.getStatus();
			response.close();
			Assert.assertEquals(204, status);
		}
	}

}
