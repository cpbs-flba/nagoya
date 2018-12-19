/**
 * 
 */
package com.nagoya.model.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

/**
 * @author flba
 *
 */
@Audited
@MappedSuperclass
public abstract class DBO extends SimpleDBO {

	private static final long serialVersionUID = 1L;
	
	@Version
	@Column(name = "rev")
	private Long revision;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "creation_user")
	private String creationUser;

	@Column(name = "modification_date")
	private Date modificationDate;

	@Column(name = "modification_user")
	private String modificationUser;

	/**
	 * @return the revision
	 */
	public Long getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(Long revision) {
		this.revision = revision;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationUser
	 */
	public String getCreationUser() {
		return creationUser;
	}

	/**
	 * @param creationUser the creationUser to set
	 */
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * @return the modificationUser
	 */
	public String getModificationUser() {
		return modificationUser;
	}

	/**
	 * @param modificationUser the modificationUser to set
	 */
	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

}
