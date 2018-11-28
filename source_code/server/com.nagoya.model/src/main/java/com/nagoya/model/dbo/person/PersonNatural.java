package com.nagoya.model.dbo.person;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

@Audited
@Entity(name = "tperson_natural")
@PrimaryKeyJoinColumn(name = "person_id")
public class PersonNatural extends Person {

	private static final long serialVersionUID = 1L;

	@Column(name = "first_name")
	private String firstname;

	@Column(name = "last_name")
	private String lastname;

	@Column(name = "birthdate")
	private Date birthdate;
	
	public PersonNatural() {
		this.setPersonType(PersonType.NATURAL); 
	}
	
	public PersonNatural(Person person) {
		this.setPersonType(PersonType.NATURAL);
		this.setEmail(person.getEmail());
		this.setPassword(person.getPassword());
		this.setAddress(person.getAddress());
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

