package com.advantage.fileutils;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * This class is for reading from an Excel file.
 * This class only support xls file.
 */
public class Excel {
	private File inputWorkbook;
	private static Workbook w;
	private static Sheet sheet;

	public Excel(String sExcelFile, String sWorkSheet)
	{
		try
		{
			inputWorkbook = new File(sExcelFile);
			w = Workbook.getWorkbook(inputWorkbook);
			sheet = w.getSheet(sWorkSheet);			
		}
		catch (Exception ex)
		{
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
	public String[][] getSheetData()
	{
		try
		{
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			String[][] data = new String[rows][cols];

			for (int i = 0; i < cols; i++)
			{
				for (int j = 0; j < rows; j++)
				{
					Cell cell = sheet.getCell(i, j);
					data[j][i] = cell.getContents().toString();
				}
			}

			return data;
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
	public int getRowCount()
	{
		try
		{
			return sheet.getRows();
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
	public int getColumnCount()
	{
		try
		{
			return sheet.getColumns();
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
	public static int getNumberOfSheets()
	{
		try
		{
			return w.getNumberOfSheets();
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
}