/*
 * Copyright (c) 2018. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 * 
 * Author: Florin Bogdan Balint
 * 
 */
/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : flba
 */

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
