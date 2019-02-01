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
package com.nagoya.common.crypto;

/**
 * 
 * Encrypts passwords using BCRYPT and a randomly generated salt for each
 * password.
 * 
 * @author Florin Bogdan Balint
 *
 */

public class DefaultPasswordEncryptionProvider {

	/**
	 * Description: encrypts a password using the BCRYPT algorithm. For each
	 * password, a random salt is generated.
	 * 
	 * @return encrypted password
	 */
	public static String encryptPassword(String password) {
		String hashpw = BCrypt.hashpw(password, BCrypt.gensalt(12));
		return hashpw;
	}

	/**
	 * Description: Checks if a password equals to an already encrypted password
	 * (which already contains the salt).
	 * 
	 * @param encryptedPassword
	 * @param password
	 * @return true if the two passwords already match
	 */
	public static boolean isPasswordCorrect(String encryptedPassword, String password) {
		boolean match = BCrypt.checkpw(password, encryptedPassword);
		return match;
	}

}
