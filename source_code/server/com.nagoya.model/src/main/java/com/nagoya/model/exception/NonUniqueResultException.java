/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class NonUniqueResultException extends Exception {

	private static final long serialVersionUID = 1L;

	public NonUniqueResultException() {
		super();
	}
	
	public NonUniqueResultException(String message) {
		super(message);
	}
}
