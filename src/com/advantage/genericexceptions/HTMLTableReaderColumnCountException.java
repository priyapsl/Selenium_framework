package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class HTMLTableReaderColumnCountException extends RuntimeException {
	/**
	 * Exception to use when HTMLTableReader cannot find any columns for the table
	 * 
	 * @param sError - Error Message
	 */
	public HTMLTableReaderColumnCountException(String sError)
	{
		super(sError);
	}
}