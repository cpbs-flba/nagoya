/**
 * 
 */
package com.nagoya.model.to.person;

import java.io.Serializable;

/**
 * @author Florin Bogdan Balint
 *
 */
public class PersonKeys implements Serializable {

	private static final long serialVersionUID = 1L;

	private String publicKey;
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
