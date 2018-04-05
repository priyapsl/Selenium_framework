package com.advantage.framework;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.advantage.datastructures.testLink.Config;
/*import com.advantage.datastructures.testLink.LoginDetails;
import com.advantage.datastructures.testLink.SendEmailDetails;*/
import com.advantage.dbutils.Database;
import com.advantage.localeutils.Translations;
import com.advantage.reporting.Logs;
import com.advantage.reporting.Report;
import com.advantage.reporting.Screenshot;
//import com.advantage.reporting.Email;



public class TestTemplate {

	protected static final String CONFIG_FILE = "config.xml";
	protected static final String TESTCASEID = "TC Id";
	protected static Config ConfigInfo;
	protected static Browser browser;
	protected Database db;
	protected static Translations translations;
	protected static WebDriver driver;
	
	protected static boolean bScreenshotsEnabled;
	protected static String sScreenshotFolder;
	protected static String sScreenshotPrefixName;
	protected static boolean bSendEmail;
	protected static String  sSMPT_Server;
	protected static String  sFromEmailAddress;
	protected static String[]  sToEmailAddresses;
	protected static String sSubject;
	protected static String  sMessageText;
	protected static String[]  sResultPath;
	
	public static final String screenshots_enabled = "screenshots.enabled";
	public static final String screenshots_output = "screenshots.output";
	public static final String screenshots_prefix = "screenshots.prefix";
	public static String sSuiteName;
	public static String sTestName;
	public static String testrunid;

	@Parameters("sUseConfigFile")
	@BeforeSuite(groups = "setup")
	public void suiteInitialization(@Optional(CONFIG_FILE) String sUseConfigFile)
	{
		// If you want to use dynamic folders to store the FILE log
//		Logs.FOLDER_FILE = System.getProperty("user.dir") + System.getProperty("file.separator")
//				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "text"
//				+ System.getProperty("file.separator");
//		Logs.setProperty4FolderFile(Logs.FOLDER_FILE);
//
//		// If you want to use dynamic folders to store the FILE log
//		Logs.FOLDER_HTML = System.getProperty("user.dir") + System.getProperty("file.separator")
//				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "html"
//				+ System.getProperty("file.separator");
//		Logs.setProperty4FolderHTML(Logs.FOLDER_HTML);
		
		Logs.initializeLoggers();
		ConfigInfo = new Config(System.getProperty("user.dir") + "\\" + CONFIG_FILE);
		ConfigInfo.initialize();
		
		bScreenshotsEnabled = ConfigInfo.isbIsCaptureScreenshots();
		sScreenshotFolder = Misc.getProperty(screenshots_output, System.getProperty("org.uncommons.reportng.outputDirectory") + "\\Screenshots");
		sScreenshotPrefixName = Misc.getProperty(screenshots_prefix, "Error");
	}

	@AfterSuite
	public void suiteEnd()
	{
//		bSendEmail = ConfigInfo.isbIsSendEmails();
//		if(bSendEmail== true)
//		{
//			Email email = new Email();
//			
//		
//			sSMPT_Server = ConfigInfo.getsSMTPSever();
//			sFromEmailAddress = ConfigInfo.getsFromEmailId();
//			sToEmailAddresses = ConfigInfo.getsToEmailId();
//			sSubject = ConfigInfo.getsSubject();
//			sMessageText = ConfigInfo.getsMessage();
//			//sResultPath = ConfigInfo.getsResultPath();
//			sResultPath = System.getProperty("org.uncommons.reportng.outputDirectory").split(" ");
//			
//			SendEmailDetails emaildetails = new SendEmailDetails(sSMPT_Server, sFromEmailAddress, sToEmailAddresses, 
//					sSubject, sMessageText,new String[] {});
//			email.sendEmail(emaildetails);
//		}
				
		
	}

	@BeforeMethod
	//@Parameters("testrunid")
	public void testIntiation(final ITestContext testContext)
	{
		Report.logTestInitiationAndEnd();
		sSuiteName= testContext.getCurrentXmlTest().getSuite().getName();
		sTestName = testContext.getName();
		System.out.println(sTestName);
		if(ConfigInfo.bIsAPITesting()==true){
			Report.logInfo("API Automation Testing");			
		}
		else {
			browser = new Browser(ConfigInfo);
			browser.launchBrowser();			
			browser.openApplication();
			driver = Browser.driver;	
		}
	}

	@AfterMethod(alwaysRun = true)
	public void testEnd()
	{
		if(ConfigInfo.bIsAPITesting()==true){
			Report.logInfo("API Automation Testing");			
		}
		else {
			browser.quitBrowser();
			Report.logTestInitiationAndEnd();		
		}
	}

	public static void readTranslation(String localeName, String filePath, String sXpath)
	{
		translations = new Translations(localeName, filePath, sXpath);
		translations.readAddValues();
	}

	public static void readTranslation(String localeName, String filePath)
	{
		translations = new Translations(localeName, filePath, "");
		translations.readAddValues();
	}
	
	public static void setScreenshotPreferences()
	{
		// Should screenshots be saved?
		if (bScreenshotsEnabled)
		{
			Screenshot.enableAllowScreenshots();
			Screenshot.setScreenshotSettings(sScreenshotFolder, sScreenshotPrefixName);
			Screenshot.bCheckCreateScreenshotFolder();
		}
		else
			Screenshot.disableAllowScreenshots();
	}
}
