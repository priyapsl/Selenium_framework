package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables for Guest User
 */
public class GuestUserDetails {
	
	public String sLoginName, sPassword, sRepeatPassword, sFirstName, sLastName, sEmail;	
	
	/**
	 * Constructor that required  fields to add guest user data
	 * 
	 * @param sLoginName - Login Name
	 * @param sPassword - Password
	 * @param sRepeatPassword - Repeat Password
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sEmail - Email
	 * 
	 */
	
	public GuestUserDetails(String sLoginName, String sPassword, String sRepeatPassword, String sFirstName,
			String sLastName, String sEmail)
	{
		// No language assume English user
		set(sLoginName, sPassword, sRepeatPassword, sFirstName, sLastName, sEmail);
	}

	
	
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sEmail == null) ? 0 : sEmail.hashCode());
		result = prime * result
				+ ((sFirstName == null) ? 0 : sFirstName.hashCode());
		result = prime * result
				+ ((sLastName == null) ? 0 : sLastName.hashCode());
		result = prime * result
				+ ((sLoginName == null) ? 0 : sLoginName.hashCode());
		result = prime * result
				+ ((sPassword == null) ? 0 : sPassword.hashCode());
		result = prime * result
				+ ((sRepeatPassword == null) ? 0 : sRepeatPassword.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuestUserDetails other = (GuestUserDetails) obj;
		if (sEmail == null) {
			if (other.sEmail != null)
				return false;
		} else if (!sEmail.equals(other.sEmail))
			return false;
		if (sFirstName == null) {
			if (other.sFirstName != null)
				return false;
		} else if (!sFirstName.equals(other.sFirstName))
			return false;
		if (sLastName == null) {
			if (other.sLastName != null)
				return false;
		} else if (!sLastName.equals(other.sLastName))
			return false;
		if (sLoginName == null) {
			if (other.sLoginName != null)
				return false;
		} else if (!sLoginName.equals(other.sLoginName))
			return false;
		if (sPassword == null) {
			if (other.sPassword != null)
				return false;
		} else if (!sPassword.equals(other.sPassword))
			return false;
		if (sRepeatPassword == null) {
			if (other.sRepeatPassword != null)
				return false;
		} else if (!sRepeatPassword.equals(other.sRepeatPassword))
			return false;
		return true;
	}




	@Override
	public String toString() {
		return "GuestUserDetails [sLoginName=" + sLoginName + ", sPassword="
				+ sPassword + ", sRepeatPassword=" + sRepeatPassword
				+ ", sFirstName=" + sFirstName + ", sLastName=" + sLastName
				+ ", sEmail=" + sEmail + "]";
	}




	/**
	 * Sets all the variables.
	 * 
	 * @param sTestSuiteName - Test Suite Name
	 * @param sDescription - Description	 
	 */
	private void set(String sLoginName, String sPassword, String sRepeatPassword, String sFirstName,
			String sLastName, String sEmail)
	{
		this.sLoginName = sLoginName;
		this.sPassword = sPassword;	
		this.sRepeatPassword = sRepeatPassword;	
		this.sFirstName = sFirstName;	
		this.sLastName = sLastName;	
		this.sEmail = sEmail;		
		
			
	}


}