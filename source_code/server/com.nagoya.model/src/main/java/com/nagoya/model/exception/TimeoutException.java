/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class TimeoutException extends Exception {

	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}
	
	public TimeoutException(String message) {
		super(message);
	}
}
