package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class IllegalCharacterException extends RuntimeException {
	/**
	 * Exception to use when a illegal characters enters 
	 * 
	 * @param sError - Error Message
	 */
	public IllegalCharacterException(String sError)
	{
		super(sError);
	}
}