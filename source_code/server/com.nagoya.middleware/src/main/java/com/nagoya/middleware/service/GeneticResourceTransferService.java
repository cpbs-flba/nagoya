/**
 * 
 */
package com.nagoya.middleware.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.geneticresource.GeneticResourceDAO;
import com.nagoya.dao.geneticresource.impl.GeneticResourceDAOImpl;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.model.exception.NotFoundException;
import com.nagoya.middleware.rest.UserResource;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.resource.GeneticResource;
import com.nagoya.model.dbo.resource.ResourceFile;
import com.nagoya.model.dbo.resource.VisibilityType;
import com.nagoya.model.dbo.user.OnlineUser;
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
import com.nagoya.model.to.resource.GeneticResourceTransformer;
import com.nagoya.model.to.resource.filter.GeneticResourceFilter;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceTransferService {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceTransferService.class);

	private UserService userService;
	private PersonDAO personDAO;
	private GeneticResourceDAO geneticResourceDAO;

	public GeneticResourceTransferService(Session session) {
		this.geneticResourceDAO = new GeneticResourceDAOImpl(session);
		this.personDAO = new PersonDAOImpl(session);
		this.userService = new UserService(session);
	}

	public DefaultReturnObject create(String authorization,
			com.nagoya.model.to.resource.GeneticResourceTransfer geneticRessourceTransfer)
			throws NotAuthorizedException, TimeoutException, InvalidTokenException, InvalidObjectException,
			ResourceOutOfDateException, BadRequestException, ConflictException {
		LOGGER.debug("Adding genetic resource");
		OnlineUser onlineUser = userService.validateSession(authorization);

		validateTransfer(geneticRessourceTransfer);
		
		// the sender is the person who initiated the transaction
		Person sender = onlineUser.getPerson();
		String receiverEmail = geneticRessourceTransfer.getReceiver().getEmail();
		Person receiver = personDAO.findPersonForEmail(receiverEmail);
		if (receiver == null) {
			throw new BadRequestException("Cannot find receiver for e-mail address: " + receiverEmail);
		}
		
		Set<com.nagoya.model.dbo.resource.GeneticResource> geneticResources = new HashSet<>();
		// TODO: find all resources
		
		// create the object and persist
		com.nagoya.model.dbo.resource.GeneticResourceTransfer geneticResourceTransfer = new com.nagoya.model.dbo.resource.GeneticResourceTransfer();
		geneticResourceTransfer.setSender(sender);
		geneticResourceTransfer.setReceiver(receiver);
		geneticResourceTransfer.getGeneticResources().addAll(geneticResources);
		geneticResourceDAO.insert(geneticResourceTransfer, true);
		
		// TODO: send confirmation mail to sender
		// TODO: send request mail with token to receiver

		DefaultReturnObject result = new DefaultReturnObject();
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
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

	public DefaultReturnObject read(String authorization, String resourceId) throws NotAuthorizedException,
			ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
			InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (StringUtil.isNullOrBlank(resourceId)) {
			throw new BadRequestException("Resource ID is missing.");
		}
		// verify the file id
		Long resourceIdAsLong = null;
		try {
			resourceIdAsLong = Long.parseLong(resourceId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Resource ID and/or File ID is invalid.");
		}

		GeneticResource dbo = (GeneticResource) geneticResourceDAO.find(resourceIdAsLong,
				com.nagoya.model.dbo.resource.GeneticResource.class);

		if (dbo == null) {
			throw new NotFoundException();
		}

		isUserAuthorizedForRessource(onlineUser, dbo, false);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	private void isUserAuthorizedForRessource(OnlineUser onlineUser, GeneticResource dbo, boolean writeOperation)
			throws ForbiddenException {
		boolean authorized = false;
		// if the user is the owner
		if (dbo.getOwner().getId().equals(onlineUser.getPerson().getId())) {
			authorized = true;
		}
		// if the user is in the correct group
		if (dbo.getVisibilityType().equals(VisibilityType.PUBLIC)) {
			authorized = true;
		}
		// if the user is in the correct group
		if (dbo.getVisibilityType().equals(VisibilityType.GROUP) && !writeOperation) {
			// Verify if a user is in the group
			authorized = true;
		}
		if (!authorized) {
			throw new ForbiddenException("Access denied");
		}
	}

	public DefaultReturnObject delete(String authorization, String resourceId) throws NotAuthorizedException,
			ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
			InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (StringUtil.isNullOrBlank(resourceId)) {
			throw new BadRequestException("Resource ID is missing.");
		}

		// verify the file id
		Long resourceIdAsLong = null;
		try {
			resourceIdAsLong = Long.parseLong(resourceId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Resource ID and/or File ID is invalid.");
		}
		GeneticResource dbo = (GeneticResource) geneticResourceDAO.find(resourceIdAsLong,
				com.nagoya.model.dbo.resource.GeneticResource.class);

		if (dbo == null) {
			throw new NotFoundException();
		}

		isUserAuthorizedForRessource(onlineUser, dbo, true);

		geneticResourceDAO.delete(dbo, true);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public DefaultReturnObject delete(String authorization, String resourceId, String fileId)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException,
			NonUniqueResultException, BadRequestException, InvalidObjectException, ResourceOutOfDateException,
			ForbiddenException, NotFoundException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (StringUtil.isNullOrBlank(resourceId) || StringUtil.isNullOrBlank(fileId)) {
			throw new BadRequestException("Resource ID and/or File ID is missing.");
		}
		Long resourceIdAsLong = null;
		Long fileIdAsLong = null;
		try {
			resourceIdAsLong = Long.parseLong(resourceId);
			fileIdAsLong = Long.parseLong(fileId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Resource ID and/or File ID is invalid.");
		}
		GeneticResource dbo = (GeneticResource) geneticResourceDAO.find(resourceIdAsLong,
				com.nagoya.model.dbo.resource.GeneticResource.class);

		if (dbo == null) {
			throw new NotFoundException();
		}

		isUserAuthorizedForRessource(onlineUser, dbo, true);

		// search through the files and delete it
		Iterator<ResourceFile> iterator = dbo.getFiles().iterator();
		while (iterator.hasNext()) {
			ResourceFile rf = iterator.next();
			if (rf.getId().equals(fileIdAsLong)) {
				iterator.remove();
			}
		}

		geneticResourceDAO.update(dbo, true);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		dto.getFiles().clear();
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public DefaultReturnObject addResourceFile(String authorization, String resourceId,
			com.nagoya.model.to.resource.ResourceFile resourceFile) throws NotAuthorizedException, ConflictException,
			TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
			InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (StringUtil.isNullOrBlank(resourceId) || resourceFile == null) {
			throw new BadRequestException("Resource ID and/or File ID is missing.");
		}
		Long resourceIdAsLong = null;
		try {
			resourceIdAsLong = Long.parseLong(resourceId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Resource ID and/or File ID is invalid.");
		}
		GeneticResource dbo = (GeneticResource) geneticResourceDAO.find(resourceIdAsLong,
				com.nagoya.model.dbo.resource.GeneticResource.class);

		if (dbo == null) {
			throw new NotFoundException();
		}

		isUserAuthorizedForRessource(onlineUser, dbo, true);

		ResourceFile dboResourceFile = GeneticResourceTransformer.getDBO(resourceFile);
		dbo.getFiles().add(dboResourceFile);
		geneticResourceDAO.update(dbo, true);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		dto.getFiles().clear();
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public DefaultReturnObject update(String authorization, String resourceId,
			com.nagoya.model.to.resource.GeneticResource geneticResource) throws NotAuthorizedException,
			ConflictException, TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
			InvalidObjectException, ResourceOutOfDateException, ForbiddenException, NotFoundException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (StringUtil.isNullOrBlank(resourceId) || geneticResource == null) {
			throw new BadRequestException("Resource ID and/or File ID is missing.");
		}
		Long resourceIdAsLong = null;
		try {
			resourceIdAsLong = Long.parseLong(resourceId);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Resource ID and/or File ID is invalid.");
		}
		GeneticResource dbo = (GeneticResource) geneticResourceDAO.find(resourceIdAsLong,
				com.nagoya.model.dbo.resource.GeneticResource.class);

		if (dbo == null) {
			throw new NotFoundException();
		}

		isUserAuthorizedForRessource(onlineUser, dbo, true);

		GeneticResource dboGeneticResource = GeneticResourceTransformer.getDBO(dbo, geneticResource);
		geneticResourceDAO.update(dboGeneticResource, true);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		dto.getFiles().clear();
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public DefaultReturnObject search(String authorization, GeneticResourceFilter geneticRessourceFilter)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException,
			InvalidObjectException, ResourceOutOfDateException {
		OnlineUser onlineUser = userService.validateSession(authorization);

		List<GeneticResource> dbos = geneticResourceDAO.search(geneticRessourceFilter, onlineUser.getPerson(), 50);

		List<com.nagoya.model.to.resource.GeneticResource> dtos = new ArrayList<>();
		for (GeneticResource dbo : dbos) {
			com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
			dto.getFiles().clear();
			dtos.add(dto);
		}

		DefaultReturnObject result = new DefaultReturnObject();
		result.setEntity(dtos);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

}
