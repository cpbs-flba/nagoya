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

package com.nagoya.blockchain.api;

import java.io.IOException;
import java.util.List;

import com.nagoya.model.blockchain.Asset;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.OperationFailedException;

/**
 * @author flba
 *
 */
public interface BlockchainDriver {

    public static final String GENETIC_RESOURCE_ID = "gr_id";
    public static final String RECEIVER_CONFIRMED  = "receiver_confirmed";

    /**
     * Generates credentials for a specified user: a public key and a private key.
     * 
     * @return
     */
    public Credentials createCredentials();

    /**
     * Creates an asset.
     * 
     * @param credentials
     * @param asset
     * @return transaction ID
     */
    public String createAsset(Credentials credentials, Asset asset)
        throws NotAuthorizedException, OperationFailedException;

    /**
     * 
     * @param sender
     * @param receiver
     * @param asset
     * @return
     */
    public String transferAsset(Credentials sender, Credentials receiver, Asset asset)
        throws NotAuthorizedException, OperationFailedException;

    /**
     * 
     * @param query
     * @return
     * @throws IOException
     */
    public List<Asset> search(String query)
        throws IOException;

}
