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

public enum ServerProperty {
    SERVER_PROTOCOL("server.protocol"), //
    SERVER_HOST("server.host"), //

    SERVER_ADMIN_USER("server.admin.user"), //
    SERVER_ADMIN_PASSWORD("server.admin.password"), //

    SERVER_PORT_HTTPS("server.port.https"), //
    SERVER_KEYSTORE("server.keystore"), //
    SERVER_KEYSTORE_PASSWORD("server.keystore.password"), //
    SERVER_PORT("server.port"), //
    SERVER_CONTEXT_PATH("server.context.path"), //
    SERVER_EIS_CONFIG("server.eis.config"), //
    SERVER_ENABLE_CROSS_ORIGIN("server.enable.cross.origin"), //

    SERVER_HOST_NAME("server.host.name"), //
    SERVER_MAIL_CONFIRMATION_PATH("server.mail.confirmation.path"), //
    EMAIL_CONFIG_FILENAME("email.config.filename"), //

    // BUSINESS LOGIC VALUES BELOW...
    CONTRACT_EXPIRAETION_TIME("contract.expiration.time")//
    ;

    private String property;

    private ServerProperty(String property) {
        this.property = property;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
}
