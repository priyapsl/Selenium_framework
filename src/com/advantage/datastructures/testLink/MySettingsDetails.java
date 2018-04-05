package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables for My Settings
 */
public class MySettingsDetails {
	
	public String sEditOption, sFirstName, sLastName, sEmail, sLocale, sOldPassword, sNewPassword, sConfirmPassword;	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((sConfirmPassword == null) ? 0 : sConfirmPassword.hashCode());
		result = prime * result
				+ ((sEditOption == null) ? 0 : sEditOption.hashCode());
		result = prime * result + ((sEmail == null) ? 0 : sEmail.hashCode());
		result = prime * result
				+ ((sFirstName == null) ? 0 : sFirstName.hashCode());
		result = prime * result
				+ ((sLastName == null) ? 0 : sLastName.hashCode());
		result = prime * result + ((sLocale == null) ? 0 : sLocale.hashCode());
		result = prime * result
				+ ((sNewPassword == null) ? 0 : sNewPassword.hashCode());
		result = prime * result
				+ ((sOldPassword == null) ? 0 : sOldPassword.hashCode());
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
		MySettingsDetails other = (MySettingsDetails) obj;
		if (sConfirmPassword == null) {
			if (other.sConfirmPassword != null)
				return false;
		} else if (!sConfirmPassword.equals(other.sConfirmPassword))
			return false;
		if (sEditOption == null) {
			if (other.sEditOption != null)
				return false;
		} else if (!sEditOption.equals(other.sEditOption))
			return false;
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
		if (sLocale == null) {
			if (other.sLocale != null)
				return false;
		} else if (!sLocale.equals(other.sLocale))
			return false;
		if (sNewPassword == null) {
			if (other.sNewPassword != null)
				return false;
		} else if (!sNewPassword.equals(other.sNewPassword))
			return false;
		if (sOldPassword == null) {
			if (other.sOldPassword != null)
				return false;
		} else if (!sOldPassword.equals(other.sOldPassword))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "MySettingsDetails [sEditOption=" + sEditOption
				+ ", sFirstName=" + sFirstName + ", sLastName=" + sLastName
				+ ", sEmail=" + sEmail + ", sLocale=" + sLocale
				+ ", sOldPassword=" + sOldPassword + ", sNewPassword="
				+ sNewPassword + ", sConfirmPassword=" + sConfirmPassword + "]";
	}


	/**
	 * Constructor that required  fields to edit user details
	 * 
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sEmail - Email
	 * @param sLocale - Locale
	 * @param sOldPassword - Old Password
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password
	 */
	
	public MySettingsDetails(String sEditOption, String sFirstName, String sLastName, String sEmail, String sLocale,
			String sOldPassword, String sNewPassword, String sConfirmPassword)
	{
		// Edit all user personal data
		set(sEditOption, sFirstName, sLastName, sEmail, sLocale, sOldPassword, sNewPassword, sConfirmPassword);
	}

	
	/**
	 * Constructor that required  fields to edit user details (Personal data)
	 * 
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sEmail - Email
	 * @param sLocale - Locale	
	 */
	
	public MySettingsDetails(String sEditOption, String sFirstName, String sLastName, String sEmail, String sLocale)
	{
		// Edit all user personal data
		set(sEditOption, sFirstName, sLastName, sEmail, sLocale, "", "", "");
	}
	
	/**
	 * Constructor that required  fields to edit user details (Change Password)
	 *  
	 * @param sOldPassword - Old Password
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password
	 */
	
	public MySettingsDetails(String sEditOption, String sOldPassword, String sNewPassword, String sConfirmPassword)
	{
		// Edit all user personal data
		set(sEditOption,"", "", "", "", sOldPassword, sNewPassword, sConfirmPassword);
	}
	
	/**
	 * Constructor that required  fields to edit user details
	 * 
	 * @param sEmail - Email
	 * @param sLocale - Locale	 
	 */
	
	public MySettingsDetails(String sEditOption,String sEmail, String sLocale)
	{
		// Edit all user personal data
		set(sEditOption,"", "", sEmail, sLocale, "", "", "");
	}
	
	/**
	 * Sets all the variables.
	 * 
	 *@param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sEmail - Email
	 * @param sLocale - Locale
	 * @param sOldPassword - Old Password
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password 
	 */
	private void set(String sEditOption, String sFirstName, String sLastName, String sEmail, String sLocale,
			String sOldPassword, String sNewPassword, String sConfirmPassword)
	{
		this.sEditOption = sEditOption;
		this.sFirstName = sFirstName;
		this.sLastName = sLastName;
		this.sEmail = sEmail;
		this.sLocale = sLocale;
		this.sOldPassword = sOldPassword;
		this.sNewPassword = sNewPassword;
		this.sConfirmPassword = sConfirmPassword;
			
	}


}