/*******************************************************************************
 * Copyright (c) 2004 - 2019 CPB Software AG
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 *
 * This software is published under the Apache License, Version 2.0, January 2004, 
 * http://www.apache.org/licenses/
 *  
 * Author: Florin Bogdan Balint
 *******************************************************************************/
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
