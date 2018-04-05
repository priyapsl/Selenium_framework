package com.advantage.restapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class PropertiesFileReader {

	Properties properties = new Properties();
	public static Logger logger = null;

	static {
		logger = Logger.getLogger(PropertiesFileReader.class);
		PropertyConfigurator.configure("log4j.properties");
	}

	public void loadPropertyFile(String file) throws FileNotFoundException,
			IOException {
		properties.load(new FileInputStream(file));
	}

	public String getPropertyValue(String property) {
		return properties.getProperty(property);
	}

}
