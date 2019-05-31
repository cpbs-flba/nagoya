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

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.common.crypto.AESEncryptionProvider;
import com.nagoya.common.util.StringUtil;
import com.nagoya.middleware.service.BlockchainService;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.OperationFailedException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractAcceptanceService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractAcceptanceService.class);

    public ContractAcceptanceService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {
        LOGGER.debug("Accepting contract");

        String token = ((String) params[0]);
        String privateKey = ((String) params[1]);

        if (StringUtil.isNullOrBlank(token)) {
            throw new BusinessLogicException(400, "E400_TOKEN_MISSING", "E400_TOKEN_MISSING");
        }

        UserRequestDBO userRequest = null;
        try {
            userRequest = getPersonDAO().findUserRequest(token);
        } catch (ConflictException e) {
            throw new InternalException(e);
        }
        if (userRequest == null) {
            throw new BusinessLogicException(400, "E400_TOKEN_MISSING", "E400_TOKEN_MISSING");
        }

        // we found the request, now we have to delete it
        ContractDBO contract = userRequest.getContract();

        Date expirationDate = userRequest.getExpirationDate();
        Date currentDate = Calendar.getInstance().getTime();

        // if the user request expired, then we are done here
        if (expirationDate.before(currentDate)) {
            contract.setStatus(Status.EXPIRED);
            try {
                getPersonDAO().update(contract, true);
                getPersonDAO().delete(userRequest, true);
            } catch (InvalidObjectException | ResourceOutOfDateException e) {
                throw new InternalException(e);
            }
            throw new BusinessLogicException(400, "E400_TOKEN_EXPIRED", "E400_TOKEN_EXPIRED");
        }

        if (!userRequest.getRequestType().equals(RequestType.CONTRACT_ACCEPTANCE)) {
            throw new BusinessLogicException(400, "E400_TOKEN_INVALID", "E400_TOKEN_INVALID");
        }

        if (!contract.getStatus().equals(Status.CREATED)) {
            throw new BusinessLogicException(400, "E400_TOKEN_INVALID", "E400_TOKEN_INVALID");
        }

        PersonDBO person = getOnlineUser().getPerson();
        if (!person.isStorePrivateKey() && StringUtil.isNullOrBlank(privateKey)) {
            throw new BusinessLogicException(400, "E400_PK_MISSING", "E400_PK_MISSING");
        }

        // at this point everything is okay and the contract is legally concluded
        // we need the credentials and then we can transmit everything to the blockchain
        Credentials credentialsReceiver = getValidCredentials(person);
        if (person.isStorePrivateKey()) {
            String privateKeyFromDB = getOnlineUser().getPrivateKey();
            if (StringUtil.isNullOrBlank(privateKeyFromDB)) {
                throw new BusinessLogicException(400, "E400_PK_MISSING", "E400_PK_MISSING");
            }
            String decodedPrivateKey = AESEncryptionProvider.decrypt(getOnlineUser().getBlockchainKey(), privateKeyFromDB);
            credentialsReceiver.setPrivateKey(decodedPrivateKey);
        } else {
            credentialsReceiver.setPrivateKey(privateKey);
        }
        contract.setCredentialsReceiver(credentialsReceiver);
        BlockchainService blockchainService = new BlockchainService(getSession());
        try {
            blockchainService.confirmContractInBlockchain(contract);
        } catch (NotAuthorizedException | OperationFailedException e) {
            throw new InternalException(e);
        }

        // delete the request only if everything else was okay...
        try {
            getPersonDAO().delete(userRequest, true);
            // now everyhting else was okay.
            contract.setStatus(Status.ACCEPTED);
            getPersonDAO().update(contract, true);
        } catch (InvalidObjectException | ResourceOutOfDateException e) {
            throw new BusinessLogicException(400, "E400_TRY_AGAIN", "E400_TRY_AGAIN");
        }

        getMailService().sendContractAccepted(contract);

        return null;
    }

}
