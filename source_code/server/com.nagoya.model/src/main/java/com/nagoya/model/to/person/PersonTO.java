/**
 * 
 */
package com.nagoya.model.to.person;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nagoya.blockchain.api.Credentials;

/**
 * @author flba
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	/**
	 * new password
	 */
	private String password;
	/**
	 * Always use this field for confirmation operations. This should be filled with
	 * the actual password.
	 */
	private String passwordConfirmation;
	private PersonType personType;

	private AddressTO address;

	private Credentials credentials;

	private Set<PersonKeysTO> keys = new HashSet<PersonKeysTO>();

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordConfirmation
	 */
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	/**
	 * @param passwordConfirmation the passwordConfirmation to set
	 */
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	/**
	 * @return the personType
	 */
	public PersonType getPersonType() {
		return personType;
	}

	/**
	 * @param personType the personType to set
	 */
	public void setPersonType(PersonType personType) {
		this.personType = personType;
	}

	/**
	 * 
	 * @return the address
	 */
	public AddressTO getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address the address to set
	 */
	public void setAddress(AddressTO address) {
		this.address = address;
	}

	/**
	 * @return the credentials
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the keys
	 */
	public Set<PersonKeysTO> getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(Set<PersonKeysTO> keys) {
		this.keys = keys;
	}

}
