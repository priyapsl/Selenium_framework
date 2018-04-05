package com.advantage.reporting;

import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * This class provides different methods for reporting.
 * @author Administrator
 *
 */
public class Report {
/**
 * 
 */
	public static String TESTSUITESEPARATOR = "=";
	public static int TESTSUITESEPARATORLENGTH = 100;
	public static String TESTSEPARATOR = "*";
	public static int TESTSEPARATORLENGTH = 100;
	public static String ITERATIONSEPARATOR = "-";
	public static int ITERATIONSEPARATORLENGTH = 43;
	private static String ATTRIBUTE = "Name";
	private static String ATTRIBUTE_VALUE = "Failed";
	
	/**
	 * This method is for log title information with message.
	 * @param sMessage
	 */

	public static void logTitle(String sMessage)
	{
		Reporter.log("<P class='testtitle'>" + sMessage + "</p>");
		Logs.logInfo(sMessage);
	}

	/**
	 * This method log information message in the report
	 * @param sMessage
	 */
	public static void logInfo(String sMessage)
	{
		Reporter.log("<P class='testpassed'>" + sMessage + "</p>");
		Logs.logInfo(sMessage);
	}

	/**
	 * This method log Pass message in the report
	 * @param sMessage
	 */
	public static void logPass(String sMessage)
	{
		Reporter.log("<P class='testpassed'>" + sMessage + "</p>");
		Logs.logInfo(sMessage);
	}

	/**
	 * This method log error message in the report
	 * @param sMessage
	 */
	public static void logError(String sMessage)
	{
		Reporter.log("<P class='testfailed'>" + sMessage + "</p>");
		sMessage = sMessage.replaceAll("<BR>", "\n");
		Logs.logError(sMessage);
		ITestResult result = Reporter.getCurrentTestResult();
		result.setAttribute(ATTRIBUTE, ATTRIBUTE_VALUE);
		result.setStatus(ITestResult.FAILURE);
		Reporter.setCurrentTestResult(result);
		if (Screenshot.getAllowScreenshotsStatus() == true)
		{
			String filePath = Screenshot.saveErrorScreenshot();
			System.out.println("filePath" + filePath);
			Reporter.log("<a href='../Screenshots/" + filePath
					+ "' target = '_blank'>Click here for error screenshot</a>");
		}
	}

	/**
	 * This method log error with RuntimeException in the report
	 * @param sMessage
	 * @param runtime
	 */
	public static void logError(String sMessage, RuntimeException runtime)
	{
		Reporter.log("<P class='testfailed'>" + sMessage + "</p>");
		sMessage = sMessage.replaceAll("<BR>", "\n");
		Logs.logError(sMessage + "\n" + "Exception:" + runtime.getMessage());
		throw runtime;
	}

	/**
	 * This method log exception in the report
	 * @param sMessage
	 * @param exception
	 */
	public static void logException(String sMessage, RuntimeException exception)
	{
		Reporter.log("<P class='testfailed'>" + sMessage + "</p>");
		Logs.logException(sMessage, exception);
	}

	/**
	 * This method log warning message in the report
	 * @param sMessage
	 */
	public static void logWarning(String sMessage)
	{
		Reporter.log("<P class='testwarning'>" + sMessage + "</p>");
		Logs.logWarning(sMessage);
	}

	/**
	 * This method log test initiation details in the report
	 * @param testCaseId
	 */
	public static void logTestInitiation(String testCaseId)
	{
		Report.logTestInitiationAndEnd();
		Reporter.log("<P class='testinfo'>Test Case : " + testCaseId + " : START </P>");
		Logs.logInfo("Test Case : " + testCaseId + " : START");
	}

	/**
	 * This method log test exit details in the report
	 * @param testCaseId
	 */
	public static void logTestExit(String testCaseId)
	{
		Reporter.log("<P class='testinfo'>Test Case : " + testCaseId + " : END</P>");
		Logs.logInfo("Test Case : " + testCaseId + " : END");
		Report.logTestInitiationAndEnd();
	}

	/**
	 * This method generate section separator string
	 * @param sSpecialChar
	 * @param iRepeatation
	 * @return
	 */
	public static String getSectionSeparatorString(String sSpecialChar, int iRepeatation)
	{
		String sb = "";
		for (int i = 0; i < iRepeatation; i++)
		{
			sb = sb + sSpecialChar;
		}
		return sb;
	}

	/**
	 * This method log sections in the report
	 * @param TESTSEPARATOR
	 * @param TESTSEPARATORLENGTH
	 */
	public static void logSection(String TESTSEPARATOR, int TESTSEPARATORLENGTH)
	{
		String sb = getSectionSeparatorString(TESTSEPARATOR, TESTSEPARATORLENGTH);
		Reporter.log("<P class='testinfo'>" + sb + "</P>");
		Logs.logSection(TESTSEPARATOR, TESTSEPARATORLENGTH);
	}

	/**
	 * This method log iteration start details in the report
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
		Reporter.log("<P class='testinfo'>" + sb + " Iteration : " + iIterationId + sSpaces + sb + "</P>");
		Logs.logInfo(sb + " Iteration : " + iIterationId + sSpaces + sb);
	}

	/**
	 * This method log iteration end details in the report
	 */
	public static void logIterationEnd()
	{
		logSection(ITERATIONSEPARATOR, TESTSEPARATORLENGTH);
		Logs.logSection(ITERATIONSEPARATOR, TESTSEPARATORLENGTH);
	}

	/**
	 * This method log test initiation and end details in the report
	 */
	public static void logTestInitiationAndEnd()
	{
		logSection(TESTSEPARATOR, TESTSEPARATORLENGTH);
	}
}
