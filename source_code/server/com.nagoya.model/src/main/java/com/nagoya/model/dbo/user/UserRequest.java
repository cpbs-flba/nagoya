package com.nagoya.model.dbo.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.Person;
import com.nagoya.model.dbo.resource.GeneticResourceTransfer;

@Entity(name = "tuser_request")
public class UserRequest extends DBO {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "person_id")
	private Person person;

	@OneToOne(optional = true)
	@JoinColumn(name = "genetic_resource_transfer_id")
	private GeneticResourceTransfer transfer;

	@Column(name = "token", nullable = false)
	private String token;

	@Column(name = "request_type", nullable = false)
	private RequestType requestType;

	@Column(name = "expiration_date", nullable = false)
	private Date expirationDate;

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the requestType
	 */
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the transfer
	 */
	public GeneticResourceTransfer getTransfer() {
		return transfer;
	}

	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(GeneticResourceTransfer transfer) {
		this.transfer = transfer;
	}

	
}
