package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class WrongPageException extends RuntimeException {
	/**
	 * Exception to use when Page/URL is not as expected for a class
	 * 
	 * @param sError - Error Message
	 */
	public WrongPageException(String sError)
	{
		super(sError);
	}
}