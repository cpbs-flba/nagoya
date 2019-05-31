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
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;
import com.nagoya.middleware.service.BlockchainService;
import com.nagoya.middleware.service.ContractFactory;
import com.nagoya.middleware.service.DefaultSecureResourceService;
import com.nagoya.middleware.service.MailService;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultResponse;
import com.nagoya.model.blockchain.Credentials;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.BusinessLogicException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InternalException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.OperationFailedException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractCreationService extends DefaultSecureResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractCreationService.class);

    public ContractCreationService(String authorization, String language) {
        super(authorization, language);
    }

    @Override
    protected DefaultResponse runOperation(Object... params)
        throws BusinessLogicException {

        ContractDAO contractDAO = new ContractDAOImpl(getSession());
        ContractFactory contractFactory = new ContractFactory(getSession());
        LOGGER.debug("Adding contract");

        String privateKey = (String) params[0];
        com.nagoya.model.to.contract.ContractTO contractTO = (com.nagoya.model.to.contract.ContractTO) params[1];

        if (contractTO == null) {
            throw new BusinessLogicException(400, "E400_CONTRACT_MISSING", "Cannot execute create operation, when the object is null.");
        }

        PersonDBO person = getOnlineUser().getPerson();
        // also inserts the contract into the DB
        com.nagoya.model.dbo.contract.ContractDBO contractDBO;
        try {
            contractDBO = contractFactory.createDBOContract(person, contractTO);
        } catch (BadRequestException | ConflictException | NonUniqueResultException e) {
            throw new BusinessLogicException(400, "E400_PERSON_MISSING", "E400_PERSON_MISSING");
        }

        // persist first part in blockchain
        if (!person.isStorePrivateKey() && StringUtil.isNullOrBlank(privateKey)) {
            throw new BusinessLogicException(400, "E400_PK_MISSING", "E400_PK_MISSING");
        }

        Credentials credentialsSender = getValidCredentials(person);
        if (person.isStorePrivateKey()) {
            String privateKeyFromDB = getOnlineUser().getPrivateKey();
            if (StringUtil.isNullOrBlank(privateKeyFromDB)) {
                throw new BusinessLogicException(400, "E400_PK_INVALID", "E400_PK_INVALID");
            }
            String decodedPrivateKey = AESEncryptionProvider.decrypt(getOnlineUser().getBlockchainKey(), privateKeyFromDB);
            credentialsSender.setPrivateKey(decodedPrivateKey);
        } else {
            credentialsSender.setPrivateKey(privateKey);
        }
        contractDBO.setCredentialsSender(credentialsSender);
        Credentials credentialsReceiver = getValidCredentials(contractDBO.getReceiver());
        contractDBO.setCredentialsReceiver(credentialsReceiver);

        // now we should everything we need to store the asset into the blockchain
        BlockchainService blockchainService = new BlockchainService(getSession());
        try {
            blockchainService.persistContractInBlockchain(contractDBO);
        } catch (NotAuthorizedException | OperationFailedException | InvalidObjectException | ResourceOutOfDateException e) {
            throw new InternalException(e);
        }

        Calendar cal = Calendar.getInstance();
        int integer = ServerPropertiesProvider.getInteger(ServerProperty.CONTRACT_EXPIRAETION_TIME, 72);
        cal.add(Calendar.HOUR_OF_DAY, integer);

        Date expirationDate = cal.getTime();
        String token = DefaultIDGenerator.generateRandomID();

        UserRequestDBO userRequest = new UserRequestDBO();
        userRequest.setRequestType(RequestType.CONTRACT_ACCEPTANCE);
        userRequest.setExpirationDate(expirationDate);
        userRequest.setToken(token);
        userRequest.setContract(contractDBO);
        userRequest.setPerson(contractDBO.getReceiver());

        contractDAO.insert(userRequest, true);

        // send emails
        MailService mailService = getMailService();
        mailService.sendContractCreationConfirmation(contractDBO.getSender().getEmail());
        mailService.sendContractAcceptancePending(token, expirationDate, contractDBO.getReceiver().getEmail());

        return null;
    }

}
