package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class SelectWindowException extends RuntimeException {
	/**
	 * Exception to use when cannot select a window
	 * 
	 * @param sError - Error Message
	 */
	public SelectWindowException(String sError)
	{
		super(sError);
	}
}
