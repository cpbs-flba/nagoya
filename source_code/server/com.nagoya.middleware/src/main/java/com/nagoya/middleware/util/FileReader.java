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
package com.nagoya.middleware.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileReader {
	
	private static final Logger LOGGER = LogManager.getLogger(FileReader.class);

	public static boolean directoryExists(String path) {
		ClassLoader classLoader = FileReader.class.getClassLoader();
		URL resource = classLoader.getResource(path);
		if (resource == null) {
			return false;
		}
		File file = new File(resource.getFile());
		if (file.exists() && file.isDirectory()) {
			return true;
		}
		return false;
	}
	
	public static String readFile(String path, Charset encoding) {
		ClassLoader classLoader = FileReader.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(path);
		ByteArrayOutputStream result = null;
		try {
			result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			byte[] byteArray = result.toByteArray();
			String stringResult = new String(byteArray, encoding);
			return stringResult;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
}
