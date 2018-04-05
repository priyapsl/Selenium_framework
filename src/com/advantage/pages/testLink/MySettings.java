package com.advantage.pages.testLink;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.advantage.datastructures.testLink.*;
import com.advantage.framework.Framework;
import com.advantage.genericexceptions.IllegalCharacterException;
import com.advantage.reporting.Report;
import com.advantage.localeutils.Translations;
import com.advantage.pages.testLink.Login;
import com.advantage.pages.testLink.HomePage;

/**
 * This class is for the MySettings page
 */

public class MySettings extends Framework {
	
	/* Page object locators */
	private final String sLoc_FirstName = "//*[@name='firstName']";
	private final String sLoc_LastName = "//*[@name='lastName']";
	private final String sLoc_Email = "//*[@name='emailAddress']";
	private final String sLoc_Locale = "//*[@name='locale']";
	private final String sLoc_Save = "//*[@value='"+ Translations.getTranslation("MySettings_SaveButton") +"']";
	private final static String sLoc_MainFrame = "//frame[@name='mainframe']";
	private final String sLoc_OldPassword = "//*[@name='oldpassword']";
	private final String sLoc_NewPassword = "//*[@name='newpassword']";
	private final String sLoc_ConfirmPassword = "//*[@name='newpassword_check']";
	//private final String sLoc_ChangePassword = "//*[@value='Change password']";
	private final String sLoc_ChangePassword = "//*[@value='"+ Translations.getTranslation("MySettings_ChangePasswordBtn") +"']";
	private final static String sLoc_MySettings = "//a[contains(@href, 'lib/usermanagement/userInfo.php')]";
	private final static String sLoc_TitleFrame = "//frame[@name='titlebar']";
	private final static String sLoc_EditSuccessMsg = "//*[@class='user_feedback']/p";
	private final static String sLoc_EditErr = "//*[@id='ext-gen28']";
	
	// Amount of time in seconds to wait for the popup on login
	public int nWaitForPopup = 5;

	// xpath to check for any errors
	private static final String sXpath_LoginError = "//div[@class='errorDiv']";
	
	/* Page object logical name */
	//private static final String sLog_MySettings = "My Settings";
	
	private static final String sLog_firstName = "First Name";
	private static final String sLog_lastName = "Last Name";
	private static final String sLog_email = "Email";
	private static final String sLog_locale = "Locale";
	private static final String sLog_Save = "Save";
	
	private static final String sLog_oldPassword = "Old Password";
	private static final String sLog_newPassword = "New Password";
	private static final String sLog_confirmPassword = "Confirm Password";
	private static final String sLog_changePassword = "Change Password";
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public MySettings(WebDriver driver)
	{
		super(driver);		
	}

	
	/**
	 * Enters the First Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterFirstName(String sInputValue)
	{
		
		WebElement firstName = findElement(driver, sLoc_FirstName);
		enterField(firstName, sLog_firstName, sInputValue);
	}
	
	/**
	 * Enters the Last Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterLastName(String sInputValue)
	{
		
		WebElement lastName = findElement(driver, sLoc_LastName);
		enterField(lastName, sLog_lastName, sInputValue);
	}
	
	/**
	 * Enters the Email field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterEmail(String sInputValue)
	{
		
		WebElement email = findElement(driver, sLoc_Email);
		enterField(email, sLog_email, sInputValue);
	}
	
	/**
	 * Select the Locale field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterLocale(String sInputValue)
	{
		WebElement locale = findElement(driver, sLoc_Locale);
		dropDownSelect(locale, sLog_locale, sInputValue);	
	}


	/**
	 * Clicks the Save button 
	 */
	public void clickSave()
	{
		
		WebElement addUserdata = findElement(driver, sLoc_Save);
		click(addUserdata, sLog_Save);
	}
	
	/** Verify invalid login name error
	 *  @param driver
	 */
	
	public String editInfoMsg()
	{
		
		WebElement successinfo = findElement(driver, sLoc_EditSuccessMsg);
		
		return successinfo.getText();
				
	}	
	
	/** Verify invalid login name error
	 *  @param driver
	 */
	
	public String EditInfoErr()
	{
		
		WebElement err = findElement(driver, sLoc_EditErr);
		
		return err.getText();
				
	}
	/**
	 * Enters the Old Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterOldPassword(String sInputValue)
	{
		
		WebElement opassword = findElement(driver, sLoc_OldPassword);
		enterField(opassword, sLog_oldPassword, sInputValue);
	}
	
	/**
	 * Enters the New Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterNewPassword(String sInputValue)
	{
		
		WebElement npassword = findElement(driver, sLoc_NewPassword);
		enterField(npassword, sLog_newPassword, sInputValue);
	}
	
	/**
	 * Enters the Confirm Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterConfirmPassword(String sInputValue)
	{
		
		WebElement cpassword = findElement(driver, sLoc_ConfirmPassword);
		enterField(cpassword, sLog_confirmPassword, sInputValue);
	}
	
	/**
	 * Clicks the Change Password button 
	 */
	public void clickChangePassword()
	{
		WebElement changepwd = findElement(driver, sLoc_ChangePassword);
		click(changepwd, sLog_changePassword);
	}
			
	/**
	 * Error detection
	 
	 * @param sErrorMsgExpected
	 */
	public void detectErrors(String sErrorMsgExpected)
	{
		String sErrorMsgActual = "";
		System.out.println(sErrorMsgExpected);

		// Are there any errors?
		if (bElementExists(sXpath_LoginError))
			sErrorMsgActual = getText(findElement(driver, sXpath_LoginError));
		else
			sErrorMsgActual = "";

		if (sErrorMsgActual.equalsIgnoreCase("") && sErrorMsgExpected.equalsIgnoreCase(""))
			Report.logPass("User logged in successfully");

		if (sErrorMsgActual.contains(sErrorMsgExpected))
			Report.logPass("The expected error message is displayed");
		else
			Report.logError("Expected Error Message1:" + sErrorMsgExpected + "<BR>" + "Actual Error Message:"
					+ sErrorMsgActual);
	}
	
	/**
	 * Edit User data into TestLink.<BR>	
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditUserSettings(String sEditInfo, MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		boolean retval=false;
		switchFrame(driver, sLoc_MainFrame);
		//Edit User information
		if(sEditInfo.equals(sLog_firstName)){
			EditFirstName(mysettingsdetails, sErrorMsg);
		}
		else if(sEditInfo.equals(sLog_lastName)){
			EditLastName(mysettingsdetails, sErrorMsg); 			
		}
		else if(sEditInfo.equals(sLog_email)){
			EditEmail(mysettingsdetails, sErrorMsg);
		}
		else if(sEditInfo.equals(sLog_locale)){
			EditLocale(mysettingsdetails, sErrorMsg);
		}
		else if(sEditInfo.equals(sLog_changePassword)){
			ChangePassword(mysettingsdetails, sErrorMsg);						
		}
		else if(sEditInfo.equals("Name")){
			EditFirstLastName(mysettingsdetails, sErrorMsg);			
		}
		else if(sEditInfo.equals("Email_Locale")){
			EditEmailLocale(mysettingsdetails, sErrorMsg);			
		}
		else if(sEditInfo.equals("Personal Data")) {
			retval =EditUserPersonalData(mysettingsdetails, sErrorMsg);
		}	
		else {
			Report.logInfo("Not a valid edit option");			
		}
		
		setDefaultFrame(driver);
		return retval;
	}	
	
	/**
	 * Verify edited User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 * @param details 
	 */
	public void VerifyEditedInfo(String sEditInfo, MySettingsDetails mysettingsdetails, String sErrorMsg, LoginDetails details)
	{
		HomePage homepage = new HomePage(driver);
		homepage.clickMySettings(driver);
		
		switchFrame(driver, sLoc_MainFrame);
		//Edit User information
		if(sEditInfo.equals(sLog_firstName)){
			WebElement firstName = findElement(driver, sLoc_FirstName);
			if(firstName.getAttribute("value").contains(mysettingsdetails.sFirstName)){
				Report.logPass("First Name is successfully updated as " + mysettingsdetails.sFirstName);
			}
			else {
				Report.logError("First Name is not updated as " + mysettingsdetails.sFirstName);
			}
			
		}
		else if(sEditInfo.equals(sLog_lastName)){
			WebElement lasttName = findElement(driver, sLoc_LastName);
			if(lasttName.getAttribute("value").contains(mysettingsdetails.sLastName)){
				Report.logPass("Last Name is successfully updated as " + mysettingsdetails.sLastName);
			}
			else {
				Report.logError("Last Name is not updated as " + mysettingsdetails.sLastName);
			} 			
		}
		else if(sEditInfo.equals(sLog_email)){
			WebElement email = findElement(driver, sLoc_Email);
			
			if(email.getAttribute("value").contains(mysettingsdetails.sEmail)){
				Report.logPass("Email is successfully updated as " + mysettingsdetails.sEmail);
			}
			else {
				Report.logError("Email is not updated as " + mysettingsdetails.sEmail);
			}
		}
		else if(sEditInfo.equals(sLog_locale)){
			WebElement locale = findElement(driver, sLoc_Locale);
			
			if(locale.getText().contains(mysettingsdetails.sLocale)){
				Report.logPass("Locale is successfully updated as " + mysettingsdetails.sLocale);
			}
			else {
				Report.logError("Locale is not updated as " + mysettingsdetails.sLocale);
			}
		}
		else if(sEditInfo.equals("Name")){
			WebElement firstName = findElement(driver, sLoc_FirstName);
			WebElement lasttName = findElement(driver, sLoc_LastName);
			
			if((firstName.getAttribute("value").equals(mysettingsdetails.sFirstName)) && 
					(lasttName.getAttribute("value").contains(mysettingsdetails.sLastName))){
				Report.logPass("First and last Name are successfully updated as " 
					+ mysettingsdetails.sFirstName + " and " + mysettingsdetails.sLastName );
			}
			else {
				Report.logError("First and last Name are not updated as " 
						+ mysettingsdetails.sFirstName + " and " + mysettingsdetails.sLastName );
			}			
			
		}
		else if(sEditInfo.equals("Email_Locale")){
			WebElement email = findElement(driver, sLoc_Email);
			WebElement locale = findElement(driver, sLoc_Locale);
			
			if((email.getAttribute("value").equals(mysettingsdetails.sEmail)) && 
					(locale.getText().contains(mysettingsdetails.sLocale))){
				Report.logPass("Email and Locale are successfully updated as " 
						+ mysettingsdetails.sEmail + " and " + mysettingsdetails.sLocale );
			}
			else {
				Report.logError("Email and Locale are not updated as " 
						+ mysettingsdetails.sEmail + " and " + mysettingsdetails.sLocale);
			}				
			
		}
		else if(sEditInfo.equals(sLog_changePassword)){			
			VerifyChangePassword(details, mysettingsdetails, sErrorMsg);		
								
		}
		else if(sEditInfo.equals("Personal Data")){
			WebElement firstName = findElement(driver, sLoc_FirstName);
			WebElement lasttName = findElement(driver, sLoc_LastName);
			WebElement email = findElement(driver, sLoc_Email);
			WebElement locale = findElement(driver, sLoc_Locale);
			
			if((firstName.getAttribute("value").equals(mysettingsdetails.sFirstName)) && 
					(lasttName.getAttribute("value").contains(mysettingsdetails.sLastName)) &&
					(email.getAttribute("value").equals(mysettingsdetails.sEmail)) && 
					(locale.getText().contains(mysettingsdetails.sLocale))){
				Report.logPass("Personal Data of user are successfully updated.");
			}
			else {
				Report.logError("Failed to update personal data." );
			}		
		}
		else {
			Report.logInfo("Not a valid edit option");
		}
		setDefaultFrame(driver);
	}
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditUserPersonalData(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterFirstName(mysettingsdetails.sFirstName);
		enterLastName(mysettingsdetails.sLastName);
		enterEmail(mysettingsdetails.sEmail);
		enterLocale(mysettingsdetails.sLocale);		
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
		
	}	
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditFirstName(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterFirstName(mysettingsdetails.sFirstName);	
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditLastName(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterLastName(mysettingsdetails.sLastName);		
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}	
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditFirstLastName(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterFirstName(mysettingsdetails.sFirstName);
		enterLastName(mysettingsdetails.sLastName);	
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}	
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditEmail(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details				
		enterEmail(mysettingsdetails.sEmail);	
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}	
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditLocale(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterLocale(mysettingsdetails.sLocale);
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}	
	
	/**
	 * Edit User data into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to edit user personal data to TestLink
	 */
	public boolean EditEmailLocale(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterEmail(mysettingsdetails.sEmail);
		enterLocale(mysettingsdetails.sLocale);	
		clickSave();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_EditUserInfoMsg")))) {
			String sMsg = "Application updated user information successfully and display message as : " + Translations.getTranslation("MySettings_EditUserInfoMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}
	}	
	/**
	 * Change Password into TestLink.<BR>
	 
	 * @param mysettingsdetails - variables need to Change Password to TestLink
	 */
	public boolean ChangePassword(MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
		// Edit user Details		
		enterOldPassword(mysettingsdetails.sOldPassword);
		enterNewPassword(mysettingsdetails.sNewPassword);
		enterConfirmPassword(mysettingsdetails.sConfirmPassword);
		clickChangePassword();
		sleep(10000);
		if ((bElementExists(driver, sLoc_EditSuccessMsg)) && 
				(editInfoMsg().equals(Translations.getTranslation("MySettings_ChangePwdMsg")))) {
			String sMsg = "Application updated user password successfully and display message as : " + Translations.getTranslation("MySettings_ChangePwdMsg");
			Report.logPass(sMsg);
			return true;
		}
		else {			
			
			String sError = "Application throws error as : " + EditInfoErr();
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError);
			return false;
							
		}

	}
	
	/**
	 * Verify changed password
	 
	 * @param details - variables need to login to TestLink
	 * @param mysettingsdetails - variables need to Change Password to TestLink
	 */
	public void VerifyChangePassword(LoginDetails details, MySettingsDetails mysettingsdetails, String sErrorMsg)
	{
				
		HomePage homepage = new HomePage(driver);
		setDefaultFrame(driver);
		homepage.clickLogout(driver);
		
		/*Login to the testlink with guest user*/
		Login loginPage = new Login(driver);
		loginPage.enterUserName(details.sUserName);
		loginPage.enterPassword(mysettingsdetails.sNewPassword);
		loginPage.clickLoginAndWait();
		
		/*Verify guest user logged successfully*/
		homepage.verifyLoggedInUser(driver, details.sUserName);		
		
		switchFrame(driver, sLoc_TitleFrame);
		WebElement mysettings = findElement(driver, sLoc_MySettings);
		if(mysettings.isDisplayed()) {
			Report.logPass("Password updated successfully.");
		}
		else {
			Report.logError("Password update failed.");
		}				
		detectErrors(sErrorMsg);
	}
	
}
