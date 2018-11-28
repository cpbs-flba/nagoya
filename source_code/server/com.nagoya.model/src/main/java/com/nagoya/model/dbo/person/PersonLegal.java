package com.nagoya.model.dbo.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;

@Audited
@Entity(name = "tperson_legal")
@PrimaryKeyJoinColumn(name = "person_id")
public class PersonLegal extends Person {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "commercial_register_number")
	private String commercialRegisterNumber;

	@Column(name = "tax_number")
	private String taxNumber;
	
	public PersonLegal() {
		this.setPersonType(PersonType.LEGAL); 
	}
	
	public PersonLegal(Person person) {
		this.setPersonType(PersonType.LEGAL);
		this.setEmail(person.getEmail());
		this.setPassword(person.getPassword());
		this.setAddress(person.getAddress());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the commercialRegisterNumber
	 */
	public String getCommercialRegisterNumber() {
		return commercialRegisterNumber;
	}

	/**
	 * @param commercialRegisterNumber the commercialRegisterNumber to set
	 */
	public void setCommercialRegisterNumber(String commercialRegisterNumber) {
		this.commercialRegisterNumber = commercialRegisterNumber;
	}

	/**
	 * @return the taxNumber
	 */
	public String getTaxNumber() {
		return taxNumber;
	}

	/**
	 * @param taxNumber the taxNumber to set
	 */
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

}
