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

package com.nagoya.middleware.service.contract;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.contract.ContractFileDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.to.contract.ContractFileTO;
import com.nagoya.model.to.contract.ContractFileTransformer;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractFileRetrievalService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractFileRetrievalService.class);

    public ContractFileRetrievalService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        LOGGER.debug("Adding file");

        // String contractId = (String) params[0];
        String fileId = (String) params[1];

        long fileIdLong = 0;
        try {
            fileIdLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_FILE_ID_MISSING", "E400_FILE_ID_MISSING");
        }

        BasicDAO<ContractFileDBO> basicDAO = new BasicDAOImpl<ContractFileDBO>(getSession());
        ContractFileDBO contractFileDBO = null;
        try {
            contractFileDBO = (ContractFileDBO) basicDAO.find(fileIdLong, ContractFileDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException(e);
        }
        if (contractFileDBO == null) {
            throw new BusinessLogicException(400, "E400_FILE_ID_INVALID", "E400_FILE_ID_INVALID");
        }

        ContractFileTO to = ContractFileTransformer.getTO(contractFileDBO);
        DefaultResponse result = new DefaultResponse(200);
        result.setEntity(to);
        return result;
    }

}
