/**
 * 
 */
package com.nagoya.middleware.rest.impl;

import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.nagoya.dao.db.ConnectionProvider;
import com.nagoya.middleware.rest.UserResource;
import com.nagoya.middleware.service.UserService;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.person.Person;
import com.nagoya.model.to.person.PersonLegal;
import com.nagoya.model.to.person.PersonNatural;

/**
 * @author flba
 * @author adim
 *
 */
public class UserResourceImpl implements UserResource {

	private static final Logger LOGGER = LogManager.getLogger(UserResourceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nagoya.middleware.rest.UserResource#login(com.nagoya.model.person.to.
	 * Person, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void login(Person person, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			DefaultReturnObject result = userService.login(person);
			ResponseBuilder responseBuilder = Response.ok(result.getEntity());
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (ForbiddenException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.FORBIDDEN).build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (BadRequestException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.middleware.rest.UserResource#register(com.nagoya.model.to.person.PersonLegal, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void register(PersonLegal person, String language, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			userService.register(person, language);
			ResponseBuilder responseBuilder = Response.status(Status.NO_CONTENT);
			response = responseBuilder.build();
		} catch (ConflictException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.CONFLICT).build();
		} catch (BadRequestException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nagoya.middleware.rest.UserResource#register(com.nagoya.model.to.person.PersonNatural, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void register(PersonNatural person, String language, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			userService.register(person, language);
			ResponseBuilder responseBuilder = Response.status(Status.NO_CONTENT);
			response = responseBuilder.build();
		} catch (ConflictException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.CONFLICT).build();
		} catch (BadRequestException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
	}

	@Override
	public void confirm(Person person, String token, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			DefaultReturnObject result = userService.confirmRequest(token, person);
			ResponseBuilder responseBuilder = null;
			if (result == null) {
				responseBuilder = Response.status(Status.NO_CONTENT);
			} else {
				responseBuilder = Response.ok(result.getEntity());
			}
			response = responseBuilder.build();
		} catch (BadRequestException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

	@Override
	public void resetPassword(Person person, String language, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			userService.resetPassword(person, language);
			ResponseBuilder responseBuilder = Response.status(Status.NO_CONTENT);
			response = responseBuilder.build();
		} catch (BadRequestException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

	@Override
	public void delete(String authorization, String language, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			DefaultReturnObject result = userService.delete(authorization, language);
			ResponseBuilder responseBuilder = Response.status(Status.NO_CONTENT);
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.middleware.rest.UserResource#update(java.lang.String, java.lang.String, com.nagoya.model.to.person.PersonNatural, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void update(String authorization, String language, PersonNatural person, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			DefaultReturnObject result = userService.update(authorization, language, person);
			ResponseBuilder responseBuilder = Response.ok(result.getEntity());
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (ForbiddenException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.FORBIDDEN).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.middleware.rest.UserResource#update(java.lang.String, java.lang.String, com.nagoya.model.to.person.PersonLegal, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void update(String authorization, String language, PersonLegal person, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			DefaultReturnObject result = userService.update(authorization, language, person);
			ResponseBuilder responseBuilder = Response.ok(result.getEntity());
			Set<Entry<String,String>> entrySet = result.getHeader().entrySet();
			for (Entry<String, String> entry : entrySet) {
				responseBuilder.header(entry.getKey(), entry.getValue());
			}
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.UNAUTHORIZED).build();
		} catch (ForbiddenException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.FORBIDDEN).build();
		} catch (TimeoutException e) {
			LOGGER.error(e, e);
			response = Response.status(Status.REQUEST_TIMEOUT).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.nagoya.middleware.rest.UserResource#logout(java.lang.String, javax.ws.rs.container.AsyncResponse)
	 */
	@Override
	public void logout(String authorization, AsyncResponse asyncResponse) {
		Response response = null;
		Session session = null;
		try {
			session = ConnectionProvider.getInstance().getSession();
			UserService userService = new UserService(session);
			userService.logout(authorization);
			ResponseBuilder responseBuilder = Response.status(Status.NO_CONTENT);
			response = responseBuilder.build();
		} catch (NotAuthorizedException e) {
			// LOGGER.error(e, e); -- this is fine at this location
			response = Response.status(Status.NO_CONTENT).build();
		} catch (TimeoutException e) {
			// LOGGER.error(e, e); -- this is fine at this location
			response = Response.status(Status.NO_CONTENT).build();
		} catch (Exception e) {
			LOGGER.error(e, e);
			response = Response.serverError().build();
		} finally {
			if (session != null) {
				ConnectionProvider.getInstance().closeSession(session);
			}
		}
		asyncResponse.resume(response);
		
	}

}
