package com.subtitlor.exceptions;

/**
 * 
 * @author armel
 *
 */

public class ValidationException extends RuntimeException {
	
	// ======================================
	// = Constructors =
	// ======================================

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064183512838649581L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}
}
