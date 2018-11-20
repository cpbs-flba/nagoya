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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
}
