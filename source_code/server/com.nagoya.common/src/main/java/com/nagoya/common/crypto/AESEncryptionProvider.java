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

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author flba
 *
 */
public class AESEncryptionProvider {

    private static final Logger LOGGER = LogManager.getLogger(AESEncryptionProvider.class);

    private static final String AES    = "AES";

    public static String encrypt(String text, String key) {
        try {
            Key secretEncryptionKey = getSecretEncryptionKey(key);
            byte[] cipherText = encryptText(text, secretEncryptionKey);
            byte[] encoded = Base64.getEncoder().encode(cipherText);
            String encryptedText = new String(encoded, StandardCharsets.UTF_8);
            return encryptedText;
        } catch (Exception e) {
            LOGGER.error("Something went wrong while encrypting some text", e);
        }
        return null;
    }

    public static String decrypt(String cipherText, String key) {
        try {
            Key secretEncryptionKey = getSecretEncryptionKey(key);
            byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText.getBytes());
            String decryptedText = decryptText(cipherTextBytes, secretEncryptionKey);
            return decryptedText;
        } catch (Exception e) {
            LOGGER.error("Something went wrong while encrypting some text", e);
        }
        return null;
    }

    /**
     * gets the AES encryption key. In your actual programs, this should be safely stored.
     * 
     * @return
     * @throws Exception
     */
    private static Key getSecretEncryptionKey(String customKey)
        throws Exception {
        // use a salt
        byte[] saltValue = new byte[] { 'T', 'h', 'X', '.', 'I', 's', '#', 'S', ',', 'c', '6', 'e', 't', '-', '+', 'y' };

        KeySpec spec = new PBEKeySpec(customKey.toCharArray(), saltValue, 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(key, AES);

        return keySpec;
    }

    /**
     * Encrypts plainText in AES using the secret key
     * 
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    private static byte[] encryptText(String plainText, Key secKey)
        throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance(AES);
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * 
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    private static String decryptText(byte[] byteCipherText, Key secKey)
        throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance(AES);
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

}
