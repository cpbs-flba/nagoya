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

import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.contract.ContractDBO;
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
public class ContractDeletionService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractDeletionService.class);

    public ContractDeletionService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        ContractDAO contractDAO = new ContractDAOImpl(getSession());
        LOGGER.debug("Deleting contract");

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong((String) params[0]);
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_MISSING", "E400_CONTRACT_ID_MISSIN");
        }

        ContractDBO contractToDelete;
        try {
            contractToDelete = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);
        } catch (NonUniqueResultException e) {
            throw new InternalException("Multiple contracts for the same ID found. ID: " + contractIdLong);
        }

        // verify sender
        long longValueUser = getOnlineUser().getPerson().getId().longValue();
        long longValueSender = contractToDelete.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new BusinessLogicException(400, "E400_CONTRACT_ID_FOREIGN", "E400_CONTRACT_ID_FOREIGN");
        }

        Status status = contractToDelete.getStatus();
        if (status.equals(Status.CREATED)) {
            contractToDelete.setStatus(Status.CANCELLED);
            try {
                contractDAO.update(contractToDelete, true);
            } catch (InvalidObjectException | ResourceOutOfDateException e) {
                throw new InternalException(e);
            }
        } else {
            throw new BusinessLogicException(400, "E400_CONTRACT_ALREADY_ACCEPTED_OR_CANCELLED", "E400_CONTRACT_ALREADY_ACCEPTED_OR_CANCELLED");
        }

        return null;
    }

}
