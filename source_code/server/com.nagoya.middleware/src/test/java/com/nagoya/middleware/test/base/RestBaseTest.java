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

package com.nagoya.middleware.test.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.nagoya.dao.DAOTest;
import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.middleware.main.JettyStopper;
import com.nagoya.middleware.main.Main;
import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;

public class RestBaseTest extends DAOTest {

    private static final Logger LOGGER    = LogManager.getLogger(RestBaseTest.class);

    public static String        serverURL = "";

    private Session             session   = null;

    public RestBaseTest() {
        serverURL = "http://localhost:" + ServerPropertiesProvider.getInteger(ServerProperty.SERVER_PORT)
            + ServerPropertiesProvider.getString(ServerProperty.SERVER_CONTEXT_PATH);
    }

    @BeforeEach
    public void init() {
        session = ConnectionProvider.getInstance().getSession();
        initializeEnvironment(session);
        try {
            serverStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        if (session != null) {
            ConnectionProvider.getInstance().closeSession(session);
        }
        new JettyStopper().start();
    }

    public static void serverStart()
        throws Exception {

        // wait for 5s to shutdown the previous test
        try {
            LOGGER.debug("Waiting for server to shutdown before (re)starting...");
            Thread.sleep(7000);
        } catch (Exception e) {
            // noop
        }

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
    public static void serverClose()
        throws Exception {
        new JettyStopper().start();
    }

}
