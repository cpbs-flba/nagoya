/**
 * 
 */
package com.nagoya.model.to.resource.filter;

import java.io.Serializable;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResourceFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String identifier;
	private String description;
	private String origin;
	private String source;
	private String hashSequence;

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the hashSequence
	 */
	public String getHashSequence() {
		return hashSequence;
	}

	/**
	 * @param hashSequence the hashSequence to set
	 */
	public void setHashSequence(String hashSequence) {
		this.hashSequence = hashSequence;
	}

}
