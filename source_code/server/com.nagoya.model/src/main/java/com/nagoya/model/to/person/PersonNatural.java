package com.nagoya.model.to.person;

import java.util.Date;

import com.nagoya.model.dbo.person.PersonType;

public class PersonNatural extends Person {

	private static final long serialVersionUID = 1L;

	private String firstname;
	private String lastname;
	private Date birthdate;
	
	public PersonNatural() {
		this.setPersonType(PersonType.NATURAL);
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
