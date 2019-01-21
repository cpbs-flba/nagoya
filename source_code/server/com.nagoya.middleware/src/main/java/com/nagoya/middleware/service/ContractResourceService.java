/**
 * 
 */

package com.nagoya.middleware.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.dao.base.impl.BasicDAOImpl;
import com.nagoya.dao.contract.ContractDAO;
import com.nagoya.dao.contract.impl.ContractDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;
import com.nagoya.middleware.util.DefaultDateProvider;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.contract.ContractDBO;
import com.nagoya.model.dbo.contract.ContractFileDBO;
import com.nagoya.model.dbo.contract.Status;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NonUniqueResultException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.NotFoundException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.contract.ContractFileTO;
import com.nagoya.model.to.contract.ContractFileTransformer;
import com.nagoya.model.to.contract.ContractTO;

/**
 * @author Florin Bogdan Balint
 *
 */
public class ContractResourceService extends ResourceService {

    private static final Logger LOGGER = LogManager.getLogger(ContractResourceService.class);
    private Session             session;
    private ContractDAO         contractDAO;
    private ContractFactory     contractFactory;

    public ContractResourceService(Session session) {
        super(session);
        this.session = session;
        this.contractDAO = new ContractDAOImpl(session);
        this.contractFactory = new ContractFactory(session);
    }

    public DefaultReturnObject create(String authorization, String language, com.nagoya.model.to.contract.ContractTO contractTO)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException, ResourceOutOfDateException,
        BadRequestException, NonUniqueResultException {
        LOGGER.debug("Adding contract");
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (contractTO == null) {
            throw new BadRequestException("Cannot execute create operation, when the object is null.");
        }

        // also inserts the contract into the DB
        com.nagoya.model.dbo.contract.ContractDBO contractDBO = contractFactory.createDBOContract(onlineUser.getPerson(), contractTO);

        Calendar cal = Calendar.getInstance();
        int integer = ServerPropertiesProvider.getInteger(ServerProperty.CONTRACT_EXPIRAETION_TIME, 72);
        cal.add(Calendar.HOUR, integer);

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
        MailService mailService = new MailService(language);
        mailService.sendContractCreationConfirmation(contractDBO.getSender().getEmail());
        mailService.sendContractAcceptancePending(token, expirationDate, contractDBO.getReceiver().getEmail());

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject delete(String authorization, String contractId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, BadRequestException, InvalidObjectException,
        ResourceOutOfDateException, NonUniqueResultException, ForbiddenException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(contractId)) {
            throw new BadRequestException("Contract ID must be specified");
        }

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Contract ID must be a number");
        }

        ContractDBO contractToDelete = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);

        // verify sender
        long longValueUser = onlineUser.getPerson().getId().longValue();
        long longValueSender = contractToDelete.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new ForbiddenException("Cannot delete foreign contract.");
        }

        Status status = contractToDelete.getStatus();
        if (status.equals(Status.CREATED)) {
            contractDAO.delete(contractToDelete, true);
        } else {
            throw new ForbiddenException("Cannot delete contract which was already accepted or is already cancelled.");
        }

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject search(String authorization, String contractStatus, String dateFromTO, String dateUntilTO)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
        ResourceOutOfDateException {
        OnlineUserDBO onlineUser = validateSession(authorization);

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
        List<ContractDBO> results = contractDAO.search(onlineUser.getPerson(), dateFromToFilter, dateUntilToFilter, statusToFilter, 0);
        LOGGER.debug("Results found: " + results.size());

        List<ContractTO> resultTOs = new ArrayList<>();
        for (ContractDBO contractDBO : results) {
            ContractTO contractTO = contractFactory.getContractTO(contractDBO);
            resultTOs.add(contractTO);
        }

        DefaultReturnObject result = refreshSession(onlineUser, resultTOs, null);
        return result;
    }

    public DefaultReturnObject create(String authorization, String language, String contractId, ContractFileTO contractFileTO)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, BadRequestException, NonUniqueResultException,
        InvalidObjectException, ResourceOutOfDateException, ForbiddenException {
        LOGGER.debug("Adding file to contract resource");
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (contractFileTO == null) {
            throw new BadRequestException("Cannot execute create operation, when the object is null.");
        }

        if (StringUtil.isNullOrBlank(contractId)) {
            throw new BadRequestException("Contract ID must be provided.");
        }

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Contract ID must be a number.");
        }

        ContractDBO contractDBO = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);

        // verify sender
        long longValueUser = onlineUser.getPerson().getId().longValue();
        long longValueSender = contractDBO.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new ForbiddenException("Cannot add file to foreign contract.");
        }

        ContractFileDBO contractFileDBO = ContractFileTransformer.getDBO(contractFileTO);
        contractDBO.getFiles().add(contractFileDBO);

        contractDAO.update(contractDBO, true);

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }

    public DefaultReturnObject retrieveFile(String authorization, String language, String contractId, String fileId)
        throws BadRequestException, NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException,
        InvalidObjectException, ResourceOutOfDateException, NotFoundException {
        OnlineUserDBO onlineUser = validateSession(authorization);

        long fileIdLong = 0;
        try {
            fileIdLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Contract ID must be a number.");
        }

        // TODO: limit contract file access if necessary...

        BasicDAO<ContractFileDBO> basicDAO = new BasicDAOImpl<ContractFileDBO>(session);
        ContractFileDBO contractFileDBO = (ContractFileDBO) basicDAO.find(fileIdLong, ContractFileDBO.class);
        if (contractFileDBO == null) {
            throw new NotFoundException("No file found");
        }

        ContractFileTO to = ContractFileTransformer.getTO(contractFileDBO);

        DefaultReturnObject result = refreshSession(onlineUser, to, null);
        return result;
    }

    public DefaultReturnObject deleteFile(String authorization, String language, String contractId, String fileId)
        throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException, BadRequestException, ForbiddenException,
        InvalidObjectException, ResourceOutOfDateException, NonUniqueResultException, NotFoundException {
        LOGGER.debug("Adding file to contract resource");
        OnlineUserDBO onlineUser = validateSession(authorization);

        if (StringUtil.isNullOrBlank(contractId) || StringUtil.isNullOrBlank(fileId)) {
            throw new BadRequestException("Contract and file ID must be provided.");
        }

        long contractIdLong = 0;
        try {
            contractIdLong = Long.parseLong(contractId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Contract ID must be a number.");
        }

        long fileIdLong = 0;
        try {
            fileIdLong = Long.parseLong(fileId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("File ID must be a number.");
        }

        ContractDBO contractDBO = (ContractDBO) contractDAO.find(contractIdLong, ContractDBO.class);
        if (contractDBO == null) {
            throw new NotFoundException("Contract not found.");
        }

        // verify sender
        long longValueUser = onlineUser.getPerson().getId().longValue();
        long longValueSender = contractDBO.getSender().getId().longValue();
        if (longValueUser != longValueSender) {
            throw new ForbiddenException("Cannot add file to foreign contract.");
        }

        if (!contractDBO.getStatus().equals(Status.CREATED)) {
            throw new ForbiddenException("File cannot be deleted any more.");
        }

        Iterator<ContractFileDBO> iterator = contractDBO.getFiles().iterator();
        while (iterator.hasNext()) {
            ContractFileDBO file = iterator.next();
            if (file.getId().longValue() == fileIdLong) {
                iterator.remove();
                break;
            }
        }

        contractDAO.update(contractDBO, true);

        DefaultReturnObject result = refreshSession(onlineUser, null, null);
        return result;
    }
}
