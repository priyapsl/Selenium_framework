package com.advantage.restapi;


import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileConversionUtil {
	
	public FileConversionUtil(){
		super();
	}
	
	public String jsonToxml(String responseString) throws JSONException, IOException{
		
		JSONObject jsonObj = new JSONObject(responseString);
		
		String xmlData = XML.toString(jsonObj);
		FileUtils.writeStringToFile(new File("\\text.xml"), xmlData);
		return xmlData;		
	}
	
	public String jsonToCSVFormat(String responseString) throws IOException, JSONException{
		System.out.println(responseString);
		JSONObject output = new JSONObject(responseString);
		 String[] Names=output.getNames(output);
		 JSONArray docs=new JSONArray(responseString);
		
		 //JSONArray docs =output.getJSONArray(responseString);		 
		 
		 String csv = CDL.toString(docs);
		 FileUtils.writeStringToFile(new File("\\text.csv"), csv);
		return csv;
	}
	public void jsonTocsv(String responseString) throws JSONException{
		
		JSONObject jsonObj = new JSONObject(responseString);
	//	JSONArray arr = jsonObj.getJSONArray(jsonObj.get("dataset").toString());
		JsonElement jelement = new JsonParser().parse(responseString);
		 printJsonRecursive(jelement);	
	}
	
	public static void printJsonRecursive(JsonElement jelement){


        if(jelement.isJsonPrimitive()){

            System.out.println(jelement.getAsString());
            return;
        }
        if(jelement.isJsonArray()){

            JsonArray jarray= jelement.getAsJsonArray();
            for(int i=0;i<jarray.size();i++){
                JsonElement element= jarray.get(i);
                printJsonRecursive(element);
            }
            return;

        }
        JsonObject  jobject= jelement.getAsJsonObject();

        Set<Entry<String, JsonElement>> set= jobject.entrySet();

        for (Entry<String, JsonElement> s : set) {

            printJsonRecursive(s.getValue());


        }

    }

}
