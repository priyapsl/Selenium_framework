package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class DropDownNoSuchElementException extends RuntimeException {
	/**
	 * Exception to use when cannot find the Drop Down element
	 * 
	 * @param sError - Error Message
	 */
	public DropDownNoSuchElementException(String sError)
	{
		super(sError);
	}
}
