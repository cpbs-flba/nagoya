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
