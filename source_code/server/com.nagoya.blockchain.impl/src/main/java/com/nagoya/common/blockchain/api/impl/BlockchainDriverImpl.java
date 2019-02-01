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

package com.nagoya.common.blockchain.api.impl;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Connection;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Output;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.model.Transactions;
import com.bigchaindb.util.KeyPairUtils;
import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.properties.BlockChainPropertiesProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.model.blockchain.Asset;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.OperationFailedException;

import io.github.novacrypto.base58.Base58;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

/**
 * 
 * @author flba
 *
 */
public class BlockchainDriverImpl implements BlockchainDriver {

    private static final int         MAX_ERROR_RETRY = 10;

    private static final Logger      LOGGER          = LogManager.getLogger(BlockchainDriverImpl.class);

    private final EdDSAParameterSpec params;

    private int                      errorCounter    = 0;

    public BlockchainDriverImpl() {
        // used for public key retrieval
        // we need the keyspec params for the re-creation of the public key
        KeyPair keyPair2 = generateKeyPair();
        EdDSAPublicKey publicKey2 = (EdDSAPublicKey) keyPair2.getPublic();
        this.params = publicKey2.getParams();

        try {
            BlockchainDriverImpl.configureConnectionManager();
        } catch (Exception e) {
            // noop
        }
    }

    public static void configureConnectionManager()
        throws TimeoutException {

        List<Connection> connections = new ArrayList<Connection>();

        // read the connections from a properties file
        for (int i = 0; i < 20; i++) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("app_id", BlockChainPropertiesProvider.getAppId());
            headers.put("app_key", BlockChainPropertiesProvider.getAppKey());

            String baseUrl = BlockChainPropertiesProvider.getURL(i);
            if (StringUtil.isNullOrBlank(baseUrl)) {
                continue;
            }

            Map<String, Object> connConfig = new HashMap<String, Object>();
            connConfig.put("baseUrl", baseUrl);
            connConfig.put("headers", headers);
            Connection connection = new Connection(connConfig);
            connections.add(connection);
        }

        BigchainDbConfigBuilder //
            .addConnections(connections) //
            .setTimeout(BlockChainPropertiesProvider.getConnectionTimeout(60000)) //
            .setup();
    }

    @Override
    public Credentials createCredentials() {
        KeyPair keyPair = generateKeyPair();
        EdDSAPublicKey publicKey = (EdDSAPublicKey) keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyString = Base58.base58Encode(publicKey.getAbyte());
        String privateKeyString = Base58.base58Encode(privateKey.getEncoded());

        LOGGER.debug("Created keys");
        LOGGER.debug("Public key: " + publicKeyString);
        LOGGER.debug("Private key: " + privateKeyString);

        Credentials credentials = new Credentials();
        credentials.setPublicKey(publicKeyString);
        credentials.setPrivateKey(privateKeyString);

        return credentials;
    }

    private EdDSAPublicKey getPublicKey(String base58EncodedKey) {
        byte[] decoded = Base58.base58Decode(base58EncodedKey);
        EdDSAPublicKeySpec keySpec = new EdDSAPublicKeySpec(decoded, params);
        EdDSAPublicKey reconverted = new EdDSAPublicKey(keySpec);
        return reconverted;
    }

    public KeyPair generateKeyPair() {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        return keyPair;
    }

    private KeyPair getKeyPairFromPrivateKey(String encodedPrivateKeyBase58) {
        byte[] encodedPrivateKeyBytes = Base58.base58Decode(encodedPrivateKeyBase58);
        KeyPair decodedKeyPair = KeyPairUtils.decodeKeyPair(encodedPrivateKeyBytes);
        return decodedKeyPair;
    }

    @Override
    public String createAsset(Credentials credentials, Asset asset)
        throws NotAuthorizedException, OperationFailedException {

        LOGGER.debug("Creating asset");

        String privateKey = credentials.getPrivateKey();
        if (StringUtil.isNullOrBlank(privateKey)) {
            throw new NotAuthorizedException();
        }

        KeyPair keyPair = getKeyPairFromPrivateKey(privateKey);

        try {
            // build and send CREATE transaction
            DefaultGenericCallback callback = new DefaultGenericCallback();
            Transaction transaction = null;

            MetaData metaData = new MetaData();
            metaData.getMetadata().putAll(asset.getMetaData());

            transaction = BigchainDbTransactionBuilder.init()//
                .addAssets(asset.getAssetData(), TreeMap.class) //
                .addOutput(asset.getAmount(), (EdDSAPublicKey) keyPair.getPublic()) //
                .addMetaData(metaData) //
                .operation(Operations.CREATE) //
                .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate()) //
                .sendTransaction(callback);

            // wait until transaction is completed
            do {
                customWait(callback);
            } while (callback.isOperationFinished() == false);
            customWait();

            // repeat if something went wrong
            if (callback.isOperationFailed()) {
                if (errorCounter < MAX_ERROR_RETRY) {
                    errorCounter++;
                    return createAsset(credentials, asset);
                } else {
                    errorCounter = 0;
                    throw new OperationFailedException(callback.getErrorMessage());
                }
            }
            errorCounter = 0;

            String transactionID = transaction.getId();
            LOGGER.debug("Asset created with transaction ID: " + transactionID);
            asset.setTxIdAssetCreation(new String(transactionID));
            asset.setTxIdAssetOperation(new String(transactionID));

            String txURL = BlockChainPropertiesProvider.getURL(0) + "/api/v1/transactions/" + transactionID;
            LOGGER.debug("URL: " + txURL);

            return transactionID;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public String transferAsset(Credentials sender, Credentials receiver, Asset asset)
        throws NotAuthorizedException, OperationFailedException {
        LOGGER.debug("Transferring asset");
        String senderPrivateKey = sender.getPrivateKey();
        if (StringUtil.isNullOrBlank(senderPrivateKey)) {
            throw new NotAuthorizedException();
        }

        EdDSAPublicKey receiversPublicKey = getPublicKey(receiver.getPublicKey());
        KeyPair senderKeyPair = getKeyPairFromPrivateKey(senderPrivateKey);

        try {
            // build and send CREATE transaction
            DefaultGenericCallback callback = new DefaultGenericCallback();

            // which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(asset.getTxIdAssetOperation());

            MetaData metaData = new MetaData();
            metaData.getMetadata().putAll(asset.getMetaData());

            // build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder.init() //
                .addInput(null, fulfill, (EdDSAPublicKey) senderKeyPair.getPublic()) //
                .addOutput(asset.getAmount(), receiversPublicKey) //
                .addAssets(asset.getTxIdAssetCreation(), String.class)//
                .addMetaData(metaData) //
                .operation(Operations.TRANSFER)//
                .buildAndSign((EdDSAPublicKey) senderKeyPair.getPublic(), (EdDSAPrivateKey) senderKeyPair.getPrivate()) //
                .sendTransaction(callback);

            // wait until transaction is completed
            do {
                customWait(callback);
            } while (callback.isOperationFinished() == false);
            customWait();

            // repeat if something went wrong
            if (callback.isOperationFailed()) {
                if (errorCounter < MAX_ERROR_RETRY) {
                    errorCounter++;
                    return transferAsset(sender, receiver, asset);
                } else {
                    errorCounter = 0;
                    throw new OperationFailedException(callback.getErrorMessage());
                }
            }
            errorCounter = 0;

            String transactionID = transaction.getId();
            LOGGER.debug("Asset transferred with transaction ID: " + transactionID);
            asset.setTxIdAssetOperation(new String(transactionID));

            String txURL = BlockChainPropertiesProvider.getURL(0) + "/api/v1/transactions/" + transactionID;
            LOGGER.debug("URL: " + txURL);
            return transactionID;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    private void customWait(DefaultGenericCallback callback) {
        // noop
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // noop
        }
    }

    private void customWait() {
        // noop
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // noop
        }
    }

    @Override
    public List<Asset> search(String query)
        throws IOException {

        List<Asset> results = new ArrayList<Asset>();

        Assets assets = AssetsApi.getAssets(query);
        List<com.bigchaindb.model.Asset> assetsList = assets.getAssets();
        for (com.bigchaindb.model.Asset asset : assetsList) {
            try {
                Asset toAdd = new Asset();

                boolean transferConfirmed = extractAsset(asset, toAdd);

                if (transferConfirmed) {
                    results.add(toAdd);
                }

            } catch (Exception e) {
                // an asset either can or cannot be extracted from the blockchain
                LOGGER.error("Could not extract data from blockchain asset.");
            }
        }
        return results;
    }

    @SuppressWarnings("all")
    private boolean extractAsset(com.bigchaindb.model.Asset asset, Asset toAdd)
        throws IOException {

        toAdd.setTxIdAssetCreation(asset.getId());

        Object data = asset.getData();
        if (data instanceof java.util.Map) {
            java.util.Map dataMap = (java.util.Map) data;
            toAdd.setAssetData(dataMap);
        } else {
            LOGGER.error("Unkown data type: " + data.getClass());
        }

        boolean transferConfirmed = false;
        Transactions txs = TransactionsApi.getTransactionsByAssetId(asset.getId(), Operations.TRANSFER);

        for (Transaction tx : txs.getTransactions()) {
            String txid = tx.getId().replaceAll("\"", "");
            List<Output> outputs = tx.getOutputs();
            for (Output output : outputs) {
                String amount = output.getAmount();
                if (StringUtil.isNotNullOrBlank(amount)) {
                    toAdd.setAmount(amount);
                }
            }
            Object metaData = tx.getMetaData();
            if (metaData instanceof java.util.Map) {
                java.util.Map metaDataMap = (java.util.Map) metaData;
                String confirmedFlag = (String) metaDataMap.get("receiver_confirmed");
                if (StringUtil.isNotNullOrBlank(confirmedFlag)) {
                    if (confirmedFlag.equals("true")) {
                        transferConfirmed = true;
                        toAdd.setMetaData(metaDataMap);
                        toAdd.setTxIdAssetConfirmation(txid);
                    } else {
                        toAdd.setTxIdAssetOperation(txid);
                    }
                }
            } else {
                LOGGER.error("Unkown metadata type: " + data.getClass());
            }
        }
        return transferConfirmed;
    }

}
