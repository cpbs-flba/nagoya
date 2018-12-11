/**
 * 
 */
package com.nagoya.model.dbo.resource;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.nagoya.model.dbo.DBO;

/**
 * @author Florin Bogdan Balint
 *
 */
@Entity(name = "tgenetic_resource_file")
public class ResourceFile extends DBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "content")
	private byte[] content;

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

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

}
