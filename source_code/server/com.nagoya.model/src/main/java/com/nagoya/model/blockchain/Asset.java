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

package com.nagoya.model.blockchain;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract class representing an asset.
 * 
 * @author flba
 *
 */
public class Asset implements Serializable {

    private static final long   serialVersionUID = 1L;

    private String              amount;

    /**
     * ID of the transaction, which was generated when the asset was created.
     */
    private String              transactionId;

    /**
     * This data will be persisted permanently in the blockchain.
     */
    private Map<String, Object> assetData        = new TreeMap<>();

    /**
     * This data can be updated in the blockchain.
     */
    private Map<String, String> metaData         = new TreeMap<>();

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the assetData
     */
    public Map<String, Object> getAssetData() {
        return assetData;
    }

    /**
     * @param assetData the assetData to set
     */
    public void setAssetData(Map<String, Object> assetData) {
        this.assetData = assetData;
    }

    /**
     * @return the metaData
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }

    /**
     * @param metaData the metaData to set
     */
    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

}
