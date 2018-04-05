package com.advantage.fileutils;

import java.io.FileReader;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVReader;

/**
 * This class is for reading from a CSV file.
 * 
 * 
 */
public class CSV {	
    CSVReader csvreader;
    CSVReader csvreader1;
    String[] header = null;
    String[] row = null;
    int columnCount;
    int rowcount; 
    
    
    public CSV (String sStrFile, char Delimiter){  
    
	    try {
	    	csvreader= new CSVReader(new FileReader(sStrFile), Delimiter);
	    	csvreader1= new CSVReader(new FileReader(sStrFile), Delimiter);
		} catch (Exception ex) {
			return;
		}
    }
    
    /**
	 * Get CSV file Header (Column Names)
	 *  
	 * @return Array of String 
	 */
    public String[] getHeader(){    	
		try {
			header = this.csvreader.readNext();
			if (header != null) {          
		    	   columnCount = header.length;     	   
	    	}
			
		}
        catch (Exception e) {
            e.printStackTrace();
        }        	
    	return  header;
    }
    
    /**
   	 * Get CSV file Row Count 
   	 *  
   	 * @return Count Of Rows
   	 */
    public int getRowCount(){
    	
    	rowcount = 0;

            try {
				while (csvreader.readNext() != null){
					rowcount++;					
				}
				
            }
            catch (Exception e) {
                e.printStackTrace();
            }
           
    	return rowcount;
    }
    
    /**
   	 * Get/Read CSV File data 
   	 *  
   	 * @return 2-dimensional Array of String
   	 */
    
    public String[][] getCSVData(int iRowCount, int iColCount){
    	
    	String[][]s = new String[iRowCount+1][iColCount+1];
    	String[] row;
    	int r=0;
    	int c =0;
    	try {
    		
			while((row = csvreader1.readNext()) != null) {
				for(String token : row)
                {
					s[r][c]= token;	                   
                    c++;
                }
				c=0;
				r++;
			}
    	}
        catch (Exception e) {
            e.printStackTrace();
        }    	
		return s;
    }  
    
    /**
   	 * Get/Read CSV File data 
   	 *  
   	 * @return CSV data in the form of arraylist
   	 */
    
    public ArrayList getcsv(){
    	//String[] header = getHeader();
    	ArrayList csvDtl = new ArrayList<String>();
        try
        {
            //Get the CSVReader instance with specifying the delimiter to be used
            
            String [] nextLine;
           
            //Read one line at a time
            while ((nextLine = csvreader.readNext()) != null)
            {
                for(String token : nextLine)
                {
                	
                	csvDtl.add(token);
                    //Print all tokens
                    
                }
            }
            System.out.println(csvDtl);
            for(int i=0; i<header.length; i++) {
            	csvDtl.remove(i);            	
            }
            System.out.println(csvDtl);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                csvreader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return csvDtl;
    }
}