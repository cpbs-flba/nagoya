package com.nagoya.dao.person;

import java.util.List;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.person.PersonDBO;
import com.nagoya.model.dbo.person.PersonLegalDBO;
import com.nagoya.model.dbo.person.PersonNaturalDBO;
import com.nagoya.model.dbo.user.OnlineUserDBO;
import com.nagoya.model.dbo.user.UserRequestDBO;
import com.nagoya.model.exception.ConflictException;
import com.nagoya.model.exception.InvalidObjectException;

public interface PersonDAO extends BasicDAO<PersonDBO> {

	/**
	 * Searches for a person based on the provided email address.
	 * 
	 * @param email
	 * @return
	 * @throws ConflictException - if more than one entry exists
	 */
	public PersonDBO findPersonForEmail(String email) throws ConflictException;

	/**
	 * Tries to register a person into the db.
	 * 
	 * @param person
	 * @return
	 * @throws ConflictException
	 */
	public PersonDBO register(PersonDBO person) throws ConflictException;

	/**
	 * Removes old saved sessions for the specified person.
	 * 
	 * @param person
	 */
	public void removeOldSessions(PersonDBO person);

	/**
	 * Returns the online user object based on the specified session token.
	 * 
	 * @param sessionToken
	 * @return
	 * @throws ConflictException
	 */
	public OnlineUserDBO getOnlineUser(String sessionToken) throws ConflictException;

	/**
	 * Searches for the user request based on the token.
	 * 
	 * @param token
	 * @return the userRequest object or NULL.
	 * @throws ConflictException
	 */
	public UserRequestDBO findUserRequest(String token) throws ConflictException;

	/**
	 * 
	 * @param person
	 * @throws InvalidObjectException
	 */
	public void delete(PersonDBO person) throws InvalidObjectException;

	/**
	 * Searches for a natural person based on the specified filter.
	 * 
	 * @return
	 */
	public List<PersonNaturalDBO> searchNatural(String filter, int maxResults);
	
	/**
	 * Searches for a legal person based on the specified filter.
	 * 
	 * @return
	 */
	public List<PersonLegalDBO> searchLegal(String filter, int maxResults);

}
