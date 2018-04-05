package com.advantage.restapi;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.advantage.datastructures.*;
import com.advantage.datastructures.testLink.TestDataDetails;
import com.advantage.datastructures.testLink.TestStepDetails;
import com.advantage.fileutils.CSV;
import com.advantage.framework.TestTemplate;
import com.advantage.reporting.Logs;

import java.util.ArrayList;
import java.util.List;
public class DataProviderClass extends TestTemplate {
	
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
		
	}

	@DataProvider(name = "JSON")
	public static Object[][] getTC01Data(){
		String sTestDataFilePath;		
		sTestDataFilePath = ConfigInfo.getTestDataPath() + "\\TC_JSON.csv";		
		System.out.println(sTestDataFilePath);
		Object[][] testngDataObject = null;
		
		String sStepNo=null, sURL=null, sTestStepData=null, 
				sExpResponseCode=null, sExpContentType=null, 
				sExpTestData=null, sOperation=null;
		String sEmailTag=null, sFirstNameTag=null, sIdTag=null, sLastNameTag=null;
		
		CSV csv;
		CSV csv1;
		try
		{
			csv = new CSV(sTestDataFilePath, ',');
			String[] fileHeader = csv.getHeader();
			int iRowCount = csv.getRowCount();
			int iColCount = fileHeader.length;		
			
			csv1 = new CSV(ConfigInfo.getTestDataPath() + "\\TC_JSON_TestData.csv", ',');
			String[] fileHeader1 = csv1.getHeader();
			int iRowCount1 = csv1.getRowCount();
			int iColCount1 = fileHeader1.length;
						
			// Initialize array which will store the test data			
			testngDataObject = new Object[1][2];
			
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < 1; i++)
			{
			
				// Construct the xpath to the specific test case
					
					List<TestStepDetails> testStepDtl = new ArrayList<TestStepDetails>();
					String[][] dataexcel= csv.getCSVData(iRowCount, iColCount);					
					
					for(int j=1;j<iRowCount+1;j++){
						for( int k=0;k<iColCount;k++) {					
							
							if(dataexcel[0][k].equals("STEP_NO")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sStepNo = dataexcel[j][k];
									
								}
																	
							}
							else if(dataexcel[0][k].equals("URL")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sURL = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("TEST_STEP_DATA")){
								if(dataexcel[j][k]=="" || dataexcel[j][k]==null){
									sTestStepData = null;
								}
								else {
									sTestStepData = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_RESPONSE_CODE")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpResponseCode = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_CONTENT_TYPE")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpContentType = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_TEST_DATA")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpTestData = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("OPERATION")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sOperation = dataexcel[j][k];
									
								}
							}
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}								
										
						testStepDtl.add(new TestStepDetails(sStepNo, sURL, sTestStepData, sExpResponseCode, sExpContentType, sExpTestData, sOperation));							
					}	
					
					// Put in the object array for testNG
					testngDataObject[i][0] = testStepDtl;
					
					List<TestDataDetails> testDataDtl = new ArrayList<TestDataDetails>();
					String[][] datacsv1= csv1.getCSVData(iRowCount1, iColCount1);					
					
					for(int j=1;j<iRowCount1+1;j++){
						for( int k=0;k<iColCount1;k++) {					
							
							if(datacsv1[0][k].equals("tag=email")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sEmailTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
																	
							}
							else if(datacsv1[0][k].equals("tag=firstName")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sFirstNameTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							else if(datacsv1[0][k].equals("tag=id")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sIdTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							else if(datacsv1[0][k].equals("tag=lastName")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sLastNameTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}								
										
						testDataDtl.add(new TestDataDetails(sEmailTag, sFirstNameTag, sIdTag, sLastNameTag));							
					}	
					
					// Put in the object array for testNG				
					testngDataObject[i][1] = testDataDtl;	
					System.out.println(testDataDtl);
				}
			//System.out.println(testngDataObject);
		}
		
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}			
		
		return testngDataObject;	
	}
	
	
	@DataProvider(name = "XML")
	public static Object[][] getDataForXML(){
		String sTestDataFilePath;		
		sTestDataFilePath = ConfigInfo.getTestDataPath() + "\\TC_XML.csv";		
		System.out.println(sTestDataFilePath);
		Object[][] testngDataObject = null;
		
		String sStepNo=null, sURL=null, sTestStepData=null, 
				sExpResponseCode=null, sExpContentType=null, 
				sExpTestData=null, sOperation=null;
		String sEmailTag=null, sFirstNameTag=null, sIdTag=null, sLastNameTag=null;
		
		CSV csv;
		CSV csv1;
		try
		{
			csv = new CSV(sTestDataFilePath, ',');
			String[] fileHeader = csv.getHeader();
			int iRowCount = csv.getRowCount();
			int iColCount = fileHeader.length;		
			
			csv1 = new CSV(ConfigInfo.getTestDataPath() + "\\TC_XML_TestData.csv", ',');
			String[] fileHeader1 = csv1.getHeader();
			int iRowCount1 = csv1.getRowCount();
			int iColCount1 = fileHeader1.length;
						
			// Initialize array which will store the test data			
			testngDataObject = new Object[1][2];
			
			// Loop through all the test cases and load the data into the array
			for (int i = 0; i < 1; i++)
			{
			
				// Construct the xpath to the specific test case
					
					List<TestStepDetails> testStepDtl = new ArrayList<TestStepDetails>();
					String[][] dataexcel= csv.getCSVData(iRowCount, iColCount);					
					
					for(int j=1;j<iRowCount+1;j++){
						for( int k=0;k<iColCount;k++) {					
							
							if(dataexcel[0][k].equals("STEP_NO")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sStepNo = dataexcel[j][k];
									
								}
																	
							}
							else if(dataexcel[0][k].equals("URL")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sURL = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("TEST_STEP_DATA")){
								if(dataexcel[j][k]=="" || dataexcel[j][k]==null){
									sTestStepData = null;
								}
								else {
									sTestStepData = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_RESPONSE_CODE")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpResponseCode = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_CONTENT_TYPE")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpContentType = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("EXP_TEST_DATA")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sExpTestData = dataexcel[j][k];
									
								}
							}
							else if(dataexcel[0][k].equals("OPERATION")){
								if(dataexcel[j][k].equals("")){
									continue;
								}
								else {
									sOperation = dataexcel[j][k];
									
								}
							}
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}								
										
						testStepDtl.add(new TestStepDetails(sStepNo, sURL, sTestStepData, sExpResponseCode, sExpContentType, sExpTestData, sOperation));							
					}	
					
					// Put in the object array for testNG
					testngDataObject[i][0] = testStepDtl;
					
					List<TestDataDetails> testDataDtl = new ArrayList<TestDataDetails>();
					String[][] datacsv1= csv1.getCSVData(iRowCount1, iColCount1);					
					
					for(int j=1;j<iRowCount1+1;j++){
						for( int k=0;k<iColCount1;k++) {					
							
							if(datacsv1[0][k].equals("tag=email")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sEmailTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
																	
							}
							else if(datacsv1[0][k].equals("tag=firstName")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sFirstNameTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							else if(datacsv1[0][k].equals("tag=id")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sIdTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							else if(datacsv1[0][k].equals("tag=lastName")){
								if(datacsv1[j][k].equals("")){
									continue;
								}
								else {
									sLastNameTag = datacsv1[0][k]+"&"+datacsv1[j][k];
									
								}
							}
							
							else {
								Logs.logWarning("Invalid excel column name");
							}						
						}								
										
						testDataDtl.add(new TestDataDetails(sEmailTag, sFirstNameTag, sIdTag, sLastNameTag));							
					}	
					
					// Put in the object array for testNG				
					testngDataObject[i][1] = testDataDtl;	
					System.out.println(testDataDtl);
				}
			//System.out.println(testngDataObject);
		}
		
		catch (Exception ex)
		{
			Logs.logError("Unable to read test data", ex);
			browser.quitBrowser();
			System.exit(0);
		}			
		
		return testngDataObject;	
	}
	
}
