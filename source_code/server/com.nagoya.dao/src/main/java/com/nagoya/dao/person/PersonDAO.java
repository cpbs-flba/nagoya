package com.nagoya.dao.person;

import com.nagoya.dao.base.BasicDAO;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.user.OnlineUser;
import com.nagoya.model.dbo.user.UserRequest;
import com.nagoya.model.exception.ConflictException;

public interface PersonDAO extends BasicDAO<Person> {

	/**
	 * Searches for a person based on the provided email address.
	 * @param email
	 * @return
	 * @throws ConflictException - if more than one entry exists
	 */
	public Person findPersonForEmail(String email) throws ConflictException;
	
	/**
	 * Tries to register a person into the db.
	 * @param person
	 * @return
	 * @throws ConflictException
	 */
	public Person register(Person person) throws ConflictException;
	
	/**
	 * Removes old saved sessions for the specified person.
	 * 
	 * @param person
	 */
	public void removeOldSessions(Person person);
	
	/**
	 * Returns the online user object based on the specified session token.
	 * 
	 * @param sessionToken
	 * @return
	 * @throws ConflictException 
	 */
	public OnlineUser getOnlineUser(String sessionToken) throws ConflictException;
	
	/**
	 * Searches for the user request based on the token.
	 * @param token
	 * @return the userRequest object or NULL.
	 * @throws ConflictException 
	 */
	public UserRequest findUserRequest(String token) throws ConflictException;
	
	
}
