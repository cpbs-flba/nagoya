/**
 * 
 */

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
