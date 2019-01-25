/**
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author flba
 *
 */
public class TestConnectionProvider {

    private static final Logger           LOGGER = LogManager.getLogger(TestConnectionProvider.class);

    private static TestConnectionProvider instance;

    private SessionFactory                sessionFactory;

    private TestConnectionProvider() {
        // A SessionFactory is set up once for an application!
        String resource = "hibernate.junit.cfg.xml";
        sessionFactory = new Configuration().configure(resource).buildSessionFactory();
    }

    public static TestConnectionProvider getInstance() {
        if (instance == null) {
            instance = new TestConnectionProvider();
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
