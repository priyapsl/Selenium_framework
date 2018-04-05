package com.advantage.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.advantage.genericexceptions.CheckBoxNotEnabled;
import com.advantage.genericexceptions.GenericActionNotCompleteException;
import com.advantage.genericexceptions.JavaScriptException;
import com.advantage.reporting.Report;

public class Ajax {

	private final static String sTempUniqueNode = "dndelete";

	/**
	 * Gets sTempUniqueNode for use with methods <B>wasNodeRemovedFromDOM</B> or <B>removeNodeFromDOM</B>
	 * 
	 * @return AJAX.sTempUniqueNode (private variable)
	 */
	public static String getTempUniqueNode()
	{
		return sTempUniqueNode;
	}

	/**
	 * Constructs the JavaScript to add a new child node using getTempUniqueNode() for the value to the
	 * specified parent node
	 * 
	 * @param sParentID - ID of node where the new child node will be added to the DOM
	 * @return
	 */
	public static String getJS_AddNodeToDOM(String sParentID)
	{
		String sAddNodeJS = "var myNode=document.createElement('" + getTempUniqueNode() + "'); ";
		sAddNodeJS += "myNode.setAttribute('id', '" + getTempUniqueNode() + "'); ";
		sAddNodeJS += "document.getElementById('" + sParentID + "').appendChild(myNode); ";
		return sAddNodeJS;
	}

	/**
	 * Determines if the element specified was removed from the DOM before timeout.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) This method should be used in conjunction with the method <B>addNodeToDOM</B> which adds a temp node
	 * to the DOM<BR>
	 * 
	 * @param driver
	 * @param sLocator - How to find the element that is to be removed
	 * @return true if node was removed from the DOM else false
	 */
	public static boolean wasNodeRemovedFromDOM(WebDriver driver, String sLocator)
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.bTimeoutOccurred())
		{
			// Check if the element was removed
			WebElement element = Framework.findElement(driver, sLocator, false);
			if (element == null)
				return true;

			Framework.sleep(Framework.nPollInterval);
		}

		return false;
	}

	/**
	 * Uses javascript to remove a node from the DOM. The purpose of this is to then trigger the AJAX which
	 * adds the node back at which time the AJAX request is complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The javascript only works for ID. If no ID exists, then different javascript to add the node will be
	 * needed.<BR>
	 * 2) This method needs to be called before the action that triggers the AJAX update.<BR>
	 * 
	 * @param driver
	 * @param sID - ID of node to be deleted
	 */
	public static void removeNodeFromDOM(WebDriver driver, String sID)
	{
		String sRemoveNodeJS = "var dn_temp = document.getElementById('" + sID + "'); ";
		sRemoveNodeJS += "dn_temp.parentNode.removeChild(dn_temp); ";
		if (!Framework.executeJavaScript(driver, sRemoveNodeJS, false))
		{
			String sError = "Javascript execution failed for following:  " + sRemoveNodeJS
					+ Framework.getNewLine();
			Report.logError(sError, new JavaScriptException(sError));
		}
	}

	/**
	 * Constructs the JavaScript to add a new child node to the specified parent node
	 * 
	 * @param sParentID - ID of node where the new child node will be added to the DOM
	 * @param sNewChildNodeID - Unique value for both the child node name and ID to be added to the DOM
	 * @return
	 */
	public static String getJS_AddNodeToDOM(String sParentID, String sNewChildNodeID)
	{
		String sAddNodeJS = "var myNode=document.createElement('" + sNewChildNodeID + "'); ";
		sAddNodeJS += "myNode.setAttribute('id', '" + sNewChildNodeID + "'); ";
		sAddNodeJS += "document.getElementById('" + sParentID + "').appendChild(myNode); ";
		return sAddNodeJS;
	}

	public static void enterText(WebElement element, String sLog, String sValue, String parentElementId,
			boolean bException)
	{
		String sJavaScript = getJS_AddNodeToDOM(parentElementId);
		enter_DOM(element, sLog, sValue, sJavaScript, bException);
	}

	/**
	 * This method adds a node to the DOM before clearing & entering the value into the field and TABs off to
	 * generate AJAX call and waits for the AJAX call to complete<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param element - Element to enter text
	 * @param sLog - Element name to log
	 * @param sValue - Value to enter into the field
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean enter_DOM(WebElement element, String sLog, String sValue, String sJavaScript,
			boolean bException)
	{
		// Will be set in try/catch block if no error occurs
		WebDriver useDriver = null;

		boolean bJS_Error;
		String sException = "";
		String sAddNodeJS = "";
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

			if (useDriver == null)
			{
				String sError = "Could not get WebDriver from WebElement ('" + sLog + "')";
				Report.logError(sError, new RuntimeException(sError));
			}

			// Set flag to indicate no error occurred
			bJS_Error = false;
		}
		catch (Exception ex)
		{
			// Set flag to indicate an error occurred
			bJS_Error = true;
			sException = ex.getMessage();
		}

		/*
		 * Stop if any error occurred
		 */
		if (bJS_Error)
		{
			String sError = "Javascript execution failed for following:  " + sAddNodeJS;
			Report.logError(sError);
			String sError2 = "Error was following:  " + sException + Framework.getNewLine();
			Report.logError(sError2, new JavaScriptException(sError + sError2));
		}

		// Enter field
		Framework.enterField(element, sLog, sValue);

		// Add Node to DOM
		if (!Framework.executeJavaScript(useDriver, sJavaScript, false))
		{
			String sError = "Could not add node to DOM using the following Javascript:  " + sJavaScript
					+ Framework.getNewLine();
			Report.logError(sError, new JavaScriptException(sError));
		}

		// TAB away from the field to generate AJAX call
		try
		{
			element.sendKeys(Keys.TAB);
		}
		catch (Exception ex)
		{
			String sError = "Tabbing off element ('" + sLog + "') generated the following exception:  "
					+ ex.getMessage();
			Report.logError(sError, (RuntimeException) ex);
		}

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(useDriver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred." + Framework.getNewLine();
				Report.logError(sError, new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Report.logWarning("AJAX did not complete before timeout occurred");
		return false;
	}

	public static void clickLink(WebDriver driver, String sLocator, String sLog, String parentElementId,
			boolean bException)
	{
		String sJavaScript = getJS_AddNodeToDOM(parentElementId);
		click_DOM(driver, sLocator, sLog, sJavaScript, bException);
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before clicking the option and
	 * waiting for the node to removed. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Javascript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the option to click
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean click_DOM(WebDriver driver, String sLocator, String sLog, String sJavaScript,
			boolean bException)
	{
		// Run the JavaScript
		if (!Framework.executeJavaScript(driver, sJavaScript, false))
		{
			String sError = "Javascript execution failed for following:  " + sJavaScript
					+ Framework.getNewLine();
			Report.logError(sError, new JavaScriptException(sError));
		}

		// Click the option
		Framework.click(driver, sLocator, sLog);

		// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
		boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
		if (bCompleteBeforeTimeout)
		{
			// AJAX completed successfully before timeout
			return true;
		}
		else
		{
			// Attempts manual removal (logs error but does not throw an exception)
			removeNodeFromDOM(driver, sTempUniqueNode);

			// AJAX did not complete before timeout & user wants an exception thrown
			if (bException)
			{
				String sError = "AJAX did not complete before timeout occurred." + Framework.getNewLine();
				Report.logError(sError, new GenericActionNotCompleteException(sError));
			}
		}

		// AJAX did not complete before timeout (and user did not want an exception thrown)
		Report.logWarning("AJAX did not complete before timeout occurred");
		return false;
	}

	public static void dropdownSelectItem(WebDriver driver, String sLocator, String sLog,
			SelectionBy selectionBy, String parentElementId, String sValue, boolean bException)
	{
		String sJavaScript = getJS_AddNodeToDOM(parentElementId);
		dropdownSelectItem_DOM(driver, sLocator, sLog, selectionBy, sJavaScript, sValue, bException);
	}

	/**
	 * This method adds a node to the DOM using the <B>specified JavaScript</B> before selecting the option
	 * and waiting for the node to removed as necessary. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Javascript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 
	 * @param driver - Web Driver
	 * @param sLocator - How to find the drop down to work with
	 * @param sLog - Element Name to log
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param dropdown - The drop down option to be selected
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean dropdownSelectItem_DOM(WebDriver driver, String sLocator, String sLog,
			SelectionBy selectionBy, String sJavaScript, String sValue, boolean bException)
	{
		// Get the drop down to work with
		WebElement element = Framework.findElement(driver, sLocator);

		// Get current selected option before action
		int nBeforeAction = Framework.getSelectedIndex(element);

		// Use regular drop down selection method
		switch (selectionBy)
		{
		case ByValue:
			Framework.dropDownSelect(element, sLog, sValue);
			break;
		case Index:
			Framework.dropDownSelectByIndex(element, sLog, sValue);
			break;
		case RegEx:
			Framework.dropDownSelectByRegEx(element, sLog, sValue);
			break;
		default:
			Report.logError("Invalid selection type");
			break;
		}

		// Get current selected option again
		int nAfterAction = Framework.getSelectedIndex(element);

		// Did selected option change?
		if (nBeforeAction != nAfterAction)
		{
			// Run the JavaScript
			if (!Framework.executeJavaScript(driver, sJavaScript, false))
			{
				String sError = "Javascript execution failed for following:  " + sJavaScript
						+ Framework.getNewLine();
				Report.logError(sError, new JavaScriptException(sError));
			}

			try
			{
				// Trigger AJAX by tabbing off element
				element.sendKeys(Keys.TAB);
			}
			catch (Exception ex)
			{
				String sError = "Tabbing off element ('" + sLog + "') generated the following exception:  "
						+ ex.getMessage();
				Report.logError(sError);
			}

			// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
			boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(driver, sTempUniqueNode);
			if (bCompleteBeforeTimeout)
			{
				// AJAX completed successfully before timeout
				return true;
			}
			else
			{
				// Attempts manual removal (logs error but does not throw an exception)
				removeNodeFromDOM(driver, sTempUniqueNode);

				// AJAX did not complete before timeout & user wants an exception thrown
				if (bException)
				{
					String sError = "AJAX did not complete before timeout occurred." + Framework.getNewLine();
					Report.logError(sError, new GenericActionNotCompleteException(sError));
				}
			}

			// AJAX did not complete before timeout (and user did not want an exception thrown)
			Report.logWarning("AJAX did not complete before timeout occurred");
			return false;
		}
		else
		{
			// No AJAX should be triggered
			return true;
		}
	}

	public static void checkbox(WebElement element, String sLog, boolean bCheck, String parentElementId,
			boolean bException)
	{
		String sJavaScript = getJS_AddNodeToDOM(parentElementId);
		checkbox_DOM(element, sLog, bCheck, sJavaScript, bException);
	}

	/**
	 * This method adds a node to the DOM before selecting/unselecting the check box and TABs off to generate
	 * AJAX call and waits for the AJAX call to complete. (<B>Recommended Method</B>)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) JavaScript must add the same node as getTempUniqueNode()<BR>
	 * 2) No need to manually remove node upon failure as method handles this.<BR>
	 * 3) An exception can still be thrown just not related to AJAX timeout<BR>
	 * 4) Check box only selected or unselected if it generates an AJAX call<BR>
	 * 5) Verifies that check box is enabled<BR>
	 * 
	 * @param element - Check box element
	 * @param sLog - Element name to log
	 * @param bCheck - true to make check box selected after method else false
	 * @param sJavaScript - JavaScript to add the node to the DOM
	 * @param bException - true if you want an exception thrown in the case that AJAX does not complete before
	 *            timeout occurs
	 * @return true if successful else false
	 */
	public static boolean checkbox_DOM(WebElement element, String sLog, boolean bCheck, String sJavaScript,
			boolean bException)
	{
		// Will be set in try/catch block if no error occurs
		WebDriver useDriver = null;

		boolean bJS_Error;
		String sException = "";
		String sAddNodeJS = "";
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

			if (useDriver == null)
			{
				throw new Exception("Could not get WebDriver from WebElement ('" + sLog + "')");
			}

			// Set flag to indicate no error occurred
			bJS_Error = false;
		}
		catch (Exception ex)
		{
			// Set flag to indicate an error occurred
			bJS_Error = true;
			sException = ex.getMessage();
		}

		/*
		 * Stop if any error occurred
		 */
		if (bJS_Error)
		{
			String sError = "Javascript execution failed for following:  " + sAddNodeJS;
			Report.logError(sError);
			String sError2 = "Error was following:  " + sException + Framework.getNewLine();
			Report.logError(sError2, new JavaScriptException(sError + sError2));
		}

		boolean bEnabled = Framework.bElementEnabled(element);
		if (!bEnabled)
		{
			String sError = "Check box for '" + sLog + "' was not enabled" + Framework.getNewLine();
			Report.logError(sError, new CheckBoxNotEnabled(sError));
		}

		// Check box
		boolean bCurrentStateSelected = Framework.bCheckboxSelected(element);
		boolean bOnChangeTrigger = false;
		if (bCheck)
		{
			if (bCurrentStateSelected)
			{
				Report.logInfo("Check box for '" + sLog + "' was already selected");
			}
			else
			{
				Framework.check(element, sLog, false);
				bOnChangeTrigger = true;
			}
		}
		else
		{
			if (bCurrentStateSelected)
			{
				Framework.uncheck(element, sLog, false);
				bOnChangeTrigger = true;
			}
			else
			{
				Report.logInfo("Check box for '" + sLog + "' was already unselected");
			}
		}

		// Will AJAX be triggered?
		if (bOnChangeTrigger)
		{
			// Add Node to DOM
			if (!Framework.executeJavaScript(useDriver, sJavaScript, false))
			{
				String sError = "Could not add node to DOM using the following Javascript:  " + sJavaScript
						+ Framework.getNewLine();
				Report.logError(sError, new JavaScriptException(sError));
			}

			// TAB away from the element to generate AJAX call
			try
			{
				element.sendKeys(Keys.TAB);
			}
			catch (Exception ex)
			{
				String sError = "Tabbing off element ('" + sLog + "') generated the following exception:  "
						+ ex.getMessage();
				Report.logError(sError);
			}

			// Wait for the node to be removed from the DOM which indicates that the AJAX is complete
			boolean bCompleteBeforeTimeout = wasNodeRemovedFromDOM(useDriver, sTempUniqueNode);
			if (bCompleteBeforeTimeout)
			{
				// AJAX completed successfully before timeout
				return true;
			}
			else
			{
				// Attempts manual removal (logs error but does not throw an exception)
				removeNodeFromDOM(useDriver, sTempUniqueNode);

				// AJAX did not complete before timeout & user wants an exception thrown
				if (bException)
				{
					String sError = "AJAX did not complete before timeout occurred." + Framework.getNewLine();
					Report.logError(sError, new GenericActionNotCompleteException(sError));
				}
			}

			// AJAX did not complete before timeout (and user did not want an exception thrown)
			Report.logWarning("AJAX did not complete before timeout occurred");
			return false;
		}
		else
		{
			return true;
		}
	}
}
