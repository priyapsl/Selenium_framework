package com.advantage.reporting;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

/**
 * This class provides methods which will override from testng result 
 * and gives methods for success, failure, skipped
 * @author Administrator
 *
 */
public class DotTestListener extends TestListenerAdapter {

	private String ATTRIBUTE = "Name";
	private String ATTRIBUTE_VALUE = "Failed";

	@Override
	public void onTestFailure(ITestResult tr)
	{
		Reporter.setCurrentTestResult(tr);
	}

	@Override
	public void onTestSkipped(ITestResult tr)
	{
		Reporter.setCurrentTestResult(tr);
	}

	@Override
	public void onTestSuccess(ITestResult tr)
	{
		if (tr.getAttribute(ATTRIBUTE) != null)
		{
			if (tr.getAttribute(ATTRIBUTE).toString().equalsIgnoreCase(ATTRIBUTE_VALUE))
			{
				tr.setStatus(ITestResult.FAILURE);
				onTestFailure(tr);
			}
		}
		Reporter.setCurrentTestResult(tr);
	}
}
