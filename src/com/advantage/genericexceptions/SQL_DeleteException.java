package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class SQL_DeleteException extends RuntimeException {
	/**
	 * Exception to use when a generic SQL exception occurs with a Delete statement.
	 * 
	 * @param sError - Error Message
	 */
	public SQL_DeleteException(String sError)
	{
		super(sError);
	}
}