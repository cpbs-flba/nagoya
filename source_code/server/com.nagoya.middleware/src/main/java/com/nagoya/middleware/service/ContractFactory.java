/**
 * 
 */

package com.nagoya.middleware.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.to.contract.ContractResourceTO;
import com.nagoya.model.to.person.PersonTO;
import com.nagoya.model.to.resource.GeneticResourceTO;

/**
 * @author flba
 *
 */
public class ContractFactory {

    private PersonDAO          personDAO;
    private GeneticResourceDAO geneticResourceDAO;

    public ContractFactory(Session session) {
        this.personDAO = new PersonDAOImpl(session);
        this.geneticResourceDAO = new GeneticResourceDAOImpl(session);
    }

    public com.nagoya.model.dbo.contract.ContractDBO createDBOContract(com.nagoya.model.dbo.person.PersonDBO senderDBO,
        com.nagoya.model.to.contract.ContractTO contractTO)
        throws BadRequestException, ConflictException, NonUniqueResultException {
        com.nagoya.model.dbo.contract.ContractDBO contractDBO = new com.nagoya.model.dbo.contract.ContractDBO();

        // now let's start with some basic validation
        // and retrieve the persons based on their e-mail addresses
        PersonTO receiver = contractTO.getReceiver();
        if (receiver == null) {
            throw new BadRequestException("Sender/Receiver cannot be NULL.");
        }

        String emailReceiver = receiver.getEmail();

        if (StringUtil.isNullOrBlank(emailReceiver)) {
            throw new BadRequestException("Sender-email or receiver-email cannot be NULL.");
        }

        com.nagoya.model.dbo.person.PersonDBO receiverDBO = personDAO.findPersonForEmail(emailReceiver);
        if (senderDBO == null || receiverDBO == null) {
            throw new BadRequestException("Sender/Receiver could not be found.");
        }

        contractDBO.setSender(senderDBO);
        contractDBO.setReceiver(receiverDBO);

        Set<com.nagoya.model.dbo.contract.ContractResourceDBO> contractResources = new HashSet<>();

        Set<ContractResourceTO> contractResourcesDTO = contractTO.getContractResources();
        if (contractResourcesDTO.isEmpty()) {
            throw new BadRequestException("At least one contract resource must exist!");
        }
        for (com.nagoya.model.to.contract.ContractResourceTO contractResourceTO : contractResourcesDTO) {
            double amount = contractResourceTO.getAmount();
            if (amount <= 0) {
                throw new BadRequestException("Amount must not be greater than 0!");
            }

            String measuringUnit = contractResourceTO.getMeasuringUnit();
            if (StringUtil.isNullOrBlank(measuringUnit)) {
                throw new BadRequestException("A measuring unit must be provided (e.g., 'kg') !");
            }

            GeneticResourceTO geneticResource = contractResourceTO.getGeneticResource();
            Long geneticResourceId = geneticResource.getId();
            com.nagoya.model.dbo.resource.GeneticResourceDBO dboResource = (com.nagoya.model.dbo.resource.GeneticResourceDBO) geneticResourceDAO
                .find(geneticResourceId.longValue(), com.nagoya.model.dbo.resource.GeneticResourceDBO.class);

            com.nagoya.model.dbo.contract.ContractResourceDBO toAdd = new com.nagoya.model.dbo.contract.ContractResourceDBO();
            toAdd.setGeneticResource(dboResource);
            toAdd.setMeasuringUnit(measuringUnit);
            toAdd.setAmount(new BigDecimal(amount));
            contractResources.add(toAdd);
        }

        // finally add all resources
        contractDBO.getContractResources().addAll(contractResources);
        contractDBO.setStatus(Status.CREATED);

        personDAO.insert(contractDBO, true);

        return contractDBO;
    }

}
