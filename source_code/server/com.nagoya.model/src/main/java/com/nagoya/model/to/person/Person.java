/**
 * 
 */
package com.nagoya.model.to.person;

import java.io.Serializable;

import com.nagoya.model.dbo.person.PersonType;

/**
 * @author flba
 *
 */
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	/**
	 * Always use this field for confirmation operations. This should be filled with
	 * the actual password.
	 */
	private String passwordConfirmation;
	private PersonType personType;

	private Address address;

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
	public Address getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

}
