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

import org.eclipse.jetty.server.Server;

public class JettyProvider {

    private static JettyProvider instance;

    private Server               server;

    private JettyProvider() {
        server = new Server();
    }

    public static JettyProvider getInstance() {
        if (JettyProvider.instance == null) {
            JettyProvider.instance = new JettyProvider();
        }
        return JettyProvider.instance;
    }

    public synchronized Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
