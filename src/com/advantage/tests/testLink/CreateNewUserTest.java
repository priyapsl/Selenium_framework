package com.advantage.tests.testLink;


import java.lang.reflect.Method;
//import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.advantage.datastructures.testLink.CreateNewUserDetails;
import com.advantage.datastructures.testLink.LoginDetails;
//import com.advantage.dbutils.Database;
//import com.advantage.fileutils.Excel;
import com.advantage.fileutils.Excel_poi;
import com.advantage.fileutils.VTD_XML;
import com.advantage.framework.Framework;
import com.advantage.framework.TestTemplate;
import com.advantage.localeutils.Translations;
import com.advantage.pages.testLink.HomePage;
import com.advantage.pages.testLink.Login;
import com.advantage.pages.testLink.UserRoles;
import com.advantage.reporting.Logs;
import com.advantage.reporting.Report;


public class CreateNewUserTest extends TestTemplate {
	
	// root node of the xml file
	private static String sRootNode = "/data/";
	
	// xpath nodes to find data in the xml file for specific tests
	private static String sNode_Login = "Login";
	private static String sNode_NewUser = "NewUser";	
	
	private static final String CreateNewUser = "runCreateNewUser";
	
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
		System.out.println(sTestDataFilePath);

		/*
		 * Determine which data to get from the xml file
		 */
		String sXpath_Root = sRootNode;
		if (m.getName().equals(CreateNewUser))
		{
			sXpath_Root += sNode_NewUser;
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
			System.out.println(nTCNodes);
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < nTCNodes; i++)
			{
				/*
				 * Construct the xpath to the specific test case
				 */
				String sXpath_Start = sXpath_Root + "[" + (i + 1) + "]/";
				System.out.println(sXpath_Start);
				// Login variables
				String sUserName = xml.getNodeValue(sXpath_Start + "/Login/UserName", "");
				String sPassword = xml.getNodeValue(sXpath_Start + "/Login/Password", "");
				
				// Instantiate the objects
				LoginDetails details = new LoginDetails(sUserName, sPassword);
				
				
				// Put in the object array for testNG
				testngDataObject[i][0] = details;
				
				String sXpath_Options=  sXpath_Start+"/TestOptions";
				System.out.println(sXpath_Options);
				// Get number of test cases
				int nNodes = xml.getNodesCount(sXpath_Options);	
				System.out.println(nNodes);
				List<CreateNewUserDetails> lstNewUser=new ArrayList<CreateNewUserDetails>();

				/*
				 * Put data in variables before instantiating the object to hold them
				 */
				for (int j = 0;  j < nNodes; j++)
				{
					
				String sXpath_TO = sXpath_Options + "[" + (j + 1) + "]/";
				
				// Required Create New User variables
				String sLogin = xml.getNodeValue(sXpath_TO + "Login", "");
				String sFName = xml.getNodeValue(sXpath_TO + "FName", "");
				String sLName = xml.getNodeValue(sXpath_TO + "LName", "");
				String sNPassword = xml.getNodeValue(sXpath_TO + "Password", "");
				String sEmail = xml.getNodeValue(sXpath_TO + "Email", "");
				String sRole = xml.getNodeValue(sXpath_TO + "Role", "");
				String sLocale = xml.getNodeValue(sXpath_TO + "Locale", "");
				String sAuthMethod = xml.getNodeValue(sXpath_TO + "AuthMethod", "");
				boolean bActiveFlag = xml
					.getNodeValue(sXpath_TO + "Active", true);
				
				lstNewUser.add(new CreateNewUserDetails(sLogin,
						sFName, sLName, sNPassword, sEmail, sRole, sLocale, 
						sAuthMethod, bActiveFlag));
				}
				// Put in the object array for testNG				
				testngDataObject[i][1] = lstNewUser;	
				System.out.println(lstNewUser);
			}
		}
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}		
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
		String sTestDataFilePath = ConfigInfo.getTestDataPath() + "\\" + ConfigInfo.SetLanguage() + "\\data_TestLink.xlsx";
		
		// Object array for testNG
		Object[][] testngDataObject = null;
		
		String sUserName = null, sPassword = null, sLogin = null, sFName= null, sLName= null,sNPassword= null,sEmail= null, sRole= null, sLocale= null, sAuthMethod= null;
		boolean bActiveFlag = false;
		
		Excel_poi excel;
		try
		{
			excel = new Excel_poi(sTestDataFilePath, "NewUser");
			
			int iRowCount = excel.getExcelRowCount();
			int iColCount = excel.getExcelColumnCount();
			
			// Initialize array which will store the test data			
			testngDataObject = new Object[1][2];
			
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < 1; i++)
			{
			
				// Construct the xpath to the specific test case
					List<CreateNewUserDetails> lstNewUser=new ArrayList<CreateNewUserDetails>();
					String[][] dataexcel= excel.getExcelSheetData();		
					LoginDetails details = null;
					/*for(int j=0;j<iRowCount;j++){
						for( int k=0;k<iColCount;k++) {
							System.out.println(j);
							System.out.println(k); 
							System.out.println(dataexcel[j][k]);
						}
					}*/					
					
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
							else if (dataexcel[0][k].equals("Login")){
								 sLogin = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("FName")){
								 sFName = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("LName")){
								 sLName = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("NPassword")){
								 sNPassword = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Email")){
								 sEmail = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Role")){
								 sRole = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Locale")){
								 sLocale = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("AuthMethod")){
								 sAuthMethod = dataexcel[j][k];
							}
							else if (dataexcel[0][k].equals("Active")){
								 bActiveFlag = dataexcel[j][k]!= null;
							}
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}		
						// Instantiate the objects
						details = new LoginDetails(sUserName, sPassword);					
						
						lstNewUser.add(new CreateNewUserDetails(sLogin,
								sFName, sLName, sNPassword, sEmail, sRole, sLocale, 
								sAuthMethod, bActiveFlag));							
					}	
					// Put in the object array for testNG
					testngDataObject[i][0] = details;
					
					// Put in the object array for testNG				
					testngDataObject[i][1] = lstNewUser;
					System.out.println(lstNewUser);
				}
			
		}
		
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}			
		
		return testngDataObject;
	}
	
	/**
	 * Test the create new user functionality
	 * 
	 * 
	 * @param details - Login detail variables
	 * @param lstNewUser - List of new user test case
	 */
	@Test(dataProvider = "Login")	
	//@Test(dataProvider = "DataFromExcel")
	//@Parameters("testrunid")
	public static void runCreateNewUser(LoginDetails details, List<CreateNewUserDetails> lstNewUser)
	{
		/* Test case initiation */
		
		Report.logTitle(Framework.getNewLine() + "Method " + CreateNewUser + " executing ..."
				+ Framework.getNewLine());		
		setScreenshotPreferences();
		
		/* Read Translation File */
		readTranslation(ConfigInfo.SetLanguage().toLowerCase(), 
				System.getProperty("user.dir") + "\\Locale\\" + ConfigInfo.SetLanguage() + ".xml");
		/*//DB logging
		Database db;
		
		//db = new Database(ConfigInfo.getsDBUserName(),"root", "root", "10.222.37.38", "db_webdriver");
		db = new Database(ConfigInfo.getsDBUserName(),ConfigInfo.getsDBPassword(), ConfigInfo.getsDBServer(), ConfigInfo.getiPort(), ConfigInfo.getsDBName());
		
		Connection con = db.openConnection(ConfigInfo.getsDBProvider());
		String[][] sParameterDetails =  { {"111", "int"}, {ConfigInfo.getsBrowserType(), "String"}, {"001", "String"},{"2","String"} 
		,{"test002","String"} ,{System.getProperty("os.name"),"String"},{"testlink","String"},{System.getProperty("machine.name"),"String"}};
	
		String SQL = "insert into db_webdriver.environment (id,browser,build,toolid,testrunid,osname,appname,machinename)" +
				"values (?,?,?,?,?,?,?,?)";
				//"select * from db_webdriver.environment";

		boolean rs = db.updateQuery(con, SQL,sParameterDetails);
		System.out.println("Inserted records into the table...");		
			
*/				
		/* Login to application */
		Login loginPage = new Login(driver);
		HomePage homepage = new HomePage(driver);
		UserRoles userroles = new UserRoles(driver);
		
		try{
			loginPage.loginAs(details, "");
			// Select Project 								
			homepage.selectProject(driver, "BQALabs");
			//homepage.selectProject(driver, "test");
			//Click on User Roles tab
			homepage.clickOnUserRolesTab(driver);					
								
			for(CreateNewUserDetails newUser:lstNewUser){				
				
				//Click on Create button
				UserRoles.clickCreateButton(driver);	
				
				//Create new user
				userroles.createNewUser(newUser, "");	
				
				//Verify new user added in userlist
				userroles.verfiyNewUser(newUser.sLogin, "");			
			}			
			
			 //Logout of application 
			homepage.clickLogout(driver);
			//Database.closeConnection(con);
			
		}
		catch(RuntimeException ex){
			Report.logError("Create New User Test failed due to exception.", ex);
		}
		
				
		
				
		 
		
	}

}
