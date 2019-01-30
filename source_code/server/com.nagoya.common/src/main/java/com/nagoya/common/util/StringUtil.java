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
package com.nagoya.common.util;

public final class StringUtil {

	private StringUtil() {
		// helper class
	}
	
	public static boolean isNullOrBlank(String string) {
		if (string == null) {
			return true;
		}
		
		if (string.trim().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNotNullOrBlank(String string) {
		return !isNullOrBlank(string);
	}
	
}
