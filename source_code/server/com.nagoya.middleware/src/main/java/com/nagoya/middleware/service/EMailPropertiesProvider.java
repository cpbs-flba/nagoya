/*
 * Copyright (c) 2016. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 * 
 * Author: Florin Bogdan Balint
 * 
 */
package com.nagoya.middleware.service;

import java.util.ResourceBundle;

import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;

/**
 * Provider class which reads the properties from the properties file:
 * message_queue.properties
 * 
 * @author Florin Bogdan Balint
 *
 */
public class EMailPropertiesProvider {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(ServerPropertiesProvider.getString(ServerProperty.EMAIL_CONFIG_FILENAME));

	/**
	 * Returns the value as String for the given key.
	 *
	 * @param query the properties key
	 * @return String value of the property
	 */
	public static String getString(EMailMessageProperty property) {
		String valueAsString = BUNDLE.getString(property.getProperty());
		return valueAsString;
	}

	/**
	 * Returns the value as Integer for the given key.
	 * 
	 * @param property
	 * @return Integer value of the property
	 */
	public static int getInteger(EMailMessageProperty property) {
		String valueAsString = BUNDLE.getString(property.getProperty());
		int valueToReturn = Integer.parseInt(valueAsString);
		return valueToReturn;
	}

	/**
	 * Returns the value as Boolean for the given key.
	 * 
	 * @param property
	 * @return Boolean value of the property
	 */
	public static boolean getBoolean(EMailMessageProperty property) {
		String valueAsString = BUNDLE.getString(property.getProperty());
		boolean valueToReturn = Boolean.parseBoolean(valueAsString);
		return valueToReturn;
	}
}
