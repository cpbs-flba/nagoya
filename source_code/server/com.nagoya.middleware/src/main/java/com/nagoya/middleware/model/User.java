/**
 * 
 */
package com.nagoya.middleware.model;

import java.io.Serializable;

/**
 * @author adim
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
