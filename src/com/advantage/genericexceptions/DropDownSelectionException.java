package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class DropDownSelectionException extends RuntimeException {
	/**
	 * Exception to use when cannot find the Drop Down value to select
	 * 
	 * @param sError - Error Message
	 */
	public DropDownSelectionException(String sError)
	{
		super(sError);
	}
}
