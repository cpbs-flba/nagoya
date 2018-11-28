/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class InvalidTokenException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		super();
	}
	
	public InvalidTokenException(String message) {
		super(message);
	}
}
