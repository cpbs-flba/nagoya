/**
 * 
 */
package com.nagoya.model.exception;

/**
 * @author flba
 *
 */
public class ResourceOutOfDateException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceOutOfDateException() {
		super();
	}
	
	public ResourceOutOfDateException(String message) {
		super(message);
	}
}
