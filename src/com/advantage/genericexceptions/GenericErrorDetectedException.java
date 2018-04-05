package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class GenericErrorDetectedException extends RuntimeException {
	/**
	 * Exception to use when a generic error is detected on the page
	 * 
	 * @param sError - Error Message
	 */
	public GenericErrorDetectedException(String sError)
	{
		super(sError);
	}
}
