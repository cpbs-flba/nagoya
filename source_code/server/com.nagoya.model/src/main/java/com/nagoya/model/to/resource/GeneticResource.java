/**
 * 
 */
package com.nagoya.model.to.resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.nagoya.model.dbo.resource.VisibilityType;

/**
 * @author Florin Bogdan Balint
 *
 */
public class GeneticResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String identifier;
	private String description;
	private String origin;
	private String source;
	private String hashSequence;
	private VisibilityType visibilityType;
	private Taxonomy taxonomy;
	private Set<ResourceFile> files = new HashSet<ResourceFile>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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

	/**
	 * @return the visibilityType
	 */
	public VisibilityType getVisibilityType() {
		return visibilityType;
	}

	/**
	 * @param visibilityType the visibilityType to set
	 */
	public void setVisibilityType(VisibilityType visibilityType) {
		this.visibilityType = visibilityType;
	}

	/**
	 * @return the files
	 */
	public Set<ResourceFile> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(Set<ResourceFile> files) {
		this.files = files;
	}

	/**
	 * @return the taxonomy
	 */
	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	/**
	 * @param taxonomy the taxonomy to set
	 */
	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}

}
