package com.advantage.pages.testLink;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.advantage.datastructures.testLink.*;
import com.advantage.framework.Framework;
import com.advantage.reporting.Report;

/**
 * This class is for the Login page
 */

public class Login extends Framework {
	
	/* Page object locators */
	private final String sLoc_UserName = "//*[@id='login']";
	private final String sLoc_Password = "//*[@name='tl_password']";
	private final String sLoc_Login = "//*[@name='login_submit']";
	private final String sLoc_lnkNewUser = "//a[contains(@href, 'firstLogin.php')]";
	private final String sLoc_lnkLostPassword = "//a[contains(@href, 'lostPassword.php')]";
	
	// Amount of time in seconds to wait for the popup on login
	public int nWaitForPopup = 5;

	// xpath to check for any errors
	private static final String sXpath_LoginError = "//div[@class='errorDiv']";
	
	/* Page object logical name */
	private static final String sLog_userName = "Login Name";
	private static final String sLog_password = "Password";
	private static final String sLog_login = "Login";
	private static final String sLog_newUser = "New User link";
	private static final String sLog_lostPassword = "Lost Password";
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public Login(WebDriver driver)
	{
		super(driver);
		initialize("login.php", "Login");
	}

	/**
	 * Enters the User Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterUserName(String sInputValue)
	{
		
		WebElement userName = findElement(driver, sLoc_UserName);
		
		enterField(userName, sLog_userName, sInputValue);
		
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
	
	public void clickLoginAndWait()
	{
		WebElement login = findElement(driver, sLoc_Login);
		clickAndWait(login, sLog_login);
	}

	/**
	 * Clicks the Login button 
	 */
	public void clickLogin()
	{
		WebElement login = findElement(driver, sLoc_Login);
		click(login, sLog_login);
	}
	
	/**
	 * Clicks the New User link (First Login)
	 */
	public void clickNewUser()
	{
		WebElement NewUser = findElement(driver, sLoc_lnkNewUser);
		click(NewUser, sLog_newUser);	
		sleep(10000);
	}
	
	/**
	 * Clicks the Lost Password link
	 */
	public void clickLostPassword()
	{
		WebElement LostPassword = findElement(driver, sLoc_lnkLostPassword);
		click(LostPassword, sLog_lostPassword);		
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
	 * Logins into TestLink.<BR>
	 
	 * @param details - variables need to login to TestLink
	 */
	public void loginAs(LoginDetails details, String sErrorMsg)
	{
		// Enter UserName & Password
		enterUserName(details.sUserName);
		enterPassword(details.sPassword);
		clickLoginAndWait();
		sleep(10000);
		//detectErrors(sErrorMsg);

	}
	
	/**
	 * Lost Password link
	 
	 * @param 
	 */
	public void lostPassword()
	{
		
	}

}
