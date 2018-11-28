/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class ConflictException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConflictException() {
		super();
	}
	
	public ConflictException(String message) {
		super(message);
	}
}
