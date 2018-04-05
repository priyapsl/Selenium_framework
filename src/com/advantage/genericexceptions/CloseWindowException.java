package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class CloseWindowException extends Exception {
	/**
	 * Exception to use when cannot close a window
	 * 
	 * @param sError - Error Message
	 */
	public CloseWindowException(String sError)
	{
		super(sError);
	}
}
