package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class DropDownIndexException extends RuntimeException {
	/**
	 * Exception to use when cannot convert the Drop Down index to a number
	 * 
	 * @param sError - Error Message
	 */
	public DropDownIndexException(String sError)
	{
		super(sError);
	}
}
