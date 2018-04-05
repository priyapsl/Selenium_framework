package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class NotFoundWindowException extends RuntimeException {
	/**
	 * Exception to use when cannot find a window
	 * 
	 * @param sError - Error Message
	 */
	public NotFoundWindowException(String sError)
	{
		super(sError);
	}
}
