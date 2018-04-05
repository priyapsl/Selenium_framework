package com.advantage.restapi;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.csvreader.CsvReader;

public class CSVFileReader {

	public CsvReader loadCsvContent(String csvFile) throws FileNotFoundException{	 
		return new CsvReader(new FileReader(csvFile));
	}
}
