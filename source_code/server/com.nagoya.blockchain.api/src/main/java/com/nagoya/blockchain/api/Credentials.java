/**
 * 
 */
package com.nagoya.blockchain.api;

import java.io.Serializable;

/**
 * @author flba
 *
 */
public class Credentials implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String publicKey;
	private String privateKey;

	public Credentials() {
		// noop
	}

	public Credentials(String publicKey, String privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

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
