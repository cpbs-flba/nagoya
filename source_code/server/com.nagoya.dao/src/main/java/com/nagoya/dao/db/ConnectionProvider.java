/*******************************************************************************
 * Copyright (c) 2004 - 2019 CPB Software AG
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 *
 * This software is published under the Apache License, Version 2.0, January 2004, 
 * http://www.apache.org/licenses/
 *  
 * Author: Florin Bogdan Balint
 *******************************************************************************/

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

    private static final Logger       LOGGER = LogManager.getLogger(ConnectionProvider.class);

    private static ConnectionProvider instance;

    private static SessionFactory     sessionFactory;

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
        LOGGER.debug(Thread.currentThread().getId() + " - opening session.");
        Session session = sessionFactory.openSession();
        return session;
    }

    public void closeSession(Session session) {
        LOGGER.debug(Thread.currentThread().getId() + " - closing session.");
        if (session == null) {
            LOGGER.warn("Session was NULL!");
            return;
        }
        session.clear();
        session.close();
    }

}
