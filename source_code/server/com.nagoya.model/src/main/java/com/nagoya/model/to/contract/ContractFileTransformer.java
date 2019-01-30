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

package com.nagoya.model.to.contract;

import com.nagoya.model.dbo.contract.ContractFileDBO;

/**
 * @author Florin Bogdan Balint
 *
 */
public final class ContractFileTransformer {

    private ContractFileTransformer() {
        // noop
    }

    public static ContractFileDBO getDBO(ContractFileTO contractFileTO) {
        if (contractFileTO == null) {
            return null;
        }
        ContractFileDBO contractFile = new ContractFileDBO();
        contractFile.setContent(contractFileTO.getContent());
        contractFile.setType(contractFileTO.getType());
        contractFile.setName(contractFileTO.getName());
        return contractFile;
    }

    public static ContractFileTO getTO(ContractFileDBO contractFileDBO) {
        if (contractFileDBO == null) {
            return null;
        }
        ContractFileTO result = new ContractFileTO();
        result.setId(contractFileDBO.getId());
        result.setContent(contractFileDBO.getContent());
        result.setType(contractFileDBO.getType());
        result.setName(contractFileDBO.getName());
        return result;
    }

}
