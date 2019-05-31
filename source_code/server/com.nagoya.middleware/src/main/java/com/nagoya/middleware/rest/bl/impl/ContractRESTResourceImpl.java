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

package com.nagoya.middleware.rest.bl.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import com.nagoya.middleware.rest.bl.ContractRESTResource;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.service.contract.ContractAcceptanceService;
import com.nagoya.middleware.service.contract.ContractCreationService;
import com.nagoya.middleware.service.contract.ContractDeletionService;
import com.nagoya.middleware.service.contract.ContractFileAddingService;
import com.nagoya.middleware.service.contract.ContractFileRemovalService;
import com.nagoya.middleware.service.contract.ContractFileRetrievalService;
import com.nagoya.middleware.service.contract.ContractRejectionService;
import com.nagoya.middleware.service.contract.ContractSearchService;
import com.nagoya.model.to.contract.ContractFileTO;
import com.nagoya.model.to.contract.ContractTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractRESTResourceImpl implements ContractRESTResource {

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractResource#create(java.lang.String, java.lang.String, com.nagoya.model.dto.contract.Contract,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void addContract(String authorization, String language, String privateKey, ContractTO contractTO, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractCreationService(authorization, language);
        Response response = service.runService(privateKey, contractTO);
        asyncResponse.resume(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractResource#delete(java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void deleteContract(String authorization, String language, String contractId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractDeletionService(authorization, language);
        Response response = service.runService(contractId);
        asyncResponse.resume(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#search(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void search(String authorization, String language, String contractStatus, String dateFrom, String dateUntil, String role,
        AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractSearchService(authorization, language);
        Response response = service.runService(contractStatus, dateFrom, dateUntil, role);
        asyncResponse.resume(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#addFile(java.lang.String, java.lang.String, java.lang.String,
     * com.nagoya.model.to.contract.ContractFileTO, javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void addFile(String authorization, String language, String contractId, ContractFileTO contractFileTO, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractFileAddingService(authorization, language);
        Response response = service.runService(contractId, contractFileTO);
        asyncResponse.resume(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#deleteFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void deleteFile(String authorization, String language, String contractId, String fileId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractFileRemovalService(authorization, language);
        Response response = service.runService(contractId, fileId);
        asyncResponse.resume(response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nagoya.middleware.rest.bl.ContractRESTResource#retrieveFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * javax.ws.rs.container.AsyncResponse)
     */
    @Override
    public void retrieveFile(String authorization, String language, String contractId, String fileId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractFileRetrievalService(authorization, language);
        Response response = service.runService(contractId, fileId);
        asyncResponse.resume(response);
    }

    @Override
    public void acceptContract(String authorization, String language, String tokenId, String privateKey, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractAcceptanceService(authorization, language);
        Response response = service.runService(tokenId, privateKey);
        asyncResponse.resume(response);
    }

    @Override
    public void rejectContract(String authorization, String language, String tokenId, AsyncResponse asyncResponse) {
        DefaultSecureResourceService service = new ContractRejectionService(authorization, language);
        Response response = service.runService(tokenId);
        asyncResponse.resume(response);
    }

}
