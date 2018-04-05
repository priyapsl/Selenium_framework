package com.advantage.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

import com.advantage.genericexceptions.CheckBoxNoSuchElementException;
import com.advantage.genericexceptions.CheckBoxNotEnabled;
import com.advantage.genericexceptions.CheckBoxWrongStateException;
import com.advantage.genericexceptions.ClickNoSuchElementException;
import com.advantage.genericexceptions.DropDownIndexException;
import com.advantage.genericexceptions.DropDownNoSuchElementException;
import com.advantage.genericexceptions.DropDownPartialMatchException;
import com.advantage.genericexceptions.DropDownSelectionException;
import com.advantage.genericexceptions.ElementNotEnabledException;
import com.advantage.genericexceptions.EnterFieldNoSuchElementException;
import com.advantage.genericexceptions.GenericActionNotCompleteException;
import com.advantage.genericexceptions.GetException;
import com.advantage.genericexceptions.JavaScriptException;
import com.advantage.genericexceptions.FrameNoSuchElementException;
import com.advantage.genericexceptions.WrongPageException;
import com.advantage.reporting.Report;
import com.advantage.reporting.Screenshot;

/**
 * This class is for providing a common way to use Selenium that provides logging for use with page objects.<BR>
 * <BR>
 * <B>This should be the base class used for all page objects.<B>
 */
public class Framework {
	protected static String NewLine = System.getProperty("line.separator");
	protected static String PathSeparator = System.getProperty("file.separator");

	// Selenium variables
	protected WebDriver driver;

	// Timeout in seconds
	protected static int nTimeout = 30;

	// Poll Interval in milliseconds
	protected static int nPollInterval = 1000;

	// Handle when constructor is used
	protected String sMainWindowHandle;
	// Handle when working with a pop-up
	protected String sPopupWindowHandle;

	// Variables to handle AJAX
	// Supported AJAX types
	public static final String JQUERY = "jQuery";
	public static final String DOJO = "Dojo";
	public static final String CUSTOM = "Custom";

	// Default values for the supported AJAX types
	private static final String sDojo_AJAX_Complete = "if (ajaxConcurrentCallsCount <= 0) return true; else return false;";
	private static final String sJQuery_AJAX_Complete = "if (jQuery.active <= 0) return true; else return false;";
	private String sXpath_AJAX_Custom_Complete = "//div[@id='pageLoadWarningLabel' and @class='hidden']";

	// jQuery is the default AJAX type for BLINX
	private String sAJAX_type = JQUERY;

	// General class variables
	protected String sPageName;
	protected String sUrl;
	protected String sCurrentURL;
	protected String sBrowserTitle;

	// Database variables
	protected String sDB_Server;
	protected String sDB;
	protected String sDB_User;
	protected String sDB_Password;

	/**
	 * if super is not used then this default constructor is implicitly called
	 * when this class is inherited
	 */
	public Framework()
	{
		this.sMainWindowHandle = "";
		this.sPopupWindowHandle = null;
	}

	public Framework(WebDriver driver)
	{
		this.driver = driver;
		this.sMainWindowHandle = driver.getWindowHandle();
		this.sPopupWindowHandle = null;
	}

	public Framework(WebDriver driver, int nTimeout)
	{
		this.driver = driver;
		Framework.nTimeout = nTimeout;
		this.sMainWindowHandle = driver.getWindowHandle();
		this.sPopupWindowHandle = null;
	}

	/**
	 * Verifies URL & sets URL & Page Name
	 * 
	 * @param sUrl - Expected URL
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(String sUrl, String sPageName)
	{
		if (bWaitForURL(driver, sUrl))
		{
			PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
			this.sUrl = sUrl;
			this.sPageName = sPageName;
		}
		else
		{
			String sError = "Not at " + sPageName + " page (must contain '" + sUrl + "')  URL:  "
					+ driver.getCurrentUrl() + NewLine;

			Screenshot.saveScreenshotAddSuffix("method_initialize");
			Report.logError(sError, new WrongPageException(sError));
			// throw new WrongPageException(sError);
		}
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * Note: Use this method if the same page has multiple URLs associated with it.
	 * 
	 * @param sUrl - URLs that you will allow initialization
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(String[] sUrl, String sPageName)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			for (int i = 0; i < sUrl.length; i++)
			{
				// If at an accepted URL, then complete initialization and return
				if (driver.getCurrentUrl().contains(sUrl[i]))
				{
					PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
					this.sUrl = sUrl[i];
					this.sPageName = sPageName;
					return;
				}
			}

			sleep(nPollInterval);
		}

		// Make String of all URLs for error reporting
		String sAll_URLs = "";
		for (int i = 0; i < sUrl.length; i++)
		{
			if (i != sUrl.length - 1)
				sAll_URLs += "'" + sUrl[i] + "' , ";
			else
				sAll_URLs += "'" + sUrl[i] + "'";
		}

		String sError = "Not at " + sPageName + " page (must contain any of " + sAll_URLs + ")  URL:  "
				+ driver.getCurrentUrl() + NewLine;
		Report.logError(sError);
		Screenshot.saveScreenshotAddSuffix("method_initialize");
		// throw new WrongPageException(sError);
		Report.logError(sError, new WrongPageException(sError));
	}

	/**
	 * Verifies that you are at an acceptable URL to continue initialization.<BR>
	 * <BR>
	 * Note: Use this method if the same page has multiple URLs associated with it.
	 * 
	 * @param urls - URLs that you will allow initialization
	 * @param sPageName - Page Name for logging
	 */
	protected void initialize(List<String> urls, String sPageName)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			for (String sSpecificURL : urls)
			{
				// If at an accepted URL, then complete initialization and return
				if (driver.getCurrentUrl().contains(sSpecificURL))
				{
					PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
					this.sUrl = sSpecificURL;
					this.sPageName = sPageName;
					return;
				}
			}

			sleep(nPollInterval);
		}

		// Make String of all URLs for error reporting
		String sAll_URLs = "";
		for (int i = 0; i < urls.size(); i++)
		{
			if (i != urls.size() - 1)
				sAll_URLs += "'" + urls.get(i) + "' , ";
			else
				sAll_URLs += "'" + urls.get(i) + "'";
		}

		String sError = "Not at " + sPageName + " page (must contain any of " + sAll_URLs + ")  URL:  "
				+ driver.getCurrentUrl() + NewLine;

		Screenshot.saveScreenshotAddSuffix("method_initialize");
		// throw new WrongPageException(sError);
		Report.logError(sError, new WrongPageException(sError));
	}

	/**
	 * Waits for the specified URL to appear.
	 * 
	 * @param driver
	 * @param sUrl
	 * @return true if URL appeared before timeout else false
	 */
	public static boolean bWaitForURL(WebDriver driver, String sUrl)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			if (driver.getCurrentUrl().contains(sUrl))
				return true;

			sleep(nPollInterval);
		}

		// URL did not become the expected value within the timeout
		return false;
	}

	public String getPageName()
	{
		return sPageName;
	}

	public String getUrl()
	{
		return sUrl;
	}

	/**
	 * Gets the current timeout value
	 * 
	 * @return
	 */
	public static int getTimeout()
	{
		return nTimeout;
	}

	/**
	 * Changes the default timeout
	 * 
	 * @param nTimeout - new timeout value in seconds
	 */
	public static void setTimeout(int nTimeout)
	{
		Framework.nTimeout = nTimeout;
	}

	/**
	 * Gets the current poll interval value
	 * 
	 * @return
	 */
	public static int getPollInterval()
	{
		return nPollInterval;
	}

	/**
	 * Changes the default poll interval
	 * 
	 * @param nPollInterval - new poll interval value in milliseconds
	 */
	public static void setPollInterval(int nPollInterval)
	{
		Framework.nPollInterval = nPollInterval;
	}

	public static String getPathSeparator()
	{
		return Framework.PathSeparator;
	}

	public static void setPathSepartor(String sPathSeparator)
	{
		Framework.PathSeparator = sPathSeparator;
	}

	public static String getNewLine()
	{
		return Framework.NewLine;
	}

	public static void setNewLine(String sNewLine)
	{
		Framework.NewLine = sNewLine;
	}

	public String getDB()
	{
		return sDB;
	}

	/**
	 * Sets sDB
	 * 
	 * @param sDB - Database to user
	 */
	public void setDB(String sDB)
	{
		this.sDB = sDB;
	}

	public String getDBSever()
	{
		return sDB_Server;
	}

	/**
	 * Sets sDB_Server (Database Server)
	 * 
	 * @param sDB_Server - Database Server to use
	 */
	public void setDBServer(String sDB_Server)
	{
		this.sDB_Server = sDB_Server;
	}

	public String getDBUser()
	{
		return sDB_User;
	}

	/**
	 * Sets the DB User to connect as
	 * 
	 * @param sDB_User - User to use when connecting to DB
	 */
	public void setDBUser(String sDB_User)
	{
		this.sDB_User = sDB_User;
	}

	public String getDBPassword()
	{
		return sDB_Password;
	}

	/**
	 * Sets the DB Password to use
	 * 
	 * @param sDB_Password - Password to use when connecting to DB
	 */
	public void setDBPassword(String sDB_Password)
	{
		this.sDB_Password = sDB_Password;
	}

	/**
	 * Gets the current Custom AJAX xpath for testing if AJAX page is complete.
	 * 
	 * @return
	 */
	public String getXpath_AJAX_Custom_Complete()
	{
		return sXpath_AJAX_Custom_Complete;
	}

	/**
	 * Changes the default xpath used to determine that the AJAX page is complete when sAJAX_type == CUSTOM
	 * 
	 * @param sXpath_AJAX_Custom_Complete - new xpath for testing if the AJAX page is complete
	 */
	public void setXpath_AJAX_Complete(String sXpath_AJAX_Custom_Complete)
	{
		this.sXpath_AJAX_Custom_Complete = sXpath_AJAX_Custom_Complete;
	}

	/**
	 * Gets the current AJAX type being used to wait for pages to complete
	 * 
	 * @return
	 */
	public String getAJAX_type()
	{
		return sAJAX_type;
	}

	/**
	 * Sets sAJAX_type if type is supported (DOJO, JQUERY or CUSTOM)
	 * 
	 * @param sType - AJAX type being used
	 */
	public void setAJAX_type(String sType)
	{
		if (sType == DOJO)
			sAJAX_type = DOJO;

		if (sType == JQUERY)
			sAJAX_type = JQUERY;

		if (sType == CUSTOM)
			sAJAX_type = CUSTOM;
	}

	/**
	 * Returns the By Object to locate the element. Allows for selenium 1.0.3 use of locators.<BR>
	 * Supported locators (matches in order):<BR>
	 * xpath: // or xpath=<BR>
	 * name: name=<BR>
	 * css: css=<BR>
	 * link name: link=<BR>
	 * class name: class=<BR>
	 * tag name: tag=<BR>
	 * partial link name: plink=<BR>
	 * id: id= or just id<BR>
	 * 
	 * @param sLocateUsing - How to locate the element
	 */
	public static By locatedBy(String sLocateUsing)
	{
		// The locators that are being supported.
		// Note: I am only supporting the ones I use.
		String XPATH = "xpath=";
		String NAME = "name=";
		String CSS = "css=";
		String LINK = "link=";
		String CLASS = "class=";
		String TAG = "tag=";
		String partialLINK = "plink=";

		// standard xpath
		if (sLocateUsing.startsWith("/"))
			return By.xpath(sLocateUsing);

		// xpath=//test
		if (sLocateUsing.toLowerCase().startsWith(XPATH))
			return By.xpath(sLocateUsing.substring(XPATH.length()));

		// name=something
		if (sLocateUsing.toLowerCase().startsWith(NAME))
			return By.name(sLocateUsing.substring(NAME.length()));

		// css=something
		if (sLocateUsing.toLowerCase().startsWith(CSS))
			return By.cssSelector(sLocateUsing.substring(CSS.length()));

		// link=something
		if (sLocateUsing.toLowerCase().startsWith(LINK))
			return By.linkText(sLocateUsing.substring(LINK.length()));

		// class=something
		if (sLocateUsing.toLowerCase().startsWith(CLASS))
			return By.className(sLocateUsing.substring(CLASS.length()));

		// tag=something
		if (sLocateUsing.toLowerCase().startsWith(TAG))
			return By.tagName(sLocateUsing.substring(TAG.length()));

		// plink=something
		if (sLocateUsing.toLowerCase().startsWith(partialLINK))
			return By.partialLinkText(sLocateUsing.substring(partialLINK.length()));

		// no match assume that it is ID
		return By.id(sLocateUsing);
	}

	/**
	 * Waits for an element to appear.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param sLocator - How to locate the element
	 * @return - true if element appears before timeout else false
	 */
	public boolean bWaitForEnabledElement(String sLocator)
	{
		return bWaitForEnabledElement(driver, sLocator);
	}

	/**
	 * Waits for an element to appear & be enabled.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return - true if element appears before timeout else false
	 */
	public static boolean bWaitForEnabledElement(WebDriver driver, String sLocator)
	{
		return bWaitForElement(driver, sLocator, true);
	}

	/**
	 * Waits for an element to appear.<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @param bEnabled - Wait until element is enabled
	 * @return - true if element appears before timeout else false
	 */
	public static boolean bWaitForElement(WebDriver driver, String sLocator, boolean bEnabled)
	{
		WebElement element;

		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			try
			{
				// Try to find the element
				element = driver.findElement(locatedBy(sLocator));

				// Wait until element is enabled?
				if (bEnabled)
				{
					if (element.isEnabled())
						return true;
				}
				else
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(nPollInterval);
		}

		Report.logWarning("The locator ('" + sLocator + "') did not appear before Timeout occurred");
		return false;
	}

	/**
	 * Waits for the element to appear with specified attribute & value
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttributeToCheck - Attribute on the Element to look for
	 * @param sExpectedAttributeValue - Value of the Attribute
	 * @return - true if element appears with expected attribute value before timeout else false
	 */
	public static boolean bWaitForAttributeValue(WebDriver driver, String sLocator, String sAttributeToCheck,
			String sExpectedAttributeValue)
	{
		WebElement element;

		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			element = findElement(driver, sLocator);
			if (element != null && sExpectedAttributeValue.equals(element.getAttribute(sAttributeToCheck)))
				return true;

			sleep(nPollInterval);
		}

		// Attribute value never became expected value before timeout
		Report.logWarning("Attribute ('" + sAttributeToCheck + "') did not get the value ('"
				+ sExpectedAttributeValue + "') for the locator ('" + sLocator + "') before Timeout occurred");
		return false;
	}

	/**
	 * Based on the sAJAX_type waits for the AJAX to complete loading.<BR>
	 * <BR>
	 * <B>Note:</B> Use this method if no instantiated Framework class available.
	 * 
	 * @param driver
	 * @param sAJAX_type - AJAX type to use (JQUERY, DOJO, Custom)
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public static boolean bPageLoadedAJAX(WebDriver driver, String sAJAX_type)
	{
		// Instantiate Framework
		Framework f = new Framework(driver);

		// Set the AJAX type you need
		f.setAJAX_type(sAJAX_type);

		// Wait for the page to be loaded by using AJAX check
		return f.bPageLoaded();
	}

	/**
	 * Based on the sAJAX_type waits for the AJAX to complete loading.
	 * 
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public boolean bPageLoaded()
	{
		Boolean bResult = false;
		String sJS;

		// Select the right javascript command to test that all AJAX requests
		// are complete
		if (sAJAX_type == JQUERY)
			sJS = sJQuery_AJAX_Complete;
		else if (sAJAX_type == DOJO)
			sJS = sDojo_AJAX_Complete;
		else
			return bPageLoaded(sXpath_AJAX_Custom_Complete);

		/*
		 * Wait for the ajaxConcurrentCallsCount to be <= 0 which means the page has completed loading
		 */
		try
		{
			ElapsedTime e = new ElapsedTime();
			while (!e.bTimeoutOccurred())
			{
				bResult = (Boolean) ((JavascriptExecutor) driver).executeScript(sJS);
				if (bResult.booleanValue())
					return true;

				sleep(nPollInterval);
			}
		}
		catch (Exception ex)
		{
		}

		Report.logWarning("Timeout occurred waiting for AJAX page to complete loading");
		return false;
	}

	/**
	 * Waits for the AJAX to complete loading <BR>
	 * <BR>
	 * NOTE: Use only if bPageLoaded with no parameters fails
	 * 
	 * @param nAnything
	 * @return true if the page completes loading before timeout occurs else false
	 */
	public boolean bPageLoaded(String sXpath)
	{
		// Wait for search to complete
		if (!bWaitForEnabledElement(sXpath))
		{
			return false;
		}

		return true;
	}

	/**
	 * Checks if an element exists on the page
	 * 
	 * @param sLocator - How to locate the element on the page
	 * @return true if the element exists on the page
	 */
	public boolean bElementExists(String sLocator)
	{
		return bElementExists(driver, sLocator);
	}

	/**
	 * Checks if an element exists on the page.<BR>
	 * <BR>
	 * <B>Note: </B> Does not check if element is enabled.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @return true if the element exists on the page
	 */
	public static boolean bElementExists(WebDriver driver, String sLocator)
	{
		return bElementExists(driver, sLocator, false);
	}

	/**
	 * Checks if an element exists on the page and enabled if necessary
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @param bEnabled - true if element must be enabled
	 * @return true if the element exists on the page (and enabled if requested)
	 */
	public static boolean bElementExists(WebDriver driver, String sLocator, boolean bEnabled)
	{
		try
		{
			WebElement element = driver.findElement(locatedBy(sLocator));

			// Should we check if it is enabled?
			if (bEnabled)
			{
				if (element.isEnabled())
					return true;
				else
					return false;
			}
			else
				return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Switches to the 1st pop-up window (and stores handle) or back to the stored pop-up window handle.<BR>
	 * <BR>
	 * Note: Call disposePopupWindowHandle method once done with the pop-up
	 * 
	 * @return
	 */
	public boolean switchToPopupWindow()
	{
		try
		{
			// If already working with a pop-up
			if (sPopupWindowHandle != null)
			{
				driver.switchTo().window(sPopupWindowHandle);
				return true;
			}

			// Get all the window handles
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			for (int i = 0; i < openWindows.length; i++)
			{
				// Return 1st window handle that is not the current window
				// handle
				if (!sMainWindowHandle.equals(openWindows[i]))
				{
					driver.switchTo().window(openWindows[i]);
					// Store the handle for possible use later
					sPopupWindowHandle = openWindows[i];
					return true;
				}
			}
		}
		catch (Exception ex)
		{
		}

		// There was only 1 browser window open or pop-up handle invalid (due to
		// it being closed)
		return false;
	}

	/**
	 * Switches back to the Main Window (at time of instantiation)
	 * 
	 * @return
	 */
	public boolean switchToMainWindow()
	{
		try
		{
			driver.switchTo().window(sMainWindowHandle);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Disposes of the Pop-up Window Handle
	 */
	public void disposePopupWindowHandle()
	{
		sPopupWindowHandle = null;
	}

	/**
	 * Wait until a pop-up window appears
	 * 
	 * @return
	 */
	public boolean waitForPopup()
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
			if (openWindows.length > 1)
				return true;

			sleep(nPollInterval);
		}

		// No pop-up window appeared within the timeout value
		return false;
	}

	/**
	 * Wraps the Thread.sleep method
	 * 
	 * @param nMilliSeconds
	 *            MilliSeconds to pause for
	 */
	public static void sleep(int nMilliSeconds)
	{
		try
		{
			Thread.sleep(nMilliSeconds);
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Executes JavaScript command
	 * 
	 * @param driver
	 * @param sJS - JavaScript to execute.
	 */
	public static void executeJavaScript(WebDriver driver, String sJS)
	{
		if (!executeJavaScript(driver, sJS, true))
		{
			String sError = "Javascript execution failed";
			Report.logError(sError, new JavaScriptException(sError));
			// throw new JavaScriptException(sError);
		}
	}

	/**
	 * Executes JavaScript command (Logging is specified)
	 * 
	 * @param driver
	 * @param sJS - JavaScript to execute
	 * @param bLog - true for logging
	 * @return false if any exception occurred else true
	 */
	public static boolean executeJavaScript(WebDriver driver, String sJS, boolean bLog)
	{
		try
		{
			((JavascriptExecutor) driver).executeScript(sJS);
			if (bLog)
				Report.logInfo("Executed:  " + sJS);

			return true;
		}
		catch (Exception ex)
		{
			if (bLog)
			{
				String sError = "Exception occurred executing following javascript:  " + sJS + NewLine
						+ "Exception was following:  " + ex.getMessage() + NewLine;
				Report.logWarning(sError);
			}
		}

		return false;
	}

	/**
	 * Moves window to (0,0) & re-sizes window to maximum size
	 * 
	 * @param driver
	 */
	public static void maximizeWindow(WebDriver driver)
	{
		moveWindow(driver, 0, 0);
		try
		{
			((JavascriptExecutor) driver)
					.executeScript("window.resizeTo(screen.availWidth,screen.availHeight);");
		}
		catch (Exception ex)
		{
			Report.logWarning("Javascript to maximize window generated an exception:  " + ex.getMessage());
		}
	}

	/**
	 * Moves window to specified coordinates
	 * 
	 * @param driver
	 * @param nCoordinateX
	 * @param nCoordinateY
	 */
	public static void moveWindow(WebDriver driver, int nCoordinateX, int nCoordinateY)
	{
		try
		{
			((JavascriptExecutor) driver).executeScript("window.moveTo(" + nCoordinateX + "," + nCoordinateY
					+ ");");
		}
		catch (Exception ex)
		{
			Report.logWarning("Javascript to move window generated an exception:  " + ex.getMessage());
		}
	}

	/**
	 * Resizes window to specified dimensions but does not move window's position to (0,0)
	 * 
	 * @param driver
	 * @param nWidth
	 * @param nHeight
	 */
	public static void resizeWindow(WebDriver driver, int nWidth, int nHeight)
	{
		try
		{
			((JavascriptExecutor) driver).executeScript("window.resizeTo(" + nWidth + ", " + nHeight + ");");
		}
		catch (Exception ex)
		{
			Report.logWarning("Javascript to resize the window generated an exception:  " + ex.getMessage());
		}
	}

	/**
	 * Sets the DB details needed before executing methods with queries <BR>
	 * <BR>
	 * Note: It is recommended not to use this method instead use setDB_Details(String,String,String,String)
	 * 
	 * @param sDB_Server - Database Server to connect to
	 * @param sDB - Database to use
	 */
	public void setDB_Details(String sDB_Server, String sDB)
	{
		this.sDB_Server = sDB_Server;
		this.sDB = sDB;
	}

	/**
	 * Sets the DB details needed before executing methods with queries<BR>
	 * <BR>
	 * Note: It is recommend to use this instead of the setDB_Details(String,String)
	 * 
	 * @param sUser - User to connect as
	 * @param sPassword - Password for User
	 * @param sDB_Server - Database Server to connect to
	 * @param sDB - Database to use
	 */
	public void setDB_Details(String sUser, String sPassword, String sDB_Server, String sDB)
	{
		this.sDB_User = sUser;
		this.sDB_Password = sPassword;
		this.sDB_Server = sDB_Server;
		this.sDB = sDB;
	}

	/**
	 * Selects value from drop down using visible text
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Visible text of option to select
	 */
	public static void dropDownSelect(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, 0, sValue, true);
	}

	/**
	 * If drop down exists, then Selects value from drop down using visible text
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Visible text of option to select
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, 0, sValue, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 */
	public static void dropDownSelectByValue(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, 1, sValue, true);
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByValueTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, 1, sValue, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 */
	public static void dropDownSelectByIndex(WebElement dropdown, String sDropDownName, String sValue)
	{
		dropDownSelection(dropdown, sDropDownName, 2, sValue, true);
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByIndexTry(WebElement dropdown, String sDropDownName, String sValue)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, 2, sValue, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects value from drop down using a regular expression to find the 1st matching option.
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 */
	public static void dropDownSelectByRegEx(WebElement dropdown, String sDropDownName, String sRegEx)
	{
		dropDownSelection(dropdown, sDropDownName, 3, sRegEx, true);
	}

	/**
	 * If able to find an option (visible text) matching the regular expression, then selects value from drop
	 * down.<BR>
	 * <BR>
	 * Note: To find a partial match of a string use ".*" + Option + ".*"
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 * @return true if no errors else false
	 */
	public static boolean dropDownSelectByRegExTry(WebElement dropdown, String sDropDownName, String sRegEx)
	{
		try
		{
			dropDownSelection(dropdown, sDropDownName, 3, sRegEx, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Goes through a list of visible text options and attempting to select the specified option from the drop
	 * down and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 0);
	}

	/**
	 * Goes through a list of value options and attempting to select the specified option from the drop down
	 * and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByValueMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 1);
	}

	/**
	 * Goes through a list of index options and attempting to select the specified option from the drop down
	 * and returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByIndexMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 2);
	}

	/**
	 * Goes through a list (of RegEx) and attempting to select the specified option from the drop down and
	 * returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectByRegExMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList)
	{
		return dropDownSelectionMultipleTry(dropdown, sDropDownName, sOptionList, 3);
	}

	/**
	 * Goes through a list and attempting to select the specified option from the drop down and
	 * returns after 1st successful selection (or after attempting exhausting the list given.)
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sOptionList - List of options to try and select (regular expressions allowed.)
	 * @param nOption How to locate the option. (1 - By Value, 2 - By Index, 3 - Regular Expression, Default -
	 *            Visible Text)
	 * @return true if able to make a selection else false
	 */
	public static boolean dropDownSelectionMultipleTry(WebElement dropdown, String sDropDownName,
			String[] sOptionList, int nOption)
	{
		for (int i = 0; i < sOptionList.length; i++)
		{
			String sLogName = sDropDownName + "(try '" + sOptionList[i] + "')";
			if (nOption == 1)
			{
				// Able to select this option?
				if (dropDownSelectByValueTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else if (nOption == 2)
			{
				// Able to select this option?
				if (dropDownSelectByIndexTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else if (nOption == 3)
			{
				// Able to select this option?
				if (dropDownSelectByRegExTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
			else
			{
				// Able to select this option?
				if (dropDownSelectTry(dropdown, sLogName, sOptionList[i]))
					return true;
			}
		}

		// Could not find any of the options to select
		return false;
	}

	/**
	 * Selects value from drop down using specified option
	 * 
	 * @param dropdown - Drop down to select option from
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param nOption - How to locate the option. (1 - By Value, 2 - By Index, 3 - Regular Expression, Default
	 *            - Visible Text)
	 * @param sValue - Option to select
	 * @param bLogError - if true then log error else log warning
	 * @return
	 */
	public static void dropDownSelection(WebElement dropdown, String sDropDownName, int nOption,
			String sValue, boolean bLogError)
	{
		try
		{
			if (!dropdown.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			Select value = new Select(dropdown);
			if (nOption == 1)
			{
				value.selectByValue(sValue);
				Report.logInfo("Successfully selected by value '" + sValue + "' from the drop down ('"
						+ sDropDownName + "')");
			}
			else if (nOption == 2)
			{
				value.selectByIndex(Integer.parseInt(sValue));
				Report.logInfo("Successfully selected by index '" + sValue + "' from the drop down ('"
						+ sDropDownName + "')");
			}
			else if (nOption == 3)
			{
				int nIndex = 0;
				List<WebElement> availableOptions = value.getOptions();
				for (WebElement option : availableOptions)
				{
					// Select option if regular expression matches
					if (option.getText().matches(sValue))
					{
						value.selectByIndex(nIndex);
						Report.logInfo("Successfully selected by index '" + nIndex + "' by using RegEx '"
								+ sValue + "' to match from the drop down ('" + sDropDownName + "')");
						return;
					}

					nIndex++;
				}

				throw new DropDownPartialMatchException("");
			}
			else
			{
				value.selectByVisibleText(sValue);
				Report.logInfo("Successfully selected '" + sValue + "' from the drop down ('" + sDropDownName
						+ "')");
			}
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sDropDownName + "') was not enabled";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new ElementNotEnabledException(sError);
		}
		catch (NoSuchElementException nsee)
		{
			String sError = "Could not find '" + sValue + "' in the drop down ('" + sDropDownName + "')";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new DropDownSelectionException(sError);
		}
		catch (NumberFormatException nfe)
		{
			String sError = "Could not find index '" + sValue + "' in the drop down ('" + sDropDownName
					+ "')";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new DropDownIndexException(sError);
		}
		catch (DropDownPartialMatchException ddpme)
		{
			String sError = "Could not find a partial match using the regular expression '" + sValue
					+ "' in the drop down ('" + sDropDownName + "')";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new DropDownPartialMatchException(sError);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Drop down ('" + sDropDownName
					+ "') was stale as StaleElementReferenceException was thrown.";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new DropDownNoSuchElementException(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not find drop down ('" + sDropDownName + "') due to exception:  "
					+ ex.getClass().getName() + ":  " + ex.getMessage();
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new DropDownNoSuchElementException(sError);
		}
	}

	/**
	 * Clicks specified element with logging
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 */
	public static void click(WebElement element, String sElementName)
	{
		click(element, sElementName, true);
	}

	/**
	 * If element exists, then Clicks specified element
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @return true if no errors else false
	 */
	public static boolean clickTry(WebElement element, String sElementName)
	{
		try
		{
			click(element, sElementName, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Clicks specified element with logging
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param bLogError - if true then log error else log warning
	 */
	public static void click(WebElement element, String sElementName, boolean bLogError)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			element.click();
			Report.logInfo("Clicked '" + sElementName + "' successfully");
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new ElementNotEnabledException(sError);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new ClickNoSuchElementException(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception:  "
					+ ex.getClass().getName() + ":  " + ex.getMessage();
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new ClickNoSuchElementException(sError);
		}
	}

	/**
	 * Clears & enters value into specified field<BR>
	 * <BR>
	 * Note: Field must exist.
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 */
	public static void enterField(WebElement element, String sElementName, String sInputValue)
	{
		enterField(element, sElementName, sInputValue, true, true);
	}

	/**
	 * If the field exists, then clear & enters value into the field ELSE logs warning that field could not be
	 * found.<BR>
	 * <BR>
	 * Note: Use this method if it is acceptable to skip fields that are missing (or do not always appear.)
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @return true if no errors else false
	 */
	public static boolean enterFieldTry(WebElement element, String sElementName, String sInputValue)
	{
		try
		{
			enterField(element, sElementName, sInputValue, true, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Enters value into specified field<BR>
	 * <BR>
	 * Notes:<BR>
	 * 1) Field must exist.<BR>
	 * 2) Does not clear field before entry.
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 */
	public static void onlyEnterField(WebElement element, String sElementName, String sInputValue)
	{
		enterField(element, sElementName, sInputValue, false, true);
	}

	/**
	 * Clears & enters value into specified field<BR>
	 * <BR>
	 * Note: Any thrown exception should be handled by the caller.
	 * 
	 * @param element - Element to enter information
	 * @param sElementName - Element Name to log
	 * @param sInputValue - Value to input into the field
	 * @param bClearField - true to clear field before entering value
	 * @param bLogError - if true then log error else log warning
	 */
	public static void enterField(WebElement element, String sElementName, String sInputValue,
			boolean bClearField, boolean bLogError)
	{
		try
		{
			if (!element.isEnabled())
			{
				throw new ElementNotEnabledException("");
			}

			String sLogCleared = "";
			if (bClearField)
			{
				element.clear();
				sLogCleared = "cleared & ";
			}

			element.sendKeys(sInputValue);
			Report.logInfo("Successfully " + sLogCleared + "entered the value '" + sInputValue + "' into '"
					+ sElementName + "'");
		}
		catch (ElementNotEnabledException ex)
		{
			String sError = "Element ('" + sElementName + "') was not enabled";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new ElementNotEnabledException(sError);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new EnterFieldNoSuchElementException(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception:  "
					+ ex.getClass().getName() + ":  " + ex.getMessage();
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new EnterFieldNoSuchElementException(sError);
		}
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 */
	public static void check(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		check(element, sElementName, bVerifyInitialState, true);
	}

	/**
	 * If the field exists, then Selects check box if unselected ELSE logs warning that field could not be
	 * found.<BR>
	 * <BR>
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 * @return true if no errors else false
	 */
	public static boolean checkTry(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		try
		{
			check(element, sElementName, bVerifyInitialState, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param element - Check box to select
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 * @param bLogError - if true then log error else log warning
	 */
	public static void check(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		try
		{
			// Does user want to enforce state of unselected before selecting
			// check box
			if (bVerifyInitialState)
			{
				if (element.isSelected())
					throw new CheckBoxWrongStateException("");
			}

			// Click check box only if it is not already selected
			if (!element.isSelected())
			{
				element.click();
				Report.logInfo("Successfully selected check box for '" + sElementName + "'");
			}
			else
			{
				Report.logInfo("Check box for '" + sElementName + "' was already selected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the check operation";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxWrongStateException(sError);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxNoSuchElementException(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception:  "
					+ ex.getClass().getName() + ":  " + ex.getMessage();
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxNoSuchElementException(sError);
		}
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 */
	public static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		uncheck(element, sElementName, bVerifyInitialState, true);
	}

	/**
	 * If the field exists, then Unselects check box if selected ELSE logs warning that field could not be
	 * found.<BR>
	 * <BR>
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 * @return true if no errors else false
	 */
	public static boolean uncheckTry(WebElement element, String sElementName, boolean bVerifyInitialState)
	{
		try
		{
			uncheck(element, sElementName, bVerifyInitialState, false);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param element - Check box to unselect
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 * @param bLogError - if true then log error else log warning
	 */
	public static void uncheck(WebElement element, String sElementName, boolean bVerifyInitialState,
			boolean bLogError)
	{
		try
		{
			// Does user want to enforce state of selected before unselecting
			// check box
			if (bVerifyInitialState)
			{
				if (!element.isSelected())
					throw new CheckBoxWrongStateException("");
			}

			// Click check box only if it is not already selected
			if (element.isSelected())
			{
				element.click();
				Report.logInfo("Successfully unselected check box for '" + sElementName + "'");
			}
			else
			{
				Report.logInfo("Check box for '" + sElementName + "' was already unselected");
			}
		}
		catch (CheckBoxWrongStateException cbwse)
		{
			String sError = "Required initial state of the check box '" + sElementName
					+ "' was not correct for the uncheck operation";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxWrongStateException(sError);
		}
		catch (StaleElementReferenceException ex)
		{
			String sError = "Element ('" + sElementName
					+ "') was stale as StaleElementReferenceException was thrown.";
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxNoSuchElementException(sError);
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "' due to exception:  "
					+ ex.getClass().getName() + ":  " + ex.getMessage();
			if (bLogError)
				Report.logError(sError + NewLine);
			else
				Report.logWarning(sError);

			throw new CheckBoxNoSuchElementException(sError);
		}
	}

	/**
	 * Goes to URL if site is up.<BR>
	 * <BR>
	 * Note: This function prevents the waiting indefinitely issue with WebDriver.get(...)
	 * 
	 * @param driver
	 * @param sUrl - URL to launch
	 */
	public static void get(WebDriver driver, String sUrl)
	{
		try
		{
			/*
			 * Need to determine if secure site or not
			 */
			boolean bSecureSite = false;
			if (sUrl.length() > 5)
			{
				String sSecurePrefix = sUrl.substring(0, 5);
				if (sSecurePrefix.equalsIgnoreCase("https"))
					bSecureSite = true;
			}

			/*
			 * Connect to the site and see if it is alive before using WebDriver.get(...)
			 */
			URL url = new URL(sUrl);
			if (bSecureSite)
			{
				try
				{
					HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
					httpsCon.setConnectTimeout(Framework.nTimeout * 1000);
					httpsCon.connect();
				}
				catch (SSLHandshakeException ex)
				{
					// We don't care that the certificate is invalid only that the site is responding
				}
			}
			else
			{
				URLConnection urlConn = url.openConnection();
				urlConn.setConnectTimeout(Framework.nTimeout * 1000);
				urlConn.connect();
			}

			driver.get(sUrl);
		}
		catch (Exception ex)
		{
			String sError = "The site ('" + sUrl
					+ "') is taking too long to respond or URL does not exist.  Error:  ";
			Report.logError(sError + ex + NewLine, new GetException(sError + ex + NewLine));
			// throw new GetException(sError + ex + NewLine);
		}
	}

	/**
	 * If the page is displayed try to click up to the specified times to get to next screen.
	 * 
	 * @param driver
	 * @param sURL - Page must contain this URL
	 * @param sComplete - xpath/id to determine when page is completed loading
	 * @param sType - The way to determine page is complete
	 * @param bWaitForActionToStart - True if need to wait for action to start
	 * @param sActionStartedCheck - xpath/id to determine if action has started
	 * @param bJS - true to execute JavaScript else normal click
	 * @param sLocatorOrJS - How to locate the element (button, link, etc.)
	 * @param sLogDetails - What to put in the log
	 * @param nTries - How many times to try and click to get to next page.
	 */
	public static void clickAtPage(WebDriver driver, String sURL, String sComplete, String sType,
			boolean bWaitForActionToStart, String sActionStartedCheck, boolean bJS, String sLocatorOrJS,
			String sLogDetails, int nTries)
	{
		/**
		 * If the start page is displayed try to click Next up to the specified times to get to next screen.
		 * 
		 * @param driver
		 * @param nTries
		 */
		for (int i = 0; i < nTries; i++)
		{
			/*
			 * Only try to click if still at specific page.
			 */
			if (driver.getCurrentUrl().contains(sURL))
			{
				/*
				 * Find the element, click & wait for page to complete loading.
				 */
				try
				{
					Framework selenium = new Framework(driver);

					// Wait for page to load
					selenium.setXpath_AJAX_Complete(sComplete);
					selenium.setAJAX_type(sType);
					if (!selenium.bPageLoaded())
						Report.logWarning("AJAX Timeout before '" + sLogDetails + "' still going to continue");

					if (bJS)
					{
						executeJavaScript(driver, sLocatorOrJS);
						Report.logInfo("Javascript Executed for '" + sLogDetails + "'");
					}
					else
					{
						WebElement element = driver.findElement(locatedBy(sLocatorOrJS));
						Framework.click(element, sLogDetails);
					}

					/*
					 * If there is a delay between the click and the action taking place, then we need to do a
					 * check before the AJAX complete check.
					 * 
					 * Note: If this is not done, then the AJAX complete check will success but the page will
					 * not be complete and this will cause intermittent timing issues.
					 */
					if (bWaitForActionToStart)
					{
						if (!Framework.bWaitForEnabledElement(driver, sActionStartedCheck))
							Report.logWarning("Timeout waiting for action '" + sLogDetails
									+ "' to start still going to continue");
					}

					// Wait for page to load
					if (!selenium.bPageLoaded())
						Report.logWarning("AJAX Timeout after '" + sLogDetails + "' still going to continue");
				}
				catch (Exception ex)
				{
				}
			}
			else
				return;
		}
	}

	/**
	 * Returns the WebElement for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param bLog - true for logging
	 * @return null if cannot find
	 */
	public static WebElement findElement(WebDriver driver, String sLocator, boolean bLog)
	{
		WebElement element = null;
		try
		{
			element = driver.findElement(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Report.logWarning("Could not find element using:  " + sLocator);
		}

		return element;
	}

	/**
	 * Returns the WebElement for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @return null if cannot find
	 */
	public static WebElement findElement(WebDriver driver, String sLocator)
	{
		return findElement(driver, sLocator, true);
	}

	/**
	 * Waits until the specified locator returns a matching WebElement.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @return null if no matching WebElement upon reaching timeout
	 */
	public static WebElement findElementAJAX(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			WebElement element = findElement(driver, sLocator, false);
			if (element != null)
				return element;

			sleep(nPollInterval);
		}

		Report.logWarning("The locator ('" + sLocator
				+ "') did not return any element before Timeout occurred");
		return null;
	}

	/**
	 * Returns the WebElements for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @param bLog - true for logging
	 * @return
	 */
	public static List<WebElement> findElements(WebDriver driver, String sLocator, boolean bLog)
	{
		List<WebElement> elements = null;
		try
		{
			elements = driver.findElements(locatedBy(sLocator));
		}
		catch (Exception ex)
		{
			if (bLog)
				Report.logWarning("Could not any find elements using:  " + sLocator);
		}

		return elements;
	}

	/**
	 * Returns the WebElements for the specified sLocator.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @return null if cannot find
	 */
	public static List<WebElement> findElements(WebDriver driver, String sLocator)
	{
		return findElements(driver, sLocator, true);
	}

	/**
	 * Waits until the specified locator returns a matching WebElements.
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Elements
	 * @return null if no matching WebElements upon reaching timeout
	 */
	public static List<WebElement> findElementsAJAX(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			List<WebElement> elements = findElements(driver, sLocator, false);
			if (elements != null)
				return elements;

			sleep(nPollInterval);
		}

		Report.logWarning("The locator ('" + sLocator
				+ "') did not return any elements before Timeout occurred");
		return null;
	}

	/**
	 * Gets the text for the element.<BR>
	 * Note: Text Visible to user
	 * 
	 * @param element - element to get text from
	 * @param sLogDetails - element name to log
	 * @param bLog - true for logging
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element, String sLogDetails, boolean bLog)
	{
		try
		{
			String sText = element.getText();
			if (bLog)
				Report.logInfo("Element '" + sLogDetails + "' had the following text:  " + sText);

			return sText;
		}
		catch (Exception ex)
		{
			if (bLog)
				Report.logWarning("Could not get text for '" + sLogDetails + "'");

			return null;
		}
	}

	/**
	 * Gets the text for the element. (No logging)<BR>
	 * Note: Text Visible to user
	 * 
	 * @param element - element to get text from
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element)
	{
		return getText(element, "", false);
	}

	/**
	 * Gets the text for the element (with logging.)<BR>
	 * Note: Text Visible to user
	 * 
	 * @param element - element to get text from
	 * @param sLogDetails - element name to log
	 * @return null if cannot find element and/or text
	 */
	public static String getText(WebElement element, String sLogDetails)
	{
		return getText(element, sLogDetails, true);
	}

	/**
	 * Gets the text regardless whether visible or not (without logging) using the Cobra HTML parser<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If there are no matches check that the xpath (which is case sensitive) is in the same case as the
	 * HTML source (use driver.getPageSource() to write the actual source to a file/log.) <BR>
	 * <BR>
	 * <B>Related function:</B><BR>
	 * Misc.xpathChangeCase(String sXpath, String sDelimiter, boolean bToUppercase) to be
	 * used in conjunction with this function to get correct case for xpath.
	 * 
	 * @param driver
	 * @param sXpath - xpath to node
	 * @return text of node if exists or null
	 */
	public static String getText(WebDriver driver, String sXpath)
	{
		try
		{
			HTML html = new HTML(driver);
			return html.getNodeValue(sXpath, null);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Get attribute using Cobra HTML parser instead of Selenium.<BR>
	 * Note: If there are no matches check that the xpath (which is case sensitive) is in the same case as the
	 * HTML source (use driver.getPageSource() to write the actual source to a file/log.)
	 * 
	 * @param driver
	 * @param sXpath - xpath to node
	 * @param sAttribute - attribute to get
	 * @return null if cannot find
	 */
	public static String getAttribute(WebDriver driver, String sXpath, String sAttribute)
	{
		try
		{
			HTML html = new HTML(driver);
			return html.getAttribute(sXpath, sAttribute);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Waits for the URL to change.<BR>
	 * Note: You need to save the initial URL before doing your action & then calling this method.<BR>
	 * <BR>
	 * Example:<BR>
	 * <BR>
	 * sInitialValue = driver.getCurrentUrl();<BR>
	 * button.click();<BR>
	 * Framework.bWaitForURLtoChange(driver, sInitialValue, false);<BR>
	 * 
	 * @param driver
	 * @param sInitialValue - Complete Initial URL or Part of Initial URL that will is expected to change
	 * @param bPartial - true waits until URL no longer contains sInitialValue, false waits until any
	 *            difference in URL
	 * @return true if URL changed before timeout else false
	 */
	public static boolean bWaitForURLtoChange(WebDriver driver, String sInitialValue, boolean bPartial)
	{
		/*
		 * Just in case value is null (due to driver.getCurrentUrl returning null when passed to this method)
		 */
		String sValue = sInitialValue;
		if (sValue == null)
			sValue = "";

		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			/*
			 * Just in case value is null (due to driver.getCurrentUrl returning null.)
			 */
			String sCurrentURL;
			try
			{
				sCurrentURL = driver.getCurrentUrl();
			}
			catch (Exception ex)
			{
				sCurrentURL = "";
			}

			if (bPartial)
			{
				// Check that current URL does not contain initial value any more
				if (!sCurrentURL.contains(sInitialValue))
					return true;
			}
			else
			{
				// Check for any difference in URL
				if (!sCurrentURL.equalsIgnoreCase(sInitialValue))
					return true;
			}

			sleep(nPollInterval);
		}

		return false;
	}

	/**
	 * Clicks the element and waits for the URL to change (any part)
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 */
	public static void clickAndWait(WebElement element, String sElementName)
	{
		clickAndWait(element, sElementName, null, false, null);
	}

	/**
	 * Clicks the element and waits for the URL to change and contains specified page
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param sPageToWaitFor - Waits until URL contains this value
	 */
	public static void clickAndWaitForURL(WebElement element, String sElementName, String sPageToWaitFor)
	{
		clickAndWait(element, sElementName, null, false, sPageToWaitFor);
	}

	/**
	 * Clicks the element and waits for the URL to change.<BR>
	 * Note: Use this if you need more control.
	 * 
	 * @param element - Element to click
	 * @param sElementName - Element Name to log
	 * @param sInitialValue - Part of Initial URL that will is expected to change<BR>
	 *            If null, then current URL is used.
	 * @param bPartial - true waits until URL no longer contains sInitialValue, false waits until any
	 *            difference in URL
	 * @param sValueToWaitFor - Waits until URL contains this value. If null or empty, then it does not wait.
	 */
	public static void clickAndWait(WebElement element, String sElementName, String sInitialValue,
			boolean bPartial, String sValueToWaitFor)
	{
		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			WebDriver useDriver = ((RemoteWebElement) realElement).getWrappedDriver();

			// Store the initial URL to be able to tell when the URL changes
			String sUseValue = sInitialValue;

			// If sInitialValue is not set, then use current URL
			if (sInitialValue == null)
				sUseValue = useDriver.getCurrentUrl();

			// Click the element
			click(element, sElementName);

			// Wait for the URL to change
			bWaitForURLtoChange(useDriver, sUseValue, bPartial);

			// Wait for specific URL?
			if (sValueToWaitFor == null || sValueToWaitFor.equals(""))
				return;
			else
				bWaitForURL(useDriver, sValueToWaitFor);
		}
		catch (ClickNoSuchElementException ex)
		{
			throw new ClickNoSuchElementException(ex.getMessage());
		}
		catch (Exception ex)
		{
			String sError = "Could not find '" + sElementName + "'" + NewLine;
			Report.logError(sError, new ClickNoSuchElementException(sError));
			// throw new ClickNoSuchElementException(sError);
		}
	}

	/**
	 * Finds the element using the specified locator & clicks with logging
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 */
	public static void click(WebDriver driver, String sLocator, String sElementName)
	{
		click(findElement(driver, sLocator), sElementName, true);
	}

	/**
	 * Selects check box if unselected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in unselected state
	 */
	public static void check(WebDriver driver, String sLocator, String sElementName,
			boolean bVerifyInitialState)
	{
		check(findElement(driver, sLocator), sElementName, bVerifyInitialState, true);
	}

	/**
	 * Unselects check box if selected<BR>
	 * <BR>
	 * Note: If (bVerifyInitialState == true) then user should catch possible GenericErrorDetectedException
	 * and throw a specific exception for their test.
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sElementName - Element Name to log
	 * @param bVerifyInitialState - Require check box in selected state
	 */
	public static void uncheck(WebDriver driver, String sLocator, String sElementName,
			boolean bVerifyInitialState)
	{
		uncheck(findElement(driver, sLocator), sElementName, bVerifyInitialState, true);
	}

	/**
	 * Selects value from drop down using visible text
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Option to select
	 */
	public static void dropDownSelect(WebDriver driver, String sLocator, String sDropDownName, String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, 0, sValue, true);
	}

	/**
	 * Selects value from drop down using zero based index
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Select option that has this index (zero based)
	 */
	public static void dropDownSelectByIndex(WebDriver driver, String sLocator, String sDropDownName,
			String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, 2, sValue, true);
	}

	/**
	 * Selects value from drop down using a regular expression to find the 1st matching option.
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sRegEx - Regular Expression to find 1st match of drop down options
	 */
	public static void dropDownSelectByRegEx(WebDriver driver, String sLocator, String sDropDownName,
			String sRegEx)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, 3, sRegEx, true);
	}

	/**
	 * Selects value from drop down using value attribute
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)
	 * @param sDropDownName - Name to use in log for the drop down
	 * @param sValue - Use Value Attribute of option to select
	 */
	public static void dropDownSelectByValue(WebDriver driver, String sLocator, String sDropDownName,
			String sValue)
	{
		dropDownSelection(findElement(driver, sLocator), sDropDownName, 1, sValue, true);
	}

	/**
	 * Wrapper function to work with an alert
	 * 
	 * @param driver
	 * @param bAccept - true to click 'OK' (accept)
	 * @param bLog - true to write to log
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String workWithAlert(WebDriver driver, boolean bAccept, boolean bLog)
	{
		// Give focus to the alert
		Alert alert = driver.switchTo().alert();

		// Get the text to return later
		String sMessage = alert.getText();

		if (bLog)
			Report.logInfo("Switched to alert with message:  " + sMessage);

		// Accept or Dismiss the alert
		if (bAccept)
		{
			alert.accept();
			if (bLog)
				Report.logInfo("Cleared alert via 'Accept'");
		}
		else
		{
			alert.dismiss();
			if (bLog)
				Report.logInfo("Cleared alert via 'Dismiss'");
		}

		return sMessage;
	}

	/**
	 * Clicks 'OK' (accept) for the alert and returns the message text of the alert
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String acceptAlert(WebDriver driver)
	{
		return workWithAlert(driver, true, true);
	}

	/**
	 * Dismisses the alert and returns the message text of the alert
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 * @throws NoAlertPresentException (Selenium)
	 */
	public static String dismissAlert(WebDriver driver)
	{
		return workWithAlert(driver, false, true);
	}

	/**
	 * Clicks 'OK' (accept) for the alert and returns the message text of the alert.<BR>
	 * <B>Notes:</B><BR>
	 * 1) If no alert (or any exception occurs) then null is returned<BR>
	 * 2) No logging
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 */
	public static String acceptAlertTry(WebDriver driver)
	{
		try
		{
			return workWithAlert(driver, true, false);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Dismisses the alert and returns the message text of the alert.<BR>
	 * <B>Notes:</B><BR>
	 * 1) If no alert (or any exception occurs) then null is returned<BR>
	 * 2) No Logging
	 * 
	 * @param driver
	 * @return Message text of the Alert
	 */
	public static String dismissAlertTry(WebDriver driver)
	{
		try
		{
			return workWithAlert(driver, false, false);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Waits for the attribute to change on the element.<BR>
	 * <B>Note:</B> You need to save the initial attribute value before doing your action & then calling this
	 * method.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * sInitialValue = "j_id30";<BR>
	 * WebElement button = Framework.findElement(driver, "javax.faces.ViewState");
	 * button.click();<BR>
	 * Framework.bWaitForAttributeToChange(button, "value", sInitialValue);<BR>
	 * 
	 * @param element - Element to perform check against
	 * @param sAttribute - Attribute to wait for changes
	 * @param sInitialValue - Initial value of attribute
	 * @return
	 */
	public static boolean bWaitForAttributeToChange(WebElement element, String sAttribute,
			String sInitialValue)
	{
		/*
		 * Just in case value is null
		 */
		String sValue = sInitialValue;
		if (sValue == null)
			sValue = "";

		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			/*
			 * Just in case value is null
			 */
			String sCurrentAttributeValue;
			try
			{
				sCurrentAttributeValue = element.getAttribute(sAttribute);
			}
			catch (Exception ex)
			{
				sCurrentAttributeValue = "";
			}

			// If attribute value is null set to be empty string
			if (sCurrentAttributeValue == null)
				sCurrentAttributeValue = "";

			// Check if attribute has changed
			if (!sCurrentAttributeValue.equalsIgnoreCase(sInitialValue))
				return true;

			sleep(nPollInterval);
		}

		return false;
	}

	/**
	 * This method waits for an attribute to change on an element. (If attribute does not change, then
	 * exception is thrown.)
	 * 
	 * @param driver
	 * @param sLocator - How to locate the Element
	 * @param sAttribute - Attribute to wait for changes
	 * @param sInitialValue - Initial value of attribute
	 * @param sLogging - Element Name to log
	 */
	public static void waitForAttributeChange(WebDriver driver, String sLocator, String sAttribute,
			String sInitialValue, String sLogging)
	{
		if (!bWaitForAttributeToChange(findElement(driver, sLocator, false), sAttribute, sInitialValue))
		{
			String sError = "Timeout occurred waiting for:  " + sLogging + Framework.getNewLine();
			Report.logError(sError, new GenericActionNotCompleteException(sError));
			// throw new GenericActionNotCompleteException(sError);
		}
	}

	/**
	 * Gets the attribute value on the WebElement
	 * 
	 * @param element
	 * @param sAttribute - Attribute for which to get value
	 * @return null if value not set else attribute's current value
	 */
	public static String getAttribute(WebElement element, String sAttribute)
	{
		String sCurrentAttributeValue;
		try
		{
			sCurrentAttributeValue = element.getAttribute(sAttribute);
		}
		catch (Exception ex)
		{
			sCurrentAttributeValue = null;
		}

		return sCurrentAttributeValue;
	}

	/**
	 * Gets the selected option (1st) for a drop down.
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return null if no option selected or not drop down
	 */
	public static String getSelectedOption(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return getText(options.get(i));
			}
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Waits for an element to appear & be enabled<BR>
	 * <BR>
	 * <B>Note: </B>The selenium method isEnabled() returns true in most cases. If this does not work, then
	 * try waiting for the element to be displayed.
	 * 
	 * @param element - Element to wait to become enabled
	 * @param sElementName - Element Name to log
	 * @return true if element appears & is enabled before timeout else false
	 */
	public static boolean bWaitForEnabledElement(WebElement element, String sElementName)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			try
			{
				if (element.isEnabled())
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(nPollInterval);
		}

		Report.logWarning("The element ('" + sElementName
				+ "') did not become enabled before Timeout occurred");
		return false;
	}

	/**
	 * Waits for an element to be displayed.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use if element already exists but it is hidden and you want to wait until it becomes displayed.<BR>
	 * 2) Do <B>NOT</B> use if <B>element does not exist</B> instead use same method that takes a WebDriver &
	 * Locator.<BR>
	 * 
	 * @param element - Element to wait to be displayed
	 * @param sElementName - Element Name to log
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean bWaitForDisplayedElement(WebElement element, String sElementName)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			try
			{
				// Check if displayed
				if (element.isDisplayed())
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(nPollInterval);
		}

		Report.logWarning("The element ('" + sElementName + "') was not displayed before Timeout occurred");
		return false;
	}

	/**
	 * Waits for an element to be displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return true if element is displayed before timeout else false
	 */
	public static boolean bWaitForDisplayedElement(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			try
			{
				// Try to find the element
				WebElement element = driver.findElement(locatedBy(sLocator));

				// Check if displayed
				if (element.isDisplayed())
					return true;
			}
			catch (Exception ex)
			{
			}

			sleep(nPollInterval);
		}

		Report.logWarning("The locator ('" + sLocator + "') was not displayed before Timeout occurred");
		return false;
	}

	/**
	 * Checks if an element is displayed on the page
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 * @return true if element displayed else false
	 */
	public static boolean bElementDisplayed(WebDriver driver, String sLocator)
	{
		try
		{
			// Try to find the element
			WebElement element = driver.findElement(locatedBy(sLocator));

			// Check if displayed
			if (element.isDisplayed())
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Checks if the element is displayed on the page
	 * 
	 * @param element - WebElement to check if displayed
	 * @return true if element displayed else false
	 */
	public static boolean bElementDisplayed(WebElement element)
	{
		try
		{
			// Check if displayed
			if (element.isDisplayed())
				return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element on the page
	 */
	public static void waitForDisplayedElement(WebDriver driver, String sLocator)
	{
		if (bWaitForDisplayedElement(driver, sLocator))
		{
			String sMessage = "The locator ('" + sLocator + "') was displayed";
			Report.logInfo(sMessage);
		}
		else
		{
			String sError = "The locator ('" + sLocator + "') was not displayed before Timeout occurred";
			Report.logError(sError, new GenericActionNotCompleteException(sError));
			// throw new GenericActionNotCompleteException(sError);
		}
	}

	/**
	 * Waits for an element to be displayed and throws exception if not displayed
	 * 
	 * @param element - element to wait until displayed
	 * @param sElementName - Element Name to log
	 */
	public static void waitForDisplayedElement(WebElement element, String sElementName)
	{
		if (bWaitForDisplayedElement(element, sElementName))
		{
			String sMessage = "The element ('" + sElementName + "') was displayed";
			Report.logInfo(sMessage);
		}
		else
		{
			String sError = "The element ('" + sElementName + "') was not displayed before Timeout occurred";
			Report.logError(sError, new GenericActionNotCompleteException(sError));
			// throw new GenericActionNotCompleteException(sError);
		}
	}

	/**
	 * Verifies that Element is displayed & sets URL & Page Name
	 * 
	 * @param sLocator - Locator for the Element that must be displayed on the page
	 * @param sPageName - Page Name for logging
	 * @param sUrl - URL to set
	 */
	protected void initialize(String sLocator, String sPageName, String sUrl)
	{
		if (bWaitForDisplayedElement(driver, sLocator))
		{
			PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
			this.sUrl = sUrl;
			this.sPageName = sPageName;
		}
		else
		{
			String sError = "Not at the " + sPageName + " page as the Element ('" + sLocator
					+ "') was not displayed as such initialization is not allowed." + NewLine;

			Screenshot.saveScreenshotAddSuffix("method_initialize");
			Report.logError(sError, new WrongPageException(sError));
			// throw new WrongPageException(sError);
		}
	}

	/**
	 * Gets the available options for a drop down<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) If cannot get available options for any reason, then an empty list will be returned
	 * 
	 * @param element - Drop Down to get options for
	 * @return List&lt;WebElement&gt; - Available options for the drop down (list is never <B>null</B>)
	 */
	public static List<WebElement> getOptions(WebElement element)
	{
		try
		{
			// Get drop down to work with
			Select dropdown = new Select(element);

			// Get all the available options
			List<WebElement> found = dropdown.getOptions();

			// We do not want to return a null variable as such only return variable if not null
			if (found != null)
				return found;
		}
		catch (Exception ex)
		{
		}

		// If get to this point, then return empty list
		return new ArrayList<WebElement>();
	}

	/**
	 * Gets the (HTML) value of the selected option (1st) for a drop down.
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return null if no option selected or not drop down
	 */
	public static String getSelectedValue(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return getAttribute(options.get(i), "value");
			}
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Gets the index of the selected option (1st) for a drop down.<BR>
	 * <BR>
	 * <B>Note: </B>Zero based index value<BR>
	 * 
	 * @param element - Drop Down to get 1st option selected
	 * @return -1 if no option selected or not drop down
	 */
	public static int getSelectedIndex(WebElement element)
	{
		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
					return i;
			}
		}
		catch (Exception ex)
		{
		}

		return -1;
	}

	/**
	 * Attempts to scroll into view the specified WebElement.
	 * 
	 * @param element - WebElement to scroll into view
	 * @return - false if an exception occurs else true
	 */
	public static boolean scrollIntoView(WebElement element)
	{
		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			WebDriver useDriver = ((RemoteWebElement) realElement).getWrappedDriver();

			// Execute JavaScript to scroll into view
			((JavascriptExecutor) useDriver).executeScript("arguments[0].scrollIntoView(true);", element);

			return true;
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Determine whether or not this checkbox is selected or not.
	 * 
	 * @param element - Check box element
	 * @return true if selected else false
	 */
	public static boolean bCheckboxSelected(WebElement element)
	{
		try
		{
			return element.isSelected();
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Determines if the element is currently enabled
	 * 
	 * @param element - Element to check if enabled
	 * @return true the element is enabled else false
	 */
	public static boolean bElementEnabled(WebElement element)
	{
		try
		{
			return element.isEnabled();
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Clears an element (should be only used on text fields.)
	 * 
	 * @param element - Field to clear
	 * @return true if successful else false
	 */
	public static boolean clearField(WebElement element)
	{
		try
		{
			element.clear();
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Selects or Unselects a check box (requires check box to be enabled.)
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param bCheck - true to select check box
	 * @param bVerifyInitialState - true to require check box in unselected state if to be selected or
	 *            selected state if to be unselected
	 */
	public static void checkbox(WebElement element, String sElementName, boolean bCheck,
			boolean bVerifyInitialState)
	{
		checkbox(element, sElementName, bCheck, true, bVerifyInitialState);
	}

	/**
	 * Selects or Unselects a check box
	 * 
	 * @param element - Check box to work with
	 * @param sElementName - Element Name to log
	 * @param bCheck - true to select check box
	 * @param bVerifyEnabled - true to verify check box is enabled before taking the action
	 * @param bVerifyInitialState - true to require check box in unselected state if to be selected or
	 *            selected state if to be unselected
	 */
	public static void checkbox(WebElement element, String sElementName, boolean bCheck,
			boolean bVerifyEnabled, boolean bVerifyInitialState)
	{
		if (bVerifyEnabled)
		{
			if (!bElementEnabled(element))
			{
				String sError = "Check box for '" + sElementName + "' was not enabled" + NewLine;
				Report.logError(sError, new CheckBoxNotEnabled(sError));
				// throw new CheckBoxNotEnabled(sError);
			}
		}

		if (bCheck)
			check(element, sElementName, bVerifyInitialState, false);
		else
			uncheck(element, sElementName, bVerifyInitialState, false);
	}

	/**
	 * Verifies the element is in the correct state (enabled/disabled)<BR>
	 * <BR>
	 * <B>Note: </B> Only failures are logged<BR>
	 * 
	 * @param element - Element to verify status
	 * @param sLog - Element Name to log
	 * @param bDisabled - true to verify that element is disabled
	 */
	public static void verifyStatus(WebElement element, String sLog, boolean bDisabled)
	{
		verifyStatus(element, sLog, bDisabled, false);
	}

	/**
	 * Verifies the element is in the correct state (enabled/disabled)
	 * 
	 * @param element - Element to verify status
	 * @param sLog - Element Name to log
	 * @param bDisabled - true to verify that element is disabled
	 * @param bLogAll - true to log all checks even ones that are successful
	 */
	public static void verifyStatus(WebElement element, String sLog, boolean bDisabled, boolean bLogAll)
	{
		if (bDisabled)
		{
			if (bElementEnabled(element))
				Report.logError("'" + sLog + "' was enabled but should have been disabled");
			else
			{
				if (bLogAll)
					Report.logInfo("'" + sLog + "' was disabled as expected");
			}
		}
		else
		{
			if (bElementEnabled(element))
			{
				if (bLogAll)
					Report.logInfo("'" + sLog + "' was enabled as expected");
			}
			else
				Report.logError("'" + sLog + "' was disabled but should have been enabled");
		}
	}

	/**
	 * Get a WebDriver from a WebElement. This is useful in cases where you only have a WebElement but need a
	 * WebDriver to do some action.
	 * 
	 * @param element - WebElement to get the WebDriver from
	 * @return null if exception occurs else WebDriver
	 */
	public static WebDriver getWebDriver(WebElement element)
	{
		WebDriver useDriver = null;

		try
		{
			/*
			 * Trick to get real element that can return the WebDriver.
			 * Note:
			 * 1) If you use element directly it is a proxy and this cannot be cast to RemoteWebElement
			 * 2) If WebElement cannot be bound, then this will generate an exception
			 */
			WebElement realElement = element.findElement(By.xpath("."));

			// Get the WebDriver object from the real (bound) WebElement
			useDriver = ((RemoteWebElement) realElement).getWrappedDriver();
		}
		catch (Exception ex)
		{
		}

		return useDriver;
	}

	/**
	 * Verify that an entered value matches the actual value (case insensitive) only logs a failure<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Reads "value" attribute to get actual value used in the comparison<BR>
	 * 2) Should only be used for input fields<BR>
	 * 3) Use other overloaded method if need to control case comparison or logging level<BR>
	 * 
	 * @param element - Element (input field) to verify has the correct value
	 * @param sExpectedValue - Expected (entered) value
	 */
	public static void verifyField(WebElement element, String sExpectedValue)
	{
		verifyField(element, sExpectedValue, false, false);
	}

	/**
	 * Verify that an entered value matches the actual value<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Reads "value" attribute to get actual value used in the comparison<BR>
	 * 2) Should only be used for input fields<BR>
	 * 
	 * @param element - Element (input field) to verify has the correct value
	 * @param sExpectedValue - Expected (entered) value
	 * @param bCaseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @param bLogAll - true to log success as well, false only a failure is logged
	 */
	public static void verifyField(WebElement element, String sExpectedValue, boolean bCaseSensitive,
			boolean bLogAll)
	{
		// Assume that the values match
		boolean bFailure = false;

		/*
		 * 1) Get Actual value
		 * 2) Ensure both values are not null
		 * 3) Trim both values
		 * 4) Compare the values
		 */
		String sExpected = Conversion.sEmptyInsteadOfNull(sExpectedValue).trim();
		String sActual = Conversion.sEmptyInsteadOfNull(getAttribute(element, "value")).trim();

		if (bCaseSensitive)
		{
			if (!sActual.equals(sExpected))
				bFailure = true;
		}
		else
		{
			if (!sActual.equalsIgnoreCase(sExpected))
				bFailure = true;
		}

		// Did the values match?
		if (bFailure)
		{
			Report.logError("Expected value ('" + sExpected + "') did not match the Actual value ('"
					+ sActual + "')");
		}
		else
		{
			// Log success if necessary
			if (bLogAll)
				Report.logInfo("Expected value ('" + sExpected + "') matched the Actual value ('" + sActual
						+ "')");
		}
	}

	/**
	 * Gets all cookies
	 * 
	 * @param driver
	 * @return Set&lt;Cookie&gt;
	 */
	public static Set<Cookie> getCookies(WebDriver driver)
	{
		try
		{
			return driver.manage().getCookies();
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Logs all cookies
	 * 
	 * @param driver
	 */
	public static void logAllCookies(WebDriver driver)
	{
		Set<Cookie> allCookies = getCookies(driver);
		if (allCookies == null)
			Report.logInfo("No Cookies");

		for (Cookie c : allCookies)
		{
			Report.logInfo(c.toString());
		}
	}

	/**
	 * Wait till element disappears (removed from screen/ visibility hidden) from screen
	 * 
	 * @param driver
	 * @param sLocator - How to locate the element
	 * @return - true if element disappears before timeout else false
	 * 
	 */
	public static boolean bWaitForElementNotDisplayed(WebDriver driver, String sLocator)
	{
		WebElement element;

		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			try
			{
				// Try to find the element
				element = findElement(driver, sLocator, false);

				// Wait until element is disappeared (removed from screen/ visibility hidden) from screen
				if (!bElementDisplayed(element))
					return true;
			}
			catch (Exception ex)
			{

			}
			sleep(nPollInterval);
		}

		// Element is not disappeared before timeout
		Report.logWarning("The element with locator ('" + sLocator
				+ "') appears even after timeout occurred but it is expected to disappear...");

		return false;
	}

	/**
	 * Writes the current page source to a file for debugging purposes.
	 * 
	 * @param driver
	 * @param sFileName - Filename to write the current page as
	 * @return true if saving current page as file is successful else false
	 */
	public static boolean saveCurrentPage(WebDriver driver, String sFileName)
	{
		try
		{
			File file = new File(sFileName);
			if (file.exists())
				file.delete();

			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(driver.getPageSource());
			out.close();
			return true;
		}
		catch (Exception ex)
		{
			Report.logWarning("Saving current page source failed with exception:  " + ex.getMessage());
			return false;
		}
	}

	/**
	 * Logs the current date time for debugging purposes
	 */
	public static void logCurrentDateTime()
	{
		Report.logInfo("Current Date Time:  " + new Date());
	}

	/**
	 * Method used to remove focus from HTML element.
	 * 
	 * @param driver - webDriver
	 */
	public static void loseFocus(WebDriver driver)
	{
		WebElement page = Framework.findElement(driver, "tag=html", false);
		Framework.click(page, "HTML tag");
	}
	/*Methods added - Samidha Choudhari*/
	/**
	 * Switch frame
	 * 
	 * @param driver
	 * @param sLocator - How to find the element (ID, xpath, etc)	
	 */
	public static void switchFrame(WebDriver driver, String sLocator)
	{	    
		WebElement frame = null;
		try {
			frame = findElement(driver, sLocator);
			driver.switchTo().frame(frame);        
		    sleep(5000);
		}
		catch (FrameNoSuchElementException ex){
			
				Report.logWarning("Could not find element using:  " + sLocator);
		}		
	}
	
	/**
	 * Set default content
	 * 
	 * @param driver	 	
	 */
	public static void setDefaultFrame(WebDriver driver)
	{
		    
	    driver.switchTo().defaultContent();	    
	}  
	
	/**
	 * Get Current URL
	 * 
	 * @param driver
	 * @return sCurrentURL 
	 */
	public String getCurrentURL(WebDriver driver)
	{
		sCurrentURL = driver.getCurrentUrl();        
	    return sCurrentURL;
	   
	}
	
	/**
	 * Get Title of browser
	 * 
	 * @param driver
	 * @return sBrowserTitle 
	 */
	public String getBrowserTitle(WebDriver driver)
	{
		sBrowserTitle = driver.getTitle();       
	    return sBrowserTitle;
	   
	}
	
	/**
	 * Get Height and Width of an Element
	 * 
	 * @param driver	 
	 * @return dimension
	 */
	public static Dimension getElementSize(WebDriver driver, String sLocator)
	{
		WebElement element = findElement(driver, sLocator);
		Dimension dimension=element.getSize();
		return dimension;
	}
	
	/**
	 * Get X and Y coordinates of an Element
	 * 
	 * @param driver	 
	 * @return point
	 */
	public static Point getElementLocation(WebDriver driver, String sLocator)
	{
		WebElement element = findElement(driver, sLocator);
		Point point=element.getLocation();
		return point;
	}
	
}
