package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class SQL_UpdateException extends RuntimeException {
	/**
	 * Exception to use when a generic SQL exception occurs with an Update statement.
	 * 
	 * @param sError - Error Message
	 */
	public SQL_UpdateException(String sError)
	{
		super(sError);
	}
}