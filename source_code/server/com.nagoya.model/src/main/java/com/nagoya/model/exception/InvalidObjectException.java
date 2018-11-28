/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class InvalidObjectException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidObjectException() {
		super();
	}
	
	public InvalidObjectException(String message) {
		super(message);
	}
}
