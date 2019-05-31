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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bigchaindb.model.GenericCallback;

import okhttp3.Response;

public class DefaultGenericCallback implements GenericCallback {

    private static final Logger LOGGER            = LogManager.getLogger(DefaultGenericCallback.class);

    private boolean             operationFinished = false;
    private boolean             operationFailed   = false;
    private String              errorMessage;

    @Override
    public void pushedSuccessfully(Response response) {
        LOGGER.debug("Blockchain operation finished succesfully.");
        this.operationFinished = true;
    }

    @Override
    public void transactionMalformed(Response response) {
        errorMessage = "\r\nTransaction Malformed.";
        errorMessage += "\r\nCode: " + response.code();
        errorMessage += "\r\nMessage: " + response.message();
        try {
            errorMessage += "\r\nBody: " + response.body().string();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        LOGGER.error(errorMessage);
        this.operationFinished = true;
        this.operationFailed = true;
    }

    @Override
    public void otherError(Response response) {
        errorMessage = "\r\nOther error.";
        errorMessage += "\r\nCode: " + response.code();
        errorMessage += "\r\nMessage: " + response.message();
        try {
            errorMessage += "\r\nBody: " + response.body().string();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        LOGGER.error(errorMessage);
        this.operationFinished = true;
        this.operationFailed = true;
    }

    /**
     * @return the operationFinished
     */
    public boolean isOperationFinished() {
        return operationFinished;
    }

    /**
     * @return the operationFailed
     */
    public boolean isOperationFailed() {
        return operationFailed;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
