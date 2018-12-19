package com.nagoya.model.dbo.resource;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.nagoya.model.dbo.DBO;

/**
 * 
 * For more information regarding taxonomy see:
 * <a href="https://de.wikipedia.org/wiki/Art_(Biologie)">https://de.wikipedia.org/wiki/Art_(Biologie)</a> resp.
 * <a href="https://en.wikipedia.org/wiki/Species">https://en.wikipedia.org/wiki/Species</a>
 * 
 * @author Florin Bogdan Balint
 *
 */
@Audited
@Entity(name = "tgenetic_resource_taxonomy")
public class Taxonomy extends DBO {

	private static final long serialVersionUID = 1L;

	// Reich
	@Column(name = "kingdom")
	private String kingdom;

	// Stamm
	@Column(name = "phylum")
	private String phylum;

	// Klasse
	@Column(name = "_class")
	private String _class;

	// Ordnung
	@Column(name = "_order")
	private String order;

	// Familie
	@Column(name = "family")
	private String family;

	// Unter-Familie
	@Column(name = "sub_family")
	private String subFamily;

	// Ober-Tribus
	@Column(name = "super_tribe")
	private String superTribe;

	// Tribus
	@Column(name = "tribe")
	private String tribe;

	// Gattung
	@Column(name = "genus")
	private String genus;

	// Art
	@Column(name = "species")
	private String species;
	
	// default constructor
	public Taxonomy() {
		
	}

	/**
	 * @return the kingdom
	 */
	public String getKingdom() {
		return kingdom;
	}

	/**
	 * @param kingdom the kingdom to set
	 */
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	/**
	 * @return the phylum
	 */
	public String getPhylum() {
		return phylum;
	}

	/**
	 * @param phylum the phylum to set
	 */
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	/**
	 * @return the _class
	 */
	public String get_class() {
		return _class;
	}

	/**
	 * @param _class the _class to set
	 */
	public void set_class(String _class) {
		this._class = _class;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * @return the subFamily
	 */
	public String getSubFamily() {
		return subFamily;
	}

	/**
	 * @param subFamily the subFamily to set
	 */
	public void setSubFamily(String subFamily) {
		this.subFamily = subFamily;
	}

	/**
	 * @return the superTribe
	 */
	public String getSuperTribe() {
		return superTribe;
	}

	/**
	 * @param superTribe the superTribe to set
	 */
	public void setSuperTribe(String superTribe) {
		this.superTribe = superTribe;
	}

	/**
	 * @return the tribe
	 */
	public String getTribe() {
		return tribe;
	}

	/**
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}

	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}

	/**
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * @param species the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	
}
