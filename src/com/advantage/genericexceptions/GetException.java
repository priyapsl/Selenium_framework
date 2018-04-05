package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class GetException extends RuntimeException {
	/**
	 * Exception to use when URL takes to long to respond or does not exist
	 * 
	 * @param sError - Error Message
	 */
	public GetException(String sError)
	{
		super(sError);
	}
}
