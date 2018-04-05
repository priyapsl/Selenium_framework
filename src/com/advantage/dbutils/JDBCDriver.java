package com.advantage.dbutils;

/**
 * This class read JDBC driver related parameters
 * @author Administrator
 *
 */

public class JDBCDriver {

	private String providerName;
	private String providerMnemonic;
	private String dataSourceClass;
	private String driverClassName;
	private String portSeperator;
	private String dbSeperator;
	private String baseUrl;

	/**
	 * Method to get Provider Name
	 * @return providerName
	 */
	public String getProviderName()
	{
		return providerName;
	}

	/**
	 * Method to set provider name
	 * @param providerName
	 */
	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	/**
	 * Method to get Provider Mnemonic
	 * @return providerMnemonic
	 */
	public String getProviderMnemonic()
	{
		return providerMnemonic;
	}

	/**
	 * Method to set provider Mnemonic
	 * @param providerMnemonic
	 */
	public void setProviderMnemonic(String providerMnemonic)
	{
		this.providerMnemonic = providerMnemonic;
	}

	/**
	 * Method to get DataSource class
	 * @return dataSourceClass
	 */
	public String getDataSourceClass()
	{
		return dataSourceClass;
	}

	/**
	 * Method to set datasource class
	 * @param dataSourceClass
	 */
	public void setDataSourceClass(String dataSourceClass)
	{
		this.dataSourceClass = dataSourceClass;
	}

	/** Method to get driver class name
	 * 
	 * @return driverClassName
	 */
	public String getDriverClassName()
	{
		return driverClassName;
	}

	/**
	 * Method to set driver Clas sName
	 * @param driverClassName
	 */
	public void setDriverClassName(String driverClassName)
	{
		this.driverClassName = driverClassName;
	}

	/** Method to get port seperator
	 * 
	 * @return portSeperator
	 */
	public String getPortSeperator()
	{
		return portSeperator;
	}

	/**
	 * Method to set port seperator
	 * @param portSeperator
	 */
	public void setPortSeperator(String portSeperator)
	{
		this.portSeperator = portSeperator;
	}

	/** Method to get DB seperator
	 * 
	 * @return dbSeperator
	 */
	public String getDbSeperator()
	{
		return dbSeperator;
	}

	/**
	 * Method to set DB seperatorS
	 * @param dbSeperator
	 */
	public void setDbSeperator(String dbSeperator)
	{
		this.dbSeperator = dbSeperator;
	}

	/**
	 * Method to get Base URL
	 * @return baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * Method to set Base URL
	 * @param baseUrl
	 */
	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

}
