package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class TranslationsUpdateException extends RuntimeException {
	/**
	 * Exception to use when exception occurs attempting to update the translations from a file
	 * 
	 * @param sError - Error Message
	 */
	public TranslationsUpdateException(String sError)
	{
		super(sError);
	}
}