/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class NotAuthorizedException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotAuthorizedException() {
		super();
	}
	
	public NotAuthorizedException(String message) {
		super(message);
	}
}
