package com.advantage.pages.testLink;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.advantage.datastructures.testLink.*;

import com.advantage.framework.Framework;
import com.advantage.genericexceptions.IllegalCharacterException;
import com.advantage.localeutils.Translations;
import com.advantage.reporting.Report;


/**
 * This class is for the Login page
 */

public class FirstLogin extends Framework {
	
	/* Page object locators */
	private final String sLoc_LoginName = "//*[@name='login']";
	private final String sLoc_Password = "//*[@name='password']";
	private final String sLoc_RepeatPassword = "//*[@name='password2']";
	private final String sLoc_FirstName = "//*[@name='firstName']";
	private final String sLoc_LastName = "//*[@name='lastName']";
	private final String sLoc_Email = "//*[@name='email']";
	private final String sLoc_AddUserData = "//*[@name='doEditUser']";
	private final String sLoc_lnkBackToLogin = "//a[contains(@href, 'login.php')]";
	private final String sLoc_errmsg = "//*[@id='login_div']/div[1]";
	public static WebDriver driverNULL;
	
	// Amount of time in seconds to wait for the popup on login
	public int nWaitForPopup = 5;

	// xpath to check for any errors
	private static final String sXpath_LoginError = "//div[@class='errorDiv']";
	
	/* Page object logical name */
	private static final String sLog_loginName = "Login Name";
	private static final String sLog_password = "Password";
	private static final String sLog_repeatPassword = "Repeat Password";
	private static final String sLog_firstName = "First Name";
	private static final String sLog_lastName = "Last Name";
	private static final String sLog_email = "Email";
	private static final String sLog_addUserData = "Add User Data";
	private static final String sLog_backToLogin = "Back To Login";
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public FirstLogin(WebDriver driver)
	{
		super(driver);		
	}

	/**
	 * Enters the Login Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterLoginName(String sInputValue)
	{
		
		WebElement loginName = findElement(driver, sLoc_LoginName);
		enterField(loginName, sLog_loginName, sInputValue);
	}

	/**
	 * Enters the Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterPassword(String sInputValue)
	{
		WebElement password = findElement(driver, sLoc_Password);
		enterField(password, sLog_password, sInputValue);
	}
	
	/**
	 * Enters the Repeat Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterRepeatPassword(String sInputValue)
	{
		WebElement repeatpassword = findElement(driver, sLoc_RepeatPassword);
		enterField(repeatpassword, sLog_repeatPassword, sInputValue);
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
	 * Clicks the Add User Data button 
	 */
	public void clickAddUserData()
	{
		WebElement addUserdata = findElement(driver, sLoc_AddUserData);
		click(addUserdata, sLog_addUserData);
	}
	
	/**
	 * Clicks the Back to Login link (First Login)
	 */
	public void clickBackToLogin()
	{
		WebElement backToLogin = findElement(driver, sLoc_lnkBackToLogin);
		click(backToLogin, sLog_backToLogin);		
	}
	
	/** Verify invalid data error
	 *  @param driver
	 */
	
	public String invalidDataErrMsg()
	{
		
		WebElement errmsg = findElement(driver, sLoc_errmsg);
		
		return errmsg.getText();
				
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
	 * Add data of Guest User into TestLink.<BR>
	 
	 * @param guestuserdetails - variables need to add guest user data to TestLink
	 */
	public boolean GuestUser(GuestUserDetails guestuserdetails, String sErrorMsg)
	{
		// Enter Guest user Details
		enterLoginName(guestuserdetails.sLoginName);
		enterPassword(guestuserdetails.sPassword);
		enterRepeatPassword(guestuserdetails.sRepeatPassword);
		enterFirstName(guestuserdetails.sFirstName);
		enterLastName(guestuserdetails.sLastName);
		enterEmail(guestuserdetails.sEmail);
		clickAddUserData();
		sleep(10000);

		if (driver.getCurrentUrl().contains("firstLogin.php") == false)
		{
			Report.logInfo("Guest User operation successful");	
			return true;
		}
		else if ((bElementExists(driver, sLoc_errmsg)) && 
				(invalidDataErrMsg().equals
				(Translations.getTranslation("GuestUser_LoginNameIllegalCharError")))) {
			
			String sError = "Application throws IllegalCharacterException with error as : " + Translations.getTranslation("GuestUser_LoginNameIllegalCharError");
			Report.logError(sError, new IllegalCharacterException(sError));		
			return false;		
							
		}
		else if ((bElementExists(driver, sLoc_errmsg)) && 
				(invalidDataErrMsg().equals(Translations.getTranslation("GuestUser_EmailIllegalCharError")))) {
			
			String sError = "Application throws IllegalCharacterException with error as : " + Translations.getTranslation("GuestUser_EmailIllegalCharError");
			Report.logError(sError, new IllegalCharacterException(sError));
			return false;
			//driver.close();
			//driver.quit();			
			//System.exit(0);
		}
		else if ((bElementExists(driver, sLoc_errmsg)) && 
				(invalidDataErrMsg().equals
				(Translations.getTranslation("GuestUser_UserAlreadyExistError")))) {
			
			String sError = "Application throws RuntimeException with error as : " + Translations.getTranslation("GuestUser_UserAlreadyExistError");
			Report.logError(sError, new RuntimeException(sError));					
			return false;		
							
		}
		else {
			Report.logError("Unknown error occurred");
			return false;
		}	
		//detectErrors(sErrorMsg);
		

	}	
	
}
