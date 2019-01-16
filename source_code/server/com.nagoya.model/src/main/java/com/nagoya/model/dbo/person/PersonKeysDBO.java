/**
 * 
 */
package com.nagoya.model.dbo.person;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.nagoya.model.dbo.DBO;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tperson_keys")
public class PersonKeysDBO extends DBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "public_key", nullable = false)
	private String publicKey;

	@Column(name = "private_key")
	private String privateKey;

	/**
	 * @return the publicKey
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
