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

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class FileReaderTest {

	@Test
	void test() {
		Assert.assertTrue(FileReader.directoryExists("mail/de"));
		Assert.assertFalse(FileReader.directoryExists("mail/fr"));
	}

}
