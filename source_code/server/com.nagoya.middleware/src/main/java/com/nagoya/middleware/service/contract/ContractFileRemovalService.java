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

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractFileDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractFileRemovalService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractFileRemovalService.class);
    private ContractDAO         contractDAO;

    public ContractFileRemovalService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        LOGGER.debug("Adding file");
        contractDAO = new ContractDAOImpl(getSession());

        String contractId = (String) params[0];
        String fileId = (String) params[1];

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_MISSING", "E400_CONTRACT_ID_MISSING");
        }

        long fileIdLong = 0;
        try {
            fileIdLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_FILE_ID_MISSING", "E400_FILE_ID_MISSING");
        }

        ContractDBO contractDBO = null;
        try {
            contractDBO = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException(e);
        }
        if (contractDBO == null) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_INVALID", "E400_CONTRACT_ID_INVALID");
        }

        // verify sender
        long longValueUser = getOnlineUser().getPerson().getId().longValue();
        long longValueSender = contractDBO.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new BusinessLogicException(400, "E400_CONTRACT_FOREIGN", "E400_CONTRACT_FOREIGN");
        }

        if (!contractDBO.getStatus().equals(Status.CREATED)) {
            throw new BusinessLogicException(400, "E400_CONTRACT_STATUS_NOK", "E400_CONTRACT_STATUS_NOK");
        }

        Iterator<ContractFileDBO> iterator = contractDBO.getFiles().iterator();
        while (iterator.hasNext()) {
            ContractFileDBO file = iterator.next();
            if (file.getId().longValue() == fileIdLong) {
                iterator.remove();
                break;
            }
        }

        try {
            contractDAO.update(contractDBO, true);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new BusinessLogicException(400, "E400_TRY_AGAIN", "E400_TRY_AGAIN");
        }

        return null;
    }

}
