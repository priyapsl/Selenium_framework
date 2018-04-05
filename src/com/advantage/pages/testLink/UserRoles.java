package com.advantage.pages.testLink;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.advantage.datastructures.testLink.*;
import com.advantage.framework.Framework;
import com.advantage.genericexceptions.IllegalCharacterException;
import com.advantage.localeutils.Translations;

import com.advantage.reporting.Report;

public class UserRoles extends Framework {
	
	/* Page object locators */
	
	private final static String sLoc_ViewUserstab = "//a[contains(@href, 'lib/usermanagement/usersView.php')]";
	private final static String sLoc_ViewRolestab = "//a[contains(@href, 'lib/usermanagement/rolesView.php')]";
	private final static String sLoc_AssignTestProjectRolestab = "//a[contains(@href, 'lib/usermanagement/usersAssign.php?featureType=testproject')]";
	private final static String sLoc_AssignTestPlanRolestab = "//a[contains(@href, 'lib/usermanagement/usersAssign.php?featureType=testplan')]";
	private final static String sLoc_Create = "//*[(@name='doCreate')]";
	private final String sLoc_Login = "//input[@name='login']";
	private final String sLoc_FirstName = "//input[@name='firstName']";
	private final String sLoc_LastName = "//input[@name='lastName']";
	private final String sLoc_Password = "//*[@id='password']";
	private final String sLoc_Email = "//*[@id='email']";
	private final String sLoc_Role = "//select[@name='rights_id']";
	private final String sLoc_Locale = "//select[@name='locale']";
	private final String sLoc_AuthenticationMethod = "//select[@name='authentication']";
	private final String sLoc_ActiveFlag = "//input[@name='user_is_active']";
	private final String sLoc_Save = "//*[(@name='do_update')]";	
	private final static String sLoc_MainFrame = "//frame[@name='mainframe']";
	//private final static String sLoc_userListClass = "//*[@id='ext-gen21']";
	private final static String sLoc_userListClass = "//*[@class='x-grid3-row-table']";
	private final static String sLoc_LoginNameErr = "//*[@class='user_feedback']/p";
	private final static String sLoc_EmailErr = "//*[@id='ext-gen28']";
	private final String sLoc_DialogOK = "//*[@id='ext-gen18']";
	
	// xpath to check for any errors
		private static final String sXpath_LoginError = "//*[@id='error_icon_login']";
		
	/* Page object logical name */	
	private static final String sLog_ViewUserstab = "View Users";
	public static final String sLog_ViewRolestab = "View Roles";
	public static final String sLog_AssignTestProjectRolestab = "Assign Test Project roles";
	public static final String sLog_AssignTestPlanRolestab = "Assign Test Plan roles";
	private final static String sLog_Create = "Create";
	private final String sLog_Login = "Login";
	private final String sLog_FirstName = "First Name";
	private final String sLog_LastName = "Last Name";
	private final String sLog_Password = "Password";
	private final String sLog_Email = "Email";
	private final String sLog_Role = "Role";
	private final String sLog_Locale = "Locale";
	private final String sLog_AuthenticationMethod = "Authentication Method";
	private final String sLog_ActiveFlag = "Active";
	private final String sLog_Save = "Save";
	//private final static String sLog_LoginNameErr = "Invalid Login Name";
	//private final static String sLog_EmailErr = "Invalid Email Id";
	private final String sLog_DialogOK = "OK button";
	
	
		
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public UserRoles(WebDriver driver)
	{
		super(driver);		
	}
	
	/** Verify invalid login name error
	 *  @param driver
	 */
	
	public String invalidLoginNameErr()
	{
		
		WebElement loginnameerr = findElement(driver, sLoc_LoginNameErr);
		
		return loginnameerr.getText();
				
	}	
	
	/** Verify invalid login name error
	 *  @param driver
	 */
	
	public String invalidEmailErr()
	{
		
		WebElement emailerr = findElement(driver, sLoc_EmailErr);
		
		return emailerr.getText();
				
	}
	/** Clicks on View Users tab
	 *  @param driver
	 */
	
	public static void clickViewUserstab(WebDriver driver)
	{
		WebElement ViewUsers = findElement(driver, sLoc_ViewUserstab);
		click(ViewUsers, sLog_ViewUserstab);		
	}
	
	/** Clicks on View Roles tab
	 *  @param driver
	 */
	public static void clickViewRolestab(WebDriver driver)
	{
		WebElement ViewRoles = findElement(driver, sLoc_ViewRolestab);
		click(ViewRoles, sLog_ViewRolestab);		
	}
	
	/** Clicks on Assign Test Project Roles tab
	 *  @param driver
	 */
	public static void clickAssignTestProjectRolestab(WebDriver driver)
	{
		WebElement AssignTestProjRoles = findElement(driver, sLoc_AssignTestProjectRolestab);
		click(AssignTestProjRoles, sLog_AssignTestProjectRolestab);		
	}
	
	/** Clicks on Assign Test Plan Roles tab
	 *  @param driver
	 */
	public static void clickAssignTestPlanRolestab(WebDriver driver)
	{
		WebElement AssignTestPlanRoles = findElement(driver, sLoc_AssignTestPlanRolestab);
		click(AssignTestPlanRoles, sLog_AssignTestPlanRolestab);		
	}
	
	/** Clicks on Create button
	 *  @param driver
	 * 
	 */
	public static void clickCreateButton(WebDriver driver)
	{
		switchFrame(driver, sLoc_MainFrame);
		click(driver, sLoc_Create, sLog_Create);
		sleep(10000);
		
	}
	
	/**
	 * Enters the Login field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterLogin(String sInputValue)
	{
		WebElement login = findElement(driver, sLoc_Login);
		enterField(login, sLog_Login, sInputValue);
	}
	
	/**
	 * Enters the First Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterFirstName(String sInputValue)
	{
		WebElement firstname = findElement(driver, sLoc_FirstName);
		enterField(firstname, sLog_FirstName, sInputValue);
	}

	
	/**
	 * Enters the last Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterlastName(String sInputValue)
	{
		WebElement lastname = findElement(driver, sLoc_LastName);
		enterField(lastname, sLog_LastName, sInputValue);
	}
	
	/**
	 * Enters the password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterpassword(String sInputValue)
	{
		WebElement password = findElement(driver, sLoc_Password);
		enterField(password, sLog_Password, sInputValue);
	}
	
	/**
	 * Enters the Email field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterEmail(String sInputValue)
	{
		WebElement email = findElement(driver, sLoc_Email);
		enterField(email, sLog_Email, sInputValue);
	}
	
	/**
	 * Select Role 
	 * 
	 * @param sInputValue - Value to select
	 */
	public void selectRole(String sInputValue)
	{
		WebElement role = findElement(driver, sLoc_Role);		
		dropDownSelect(role, sLog_Role, sInputValue);
	}
	
	/**
	 * Select Locale 
	 * 
	 * @param sInputValue - Value to select
	 */
	public void selectLocale(String sInputValue)
	{
		WebElement locale = findElement(driver, sLoc_Locale);		
		dropDownSelect(locale, sLog_Locale, sInputValue);
	}
	
	/**
	 * Select AuthenticationMethod 
	 * 
	 * @param sInputValue - Value to select
	 */
	public void selectAuthenticationMethod(String sInputValue)
	{
		WebElement authMethod = findElement(driver, sLoc_AuthenticationMethod);		
		dropDownSelect(authMethod, sLog_AuthenticationMethod, sInputValue);
	}
	
	/**
	 * Check Active user flag 
	 * 
	 * @param bCheck - To select check
	 * @param bVerifyInitialState - To verify initial state of checkbox is selected
	 */
//	public void checkActiveflag(String sCheckboxStatus)	
//	{
//		boolean bCheck;
//		WebElement active = findElement(driver, sLoc_ActiveFlag);
//		
//		if(sCheckboxStatus.equals("on")){
//			bCheck= true;			
//		}
//		else {
//			bCheck = false;
//		}
//		checkbox(active, sLog_ActiveFlag, bCheck, true);
//		
//	}
	
	public void checkActiveflag(boolean bCheck)	
	{
		
		WebElement active = findElement(driver, sLoc_ActiveFlag);		
		
		checkbox(active, sLog_ActiveFlag, bCheck, true);
		
	}
	
	/**
	 * Clicks the Save button
	 */
	public void clickSave()
	{
		WebElement save = findElement(driver, sLoc_Save);
		click(save, sLog_Save);
	}
	
	/**
	 * Clicks the cancel button
	 */
	public void clickCancel()
	{
		WebElement save = findElement(driver, sLoc_Save);
		click(save, sLog_Save);
	}
	
	/**
	 * Clicks the cancel button
	 */
	public void clickDialogOK()
	{
		WebElement okbtn = findElement(driver, sLoc_DialogOK);
		click(okbtn, sLog_DialogOK);
	}
	
	
	/**
	 * Checks for any login errors on the page.
	 */
	public void detectErrors(String sErrorMsgExpected)
	{
		String sErrorMsgActual = "";

		// Are there any errors?
		if (bElementExists(sXpath_LoginError))
			sErrorMsgActual = getText(findElement(driver, sXpath_LoginError));
		else
			sErrorMsgActual = "";

		if (sErrorMsgActual.equalsIgnoreCase("") && sErrorMsgExpected.equalsIgnoreCase(""))
			Report.logPass("New User created successfully");

		if (sErrorMsgActual.contains(sErrorMsgExpected))
			Report.logPass("The expected error message is displayed");
		else
			Report.logError("Expected Error Message1:" + sErrorMsgExpected + "<BR>" + "Actual Error Message:"
					+ sErrorMsgActual);
	}
	
	/**
	 * Create New User into TestLink.<BR>
	 * @param sLogin  
	 * @param sFirstName
	 * @param sLastName 
	 * @param sPassword
	 * @param sEmail
	 * @param sRole
	 * @param sLocale
	 * @param sAuthMethod
	 * @param bCheck
	 * @param bVerifyInitialState		 
	 */
	public void createNewUser(CreateNewUserDetails newuserdetails, String sErrorMsg)
	{		
		
		enterLogin(newuserdetails.sLogin);
		enterFirstName(newuserdetails.sFirstName);
		enterlastName(newuserdetails.sLastName);
		enterpassword(newuserdetails.sPassword);
		enterEmail(newuserdetails.sEmail);
		selectRole(newuserdetails.sRole);
		selectLocale(newuserdetails.sLocale);
		selectAuthenticationMethod(newuserdetails.sAuthMethod);		
		clickSave();
		sleep(10000);	
		
		if ((bElementExists(driver, sLoc_userListClass))==true) 
		{
			Report.logInfo("Save operation successful.");				
		}
		else if ((bElementExists(driver, sLoc_LoginNameErr)) && 
				(invalidLoginNameErr().equals(Translations.getTranslation("NewUser_LoginNameIllegalCharError")))) {
			
			String sError = "Application throws IllegalCharacterException with error as : " + Translations.getTranslation("NewUser_LoginNameIllegalCharError");
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError, new IllegalCharacterException(sError));
							
		}
		else if ((bElementExists(driver, sLoc_EmailErr)) && 
				(invalidEmailErr().equals(Translations.getTranslation("NewUser_EmailIllegalCharError")))) {
			
			String sError = "Application throws IllegalCharacterException with error as : " + Translations.getTranslation("NewUser_EmailIllegalCharError");
			//Report.logException(sError, new IllegalCharacterException(sError));
			Report.logError(sError, new IllegalCharacterException(sError));				
			clickDialogOK();
		}
		else if((bElementExists(driver, sLoc_LoginNameErr)) && 
				(invalidLoginNameErr().equals(Translations.getTranslation("NewUser_UserAlreadyExistError")))) {
			
			String sError = "Application throws RuntimeException with error as : " + Translations.getTranslation("NewUser_UserAlreadyExistError");
			//Report.logException(sError, new RuntimeException(sError));
			Report.logError(sError, new RuntimeException(sError));							
		} 
		else {
			Report.logError("Unknown error occurred");
		}			

		//detectErrors(sErrorMsg);
		setDefaultFrame(driver);

	}
	
	/**
	 * Click on tab present on user roles page
	 * @param sTabName  
	 		 
	 */
	public void switchTabOnUserRolesPage(String sTabName)
	{
		if(sTabName.equals(sLog_ViewUserstab)){
			clickViewUserstab(driver);			
		}
		else if (sTabName.equals(sLog_ViewRolestab)) {
			clickViewRolestab(driver);			
		}
		else if (sTabName.equals(sLog_AssignTestProjectRolestab)) {
			clickAssignTestProjectRolestab(driver);			
		}
		else {
			clickAssignTestPlanRolestab(driver);
			
		}
		
	}
	
//	/**
//	 * Click on tab present on user roles page
//	 * @param sTabName  
//	 		 
//	 */
//	public void verfiyNewUser(CreateNewUserDetails newuserdetails, String sErrorMsg)
//	{
//		switchFrame(driver, sLoc_MainFrame);
//		List<String> userDtl = new ArrayList<String>();
//		List<WebElement> userList = findElements(driver, sLoc_userListClass);	
//		
//	    for(WebElement ul : userList){		
//	    	
//	    	userDtl.add(ul.getText());	    	
//	    	  
//	    }
//	    System.out.println(userDtl.size());
//	    for(int i=0; i<userDtl.size(); i++) {
//	    	if(userDtl.get(i).toString().contains(newuserdetails.sLogin)) {
//	    		Report.logPass(newuserdetails.sLogin + " user added successfully");
//	    		break;
//	    	}
//	    	else {
//	    		if(i==userDtl.size()-1){ 
//	    			Report.logError("Create new user failed for " + newuserdetails.sLogin);
//	    		}
//	    	}   
//	    	
//	    }
//	    
//	    setDefaultFrame(driver);	
//	}
	
	/**
	 * Click on tab present on user roles page
	 * @param sLoginName  
	 		 
	 */
	public void verfiyNewUser(String sLoginName, String sErrorMsg)
	{
		switchFrame(driver, sLoc_MainFrame);
		List<String> userDtl = new ArrayList<String>();
		List<WebElement> userList = findElements(driver, sLoc_userListClass);	
		
	    for(WebElement ul : userList){		
	    	
	    	userDtl.add(ul.getText());	    	
	    	  
	    }
	    System.out.println(userDtl.size());
	    for(int i=0; i<userDtl.size(); i++) {
	    	if(userDtl.get(i).toString().contains(sLoginName)) {
	    		Report.logPass(sLoginName + " user added successfully");
	    		break;
	    	}
	    	else {
	    		if(i==userDtl.size()-1){ 
	    			Report.logError("Create new user failed for " + sLoginName);
	    		}
	    	}   
	    	
	    }
	    
	    setDefaultFrame(driver);	
	}
}

