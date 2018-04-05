package com.advantage.restapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.advantage.reporting.Report;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;



import org.json.JSONException;
//import com.json.parsers.JSONParser;
import org.json.simple.parser.JSONParser;
import org.json.simple.*;

//import com.json.parsers.JsonParserFactory;
import com.advantage.restapi.*;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;

/**
 * This class is for the Login page
 */

public class RestAPI {
	FileConversionUtil fileFormat = new FileConversionUtil();
	Response response;	
	String base_url = "";
	String expectedResponseHeaderType = "";
	Map<String, String> urlParameters = new HashMap<String, String>();
	String responseString;
	
	public RestAPI(){
		super();
	}
	
	public void RequestURL(String sURL, String sOperation, String sTestStepData, Map<String, String> urlParameters){
		boolean bNoTestStepData = false;
		if(sTestStepData==null||sTestStepData==""){
			bNoTestStepData =true;
		}
		//boolean bIsParameterised = sTestStepData.isEmpty();
		boolean isGetOperation = sOperation.equalsIgnoreCase("get");
		boolean isPostOperation = sOperation.equalsIgnoreCase("post");
		boolean isDeleteOperation = sOperation.equalsIgnoreCase("delete");
		boolean isPutOperation = sOperation.equalsIgnoreCase("put");
		
		if(bNoTestStepData){
			if(isGetOperation){
				response = RestAssured.get(sURL);
			}
			else if(isPostOperation){
				response = RestAssured.post(sURL);
			}
			else if(isDeleteOperation){
				response = RestAssured.delete(sURL);
			}
			else if(isPutOperation){
				response = RestAssured.put(sURL);
			}
			else {
				Report.logInfo("Invalid Operation");
			}
		}
		else {			
			//RequestSpecification req = RestAssured.given().log().all().parameters(urlParameters);
			//RequestSpecification req = RestAssured.given().parameters(urlParameters);
			RequestSpecification req = RestAssured.given().parameters(urlParameters);
			
			
			if(isGetOperation){				
				response = req.get(sURL);
			}
			if(isPostOperation){
				response = req.post(sURL);
			}
			if(isDeleteOperation){
				response = req.delete(sURL);
			}
			if(isPutOperation){
				response = req.put(sURL);
			}
		}
	}
	
	public boolean validateResponseCode(String sresponseCode) throws
		NumberFormatException, IOException {
		
		Integer iActualResponseCode = response.getStatusCode();
		if(Integer.parseInt(sresponseCode)==(iActualResponseCode)){
			return true;			
		}
		return false;		
	}
	
	
	public boolean validateHeaderType(String expectedResponseHeaderType) throws IOException {
		String actualResponseHeaderType = response.getHeader("Content-Type");
		
		if(actualResponseHeaderType.equalsIgnoreCase(expectedResponseHeaderType)){
			return true;
		}
		return false;

	}
	
	public boolean IsValidateData(String sExpTestData){
		
		if(sExpTestData==null||sExpTestData==""){
			return false;
		}		
			return true;		
	}
	
	
	
	public String[] validateData(String sTag) throws SAXException, IOException, ParserConfigurationException{
		boolean isXMLResponseType = expectedResponseHeaderType.contains("xml");
		
		String tagToCheck = null;
		String attributeToCheck = null;
		
		String[] splitColumnStr = sTag.split("&");
		attributeToCheck = splitColumnStr[1];
		String[] splitStr = splitColumnStr[0].split("=");
		tagToCheck = splitStr[1];
		
		return new String[]  {tagToCheck, attributeToCheck};		
	}
	
public boolean validateJsonAttribute(String sTag) throws SAXException, IOException, ParserConfigurationException, JSONException {
		
		String[] arr1 = validateData(sTag);
		
		String sTagName = arr1[0];
		String sAttributeValue = arr1[1];
		
		responseString = response.asString();
		/*String str1 = fileFormat.jsonToCSVFormat(responseString);
		System.out.println(str1);*/
		/*String str2 = fileFormat.jsonToxml(responseString);
		System.out.println(str2);*/
		JsonParserFactory parserFactory = JsonParserFactory.getInstance();
		com.codesnippets4all.json.parsers.JSONParser parser = parserFactory.newJsonParser();
		Map jsonData = parser.parseJson(responseString);
		System.out.println(jsonData);
		Set arr=jsonData.keySet();		
		 String value=(String)jsonData.get(sTagName); 
		
		 if(value.equalsIgnoreCase(sAttributeValue)){
			 return true;
		 }
		return false;
	}
	
	public boolean compareXmlOutput(String sTag) throws SAXException,
			IOException, ParserConfigurationException {
		String[] arr1 = validateData(sTag);
		
		String sTagName = arr1[0];
		String sAttributeValue = arr1[1];
		boolean result = false;
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		builder = builderFactory.newDocumentBuilder();
		responseString = response.asString();
		InputSource is = new InputSource(new StringReader(responseString));

		document = builder.parse(is);

		// Getting all elements from xml
		NodeList nodeList = document.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			if (element.getNodeName().equals(sTagName)) {
				NodeList nl = document.getElementsByTagName(sTagName);
				for (int n = 0; n < nl.getLength(); n++) {
					if (nl.item(n).getTextContent().equals(sAttributeValue)) {
						result = true;
						break;
					}
				}
			}
			
		}
		return result;
	}
}
