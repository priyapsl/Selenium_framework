package com.advantage.reporting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

/**
 * This class provides different methods for generating logs of test execution
 * @author Administrator
 *
 */
public class Logs {

	public static Logger log;
	public static Logger logHTML;
	public static String LOG_PROPS = "logger.properties";
	public static String FOLDER_FILE_PROP = "Logs.FILE";
	public static String FOLDER_HTML_PROP = "Logs.HTML";
	public static String FILE = "FILE";
	public static String HTML = "HTML";
	public static String FOLDER_FILE = System.getProperty("user.dir");
	public static String FOLDER_HTML = System.getProperty("user.dir");
	public static String TESTSUITESEPARATOR = "=";
	public static int TESTSUITESEPARATORLENGTH = 100;
	public static String TESTSEPARATOR = "*";
	public static int TESTSEPARATORLENGTH = 100;
	public static String ITERATIONSEPARATOR = "-";
	public static int ITERATIONSEPARATORLENGTH = 43;

	/**
	 * This method is for addproperty with key-value
	 * @param sKey
	 * @param sValue
	 * @return
	 */
	public static boolean addProperty(String sKey, String sValue)
	{
		String sExistingPropertyValue = System.getProperty(sKey);
		if (sExistingPropertyValue == null)
		{
			Properties applicationProps = new Properties(System.getProperties());
			applicationProps.put(sKey, sValue);
			System.setProperties(applicationProps);
			return true;
		}
		else
			return false;
	}

	/**
	 * This method sets property to HTML folder
	 * @param sValue
	 */
	public static void setProperty4FolderHTML(String sValue)
	{
		if (!addProperty(FOLDER_HTML_PROP, sValue))
			System.setProperty(FOLDER_HTML_PROP, sValue);
	}

	/**
	 * THis method set property for file and folder
	 * @param sValue
	 */
	public static void setProperty4FolderFile(String sValue)
	{
		if (!addProperty(FOLDER_FILE_PROP, sValue))
			System.setProperty(FOLDER_FILE_PROP, sValue);
	}

	/**
	 * This method creates folder with unique name (current date timestamp)
	 * @return
	 */
	public static String generateUniqueFolderName()
	{
		return sConvertDate(new Date(), "yyyy-MM-dd_HH-mm-ss");
	}

	/**
	 * This method convert date format
	 * @param value
	 * @param sToFormat
	 * @return
	 */
	public static String sConvertDate(Date value, String sToFormat)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(sToFormat);
			String sConverted = new String(dateformat.format(value));
			return sConverted;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**This method initialize logger for log and HTML file
	 * 
	 */
	public static void initializeLoggers()
	{
		log = Logger.getLogger(Logs.FILE);
		logHTML = Logger.getLogger(Logs.HTML);

		// Makes the logger ready for use. (If logger used before this, then an error occurs.)
		PropertyConfigurator.configure(Logs.LOG_PROPS);
	}

	/** This method logs info in log file
	 * 
	 * @param objectToLog
	 * @param sPrefixText
	 * @param sNullText
	 */
	public static void logObject(Object objectToLog, String sPrefixText, String sNullText)
	{
		if (objectToLog == null)
			Logs.log.warn(sNullText);
		else
			Logs.log.info(sPrefixText + objectToLog.toString());
	}

	/**
	 * This method logs failure time in log files
	 */
	public static void failureTime()
	{
		Logs.log.error("Failure Time:  " + new Date());
	}
	/**
	 * THis method logs section in the log files
	 * @param sRepeat
	 * @param nTimes
	 */
	public static void logSection(String sRepeat, int nTimes)
	{
		// Append the string you want to repeat the specified number of times
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nTimes; i++)
		{
			sb.append(sRepeat);
		}

		// Write to the log the constructed string of repeating text
		Logs.log.info(sb);
	}

	/**
	 * This method logs information in log files
	 * @param sMessage
	 */
	public static void logInfo(String sMessage)
	{
		Logs.log.info(sMessage);
	}

	/**
	 * This method logs error in log files
	 *  @param sMessage
	 */
	public static void logError(String sMessage)
	{
		Logs.log.error(sMessage);
	}
	
	/**
	 * This method logs error with runtime exception in log files
	 * @param sMessage
	 * @param runtime
	 */
	public static void logError(String sMessage, Exception runtime)
	{
		Logs.log.error(sMessage);
	}
	
	/**
	 * This method logs exception with exception in log files
	 * @param sMessage
	 * @param exception
	 */
	public static void logException(String sMessage, Exception exception)
	{
		Logs.log.fatal(sMessage);
	}
	
	/**
	 * This method logs warning in log file
	 * @param sMessage
	 */
	public static void logWarning(String sMessage)
	{
		Logs.log.warn(sMessage);
	}

	/**
	 * This method logs test initiation details in log files
	 * @param testCaseId
	 */
	public static void logTestInitiation(String testCaseId)
	{
		Logs.logInfo("Test Case : " + testCaseId + " : START");
	}

	/**
	 * This method logs test exit details in log files
	 * @param testCaseId
	 */
	public static void logTestExit(String testCaseId)
	{
		Logs.logInfo("Test Case : " + testCaseId + " : END");
	}

	/**
	 * This method logs iteration start details in log files
	 * @param iIterationId
	 */
	public static void logIterationStart(int iIterationId)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ITERATIONSEPARATORLENGTH; i++)
		{
			sb.append(ITERATIONSEPARATOR);
		}

		String sSpaces = "";
		sSpaces = iIterationId <= 99 ? " " : "";
		sSpaces = iIterationId <= 9 ? "  " : "";
		Logs.logInfo(sb + " Iteration : " + iIterationId + sSpaces + sb);
	}

	/**
	 * This method log iteration end details in log files
	 */
	public static void logIterationEnd()
	{
		Logs.logSection(ITERATIONSEPARATOR, TESTSEPARATORLENGTH);
	}

	@Test
	public static void unitTest()
	{
		/*
		 * Set various options for logging before
		 * PropertyConfigurator.configure(...) is called
		 */
		// If you want to change the default properties file used by Log4j
		Logs.LOG_PROPS = "logger.properties";

		/*
		 * If you want to change the default property that stores the FILE log
		 * location. This is used in the LOG_PROPS file.
		 */
		Logs.FOLDER_FILE_PROP = "Logs.FILE";

		/*
		 * If you want to change the default property that stores the HTML log
		 * location. This is used in the LOG_PROPS file.
		 */
		Logs.FOLDER_HTML_PROP = "Logs.HTML";

		// If you want to use dynamic folders to store the FILE log
		Logs.FOLDER_FILE = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "text"
				+ System.getProperty("file.separator");
		Logs.setProperty4FolderFile(Logs.FOLDER_FILE);

		// If you want to use dynamic folders to store the FILE log
		Logs.FOLDER_HTML = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ Logs.generateUniqueFolderName() + System.getProperty("file.separator") + "html"
				+ System.getProperty("file.separator");
		Logs.setProperty4FolderHTML(Logs.FOLDER_HTML);

		// Makes the logger ready for use. (If logger used before this, then an
		// error occurs.)
		Logs.initializeLoggers();

		logSection("-", 50);

		Logs.log.info("Log 1");
		Logs.log.error("Log 1");
		Logs.logHTML.info("Log 2");

		// Only goes to log1
		Logs.log.info("Some action 1");

		// Only goes to log2
		Logs.logHTML.error("Some error 1<BR>");

		Logs.log.info("Some action 2");
		Logs.logHTML.info("Some <B>action</B> 2<BR>");
	}

}
