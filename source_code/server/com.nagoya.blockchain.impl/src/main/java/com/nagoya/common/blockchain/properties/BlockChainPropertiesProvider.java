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

package com.nagoya.common.blockchain.properties;

import java.util.ResourceBundle;

public class BlockChainPropertiesProvider {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("blockchain");

    /**
     * Returns the base URL of the blockchain
     * 
     * @return
     */
    public static String getString(String property, String defaultValue) {
        String valueAsString = null;
        try {
            valueAsString = BUNDLE.getString(property);
        } catch (Exception e) {
            valueAsString = defaultValue;
        }
        return valueAsString;
    }

    public static int getConnectionTimeout(int defaultValue) {
        int result = defaultValue;
        try {
            String valueAsString = BUNDLE.getString("blockchain.connection.timeout");
            result = Integer.parseInt(valueAsString);
        } catch (Exception e) {
            // noop
        }
        return result;
    }

    /**
     * Returns the base URL of the blockchain
     * 
     * @return
     */
    public static String getURL(int urlNumber) {
        return getString("blockchain.base.url." + urlNumber, null);
    }

    /**
     * Returns the base URL of the blockchain
     * 
     * @return
     */
    public static String getAppId() {
        return getString("blockchain.app.id", "");
    }

    /**
     * Returns the base URL of the blockchain
     * 
     * @return
     */
    public static String getAppKey() {
        return getString("blockchain.app.key", "");
    }
}
