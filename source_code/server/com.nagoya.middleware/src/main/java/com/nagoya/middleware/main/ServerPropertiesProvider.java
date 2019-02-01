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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.nagoya.common.util.StringUtil;

public class ServerPropertiesProvider {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("serverconfig");

    /**
     * Returns the value as String for the given key.
     *
     * @param property
     * @return String value of the property
     */
    public static String getString(ServerProperty property) {
        String valueAsString = BUNDLE.getString(property.getProperty());
        return valueAsString;
    }

    /**
     * Returns the value as Boolean for the given key.
     *
     * @param property
     * @return Boolean value of the property
     */
    public static boolean getBoolean(ServerProperty property) {
        String valueAsString = BUNDLE.getString(property.getProperty());
        boolean valueAsBoolean = Boolean.parseBoolean(valueAsString);
        return valueAsBoolean;
    }

    /**
     * Returns the value as Boolean for the given key. If no property is defined, returns the default value.
     *
     * @param property
     * @return Boolean value of the property
     */
    public static boolean getBoolean(ServerProperty property, boolean defaultValue) {
        try {
            String valueAsString = BUNDLE.getString(property.getProperty());
            boolean valueAsBoolean = Boolean.parseBoolean(valueAsString);
            return valueAsBoolean;
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the value as Integer for the given key.
     * 
     * @param property
     * @return Integer value of the property
     */
    public static int getInteger(ServerProperty property) {
        String valueAsString = BUNDLE.getString(property.getProperty());
        int valueToReturn = Integer.parseInt(valueAsString);
        return valueToReturn;
    }

    /**
     * Returns the value as Integer for the given key.
     * 
     * @param property
     * @return Integer value of the property
     */
    public static int getInteger(ServerProperty property, int defaultValue) {
        String valueAsString = BUNDLE.getString(property.getProperty());
        if (StringUtil.isNullOrBlank(valueAsString)) {
            return defaultValue;
        }
        int valueToReturn = Integer.parseInt(valueAsString);
        return valueToReturn;
    }
}
