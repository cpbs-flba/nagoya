/**
 * 
 */
package com.nagoya.middleware.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagoya.common.crypto.DefaultPasswordEncryptionProvider;
import com.nagoya.dao.person.PersonDAO;
import com.nagoya.dao.person.impl.PersonDAOImpl;
import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.rest.UserResource;
import com.nagoya.middleware.service.blockchain.BlockchainHelper;
import com.nagoya.middleware.util.DefaultDateProvider;
import com.nagoya.middleware.util.DefaultIDGenerator;
import com.nagoya.middleware.util.DefaultReturnObject;
import com.nagoya.model.dbo.person.PersonKeys;
import com.nagoya.model.dbo.user.OnlineUser;
import com.nagoya.model.dbo.user.RequestType;
import com.nagoya.model.dbo.user.UserRequest;
import com.nagoya.model.exception.BadRequestException;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.ForbiddenException;
import com.nagoya.model.exception.InvalidObjectException;
import com.nagoya.model.exception.InvalidTokenException;
import com.nagoya.model.exception.NotAuthorizedException;
import com.nagoya.model.exception.ResourceOutOfDateException;
import com.nagoya.model.exception.TimeoutException;
import com.nagoya.model.to.person.Person;
import com.nagoya.model.to.person.PersonTransformer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * @author flba
 *
 */
public class UserService {

	private static final Logger LOGGER = LogManager.getLogger(UserService.class);

	private BlockchainHelper blockchainHelper;
	private PersonDAO personDAO;

	public UserService(Session session) {
		this.personDAO = new PersonDAOImpl(session);
		this.blockchainHelper = new BlockchainHelper();
	}

	public DefaultReturnObject login(Person person)
			throws NotAuthorizedException, BadRequestException, ConflictException, ForbiddenException {
		validateCredentialsExist(person);

		String email = person.getEmail();

		com.nagoya.model.dbo.person.Person existentPerson = personDAO.findPersonForEmail(email);
		if (existentPerson == null) {
			throw new NotAuthorizedException("Invalid email/password combination.");
		}

		boolean passwordCorrect = DefaultPasswordEncryptionProvider.isPasswordCorrect(existentPerson.getPassword(),
				person.getPassword());
		if (!passwordCorrect) {
			throw new NotAuthorizedException("Invalid email/password combination.");
		}

		boolean emailConfirmed = existentPerson.isEmailConfirmed();
		if (!emailConfirmed) {
			throw new ForbiddenException();
		}

		// login was okay, return the user object
		Person dto = PersonTransformer.getDTO(existentPerson);
		// hide the password hash
		dto.setPassword(null);
		DefaultReturnObject result = new DefaultReturnObject();
		result.setEntity(dto);

		// also set the JSON web token in the bearer authorization
		String jsonWebToken = createSession(existentPerson);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + jsonWebToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);

		return result;
	}

	public void register(Person person, String language) throws BadRequestException, ConflictException {
		validate(person);
		com.nagoya.model.dbo.person.Person toRegister = PersonTransformer.getDBO(null, person);

		// encrypt password
		String plainPassword = toRegister.getPassword();
		String encryptPassword = DefaultPasswordEncryptionProvider.encryptPassword(plainPassword);
		toRegister.setPassword(encryptPassword);

		// persist into the DB
		com.nagoya.model.dbo.person.Person registered = personDAO.register(toRegister);

		UserRequest userRequest = new UserRequest();
		userRequest.setPerson(registered);
		userRequest.setRequestType(RequestType.REGISTRATION);
		Date expirationDate = DefaultDateProvider.getDeadline24h();
		userRequest.setExpirationDate(expirationDate);
		String registrationToken = DefaultIDGenerator.generateRandomID();
		userRequest.setToken(registrationToken);
		personDAO.insert(userRequest, true);

		// send confirmation e-mail
		String email = person.getEmail();
		MailService mailService = new MailService(language);
		mailService.sendRegistratationMail(email, registrationToken, expirationDate);
	}

	public void resetPassword(Person person, String language) throws BadRequestException, ConflictException {
		if (person == null) {
			throw new BadRequestException();
		}
		String providedEmail = person.getEmail();
		if (StringUtil.isNullOrBlank(providedEmail)) {
			throw new BadRequestException();
		}
		com.nagoya.model.dbo.person.Person personForMail = personDAO.findPersonForEmail(providedEmail);
		if (personForMail == null) {
			return;
		}

		// we have a valid user
		UserRequest userRequest = new UserRequest();
		userRequest.setPerson(personForMail);
		userRequest.setRequestType(RequestType.PASSWORD_RECOVERY);
		Date expirationDate = DefaultDateProvider.getDeadline24h();
		userRequest.setExpirationDate(expirationDate);
		String token = DefaultIDGenerator.generateRandomID();
		userRequest.setToken(token);
		personDAO.insert(userRequest, true);

		// send confirmation e-mail
		String email = person.getEmail();
		MailService mailService = new MailService(language);
		mailService.sendPasswordResetMail(email, token, expirationDate);
	}

	public DefaultReturnObject confirmRequest(String token, Person personTO) throws TimeoutException,
			BadRequestException, ConflictException, InvalidObjectException, ResourceOutOfDateException {
		if (StringUtil.isNullOrBlank(token)) {
			throw new BadRequestException();
		}

		UserRequest userRequest = personDAO.findUserRequest(token);
		if (userRequest == null) {
			throw new BadRequestException();
		}

		Date expirationDate = userRequest.getExpirationDate();
		Date currentDate = Calendar.getInstance().getTime();

		if (expirationDate.before(currentDate)) {
			personDAO.delete(userRequest, true);
			throw new TimeoutException();
		}

		if (userRequest.getRequestType().equals(RequestType.REGISTRATION)) {
			com.nagoya.model.dbo.person.Person person = userRequest.getPerson();
			// set the email confirmation flag
			person.setEmailConfirmed(true);

			// TODO: generate keys and remove mockup
			PersonKeys pk = new PersonKeys();
			pk.setPublicKey("GfHq2tTVk9z4eXgyNEBphzvcS6BQfFPmp8U3Ah3V6G63rSd9MsP1PhMSdPsU");
			pk.setPrivateKey("2mWNaEKEJrtB1rEvHsFx8aQXMJMRX8dFYUe9vrMnsBQW5vL79zphrCsZMfr9Fxb33U");
			person.getKeys().add(pk);
			personDAO.update(person, true);

			Person dto = PersonTransformer.getDTO(person);
			// hide the password hash
			dto.setPassword(null);
			DefaultReturnObject result = new DefaultReturnObject();
			result.setEntity(dto);
			return result;
		}

		if (userRequest.getRequestType().equals(RequestType.ACCOUNT_REMOVAL)) {
			com.nagoya.model.dbo.person.Person person = userRequest.getPerson();
			personDAO.delete(person);
		}

		if (userRequest.getRequestType().equals(RequestType.PASSWORD_RECOVERY)) {
			if (personTO == null) {
				throw new BadRequestException();
			}
			String newPassword = personTO.getPassword();
			String newEncryptedPassword = DefaultPasswordEncryptionProvider.encryptPassword(newPassword);
			com.nagoya.model.dbo.person.Person person = userRequest.getPerson();
			person.setPassword(newEncryptedPassword);
			personDAO.update(person, true);
		}

		personDAO.delete(userRequest, true);

		return null;
	}

	private String createSession(com.nagoya.model.dbo.person.Person person) {
		// step 1: create new session
		String sessionToken = DefaultIDGenerator.generateRandomID();
		LOGGER.debug("Created token: " + sessionToken);
		Key key = MacProvider.generateKey();
		String privateKey = new String(Base64.getEncoder().encode(key.getEncoded()), StandardCharsets.UTF_8);

		// append the deadline to the token
		Date deadline = DefaultDateProvider.getDeadline(10);
		long time = deadline.getTime();
		Map<String, Object> claims = new HashMap<>();
		claims.put("deadline", time);

		String jsonWebToken = Jwts.builder().setClaims(claims).setSubject(sessionToken)
				.signWith(SignatureAlgorithm.HS512, key).compact();

		// persist
		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setPerson(person);
		onlineUser.setExpirationDate(deadline);
		onlineUser.setPrivateKey(privateKey);
		onlineUser.setSessionToken(sessionToken);
//		personDAO.removeOldSessions(person);
		personDAO.insert(onlineUser, true);

		return jsonWebToken;
	}

	public String updateSession(OnlineUser onlineUser) throws InvalidObjectException, ResourceOutOfDateException {
		// step 1: create new session
		String sessionToken = DefaultIDGenerator.generateRandomID();
		LOGGER.debug("Created token:\r\n" + sessionToken);
		Key key = MacProvider.generateKey();
		String privateKey = new String(Base64.getEncoder().encode(key.getEncoded()), StandardCharsets.UTF_8);

		// append the deadline to the token
		Date deadline = DefaultDateProvider.getDeadline(10);
		long time = deadline.getTime();
		Map<String, Object> claims = new HashMap<>();
		claims.put("deadline", time);

		String jsonWebToken = Jwts.builder().setClaims(claims).setSubject(sessionToken)
				.signWith(SignatureAlgorithm.HS512, key).compact();

		// persist
		onlineUser.setExpirationDate(deadline);
		onlineUser.setPrivateKey(privateKey);
		onlineUser.setSessionToken(sessionToken);
		personDAO.update(onlineUser, true);

		return jsonWebToken;
	}

	public static String checkSignature(String token, String privateKey) throws BadRequestException {
		try {
			byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
			Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(privateKeyBytes).parseClaimsJws(token);
			String subject = parseClaimsJws.getBody().getSubject();
			LOGGER.debug("Extracted subject:\r\n" + subject);
			return subject;
		} catch (Exception e) {
			throw new BadRequestException("Signature not ok.");
		}
	}

	public static String extractSessionToken(String jsonWebToken) throws InvalidTokenException {
		if (StringUtil.isNullOrBlank(jsonWebToken)) {
			throw new InvalidTokenException("Invalid token");
		}
		if (jsonWebToken.indexOf(".") < 1 || jsonWebToken.lastIndexOf(".") < 1) {
			throw new InvalidTokenException("Invalid token");
		}
		String token = jsonWebToken.substring(jsonWebToken.indexOf(".") + 1, jsonWebToken.lastIndexOf("."));
		byte[] decode = Base64.getDecoder().decode(token);
		String json = new String(decode);
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode actualObj = mapper.readTree(json);
			String sessionToken = actualObj.get("sub").asText();
			return sessionToken;
		} catch (IOException e) {
			throw new InvalidTokenException("Invalid token");
		}
	}

	private void validate(Person personTO) throws BadRequestException {
		validateCredentialsExist(personTO);

		if (personTO.getPersonType() == null) {
			throw new BadRequestException();
		}
	}

	private void validateCredentialsExist(Person personTO) throws BadRequestException {
		if (personTO == null) {
			throw new BadRequestException();
		}

		String email = personTO.getEmail();
		if (StringUtil.isNullOrBlank(email)) {
			throw new BadRequestException();
		}

		String password = personTO.getPassword();
		if (StringUtil.isNullOrBlank(password)) {
			throw new BadRequestException();
		}
	}

	public DefaultReturnObject delete(String authorization, String language)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
		OnlineUser onlineUser = validateSession(authorization);
		com.nagoya.model.dbo.person.Person person = onlineUser.getPerson();

		// we have a valid user
		UserRequest userRequest = new UserRequest();
		userRequest.setPerson(person);
		userRequest.setRequestType(RequestType.ACCOUNT_REMOVAL);
		Date expirationDate = DefaultDateProvider.getDeadline24h();
		userRequest.setExpirationDate(expirationDate);
		String token = DefaultIDGenerator.generateRandomID();
		userRequest.setToken(token);
		personDAO.insert(userRequest, true);

		// send confirmation e-mail
		String email = person.getEmail();
		MailService mailService = new MailService(language);
		mailService.sendDeleteAccountMail(email, token, expirationDate);

		// also set the JSON web token in the bearer authorization
		DefaultReturnObject result = new DefaultReturnObject();
		String newToken = createSession(person);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public OnlineUser validateSession(String jsonWebToken)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
		if (StringUtil.isNullOrBlank(jsonWebToken)) {
			throw new NotAuthorizedException();
		}

		String sessionToken = extractSessionToken(jsonWebToken);
		OnlineUser onlineUser = personDAO.getOnlineUser(sessionToken);
		if (onlineUser == null) {
			throw new NotAuthorizedException();
		}

		// if the session expired, remove the object
		Date expirationDate = onlineUser.getExpirationDate();
		Date currentDate = Calendar.getInstance().getTime();
		if (currentDate.after(expirationDate)) {
			try {
				// always delete the old/expired session
				personDAO.delete(onlineUser, true);
			} catch (InvalidObjectException e) {
				LOGGER.error(e, e);
			}
			// then throw an exception
			throw new TimeoutException();
		}

		return onlineUser;
	}

	public DefaultReturnObject update(String authorization, String language, Person personTO)
			throws NotAuthorizedException, ConflictException, TimeoutException, ForbiddenException,
			InvalidObjectException, ResourceOutOfDateException, InvalidTokenException {
		// validate the session token
		OnlineUser onlineUser = validateSession(authorization);
		com.nagoya.model.dbo.person.Person foundPerson = onlineUser.getPerson();

		// all updates must be confirmed via password confirmation
		String actualPassword = foundPerson.getPassword();
		String passwordConfirmation = personTO.getPasswordConfirmation();
		if (!DefaultPasswordEncryptionProvider.isPasswordCorrect(actualPassword, passwordConfirmation)) {
			throw new ForbiddenException();
		}

		String newPassword = personTO.getPassword();
		if (StringUtil.isNotNullOrBlank(newPassword)) {
			String encryptPassword = DefaultPasswordEncryptionProvider.encryptPassword(newPassword);
			personTO.setPassword(encryptPassword);
		} else {
			personTO.setPassword(foundPerson.getPassword());
		}
		foundPerson = PersonTransformer.getDBO(foundPerson, personTO);
		personDAO.update(foundPerson, true);

		DefaultReturnObject result = new DefaultReturnObject();
		Person dto = PersonTransformer.getDTO(foundPerson);
		result.setEntity(dto);
		String newToken = updateSession(onlineUser);
		String headerValue = UserResource.HEADER_AUTHORIZATION_BEARER + newToken;
		result.getHeader().put(UserResource.HEADER_AUTHORIZATION, headerValue);
		return result;
	}

	public void logout(String authorization)
			throws NotAuthorizedException, ConflictException, TimeoutException, InvalidTokenException {
		OnlineUser onlineUser = validateSession(authorization);
		try {
			personDAO.delete(onlineUser, true);
		} catch (InvalidObjectException e) {
			LOGGER.debug("Could not delete a user session - maybe it was already deleted.");
		}
	}

}
