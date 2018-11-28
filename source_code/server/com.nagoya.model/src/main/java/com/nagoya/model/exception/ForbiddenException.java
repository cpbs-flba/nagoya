/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class ForbiddenException extends Exception {

	private static final long serialVersionUID = 1L;

	public ForbiddenException() {
		super();
	}
	
	public ForbiddenException(String message) {
		super(message);
	}
}
