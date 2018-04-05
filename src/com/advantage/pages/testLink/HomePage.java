package com.advantage.pages.testLink;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import com.advantage.framework.Framework;
import com.advantage.pages.testLink.Login;
import com.advantage.reporting.Report;

/**
 * This class is for the Home page
 */

public class HomePage extends Framework {
	
	// Common objects
	private final static String sLoc_LogOut = "//a[contains(@href, 'logout.php')]";
	private final static String sLoc_UserRolestab = "//*[contains(@href,'lib/usermanagement/usersView.php')]";
	private final static String sLoc_SelectProject = "//select[@name='testproject']";
	private final static String sLoc_TitleFrame = "//frame[@name='titlebar']";
	private final static String sLoc_MainFrame = "//frame[@name='mainframe']";
	private final static String sLoc_TestPlanlink = "//a[@href='lib/plan/planView.php']";
	private final static String sLoc_Desktoptab = "//*[contains(@href,'index.php')]";
	private final static String sLoc_SelectTestPlan = "//select[@class='chosen-select']";
	private final static String sLoc_TestSpecificationtab = "//*[contains(@href,'lib/general/frmWorkArea.php?feature=editTc')]";
	private final static String sLoc_MenuTitle = "//div[@class='menu_title']";
	private final static String sLoc_MySettings = "//a[contains(@href, 'lib/usermanagement/userInfo.php')]";
	
	/* Page object logical name */	
	private static final String sLog_Logout = "Logout";
	public static final String sLog_UserRolestab = "Users/Roles";
	public static final String sLog_ddSelectProject = "Test Project";
	public static final String sLog_lnkTestPlan = "Test Plan Management";
	public static final String sLog_Desktoptab = "Desktop";
	public static final String sLog_ddSelectTestPlan = "Test Plan";
	public static final String sLog_TestSpecificationtab = "Test Specification";
	public static final String sLog_MenuTitle = "Menu Title";
	private static final String sLog_MySettings = "My Settings";
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public HomePage(WebDriver driver)
	{
		super(driver);		
	}	
	  
	
	/**
	 * Clicks the Log out link in TestLink
	 * 
	 * @param driver
	 * @return Login
	 */
	public Login clickLogout(WebDriver driver)
	{
		switchFrame(driver, sLoc_TitleFrame);
		click(driver, sLoc_LogOut, sLog_Logout);
		sleep(1000);
		return new Login(driver);
		
	}
	
	/**
	 * Clicks the My Settings link in TestLink
	 * 
	 * @param driver
	 * @return 
	 * 
	 */
	public boolean clickMySettings(WebDriver driver)
	{
		switchFrame(driver, sLoc_TitleFrame);
		click(driver, sLoc_MySettings, sLog_MySettings);
		sleep(10000);	
		setDefaultFrame(driver);
		return true;
	}
	
	
	
	/** Clicks on User/Roles tab
	 * @param driver
	 */
	
	public void clickOnUserRolesTab(WebDriver driver)
	{
		switchFrame(driver, sLoc_TitleFrame); 
		WebElement UserRoles = findElement(driver, sLoc_UserRolestab);
		click(UserRoles, sLog_UserRolestab);	
		sleep(10000);
		setDefaultFrame(driver);
	}
	
	/** Select Project
	 * @param driver
	 * @param sProjName
	 */
	public void selectProject(WebDriver driver, String sProjName) 
	{
		switchFrame(driver, sLoc_TitleFrame);
		WebElement SelectProject = findElement(driver, sLoc_SelectProject);		
		dropDownSelect(SelectProject, sLog_ddSelectProject, sProjName);
		sleep(5000);
		setDefaultFrame(driver);
	}	
	
	/** Verify Project dropdown
	 * @param driver	
	 */
	public boolean verifyProjectDD(WebDriver driver) 
	{
	
		switchFrame(driver, sLoc_TitleFrame);
		
		if(bElementExists(driver, sLoc_SelectProject, true)==false) {
			return false;
		}
		sleep(5000);
		setDefaultFrame(driver);
		return true;
	}
	
	/** Verify Project dropdown
	 * @param driver	
	 * @param sProjName
	 */
	public boolean verifySelectedProj(WebDriver driver, String sProjName) 
	{
		switchFrame(driver, sLoc_TitleFrame);
		
		if(bWaitForAttributeValue(driver, sLoc_SelectProject, "value", sProjName)==false) {
			return false;
		}
		sleep(5000);
		setDefaultFrame(driver);
		return true;
	}
	
	
	/** Click on Test Plan Management 
	 * @param driver	
	 */
	public void clickTestPlanManagement(WebDriver driver) 
	{
		setDefaultFrame(driver);
		switchFrame(driver, sLoc_MainFrame);
		WebElement testPlan = findElement(driver, sLoc_TestPlanlink);		
		click(testPlan, sLog_lnkTestPlan);
		sleep(5000);
		setDefaultFrame(driver);
	}	
	
	/** Clicks on Desktop tab
	 * @param driver
	 */
	
	public static void clickOnDesktopTab(WebDriver driver)
	{
		switchFrame(driver, sLoc_TitleFrame); 
		WebElement UserRoles = findElement(driver, sLoc_Desktoptab);
		click(UserRoles, sLog_Desktoptab);	
		sleep(10000);
		setDefaultFrame(driver);
	}
	
	/** Select Test Plan
	 * @param driver
	 * @param sTestPlanName
	 */
	public void selectTestPlan(WebDriver driver, String sTestPlanName) 
	{
		switchFrame(driver, sLoc_MainFrame);
		sleep(10000);
		WebElement SelectTestplan = findElement(driver, sLoc_SelectTestPlan);		
		dropDownSelect(SelectTestplan, sLog_ddSelectTestPlan, sTestPlanName);
		sleep(5000);
		setDefaultFrame(driver);
	}	
	
	
	/** Clicks on Test Specification tab
	 * @param driver
	 */
	
	public static void clickOnTestSpecificationTab(WebDriver driver)
	{
		switchFrame(driver, sLoc_TitleFrame); 
		WebElement TestSpec = findElement(driver, sLoc_TestSpecificationtab);
		click(TestSpec, sLog_TestSpecificationtab);	
		sleep(10000);
		setDefaultFrame(driver);
		sleep(10000);
	}
	
	/** Verify logged in user
	 * @param driver
	 * @param sUserName - User name need to be verify
	 */
	
	public boolean verifyLoggedInUser(WebDriver driver, String sUserName)
	{
		sleep(10000);
		switchFrame(driver, sLoc_TitleFrame); 
		WebElement menutitle = findElement(driver, sLoc_MenuTitle);
		String str = menutitle.getText();
		
		if(str.contains(sUserName)){
			Report.logPass(sUserName + " logged in successfully.");			
		}
		else {
			Report.logError(sUserName + " is not logged.");
			return false;
		}			
		sleep(10000);
		setDefaultFrame(driver);
		sleep(10000);
		return true;
	}

}
