package com.advantage.reporting;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.advantage.framework.Misc;

/**
 * This class is for taking screenshots.<BR>
 * <BR>
 * <B>Note: The screenshots will be blank if running in a minimized Terminal Services window. However, other
 * windows can overlay this window.</B>
 */
public class Screenshot {
	private static String NewLine = System.getProperty("line.separator");
	private static String PathSeparator = System.getProperty("file.separator");
	private static int nScreenshotCounter = 1;
	private static String sScreenshotFolder;
	private static String sScreenshotPrefixName;
	private static boolean bAllowScreenshots = true;

	/**
	 * Method to get path separator
	 * @return
	 */
	public static String getPathSeparator()
	{
		return PathSeparator;
	}

	/**
	 * Method to set path separator
	 * @param sPathSeparator
	 */
	public static void setPathSepartor(String sPathSeparator)
	{
		Screenshot.PathSeparator = sPathSeparator;
	}

	/**
	 * Method to get Newline
	 * @return
	 */
	public static String getNewLine()
	{
		return NewLine;
	}

	/**
	 * Method to set Newline
	 * @param sNewLine
	 */
	public static void setNewLine(String sNewLine)
	{
		Screenshot.NewLine = sNewLine;
	}

	/**
	 * Method to get screeenshot counter
	 * @return
	 */
	public static int getScreenshotCounter()
	{
		return nScreenshotCounter;
	}

	/**
	 * Allows you to change nScreenshotCounter to any value.<BR>
	 * <BR>
	 * Note: This is not recommended and it is unnecessary.
	 * 
	 * @param nValue
	 */
	public static void setScreenshotCounter(int nValue)
	{
		nScreenshotCounter = nValue;
	}

	/**
	 * Resets the nScreenshotCounter back to initial value of 1
	 */
	public static void resetScreenshotCounter()
	{
		nScreenshotCounter = 1;
	}

	/**
	 * Allow screenshots to be taken
	 */
	public static void enableAllowScreenshots()
	{
		Screenshot.bAllowScreenshots = true;
	}

	/**
	 * Disable screenshots from being taken
	 */
	public static void disableAllowScreenshots()
	{
		Screenshot.bAllowScreenshots = false;
	}
	
	/**
	 * Method to allow to get screenshot status
	 * @return
	 */
	public static boolean getAllowScreenshotsStatus()
	{
		return Screenshot.bAllowScreenshots;
	}

	/**
	 * Method to set screenshot settings
	 * @param sScreenshotFolder
	 * @param sScreenshotPrefixName
	 */
	public static void setScreenshotSettings(String sScreenshotFolder, String sScreenshotPrefixName)
	{
		Screenshot.sScreenshotFolder = sScreenshotFolder;
		Screenshot.sScreenshotPrefixName = sScreenshotPrefixName;
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"
	 * 
	 * Example: c:\\temp\\screenshot001.png
	 * 
	 * @return
	 */
	public static boolean saveScreenshot()
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(nScreenshotCounter, 3) + ".png";
		if (saveScreenshot(sFilename))
		{
			nScreenshotCounter++;
			return true;
		}

		return false;
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + "_" + sSuffix + ".png"
	 * 
	 * Example: c:\\temp\\screenshot001_login.png
	 * 
	 * @param sSuffix
	 * @return
	 */
	public static boolean saveScreenshotAddSuffix(String sSuffix)
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(nScreenshotCounter, 3) + "_" + sSuffix + ".png";
		System.out.println("hi" + sFilename);
		if (saveScreenshot(sFilename))
		{
			nScreenshotCounter++;
			return true;
		}

		return false;
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + "_" + sSuffix + ".png"
	 * 
	 * Example: c:\\temp\\screenshot001_login.png
	 * 
	 * @param sSuffix
	 * @return
	 */
	public static String saveErrorScreenshot()
	{
		String sFileName = sScreenshotPrefixName + getCounter(nScreenshotCounter, 3) + ".png";
		String sFilePath = sScreenshotFolder + PathSeparator + sFileName;
		if (saveScreenshot(sFilePath))
		{
			nScreenshotCounter++;
			return sFileName;
		}
		return null;
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)
	 * 
	 * @param sFilename
	 * @return
	 */
	public static boolean saveScreenshot(String sFilename)
	{
		/*
		 * Allow user to take screenshots in program that can be enabled/disabled without modifying the code
		 * in many places instead just using a configurable switch
		 */
		if (!Screenshot.bAllowScreenshots)
			return false;

		try
		{
			Robot robot = new Robot();
			BufferedImage capture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit()
					.getScreenSize()));
			ImageIO.write(capture, "png", new File(sFilename));
			Logs.log.info("Saved screenshot to file:  " + sFilename);
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.error(ex);
			return false;
		}
	}

	/**
	 * Creates a string with leading zeros (if necessary) to make specified total length.
	 * 
	 * @param nCounter - counter to convert to a string
	 * @param nTotalLength - size of string
	 * @return
	 */
	public static String getCounter(int nCounter, int nTotalLength)
	{
		String sConvert = String.valueOf(nCounter);
		String sLeadingZeros = "";
		int nPadding = nTotalLength - sConvert.length();
		for (int i = 0; i < nPadding; i++)
		{
			sLeadingZeros += "0";
		}

		return sLeadingZeros + sConvert;
	}

	/**
	 * This function will check & create the screenshot folder(s) as necessary.<BR>
	 * <BR>
	 * Note: If you do not know or cannot guarantee that the screenshot folder will exist, then use this
	 * function before creating any screenshots.
	 * 
	 * @return true if the screenshot folder exists after execution else false
	 */
	public static boolean bCheckCreateScreenshotFolder()
	{
		return Misc.bCheckCreateFolder(sScreenshotFolder);
	}

	/**
	 * Saves screenshot. (User needs to ensure file is unique and it is has a PNG extension)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) There seems to be an issue with Firefox on certain platforms that prevents screenshots from being
	 * taken.<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @param sFilename - File name needs to be unique and have PNG extension
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot(WebDriver driver, String sFilename)
	{
		/*
		 * Allow user to take screenshots in program that can be enabled/disabled without modifying the code
		 * in many places instead just using a configurable switch
		 */
		if (!Screenshot.bAllowScreenshots)
			return false;

		try
		{
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(sFilename));
			Logs.log.info("Saved screenshot to file:  " + sFilename);
			return true;
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			Logs.log.error(ex);
			return false;
		}
	}

	/**
	 * Saves unique screenshot to following location with name: sScreenshotFolder + PathSeparator +
	 * sScreenshotPrefixName + nScreenshotCounter + ".png"<BR>
	 * <BR>
	 * <B>Note: </B><BR>
	 * 1) Must use this method if using Grid<BR>
	 * 2) If driver cannot be cast to TakesScreenshot, no screenshot will be taken<BR>
	 * 3) It is possible for the screenshot to be blank<BR>
	 * 4) There seems to be an issue with Firefox on certain platforms that prevents screenshots from being
	 * taken.<BR>
	 * <BR>
	 * <B>Example:</B> <BR>
	 * saveScreenshot(driver) => c:\\temp\\screenshot001.png<BR>
	 * 
	 * @param driver - WebDriver that has the capability of TAKES_SCREENSHOT
	 * @return true if successful else false
	 */
	public static boolean saveScreenshot(WebDriver driver)
	{
		String sFilename = sScreenshotFolder + PathSeparator + sScreenshotPrefixName
				+ getCounter(nScreenshotCounter, 3) + ".png";
		if (saveScreenshot(driver, sFilename))
		{
			nScreenshotCounter++;
			return true;
		}

		return false;
	}
}