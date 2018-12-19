/**
 * 
 */
package com.nagoya.model.dbo.resource;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.Person;

/**
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tgenetic_resource")
public class GeneticResource extends DBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "description")
	private String description;

	@Column(name = "origin")
	private String origin;

	@Column(name = "source")
	private String source;

	@Column(name = "hash_sequence")
	private String hashSequence;

	@Enumerated(EnumType.STRING)
	@Column(name = "visibility_type", nullable = false)
	private VisibilityType visibilityType;

	@NotAudited
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "genetic_resource_id")
	private Set<ResourceFile> files = new HashSet<ResourceFile>();

	@NotAudited
	@OneToOne
	@JoinColumn(name = "person_id")
	private Person owner;

	@NotAudited
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "taxonomy_id")
	private Taxonomy taxonomy;

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
	 * @return the owner
	 */
	public Person getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Person owner) {
		this.owner = owner;
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
