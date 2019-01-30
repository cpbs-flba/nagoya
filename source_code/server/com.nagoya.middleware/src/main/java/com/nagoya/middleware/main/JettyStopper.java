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

package com.nagoya.middleware.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JettyStopper extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(JettyStopper.class);

    @Override
    public void run() {
        for (int i = 5; i > 0; i--) {
            LOGGER.info("Shutting down server in " + i + " ...");
            try {
                Thread.sleep(1000l);
            } catch (Exception ex) {
                LOGGER.error("Error while stopping server: " + ex.getMessage(), ex);
            }
        }

        try {
            JettyProvider.getInstance().getServer().stop();
        } catch (Exception e) {
            LOGGER.error(e, e.getCause());
        } finally {
            // force shut-down if necessary only
            if (!JettyProvider.getInstance().getServer().isStopped()) {
                JettyProvider.getInstance().getServer().destroy();
            }
        }
        LOGGER.info("Server shutdown successfully.");
    }

}
