package com.advantage.datastructures.testLink;


/**
 * Data Structure to hold variables for Login
 */
public class LoginDetails {
	public String sUserName, sPassword;
	

	/**
	 * Constructor for Login
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 */
	public LoginDetails(String sUserName, String sPassword)
	{
		// No language assume English user
		set(sUserName, sPassword);
	}

	

	/**
	 * Sets all the variables.
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password	
	 */
	private void set(String sUserName, String sPassword)
	{
		this.sUserName = sUserName;
		this.sPassword = sPassword;		
	}

	
}