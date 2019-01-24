/*
 * (C) Copyright 2004 - 2019 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : Florin Bogdan Balint
 */

package com.nagoya.common.crypto;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AESEncryptionProviderTest {

    @Test
    @DisplayName("simple positive test")
    public void tp1() {
        String toEncrypt = "hello world text";
        String secretKey = "somePassword";

        // encrypt and verify
        String encryptedText = AESEncryptionProvider.encrypt(toEncrypt, secretKey);
        Assert.assertNotNull(encryptedText);

        // decrypt and verify
        String decryptedText = AESEncryptionProvider.decrypt(encryptedText, secretKey);
        Assert.assertEquals(toEncrypt, decryptedText);
    }
}
