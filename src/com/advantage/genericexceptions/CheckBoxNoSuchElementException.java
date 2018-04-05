package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class CheckBoxNoSuchElementException extends RuntimeException {
	/**
	 * Exception to use when cannot find the Check Box element
	 * 
	 * @param sError - Error Message
	 */
	public CheckBoxNoSuchElementException(String sError)
	{
		super(sError);
	}
}
