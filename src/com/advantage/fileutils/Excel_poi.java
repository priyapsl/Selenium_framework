package com.advantage.fileutils;

import java.io.File;
import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.advantage.reporting.Report;


/**
 * This class is for reading from an Excel file.
 * This class support both xls and xlsx files.
 * 
 */
public class Excel_poi {	
	
	private FileInputStream excelFile;
	static Workbook workbook = null;
	Sheet sheet=null;
	public Excel_poi(String sExcelFile,String sWorkSheet ){
		
		try {
			excelFile = new FileInputStream(new File(sExcelFile));
			if(sExcelFile.toLowerCase().endsWith("xlsx")){
	            workbook = new XSSFWorkbook(excelFile);
	        }else if(sExcelFile.toLowerCase().endsWith("xls")){
	            workbook = new HSSFWorkbook(excelFile);
	        }
	        else {
	        	Report.logWarning("Invalid file");
	        }
			sheet = workbook.getSheet(sWorkSheet);
		} catch (Exception ex ) {
			return;
		}	
		        
	}
	
	/**
	 * Gets all the data from a specific Excel Worksheet
	 * 
	 * @param sExcelFile - Excel file to read from
	 * @param sWorkSheet - Excel Worksheet to read from
	 * @return null if any error
	 */
	/*public ArrayList getXlsxSheetData()
	{
				
		try
		{	
			Row row;
	        Cell cell;
	        	        	
	        // Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        ArrayList xlsxData = new ArrayList();
	        while (rowIterator.hasNext()) 
	        {
	                row = rowIterator.next();
	
	                // For each row, iterate through each columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                
	                while (cellIterator.hasNext()) 
	                {
	                	cell = cellIterator.next();
	                		
		                switch (cell.getCellType()) 
		                {
			
			                case Cell.CELL_TYPE_BOOLEAN:
			                        //System.out.println(cell.getBooleanCellValue());
			                		//xlsxData.add(cell.)
			                        xlsxData.add(cell.getBooleanCellValue());
			                        break;
			
			                case Cell.CELL_TYPE_NUMERIC:
			                        //System.out.println(cell.getNumericCellValue());
			                        xlsxData.add(cell.getNumericCellValue());

			                        break;
			
			                case Cell.CELL_TYPE_STRING:
			                        //System.out.println(cell.getStringCellValue());
			                        xlsxData.add(cell.getStringCellValue());
			                        break;
			
			                case Cell.CELL_TYPE_BLANK:
			                        System.out.println(" ");
			                        break;
			
			                default:
			                        System.out.println(cell);
			
		                }
	                }
	        }
	        return xlsxData;
		}
		catch (Exception ex) 
		{
		       return null;
		}
		
	}*/
	
	
	/**
	 * Gets all the data from a specific Excel Worksheet
	 * 
	 * @param sExcelFile - Excel file to read from
	 * @param sWorkSheet - Excel Worksheet to read from
	 * @return null if any error
	 */
	public String[][] getExcelSheetData()
	{
				
		try
		{	
			
			int rowcount = sheet.getPhysicalNumberOfRows();
			
            Row row1 = sheet.getRow(0);
            int colcount=row1.getPhysicalNumberOfCells();
            
            String[][]s = new String[rowcount][colcount];
            for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row row = sheet.getRow(i);
                for(int j=0; j<row.getPhysicalNumberOfCells();j++)
                {
                    Cell c = sheet.getRow(i).getCell(j);
                    if(c==null){
                    	 c = row.createCell(j);  
                         c.setCellValue("");  
                    }
                    int celltype = c.getCellType();
                   
                    
                    if(celltype==1)
                    {
                        s[i][j] =c.getStringCellValue();               
                    }
                    if(celltype==0)
                    {   
                        s[i][j] =String.valueOf((int)c.getNumericCellValue()); 
                    }
                    if(celltype==4)
                    {   
                        s[i][j] =String.valueOf((boolean)c.getBooleanCellValue()); 
                    }
                    
                    if(celltype==3){
                    	s[i][j] ="";
                    }

                }
            }
            
	        return s;

		}
		catch (Exception ex) 
		{
		       return null;
		}
		
		
	}

	
	/**
	 * Returns the number of rows in sheet
	 * 
	 * @return -1 in case fo error
	 */
	public int getExcelRowCount()
	{
		try
		{
			return sheet.getPhysicalNumberOfRows();
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
	
	
	/**
	 * Returns the number of columns in sheet
	 * 
	 * @return -1 in case of error
	 */
	public int getExcelColumnCount()
	{
		try
		{
			return sheet.getRow(0).getLastCellNum();
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
	
	
	/**
	 * Returns the number of sheets in excel
	 * 
	 * @return -1 in case of error
	 */
	public static int getExcelNumberOfSheets()
	{
		try
		{
			return workbook.getNumberOfSheets();
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
	
	
}