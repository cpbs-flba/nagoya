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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.common.util.DefaultDateProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.middleware.service.ContractFactory;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.to.contract.ContractTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractSearchService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractSearchService.class);
    private ContractDAO         contractDAO;

    public ContractSearchService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        contractDAO = new ContractDAOImpl(getSession());
        ContractFactory contractFactory = new ContractFactory(getSession());

        LOGGER.debug("Adding contract");
        String contractStatus = (String) params[0];
        String dateFromTO = (String) params[1];
        String dateUntilTO = (String) params[2];
        String role = (String) params[3];

        Date dateFromToFilter = DefaultDateProvider.getDateFromString(dateFromTO);
        Date dateUntilToFilter = DefaultDateProvider.getDateFromString(dateUntilTO);
        Status statusToFilter = null;

        if (contractStatus != null) {
            try {
                statusToFilter = Status.valueOf(contractStatus);
            } catch (Exception e) {
                statusToFilter = null;
            }
        }

        LOGGER.debug("Searching for contracts");
        List<ContractDBO> results = contractDAO.search(getOnlineUser().getPerson(), dateFromToFilter, dateUntilToFilter, statusToFilter, 0);
        LOGGER.debug("Results found: " + results.size());

        try {
            updateContracts(results);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new InternalException(e);
        }

        List<ContractTO> resultTOs = new ArrayList<>();
        for (ContractDBO contractDBO : results) {
            ContractTO contractTO = contractFactory.getContractTO(contractDBO);

            // filter by role if set
            if (StringUtil.isNotNullOrBlank(role)) {
                boolean roleSetCorrectly = false;
                if (role.equalsIgnoreCase("sender")) {
                    roleSetCorrectly = true;
                    if (contractTO.getSender().getEmail().equals(getOnlineUser().getPerson().getEmail())) {
                        resultTOs.add(contractTO);
                    }
                }
                if (role.equalsIgnoreCase("receiver")) {
                    roleSetCorrectly = true;
                    if (contractTO.getReceiver().getEmail().equals(getOnlineUser().getPerson().getEmail())) {
                        resultTOs.add(contractTO);
                    }
                }
                if (!roleSetCorrectly) {
                    resultTOs.add(contractTO);
                }
            } else {
                resultTOs.add(contractTO);
            }
        }

        DefaultResponse result = new DefaultResponse();
        result.setEntity(resultTOs);
        return result;
    }

    private void updateContracts(List<ContractDBO> contracts)
        throws InvalidObjectException, ResourceOutOfDateException {
        // clean up requests
        for (ContractDBO contractDBO : contracts) {
            boolean edited = false;
            Iterator<UserRequestDBO> iterator = contractDBO.getUserRequests().iterator();
            while (iterator.hasNext()) {
                UserRequestDBO userRequest = iterator.next();
                Date expirationDate = userRequest.getExpirationDate();
                Date currentDate = Calendar.getInstance().getTime();
                if (currentDate.after(expirationDate)) {
                    iterator.remove();
                    edited = true;
                }
            }

            // if a contract has no more requests and its status is accepted
            // then the contract is automatically cancelled
            boolean statusIsCreated = contractDBO.getStatus().equals(Status.CREATED);
            boolean userRequestsIsEmpty = contractDBO.getUserRequests().isEmpty();
            if (statusIsCreated && userRequestsIsEmpty) {
                contractDBO.setStatus(Status.EXPIRED);
                edited = true;
            }
            if (edited) {
                LOGGER.debug("Updating contract: " + contractDBO.getId());
                contractDAO.update(contractDBO, true);
            }
        }
    }

}
