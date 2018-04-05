package com.advantage.datastructures.testLink;

/**
 * Data Structure to hold variables for Create New User
 */
public class CreateNewUserDetails {
	@Override
	public String toString() {
		return "CreateNewUserDetails [sLogin=" + sLogin + ", sFirstName="
				+ sFirstName + ", sLastName=" + sLastName + ", sPassword="
				+ sPassword + ", sEmail=" + sEmail + ", sRole=" + sRole
				+ ", sLocale=" + sLocale + ", sAuthMethod=" + sAuthMethod
				+ ", bActiveFlag=" + bActiveFlag + "]";
	}


	public String sLogin, sFirstName, sLastName, sPassword, sEmail, sRole, sLocale, sAuthMethod;
	public boolean bActiveFlag; // Flag whether to active user or not


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateNewUserDetails other = (CreateNewUserDetails) obj;
		if (bActiveFlag != other.bActiveFlag)
			return false;
		if (sAuthMethod == null) {
			if (other.sAuthMethod != null)
				return false;
		} else if (!sAuthMethod.equals(other.sAuthMethod))
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
		if (sLogin == null) {
			if (other.sLogin != null)
				return false;
		} else if (!sLogin.equals(other.sLogin))
			return false;
		if (sPassword == null) {
			if (other.sPassword != null)
				return false;
		} else if (!sPassword.equals(other.sPassword))
			return false;
		if (sRole == null) {
			if (other.sRole != null)
				return false;
		} else if (!sRole.equals(other.sRole))
			return false;
		return true;
	}

	/**
	 * Constructor that required  fields to create new user
	 * 
	 * @param sLogin - Login Name
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sPassword - Password
	 *  @param sEmail - Email
	 */
	public CreateNewUserDetails(String sLogin, String sFirstName, 
			String sLastName, String sPassword, String sEmail)
	{
		// No language assume English user
		set(sLogin, sFirstName, sLastName, sPassword, sEmail, "", "", "", true);
	}

	/**
	 * Constructor that required  fields to create new user by defining Role
	 * 
	 * @param sLogin - Login Name
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sPassword - Password
	 * @param sEmail - Email
	 * @param sRole - Role
	 */
	public CreateNewUserDetails(String sLogin, String sFirstName, 
			String sLastName, String sPassword, String sEmail, String sRole)
	{
		// No language assume English user
		set(sLogin, sFirstName, sLastName, sPassword, sEmail, sRole, "", "", true);
	}
	
	/**
	 * Constructor that required  fields to create new user by defining Role, Locale
	 * 
	 * @param sLogin - Login Name
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sPassword - Password
	 * @param sEmail - Email
	 * @param sRole - Role
	 * @param sLocale - Locale
	 */
	public CreateNewUserDetails(String sLogin, String sFirstName, 
			String sLastName, String sPassword, String sEmail, String sRole, String sLocale)
	{
		// No language assume English user
		set(sLogin, sFirstName, sLastName, sPassword, sEmail, sRole, sLocale, "", true);
	}
	
	/**
	 * Constructor that required  fields to create new user by defining Role, Locale, Authentication Method
	 * 
	 * @param sLogin - Login Name
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sPassword - Password
	 * @param sEmail - Email
	 * @param sRole - Role
	 * @param sLocale - Locale
	 * @param sAuthMethod - Authentication Method
	 */
	public CreateNewUserDetails(String sLogin, String sFirstName, 
			String sLastName, String sPassword, String sEmail, String sRole, String sLocale, String sAuthMethod)
	{
		// No language assume English user
		set(sLogin, sFirstName, sLastName, sPassword, sEmail, sRole, sLocale, sAuthMethod, true);
	}
	
	/**
	 * Constructor that required  fields to create new user by defining Role, Locale, Authentication Method, ActiveFlag
	 * 
	 * @param sLogin - Login Name
	 * @param sFirstName - First Name
	 * @param sLastName - Last Name
	 * @param sPassword - Password
	 * @param sEmail - Email
	 * @param sRole - Role
	 * @param sLocale - Locale
	 * @param sAuthMethod - Authentication Method
	 * @param bActiveFlag - Active flag for user (Active - true and Disable  - False)
	 * 
	 */
	public CreateNewUserDetails(String sLogin, String sFirstName, 
			String sLastName, String sPassword, String sEmail, String sRole, String sLocale, String sAuthMethod, boolean bActiveFlag)
	{
		// No language assume English user
		set(sLogin, sFirstName, sLastName, sPassword, sEmail, sRole, sLocale, sAuthMethod, bActiveFlag);
	}
		
	
	/**
	 * Sets all the variables.
	 * 
	 * @param sUserName - User Name
	 * @param sPassword - Password
	 * @param lang - Language to use
	 * @param bChangePassword - Click Change Password link?
	 * @param sNewPassword - New Password
	 * @param sConfirmPassword - Confirm Password
	 */
	private void set(String sLogin, String sFirstName, String sLastName, 
			String sPassword, String sEmail, String sRole, String sLocale, 
			String sAuthMethod, boolean bActiveFlag)
	{
		this.sLogin = sLogin;
		this.sFirstName = sFirstName;
		this.sLastName = sLastName;
		this.sPassword = sPassword;
		this.sEmail = sEmail;
		this.sRole = sRole;
		this.sLocale = sLocale;
		this.sAuthMethod = sAuthMethod;
		this.bActiveFlag = bActiveFlag;		
	}


}