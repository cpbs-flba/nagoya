/**
 * 
 */
package com.nagoya.middleware.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.GeneticResourceTransferDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.dao.geneticresource.impl.GeneticResourceTransferDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.util.DefaultDateProvider;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.resource.GeneticResource;
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
import com.nagoya.model.to.resource.GeneticResourceTransfer;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceTransferService extends ResourceService {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceTransferService.class);

	private PersonDAO personDAO;
	private GeneticResourceDAO geneticResourceDAO;
	private GeneticResourceTransferDAO geneticResourceTransferDAO;
	private MailService mailService;

	public GeneticResourceTransferService(Session session, String language) {
		super(session);
		this.geneticResourceDAO = new GeneticResourceDAOImpl(session);
		this.personDAO = new PersonDAOImpl(session);
		this.geneticResourceTransferDAO = new GeneticResourceTransferDAOImpl(session);
		this.mailService = new MailService(language);
	}

	public DefaultReturnObject delete(String authorization, String transferId) throws NotAuthorizedException,
			ConflictException, TimeoutException, InvalidTokenException, InvalidObjectException,
			ResourceOutOfDateException, BadRequestException, NonUniqueResultException, ForbiddenException {
		OnlineUser onlineUser = validateSession(authorization);

		if (StringUtil.isNullOrBlank(transferId)) {
			throw new BadRequestException("transfer ID cannot be NULL.");
		}

		long id = 0;
		try {
			id = Long.parseLong(transferId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("transfer ID must be valid.");
		}

		com.nagoya.model.dbo.resource.GeneticResourceTransfer transfer = geneticResourceTransferDAO.find(id);
		if (transfer != null) {
			if (transfer.isReceiverAcceptedTransfer()) {
				throw new ForbiddenException(
						"Cannot delete transfer, because it was already accepted by the receiver.");
			}
			if (transfer.isPersistedInBlockChain()) {
				throw new ForbiddenException(
						"Cannot delete transfer, because it was already persisted in the blokcchain.");
			}
		}

		// everything is okay - transfer can be deleted
		geneticResourceTransferDAO.delete(transfer, true);

		DefaultReturnObject result = refreshSession(onlineUser, null, null);

		return result;
	}

	public DefaultReturnObject create(String authorization,
			com.nagoya.model.to.resource.GeneticResourceTransfer geneticRessourceTransfer)
			throws NotAuthorizedException, TimeoutException, InvalidTokenException, InvalidObjectException,
			ResourceOutOfDateException, BadRequestException, ConflictException, NonUniqueResultException {
		LOGGER.debug("Adding genetic resource");
		OnlineUser onlineUser = validateSession(authorization);

		validateTransfer(geneticRessourceTransfer);

		// the sender is the person who initiated the transaction
		Person sender = onlineUser.getPerson();
		String receiverEmail = geneticRessourceTransfer.getReceiver().getEmail();
		Person receiver = personDAO.findPersonForEmail(receiverEmail);
		if (receiver == null) {
			throw new BadRequestException("Cannot find receiver for e-mail address: " + receiverEmail);
		}

		Set<com.nagoya.model.dbo.resource.GeneticResource> geneticResources = new HashSet<>();
		for (com.nagoya.model.to.resource.GeneticResource geneticResourceTO : geneticRessourceTransfer
				.getGeneticResources()) {
			Long id = geneticResourceTO.getId();
			if (id != null) {
				com.nagoya.model.dbo.resource.GeneticResource dboResource = (GeneticResource) geneticResourceDAO
						.find(id, com.nagoya.model.dbo.resource.GeneticResource.class);
				geneticResources.add(dboResource);
			}
		}

		// create the object and persist
		com.nagoya.model.dbo.resource.GeneticResourceTransfer geneticResourceTransfer = new com.nagoya.model.dbo.resource.GeneticResourceTransfer();
		geneticResourceTransfer.setSender(sender);
		geneticResourceTransfer.setReceiver(receiver);
		geneticResourceTransfer.getGeneticResources().addAll(geneticResources);
		geneticResourceDAO.insert(geneticResourceTransfer, true);

		// send confirmation mail to sender
		mailService.sendConfirmationTransferCreation(geneticResourceTransfer);

		// save a user request
		UserRequest userRequest = new UserRequest();
		userRequest.setPerson(geneticResourceTransfer.getReceiver());
		userRequest.setRequestType(RequestType.GENETIC_RESOURCE_TRANSFER_ACCEPTANCE);
		Date expirationDate = DefaultDateProvider.getDeadline24h();
		userRequest.setExpirationDate(expirationDate);
		String token = DefaultIDGenerator.generateRandomID();
		userRequest.setToken(token);
		personDAO.insert(userRequest, true);
		
		// send request mail with token to receiver
		mailService.sendTransferAcceptancePending(token, expirationDate, geneticResourceTransfer);
		
		DefaultReturnObject result = refreshSession(onlineUser, null, null);

		return result;
	}

	private void validateTransfer(GeneticResourceTransfer geneticRessourceTransfer) throws BadRequestException {
		if (geneticRessourceTransfer == null) {
			throw new BadRequestException("Cannot create transfer for NULL object.");
		}

		if (geneticRessourceTransfer.getReceiver() == null) {
			throw new BadRequestException("Cannot create transfer for NULL receiver.");
		}

		String email = geneticRessourceTransfer.getReceiver().getEmail();
		if (StringUtil.isNullOrBlank(email)) {
			throw new BadRequestException("Cannot create transfer for NULL receiver (email most be populated).");
		}

		if (geneticRessourceTransfer.getGeneticResources().isEmpty()) {
			throw new BadRequestException("Cannot create transfer for 0 genetic resources.");
		}
	}

}
