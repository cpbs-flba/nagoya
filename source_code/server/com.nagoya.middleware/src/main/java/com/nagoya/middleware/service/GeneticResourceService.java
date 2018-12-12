/**
 * 
 */
package com.nagoya.middleware.service;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.person.GeneticResourceDAO;
import com.nagoya.dao.person.impl.GeneticResourceDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.model.exception.NotFoundException;
import com.nagoya.middleware.rest.UserResource;
import com.nagoya.middleware.util.DefaultReturnObject;
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
import com.nagoya.model.to.resource.GeneticResourceTransformer;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceService {

	private static final Logger LOGGER = LogManager.getLogger(GeneticResourceService.class);

	private UserService userService;
	private GeneticResourceDAO geneticResourceDAO;

	public GeneticResourceService(Session session) {
		this.geneticResourceDAO = new GeneticResourceDAOImpl(session);
		this.userService = new UserService(session);
	}

	public DefaultReturnObject create(String authorization,
			com.nagoya.model.to.resource.GeneticResource geneticRessource)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException,
			InvalidObjectException, ResourceOutOfDateException, BadRequestException {
		LOGGER.debug("Adding genetic resource");
		OnlineUser onlineUser = userService.validateSession(authorization);

		if (geneticRessource == null) {
			throw new BadRequestException("Cannot execute create operation, when the object is null.");
		}

		GeneticResource dbo = GeneticResourceTransformer.getDBO(null, geneticRessource);
		dbo.setOwner(onlineUser.getPerson());
		geneticResourceDAO.insert(dbo, true);

		DefaultReturnObject result = new DefaultReturnObject();
		com.nagoya.model.to.resource.GeneticResource dto = GeneticResourceTransformer.getDTO(dbo);
		// do not resend all the data, just the object
		dto.getFiles().clear();
		result.setEntity(dto);
		String newToken = userService.updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
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

	private void isUserAuthorizedForRessource(OnlineUser onlineUser, GeneticResource dbo, boolean writeOperation) throws ForbiddenException {
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
			com.nagoya.model.to.resource.GeneticResource geneticResource) throws NotAuthorizedException, ConflictException,
			TimeoutException, InvalidTokenException, NonUniqueResultException, BadRequestException,
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

}
