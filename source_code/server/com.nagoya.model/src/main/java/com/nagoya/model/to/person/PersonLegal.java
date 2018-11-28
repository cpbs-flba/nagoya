package com.nagoya.model.to.person;

public class PersonLegal extends Person {

	private static final long serialVersionUID = 1L;

	private String name;
	private String commercialRegisterNumber;
	private String taxNumber;

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
