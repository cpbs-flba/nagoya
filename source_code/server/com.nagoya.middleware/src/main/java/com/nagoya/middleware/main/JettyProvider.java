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
