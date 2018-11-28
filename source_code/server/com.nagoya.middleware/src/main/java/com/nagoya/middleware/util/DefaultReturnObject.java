/**
 * 
 */
package com.nagoya.middleware.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author flba
 * @author adim
 *
 */
public class DefaultReturnObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object entity;
	private Map<String, String> header = new HashMap<String, String>();

	/**
	 * @return the entity
	 */
	public Object getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Object entity) {
		this.entity = entity;
	}

	/**
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

}
