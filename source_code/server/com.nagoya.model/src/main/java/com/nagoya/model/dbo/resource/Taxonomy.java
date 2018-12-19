package com.nagoya.model.dbo.resource;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.SimpleDBO;

/**
 * 
 * @author Florin Bogdan Balint
 *
 */
@Entity(name = "tgenetic_resource_taxonomy")
public class Taxonomy extends SimpleDBO {

	private static final long serialVersionUID = 1L;

	@NotAudited
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private Taxonomy parent;

	@Column(name = "name")
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
