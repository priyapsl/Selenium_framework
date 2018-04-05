package com.advantage.framework;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import com.advantage.datastructures.testLink.Config;
import com.advantage.genericexceptions.GenericUnexpectedException;
import com.advantage.reporting.Logs;
import com.advantage.reporting.Report;

public class Browser {

	protected static WebDriver driver;
	BrowserType browserType;
	String sAppURL;
	String sDriversPath;
	String sIEDriverName = "IEDriverServer.exe";
	String sChromeDriverName = "chromedriver.exe";
	String sIEProperty = "webdriver.ie.driver";
	String sChromeProperty = "webdriver.chrome.driver";
	String sPlatform;
	String sVersion;
	int nConnectionRetries = 3;
	int nElementTimeout = 30;
	int nPageTimeout = 5;
	static String sHubURL;

	/**
	 * This class select browser mentioned in config file and also other parameters 
	 * related to test environment.
	 * @param ConfigInfo
	 */
	public Browser(Config ConfigInfo)
	{
		browserType = BrowserType.Firefox;

		if (ConfigInfo.getsBrowserType().equalsIgnoreCase("IE"))
			browserType = BrowserType.IE;

		if (ConfigInfo.getsBrowserType().equalsIgnoreCase("Chrome"))
			browserType = BrowserType.Chrome;

		sAppURL = ConfigInfo.getsAppURL();
		sDriversPath = System.getProperty("user.dir") + "\\lib\\";
		sPlatform = ConfigInfo.getsGridPlatform();
		sVersion = ConfigInfo.getsGridVersion();
		sHubURL = ConfigInfo.getsGridHubURL();
	}

	/**
	 * set driverspath
	 * @param sDriverFilePath
	 */
	public void setDriversPath(String sDriverFilePath)
	{
		sDriversPath = sDriverFilePath;
	}

	/**
	 * This method launches browser as per browser type mentioned in config file
	 */
	public void launchBrowser()
	{
		// Create the appropriate browser driver
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);


		/*if (sHubURL != null || !sHubURL.equalsIgnoreCase(""))
		{
			initDriverForGrid();
			return;
		}*/

		if (browserType == null || browserType.toString().equalsIgnoreCase(""))
			browserType = BrowserType.Firefox;

		if (browserType == BrowserType.Firefox)
		{
			driver = new FirefoxDriver();
			driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.MINUTES);
		}
		else if (browserType == BrowserType.IE)
		{
			capabilities.setCapability(
					InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("useLegacyInternalServer", true);
			System.setProperty(sIEProperty, sDriversPath + sIEDriverName);
			driver = new InternetExplorerDriver(capabilities);
			driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.MINUTES);
		}
		else if (browserType == BrowserType.Chrome)
		{
			capabilities = DesiredCapabilities.chrome();
			System.setProperty(sChromeProperty, sDriversPath + sChromeDriverName);
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		System.out.println("Thread</span> id = " + Thread.currentThread().getId());
	}
	
	/**
	 * This method sets platform of a test execution
	 * @return platform
	 */

	private Platform getPlatform()
	{
		if (sPlatform.equalsIgnoreCase("WINDOWS"))
			return Platform.WINDOWS;

		if (sPlatform.equalsIgnoreCase("XP"))
			return Platform.XP;

		if (sPlatform.equalsIgnoreCase("VISTA"))
			return Platform.VISTA;

		if (sPlatform.equalsIgnoreCase("MAC"))
			return Platform.MAC;

		if (sPlatform.equalsIgnoreCase("UNIX"))
			return Platform.UNIX;

		if (sPlatform.equalsIgnoreCase("LINUX"))
			return Platform.LINUX;

		if (sPlatform.equalsIgnoreCase("ANDROID"))
			return Platform.ANDROID;

		// Default to ANY
		return Platform.ANY;
	}

	/**
	 * This method initiate driver for Grid as per browser type
	 */
	private void initDriverForGrid()
	{
		DesiredCapabilities cap = null;
		
		if (browserType == BrowserType.Firefox)
		{
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);
		}
		if (browserType == BrowserType.Chrome)
		{
			cap = DesiredCapabilities.chrome();
			cap.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
			cap.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);
		}
		if (browserType == BrowserType.IE)
		{
			cap = DesiredCapabilities.internetExplorer();
			cap.setCapability("useLegacyInternalServer", true);
			cap.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
			cap.setPlatform(getPlatform());
			cap.setVersion(sVersion);
		}

		try
		{
			Report.logInfo("Going to use Selenium Grid ('" + sHubURL + "')....");

			for (int i = 0; i < nConnectionRetries; i++)
			{
				try
				{
					// Attempt to get connection
					driver = new Augmenter().augment(new RemoteWebDriver(new URL(sHubURL), cap));

					// If we get a connection, then break the loop immediately
					break;
				}
				catch (org.openqa.selenium.WebDriverException ex)
				{
					Report.logWarning("Attempt " + (i + 1) + " to get connection failed");
					if (i + 1 >= nConnectionRetries)
					{
						// Max attempts reached log error and re-throw exception
						Report.logError("Max connection re-tries (" + (i + 1) + ") reached.",
								(RuntimeException) ex);
						throw ex;
					}
					else
					{
						// Convert to milliseconds and wait before retrying to get connection
						Framework.sleep(nElementTimeout * 1000);
					}
				}
			}

			Report.logInfo("Remote Host:  " + sHubURL);
			driver.manage().timeouts().pageLoadTimeout(nPageTimeout, TimeUnit.MINUTES);
		}
		catch (Exception ex)
		{
			Report.logInfo("----------- Debugging Variables -------------");
			Report.logInfo("Browser Name = '" + browserType.toString() + "', Platform = '" + getPlatform()
					+ "', Version = '" + sVersion + "'");
			Report.logInfo("--------------------------------------------");

			String sNodeIssue = "java.lang.String cannot be cast to java.util.Map".toLowerCase();
			String sStackTraceMessage = ex.getMessage().toLowerCase();
			if (sStackTraceMessage.contains(sNodeIssue))
			{
				Report.logWarning("Check the URL for Selenium Grid is correct.  (By default it ends in /wd/hub)");
			}

			ex.printStackTrace();
			String sError = "Error message was following:  " + ex.getMessage() + Framework.getNewLine();
			Report.logError(sError, new GenericUnexpectedException(sError));
		}
	}

	/**
	 * THis method closes and stops webdriver
	 */
	public void quitBrowser()
	{
		Exception eException = null;
		try
		{
			driver.close();
			driver.quit();

			Thread.sleep(1000);
		}
		catch (Exception exception)
		{
			eException = exception;
		}
		finally
		{
			if (eException != null)
				Logs.logException("Problem with closing browser...", eException);
		}
	}

	/**
	 * This method launches application URL mentioned in config file
	 */
	public void openApplication()
	{
		try
		{
			driver.get(sAppURL);
		}
		catch (Exception exception)
		{
			Logs.logException("Problem with opening application URL:" + sAppURL, exception);
		}
	}

	/**
	 * This method refresh browser 
	 * 
	 */
	public void refresh()
	{
		try
		{
			Navigation nav = driver.navigate();
			nav.refresh();
		}
		catch (Exception exception)
		{
			Logs.logException("Unable to refresh browser", exception);
		}
	}

	/**
	 * This method is for navigate back on browser
	 */
	public void back()
	{
		try
		{
			Navigation nav = driver.navigate();
			nav.back();
		}
		catch (Exception exception)
		{
			Logs.logException("Unable to back on browser", exception);
			System.out.println("ERROR!");
		}
	}

	@Test
	public static void unitTest()
	{
		// Browser browser = new Browser("", "http://google.com");
		// browser.launchBrowser();
		// browser.openApplication();
		// browser.quitBrowser();
		//
		// Browser browser1 = new Browser("IE", "http://google.com");
		// browser1.launchBrowser();
		// browser1.openApplication();
		// browser1.quitBrowser();
	}
}
