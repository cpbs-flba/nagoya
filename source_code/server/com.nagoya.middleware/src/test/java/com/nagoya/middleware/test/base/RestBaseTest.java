package com.nagoya.middleware.test.base;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.middleware.main.JettyStopper;
import com.nagoya.middleware.main.Main;
import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RestBaseTest extends DAOTest {

	private static final Logger LOGGER = LogManager.getLogger(RestBaseTest.class);

	public static String serverURL = "";

	private Session session = null;

	public RestBaseTest() {
		serverURL = "http://localhost:" + ServerPropertiesProvider.getInteger(ServerProperty.SERVER_PORT)
				+ ServerPropertiesProvider.getString(ServerProperty.SERVER_CONTEXT_PATH);
	}

	@BeforeEach
	public void init() {
		session = ConnectionProvider.getInstance().getSession();
		initializeEnvironment(session);
	}

	@AfterEach
	public void tearDown() {
		if (session != null) {
			ConnectionProvider.getInstance().closeSession(session);
		}
	}

	@BeforeAll
	public static void serverStart() throws Exception {

		Thread serverThread = new Thread() {
			@Override
			public void run() {
				try {
					Main.main(null);
				} catch (Exception e) {
					LOGGER.error(e, e);
				}
			}
		};
		serverThread.start();
	}

	@AfterAll
	public static void serverClose() throws Exception {
		new JettyStopper().start();
	}

}
