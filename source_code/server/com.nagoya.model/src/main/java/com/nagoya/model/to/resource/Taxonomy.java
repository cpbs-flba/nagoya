package com.nagoya.model.to.resource;

import java.io.Serializable;

/**
 * 
 * @author Florin Bogdan Balint
 *
 */
public class Taxonomy implements Serializable {

	private static final long serialVersionUID = 1L;

	private Taxonomy parent;
	private String name;

	/**
	 * @return the parent
	 */
	public Taxonomy getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Taxonomy parent) {
		this.parent = parent;
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
