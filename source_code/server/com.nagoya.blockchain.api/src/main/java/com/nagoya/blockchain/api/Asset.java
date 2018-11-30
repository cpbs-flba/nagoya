package com.nagoya.blockchain.api;

import java.io.Serializable;

/**
 * Abstract class representing an asset.
 * 
 * @author flba
 *
 */
public abstract class Asset implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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

}
