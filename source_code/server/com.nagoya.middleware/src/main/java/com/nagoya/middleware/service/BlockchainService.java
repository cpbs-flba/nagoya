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

package com.nagoya.middleware.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;
import com.nagoya.common.util.DefaultDateProvider;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.model.blockchain.Asset;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractResourceDBO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.dbo.person.PersonType;
import com.nagoya.model.dbo.resource.GeneticResourceDBO;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.OperationFailedException;
import com.nagoya.model.exception.ResourceOutOfDateException;

/**
 * @author flba
 *
 */
public class BlockchainService {

    private ContractDAO contractDAO;

    public BlockchainService(Session session) {
        this.contractDAO = new ContractDAOImpl(session);
    }

    public void persistContractInBlockchain(ContractDBO contractDBO)
        throws NotAuthorizedException, OperationFailedException, InvalidObjectException, ResourceOutOfDateException {

        Set<ContractResourceDBO> contractResources = contractDBO.getContractResources();
        for (ContractResourceDBO contractResourceDBO : contractResources) {
            BlockchainDriver blockchainDriver = new BlockchainDriverImpl();

            // step 1: create blockchain asset from contract data
            Asset asset = createAssetFromContractResource(contractDBO, contractResourceDBO);

            // step 2: persist asset into blockchain
            String transactionId = blockchainDriver.createAsset(contractDBO.getCredentialsSender(), asset);
            contractResourceDBO.setTransactionId(transactionId);
            asset.setTransactionId(transactionId);

            // step 3: transfer asset to new owner
            blockchainDriver.transferAsset(contractDBO.getCredentialsSender(), contractDBO.getCredentialsReceiver(), asset);
        }

        // update the contract
        contractDAO.update(contractDBO, true);
    }

    private Asset createAssetFromContractResource(ContractDBO contractDBO, ContractResourceDBO contractResourceDBO) {
        Asset asset = new Asset();
        String amount = contractResourceDBO.getAmount().stripTrailingZeros().toPlainString();
        asset.setAmount(amount);
        String measuringUnit = contractResourceDBO.getMeasuringUnit();

        GeneticResourceDBO geneticResource = contractResourceDBO.getGeneticResource();

        // genetic resource
        Map<String, Object> gr = new HashMap<>();
        gr.put("id", geneticResource.getIdentifier());
        gr.put("hash_sequence", geneticResource.getHashSequence());
        gr.put("taxonomy", geneticResource.getTaxonomy().toString());
        gr.put("trading_date", DefaultDateProvider.getCurrentDateAsString());

        Map<String, Object> sender = getPersonData(contractDBO.getSender());
        Map<String, Object> receiver = getPersonData(contractDBO.getReceiver());

        asset.getAssetData().put("genetic_resource", gr);
        asset.getAssetData().put("sender", sender);
        asset.getAssetData().put("receiver", receiver);

        asset.getMetaData().put("unit", measuringUnit);
        asset.getMetaData().put("source", geneticResource.getSource());
        asset.getMetaData().put("origin", geneticResource.getOrigin());
        asset.getMetaData().put("receiver_confirmed", "false");

        return asset;
    }

    private Map<String, Object> getPersonData(PersonDBO personDBO) {
        Map<String, Object> result = new HashMap<>();
        PersonType personType = personDBO.getPersonType();
        result.put("type", personType);
        if (personType.equals(PersonType.NATURAL)) {
            PersonNaturalDBO nat = (PersonNaturalDBO) personDBO;
            result.put("first_name", nat.getFirstname());
            result.put("last_name", nat.getLastname());
            result.put("birthday", DefaultDateProvider.getDateAsStringSimple(nat.getBirthdate()));
        }
        if (personType.equals(PersonType.LEGAL)) {
            PersonLegalDBO legal = (PersonLegalDBO) personDBO;
            result.put("name", legal.getName());
            result.put("commercial_register_nr", legal.getCommercialRegisterNumber());
            result.put("tax_nr", legal.getTaxNumber());
        }
        return result;
    }

}
