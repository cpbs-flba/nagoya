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

package com.nagoya.blockchain.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.model.blockchain.Asset;
import com.nagoya.model.blockchain.Credentials;

/**
 * @author flba
 *
 */
public class BlockchainDriverTest {

    private static final Logger LOGGER = LogManager.getLogger(BlockchainDriverTest.class);

    @Test
    @DisplayName("Create credentials")
    public void credentialsTest()
        throws Exception {
        LOGGER.debug("Credentials");
        BlockchainDriverImpl blockchainDriver = new BlockchainDriverImpl();
        Credentials credentials = blockchainDriver.createCredentials();
        Assert.assertNotNull(credentials.getPublicKey());
        Assert.assertNotNull(credentials.getPrivateKey());
    }

    @Test
    @DisplayName("Blockchain asset creation test")
    public void createAssetTest()
        throws Exception {
        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentialsSender = blockchainDriver.createCredentials();

        // genetic resource
        Asset asset = new Asset();
        asset.getAssetData().put("name", "James Bond");
        asset.getAssetData().put("age", "doesn't matter");
        asset.getMetaData().put("where is he now?", "Thailand");
        asset.setAmount("1");

        String txId1 = blockchainDriver.createAsset(credentialsSender, asset);
        asset.setTransactionId(txId1);
        Assert.assertNotNull(txId1);
    }

    @Test
    @DisplayName("Blockchain asset creation + transfer test")
    public void createAndTransferAssetTest()
        throws Exception {
        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentialsSender = blockchainDriver.createCredentials();

        // genetic resource
        Asset asset = new Asset();
        asset.getAssetData().put("name", "James Bond");
        asset.getAssetData().put("age", "doesn't matter");
        asset.getMetaData().put("where is he now?", "Thailand");
        asset.setAmount("1");

        String txId1 = blockchainDriver.createAsset(credentialsSender, asset);
        asset.setTransactionId(txId1);
        Assert.assertNotNull(txId1);

        Credentials credentialsReceiver = blockchainDriver.createCredentials();
        String txId2 = blockchainDriver.transferAsset(credentialsSender, credentialsReceiver, asset);
        Assert.assertNotNull(txId2);
    }

}
