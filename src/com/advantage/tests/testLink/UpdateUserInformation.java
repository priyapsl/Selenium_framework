package com.advantage.tests.testLink;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.advantage.datastructures.testLink.*;
import com.advantage.fileutils.Excel_poi;
import com.advantage.fileutils.VTD_XML;
import com.advantage.framework.Framework;
import com.advantage.framework.TestTemplate;

import com.advantage.localeutils.Translations;
import com.advantage.pages.testLink.HomePage;
import com.advantage.pages.testLink.Login;
import com.advantage.pages.testLink.MySettings;
import com.advantage.reporting.Logs;
import com.advantage.reporting.Report;


public class UpdateUserInformation extends TestTemplate {
	
	// root node of the xml file
	private static String sRootNode = "/data/";
	
	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Login = "Login";
	private static String sNode_FirstLogin = "EditInfo";	
	
	private static final String EditUserInfo = "runUpdateUserInformation";
	
	/**
	 * Initializes all variables from config file
	 * 
	 * @param sUseConfigFile - Configuration file to load variables from
	 */
	@Parameters("sUseConfigFile")
	@BeforeSuite(groups = "setup")
	// @Override
	public void initialization(@Optional(CONFIG_FILE) String sUseConfigFile)
	{
		Translations translations = new Translations(ConfigInfo.SetLanguage().toLowerCase(), 
				System.getProperty("user.dir") + "\\Locale\\" + ConfigInfo.SetLanguage() + ".xml",
				"/root/data/translate");
		translations.readAddValues();
	}
	
	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "Login")
	public static Object[][] dataForLogin(Method m)
	{
		String sTestDataFilePath = ConfigInfo.getTestDataPath() + "\\" + ConfigInfo.SetLanguage() + "\\data_TestLink.xml";

		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;
		if (m.getName().equals(EditUserInfo))
		{
			sXpath_Root += sNode_FirstLogin;
		}		
		else
		{
			sXpath_Root += sNode_Login;
		}

		// Object array for testNG
		Object[][] testngDataObject = null;

		VTD_XML xml;
		try
		{
			// Parse into usable format
			xml = new VTD_XML(sTestDataFilePath);
			
			int nTCNodes = xml.getNodesCount(sXpath_Root);
			// Initialize array which will store the test data
			testngDataObject = new Object[nTCNodes][2];
			
	
			// Initialize array which will store the test data
			testngDataObject = new Object[nTCNodes][2];
					

			
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < nTCNodes; i++)
			{
				/*
				 * Construct the xpath to the specific test case
				 */
				String sXpath_Start = sXpath_Root + "[" + (i + 1) + "]/";
				
				// Login variables
				String sUserName = xml.getNodeValue(sXpath_Start + "/Login/UserName", "");
				String sPassword = xml.getNodeValue(sXpath_Start + "/Login/Password", "");
				
				// Instantiate the objects
				LoginDetails details = new LoginDetails(sUserName, sPassword);
				
				
				// Put in the object array for testNG
				testngDataObject[i][0] = details;
				
				String sXpath_Options=  sXpath_Start+"/TestOptions";

				// Get number of test cases
				int nNodes = xml.getNodesCount(sXpath_Options);	
				List<MySettingsDetails> lstUserInfo =new ArrayList<MySettingsDetails>();

				/*
				 *
				 * Put data in variables before instantiating the object to hold them
				 */
				for (int j = 0;  j < nNodes; j++)
				{
					
				String sXpath_TO = sXpath_Options + "[" + (j + 1) + "]/";
				
				// Edit User information variables	
				String sEditInfo = xml.getNodeValue(sXpath_TO + "EditOption", "");
				String sFName = xml.getNodeValue(sXpath_TO + "FName", "");
				String sLName = xml.getNodeValue(sXpath_TO + "LName", "");				
				String sEmail = xml.getNodeValue(sXpath_TO + "Email", "");
				String sLocale = xml.getNodeValue(sXpath_TO + "Locale", "");
				String sOPassword = xml.getNodeValue(sXpath_TO + "OldPassword", "");
				String sNPassword = xml.getNodeValue(sXpath_TO + "NewPassword", "");
				String sConfirmPassword = xml.getNodeValue(sXpath_TO + "ConfirmPassword", "");
				
				lstUserInfo.add(new MySettingsDetails(sEditInfo, sFName, sLName, sEmail, sLocale, 
						sOPassword, sNPassword,sConfirmPassword));
				}
				// Put in the object array for testNG				
				testngDataObject[i][1] = lstUserInfo;	
				
			}
		}
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}
		System.out.println(testngDataObject);
		return testngDataObject;
	}
	
	/**
	 * Gets data driven values for the test. (This is used for both positive & negative tests.)
	 * 
	 * @param m
	 * @return data for testNG
	 */
	@DataProvider(name = "DataFromExcel")
	public static Object[][] dataForLogin1(Method m)
	{
		String sTestDataFilePath = ConfigInfo.getTestDataPath() + "\\" + ConfigInfo.SetLanguage() + "\\data_TestLink.xls";
		
		// Object array for testNG
		Object[][] testngDataObject = null;
		
		String sUserName = null, sPassword = null, sEditInfo = null, sFName= null, sLName= null,sNPassword= null,sEmail= null, sConfirmPassword= null,
				sOPassword=null, sLocale=null;
		Excel_poi excel;
		try
		{
			excel = new Excel_poi(sTestDataFilePath, "EditInfo");
			
			int iRowCount = excel.getExcelRowCount();
			int iColCount = excel.getExcelColumnCount();
			
			// Initialize array which will store the test data			
			testngDataObject = new Object[1][2];
			
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < 1; i++)
			{
			
				// Construct the xpath to the specific test case
				List<MySettingsDetails> lstUserInfo =new ArrayList<MySettingsDetails>();
				String[][] dataexcel= excel.getExcelSheetData();		
				LoginDetails details = null;
								
					
					for(int j=1;j<iRowCount;j++){
						for( int k=0;k<iColCount;k++) {									
							if(dataexcel[0][k].equals("Username")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sUserName = dataexcel[j][k];
								}
																	
							}
							else if (dataexcel[0][k].equals("Password")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sPassword = dataexcel[j][k];
								}								 
							}
							else if (dataexcel[0][k].equals("EditOption")){
								sEditInfo = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("FName")){
								 sFName = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("LName")){
								 sLName = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Email")){
								 sEmail = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Locale")){
								 sLocale = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Old Password")){
								 sOPassword = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("New Password")){
								 sNPassword = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Confirm Password")){
								sConfirmPassword = dataexcel[j][k];
							}													
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}		
						// Instantiate the objects
						details = new LoginDetails(sUserName, sPassword);					
						
						lstUserInfo.add(new MySettingsDetails(sEditInfo, sFName, sLName, sEmail, sLocale, 
								sOPassword, sNPassword,sConfirmPassword));					
					}	
					// Put in the object array for testNG
					testngDataObject[i][0] = details;
					
					// Put in the object array for testNG				
					testngDataObject[i][1] = lstUserInfo;
					System.out.println(lstUserInfo);
				}
			
		}
		
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}			
		System.out.println(testngDataObject);
		return testngDataObject;
	}
	
	/**
	 * Test the update user information functionality
	 * 
	 * @param details - Login detail variables
	 * @param lstUserInfo - list of user info variables
	 */
	@Test(dataProvider = "DataFromExcel")	
	public static void runUpdateUserInformation(LoginDetails details, List<MySettingsDetails> lstUserInfo)
	{
		/* Test case initiation */
		
		Report.logTitle(Framework.getNewLine() + "Method " + EditUserInfo + " executing ..."
				+ Framework.getNewLine());		
		setScreenshotPreferences();
		
		/* Read Translation File */
		readTranslation(ConfigInfo.SetLanguage().toLowerCase(), 
				System.getProperty("user.dir") + "\\Locale\\" + ConfigInfo.SetLanguage() + ".xml");
			
		/* Login to application */
		Login loginPage = new Login(driver);
		HomePage homepage = new HomePage(driver);
		MySettings mysettings = new MySettings(driver);
		
		try{
			loginPage.loginAs(details, "");	
			if(homepage.verifyLoggedInUser(driver, details.sUserName)==true){
				/*Click My Settings link */
				homepage.clickMySettings(driver);
									
				for(MySettingsDetails userInfo:lstUserInfo){			
					/*Edit user information*/
					boolean flag =mysettings.EditUserSettings(userInfo.sEditOption, userInfo, "");
					System.out.println(flag);
					if(flag==true){
						/*Verify edited information*/
						mysettings.VerifyEditedInfo(userInfo.sEditOption, userInfo, "", details);
					}					
				}		
				/* Logout of application */
				homepage.clickLogout(driver);
			}
			else {
				Report.logError("User does not exist");
			}
			
		}
		catch(RuntimeException ex){
			Report.logError("Update user information Test failed due to exception.", ex);
		}		
		
	}

}
