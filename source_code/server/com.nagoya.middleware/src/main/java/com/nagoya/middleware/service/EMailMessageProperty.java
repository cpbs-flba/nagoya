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
package com.nagoya.middleware.service;

public enum EMailMessageProperty {

	EMAIL("email"), //
	USERNAME("username"), //
	PASSWORD("password"), //

	MAIL_SMTP_AUTH("mail.smtp.auth"), //
	MAIL_SMTP_STARTTLS_ENABLE("mail.smtp.starttls.enable"), //
	MAIL_SMTP_HOST("mail.smtp.host"), //
	MAIL_SMTP_PORT("mail.smtp.port") //
	;
	
	private String property;

	private EMailMessageProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
}
