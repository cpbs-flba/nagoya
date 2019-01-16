/**
 * 
 */

package com.nagoya.middleware.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.contract.Contract;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.user.OnlineUser;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequest;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractResourceService extends ResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractResourceService.class);
    private Session             session;
    private ContractDAO         contractDAO;

    public ContractResourceService(Session session) {
        super(session);
        this.session = session;
        this.contractDAO = new ContractDAOImpl(session);
    }

    public DefaultReturnObject create(String authorization, String language, com.nagoya.model.dto.contract.Contract contractTO)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException, ResourceOutOfDateException,
        BadRequestException, NonUniqueResultException {
        LOGGER.debug("Adding genetic resource");
        OnlineUser onlineUser = validateSession(authorization);

        if (contractTO == null) {
            throw new BadRequestException("Cannot execute create operation, when the object is null.");
        }

        ContractFactory contractFactory = new ContractFactory(session);
        // also inserts the contract into the DB
        com.nagoya.model.dbo.contract.Contract contractDBO = contractFactory.createDBOContract(onlineUser.getPerson(), contractTO);

        Calendar cal = Calendar.getInstance();
        // TODO: externalize to config
        cal.add(Calendar.DAY_OF_YEAR, 30);

        Date expirationDate = cal.getTime();
        String token = DefaultIDGenerator.generateRandomID();

        UserRequest userRequest = new UserRequest();
        userRequest.setRequestType(RequestType.CONTRACT_ACCEPTANCE);
        userRequest.setExpirationDate(expirationDate);
        userRequest.setToken(token);
        userRequest.setContract(contractDBO);
        userRequest.setPerson(contractDBO.getReceiver());

        contractDAO.insert(userRequest, true);

        // send emails
        MailService mailService = new MailService(language);
        mailService.sendContractCreationConfirmation(contractDBO.getSender().getEmail());
        mailService.sendContractAcceptancePending(token, expirationDate, contractDBO.getReceiver().getEmail());

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject delete(String authorization, String contractId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, BadRequestException, InvalidObjectException,
        ResourceOutOfDateException, NonUniqueResultException, ForbiddenException {
        OnlineUser onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(contractId)) {
            throw new BadRequestException("Contract ID must be specified");
        }

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Contract ID must be a number");
        }

        Contract contractToDelete = (Contract) contractDAO.find(contractIdLong, Contract.class);
        Status status = contractToDelete.getStatus();
        if (status.equals(Status.CREATED)) {
            contractDAO.delete(contractToDelete, true);
        } else {
            throw new ForbiddenException("Cannot delete contract which was already accepted or is already cancelled.");
        }

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject search(String authorization, String contractStatus, String dateFrom, String dateUntil)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
        ResourceOutOfDateException {
        OnlineUser onlineUser = validateSession(authorization);

        LOGGER.debug("Searching for contracts");
        List<Contract> results = contractDAO.search(onlineUser.getPerson(), null, null, null, 0);
        LOGGER.debug("Results found: " + results.size());

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }
}