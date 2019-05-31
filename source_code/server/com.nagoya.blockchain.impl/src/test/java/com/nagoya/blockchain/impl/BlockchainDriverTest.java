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
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.model.blockchain.Asset;
import com.nagoya.model.blockchain.Credentials;

/**
 * @author flba
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockchainDriverTest {

    private static final Logger LOGGER = LogManager.getLogger(BlockchainDriverTest.class);

    @Test
    @DisplayName("Create credentials")
    public void t1()
        throws Exception {
        LOGGER.debug("\r\n\r\n\r\n");
        BlockchainDriverImpl blockchainDriver = new BlockchainDriverImpl();
        Credentials credentials = blockchainDriver.createCredentials();
        Assert.assertNotNull(credentials.getPublicKey());
        Assert.assertNotNull(credentials.getPrivateKey());
    }

    @Test
    @DisplayName("Blockchain asset creation test")
    public void t2()
        throws Exception {
        LOGGER.debug("\r\n\r\n\r\n");
        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentialsSender = blockchainDriver.createCredentials();

        // genetic resource
        Asset asset = new Asset();
        asset.getAssetData().put("name", "James Bond");
        asset.getAssetData().put("age", "doesn't matter");
        asset.getMetaData().put("where is he now?", "Thailand");
        asset.setAmount("1");

        blockchainDriver.createAsset(credentialsSender, asset);
        Assert.assertNotNull(asset.getTxIdAssetCreation());
    }

    @Test
    @DisplayName("Blockchain asset creation + transfer test")
    public void t3()
        throws Exception {
        LOGGER.debug("\r\n\r\n\r\n");
        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentialsSender = blockchainDriver.createCredentials();

        // genetic resource
        Asset asset = new Asset();
        asset.getAssetData().put("name", "James Bond");
        asset.getAssetData().put("age", "doesn't matter");
        asset.getMetaData().put("where is he now?", "Thailand");
        asset.setAmount("1");

        blockchainDriver.createAsset(credentialsSender, asset);
        Assert.assertNotNull(asset.getTxIdAssetCreation());

        Credentials credentialsReceiver = blockchainDriver.createCredentials();
        String txId2 = blockchainDriver.transferAsset(credentialsSender, credentialsReceiver, asset);
        Assert.assertNotNull(txId2);
    }

    @Test
    @DisplayName("Blockchain asset creation + transfer + metadata update test")
    public void t4()
        throws Exception {
        LOGGER.debug("\r\n\r\n\r\n");
        BlockchainDriver blockchainDriver = new BlockchainDriverImpl();
        Credentials credentialsSender = blockchainDriver.createCredentials();

        // genetic resource
        Asset asset = new Asset();
        asset.getAssetData().put("name", "Jack Ryan");
        asset.getAssetData().put("age", "30");
        asset.getAssetData().put("genetic_resource_id", "someid123");

        asset.getMetaData().put("home", "USA");
        asset.getMetaData().put("location", "DEU");
        asset.getMetaData().put("confirmed", "false");
        asset.setAmount("1");

        blockchainDriver.createAsset(credentialsSender, asset);
        Assert.assertNotNull(asset.getTxIdAssetCreation());

        Credentials credentialsReceiver = blockchainDriver.createCredentials();
        String txId2 = blockchainDriver.transferAsset(credentialsSender, credentialsReceiver, asset);
        Assert.assertNotNull(txId2);

        asset.getMetaData().put("confirmed", "true");
        blockchainDriver.transferAsset(credentialsReceiver, credentialsReceiver, asset);
        Assert.assertNotNull(asset.getTxIdAssetOperation());
    }

}
