package com.advantage.genericexceptions;

@SuppressWarnings("serial")
public class FrameNoSuchElementException extends RuntimeException {
	/**
	 * Exception to use when cannot select a frame
	 * 
	 * @param sError - Error Message
	 */
	public FrameNoSuchElementException(String sError)
	{
		super(sError);
	}
}
