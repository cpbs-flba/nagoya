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

import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractFileDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.to.contract.ContractFileTO;
import com.nagoya.model.to.contract.ContractFileTransformer;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractFileAddingService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractFileAddingService.class);
    private ContractDAO         contractDAO;

    public ContractFileAddingService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        LOGGER.debug("Adding file");
        contractDAO = new ContractDAOImpl(getSession());

        String contractId = (String) params[0];
        ContractFileTO contractFileTO = (ContractFileTO) params[1];

        if (contractFileTO == null) {
            throw new BusinessLogicException(400, "E400_FILE_MISSING", "E400_FILE_MISSING");
        }

        if (StringUtil.isNullOrBlank(contractId)) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_MISSING", "E400_CONTRACT_ID_MISSING");
        }

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_MISSING", "E400_CONTRACT_ID_MISSING");
        }

        ContractDBO contractDBO;
        try {
            contractDBO = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException("Multiple contracts for the same ID found. ID: " + contractIdLong);
        }

        // verify sender
        long longValueUser = getOnlineUser().getPerson().getId().longValue();
        long longValueSender = contractDBO.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_INVALID", "Invalid contract ID");
        }

        ContractFileDBO contractFileDBO = ContractFileTransformer.getDBO(contractFileTO);
        contractDBO.getFiles().add(contractFileDBO);

        try {
            contractDAO.update(contractDBO, true);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new BusinessLogicException(400, "E400_TRY_AGAIN", "E400_TRY_AGAIN");
        }

        return null;
    }

}
