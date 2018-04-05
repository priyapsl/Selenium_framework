package com.advantage.framework;

import org.testng.annotations.Test;

import com.advantage.genericexceptions.GenericUnexpectedException;
import com.advantage.reporting.Report;

public class Sample extends TestTemplate{

	@Test
	public void unitTest()
	{
		Report.logTestInitiation("TC001");
		
		for(int i=1; i<3; i++)
		{
			Report.logIterationStart(i);
			Report.logPass("Step 1: Logged to application");			
			Report.logPass("Step 2: Navigated to Order tab");
			Report.logError("Step 3: Order count not matching", new GenericUnexpectedException("Exception"));
			Report.logPass("Step 4: Logged out of application");
			Report.logIterationEnd();
		}
		Report.logTestExit("TC001");
		
	}
	
	@Test
	public void unitTest2()
	{
		Report.logTestInitiation("TC002");
		Report.logError("Step 3: Order count not matching");
		Report.logTestExit("TC002");
	}
	
	@Test
	public void unitTest3()
	{
		Report.logTestInitiation("TC002");
		Report.logPass("Step 3: Order count not matching");
		Report.logTestExit("TC002");
	}
}
