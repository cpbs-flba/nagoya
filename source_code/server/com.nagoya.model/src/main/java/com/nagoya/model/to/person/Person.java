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
	private PersonType personType;

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


}
