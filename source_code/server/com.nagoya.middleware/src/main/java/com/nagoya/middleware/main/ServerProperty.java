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
    EMAIL_CONFIG_FILENAME("email.config.filename") //
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
