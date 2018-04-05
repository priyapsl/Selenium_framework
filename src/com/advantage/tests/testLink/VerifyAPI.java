package com.advantage.tests.testLink;


import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.advantage.datastructures.testLink.*;
import com.advantage.framework.Framework;
import com.advantage.framework.TestTemplate;
import com.advantage.reporting.Report;

import org.xml.sax.SAXException;

import org.json.JSONException;

import com.advantage.restapi.*;
import com.csvreader.CsvReader;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;


public class VerifyAPI extends TestTemplate {
	
	private static final String VerifyAPI = "runVerifyAPI";
	
	/**i
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
		
	
	/**
	 * Test the functionality
	 * 
	 * 
	 * @param details - Login detail variables
	 * @param lstNewUser - List of new user test case
	 * @throws InterruptedException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws JSONException 
	 */
	
	@Test(dataProvider="XML", dataProviderClass=DataProviderClass.class)
	public static void runVerifyAPI(List<TestStepDetails> testStepDtl, List<TestDataDetails> testDataDtl) throws InterruptedException, ParseException, IOException, ParserConfigurationException, SAXException, JSONException
	{
		/* Test case initiation */
		
		Report.logTitle(Framework.getNewLine() + "Method " + VerifyAPI + " executing ..."
				+ Framework.getNewLine());		
		setScreenshotPreferences();
		CSVFileReader csvFileReader = new CSVFileReader();
		CsvReader csvStepDataReader, csvStepReader = null;
		String[] stepFileColumnNames = null, stepDataColumnNames;
		RestAPI api = new RestAPI();
		Map<String, String> urlParameters = new HashMap<String, String>();
		Map<String, String> ParameterDetails= new HashMap<String, String>();
		
		
		try{
			for(TestStepDetails tc:testStepDtl){	
				
				if(tc.sTestStepData.isEmpty()==false){
					System.out.println("WIth Parameter");
					csvStepDataReader = csvFileReader.loadCsvContent(
							ConfigInfo.getTestDataPath() + "\\ParameterDetails.csv");
					csvStepDataReader.readHeaders();
					stepDataColumnNames = csvStepDataReader.getHeaders();
					
					while (csvStepDataReader.readRecord()) {
						
						// reading records of TC_**_step_data.csv file
						for (int par = 0; par < stepDataColumnNames.length; par++) {
							urlParameters
									.put(stepDataColumnNames[par],
											csvStepDataReader
													.get(stepDataColumnNames[par]));
							
						}
						
						System.out.println(urlParameters.get("email"));
					
						api.RequestURL(tc.sURL, tc.sOperation, tc.sTestStepData, urlParameters);
						boolean bResCode = api.validateResponseCode(tc.sExpResponseCode);
						if(bResCode==true){
							Report.logPass("API generated correct response code as - " + tc.sExpResponseCode);
						}
						else {
							Report.logError("API generated incorrect response code and expected code is " + tc.sExpResponseCode);
						}
						
						boolean bValidateHeaderType = api.validateHeaderType(tc.sExpContentType);
						if(bValidateHeaderType==true){
							Report.logPass("API generated correct content type as - " + tc.sExpContentType);
						}
						else {
							Report.logError("API generated incorrect content type and expected type is " + tc.sExpContentType);
						}
						LABEL:
						for(TestDataDetails td:testDataDtl){
							boolean flag = api.IsValidateData(tc.sExpTestData);
							boolean  bValidated;
							boolean bValidateFirstName = false, bValidateEmail = false, bValidateLastName = false, bValidateId = false;
							
							if(flag){
								boolean isXMLResponseType = tc.sExpContentType.contains("xml");
								if (isXMLResponseType) {
									bValidateFirstName = api.compareXmlOutput(td.sFirstNameTag);
									bValidateEmail = api.compareXmlOutput(td.sEmailTag);
									bValidateLastName = api.compareXmlOutput(td.sLastNameTag);
									bValidateId = api.compareXmlOutput(td.sIdTag);
									
									bValidated = bValidateFirstName==bValidateEmail==bValidateLastName==bValidateId;
									
									if ((bValidateFirstName==true) && (bValidateLastName==true) && (bValidateEmail==true) && (bValidateId==true)) {
										Report.logPass("XML tag verification done successfully and all tags has correct attribute values");
									}
									else{
										Report.logError("XML tag verification failed.");
									}							
									
								}
								else {
									if(!td.sEmailTag.contains(urlParameters.get("email"))){
										continue;
									}
									//bValidateFirstName = api.validateJsonAttribute(td.sFirstNameTag);
									bValidateEmail = api.validateJsonAttribute(td.sEmailTag);
									//bValidateLastName = api.validateJsonAttribute(td.sLastNameTag);
									bValidateId = api.validateJsonAttribute(td.sIdTag);
									
									//bValidated = bValidateFirstName==bValidateEmail==bValidateLastName==bValidateId;
									bValidated =bValidateEmail==bValidateId;
									if ((bValidateEmail==true) && (bValidateId==true)) {
										Report.logPass("JSON verification done successfully and all tags has correct attribute values");
										break LABEL;
									}
									else{
										Report.logError("JSON verification failed.");
									}	
								}
							}
						}
						
				}	
					urlParameters.clear();					
				}
				else{
					System.out.println("WIthout Parameter");
					api.RequestURL(tc.sURL, tc.sOperation, tc.sTestStepData, urlParameters);
					boolean bResCode = api.validateResponseCode(tc.sExpResponseCode);
					if(bResCode==true){
						Report.logPass("API generated correct response code as - " + tc.sExpResponseCode);
					}
					else {
						Report.logError("API generated incorrect response code and expected code is " + tc.sExpResponseCode);
					}
					
					boolean bValidateHeaderType = api.validateHeaderType(tc.sExpContentType);
					if(bValidateHeaderType==true){
						Report.logPass("API generated correct content type as - " + tc.sExpContentType);
					}
					else {
						Report.logError("API generated incorrect content type and expected type is " + tc.sExpContentType);
					}
					LABEL:
					for(TestDataDetails td:testDataDtl){
						boolean flag = api.IsValidateData(tc.sExpTestData);
						boolean  bValidated;
						boolean bValidateFirstName = false, bValidateEmail = false, bValidateLastName = false, bValidateId = false;
						if(flag){
							boolean isXMLResponseType = tc.sExpContentType.contains("xml");
							if (isXMLResponseType) {
								bValidateFirstName = api.compareXmlOutput(td.sFirstNameTag);
								bValidateEmail = api.compareXmlOutput(td.sEmailTag);
								bValidateLastName = api.compareXmlOutput(td.sLastNameTag);
								bValidateId = api.compareXmlOutput(td.sIdTag);
								
								bValidated = bValidateFirstName==bValidateEmail==bValidateLastName==bValidateId;
								
								if (bValidated) {
									Report.logPass("XML tag verification done successfully and all tags has correct attribute values");
								}
								else{
									Report.logError("XML tag verification failed.");
								}							
								
							}
							else {
								//bValidateFirstName = api.validateJsonAttribute(td.sFirstNameTag);
								//bValidateEmail = api.validateJsonAttribute(td.sEmailTag);
								//bValidateLastName = api.validateJsonAttribute(td.sLastNameTag);
								bValidateId = api.validateJsonAttribute(td.sIdTag);
								
								//bValidated = bValidateFirstName==bValidateEmail==bValidateLastName==bValidateId;
								bValidated =bValidateEmail==bValidateId;
								if (bValidateId) {
									Report.logPass("JSON verification done successfully and all tags has correct attribute values");
									break LABEL;
								}
								else{
									Report.logError("JSON verification failed.");
								}	
							}
						}
					}
					
				}
				
			}
		}	
		catch(RuntimeException ex){
			Report.logError("Test Case failed.", ex);
		}		
		
		
	}
	
	
	
	
}
