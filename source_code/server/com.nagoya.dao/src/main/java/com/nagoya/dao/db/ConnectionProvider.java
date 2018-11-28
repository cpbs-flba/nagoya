/**
 * 
 */
package com.nagoya.dao.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author flba
 *
 */
public class ConnectionProvider {

	private static final Logger LOGGER = LogManager.getLogger(ConnectionProvider.class);

	private static ConnectionProvider instance;

	private SessionFactory sessionFactory;

	private ConnectionProvider() {
		// A SessionFactory is set up once for an application!
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	public static ConnectionProvider getInstance() {
		if (instance == null) {
			instance = new ConnectionProvider();
		}
		return instance;
	}

	public Session getSession() {
		LOGGER.debug("Opening session.");
		Session session = sessionFactory.openSession();
		return session;
	}

	public void closeSession(Session session) {
		LOGGER.debug("Closing session.");
		if (session == null) {
			return;
		}
		session.clear();
		session.close();
	}

}
