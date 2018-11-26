/**
 * 
 */
package com.nagoya.middleware.util;

import java.io.Serializable;

/**
 * @author adim
 *
 */
@SuppressWarnings("serial")
public class DefaultReturnObject implements Serializable {

	private String json;
	private String jsonWebToken;

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * @return the jsonWebToken
	 */
	public String getJsonWebToken() {
		return jsonWebToken;
	}

	/**
	 * @param jsonWebToken the jsonWebToken to set
	 */
	public void setJsonWebToken(String jsonWebToken) {
		this.jsonWebToken = jsonWebToken;
	}

}
