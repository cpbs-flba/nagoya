package com.nagoya.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DAOTest {

	private static final Logger LOGGER = LogManager.getLogger(DAOTest.class);

	public void initializeEnvironment(Session session) {
		LOGGER.info("Clearing environment.");

		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			session.createNativeQuery("DELETE FROM taddress").executeUpdate();
			session.createNativeQuery("DELETE FROM taddress_aud").executeUpdate();

			session.createNativeQuery("DELETE FROM tperson").executeUpdate();
			session.createNativeQuery("DELETE FROM tperson_aud").executeUpdate();

			session.createNativeQuery("DELETE FROM tperson_natural").executeUpdate();
			session.createNativeQuery("DELETE FROM tperson_natural_aud").executeUpdate();

			session.createNativeQuery("DELETE FROM tperson_legal").executeUpdate();
			session.createNativeQuery("DELETE FROM tperson_legal_aud").executeUpdate();

			session.createNativeQuery("DELETE FROM tclient").executeUpdate();
			session.createNativeQuery("DELETE FROM tclient_aud").executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			LOGGER.error(e);
		}

	}

	public Session getSession() {
		return TestConnectionProvider.getInstance().getSession();
	}

}
