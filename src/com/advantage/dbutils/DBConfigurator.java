package com.advantage.dbutils;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class read DB_connector_info which has all details about Database providers and its related JDBC classes
 * @author Administrator
 *
 */
public class DBConfigurator {

	private static final String DB_CONNECTOR_INFO_FILE = "db_connector_info.xml";	

	private static final String CONNECTOR = "connector";
	private static final String PROVIDER = "provider";
	private static final String MNEMONIC = "mnemonic";
	private static final String JDBC_DRIVER_CLASS = "jdbc_driver_class";
	private static final String BASE_URL = "base_url";
	private static final String PORT_SEPERATOR = "port_seperator";
	private static final String DB_SEPERATOR = "db_seperator";

	private static Map<String, JDBCDriver> driverMap;

	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String DATABASE_NAME = "databaseName";
	public static final String INTEGRATED_SECURITY = "integratedSecurity";

	/**
	 * Loads the required JDBC Driver
	 * 
	 * @param mnemonic
	 *            key string representing JDBC provider
	 * @throws ClassNotFoundException
	 *             when required driver class is not present in a class path.
	 */
	public static void loadDriver(String mnemonic) throws ClassNotFoundException
	{
		System.out.println(mnemonic);
		JDBCDriver driverInfo = getAvailableDrivers().get(mnemonic);
		System.out.println(driverInfo.getDriverClassName());
		Class.forName(driverInfo.getDriverClassName());
	}

	/**
	 * Enlists all the available JDBC drivers.
	 * 
	 * @return map of all available JDBC drivers.
	 */
	public static Map<String, JDBCDriver> getAvailableDrivers()
	{
		if (driverMap != null)
		{
			return driverMap;
		}

		driverMap = new HashMap<String, JDBCDriver>();
		try
		{
			File fXmlFile = new File(DB_CONNECTOR_INFO_FILE);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName(CONNECTOR);

			for (int temp = 0; temp < nList.getLength(); temp++)
			{

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					JDBCDriver driverInfo = new JDBCDriver();
					Element eElement = (Element) nNode;
					String providerName = eElement.getElementsByTagName(PROVIDER).item(0).getTextContent();					
					
					driverInfo.setProviderName(providerName);

					String mnemonic = eElement.getElementsByTagName(MNEMONIC).item(0).getTextContent();
					
					driverInfo.setProviderMnemonic(mnemonic);

					String className = eElement.getElementsByTagName(JDBC_DRIVER_CLASS).item(0)
							.getTextContent();
					
					driverInfo.setDriverClassName(className);

					String baseUrl = eElement.getElementsByTagName(BASE_URL).item(0).getTextContent();
					
					driverInfo.setBaseUrl(baseUrl);

					String portSeperator = eElement.getElementsByTagName(PORT_SEPERATOR).item(0)
							.getTextContent();
					driverInfo.setPortSeperator(portSeperator);

					String dbSeperator = eElement.getElementsByTagName(DB_SEPERATOR).item(0).getTextContent();
					driverInfo.setDbSeperator(dbSeperator);

					driverMap.put(mnemonic, driverInfo);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return driverMap;
	}

	/**
	 * This method generates connection URL 
	 * @param providerMnemonic
	 * @param dbProperties
	 * @return
	 */
	public static String buildConnectionUrl(String providerMnemonic, Map<String, String> dbProperties)
	{

		StringBuffer url = new StringBuffer();
		String server = dbProperties.get(SERVER);
		String port = dbProperties.get(PORT);
		String databaseName = dbProperties.get(DATABASE_NAME);
		String integratedSecurity = dbProperties.get(INTEGRATED_SECURITY);
		JDBCDriver driverInfo = driverMap.get(providerMnemonic);

		System.out.println("Selected DB configuration : " + driverInfo);

		url.append(driverInfo.getBaseUrl()).append(server);

		if (!driverInfo.getPortSeperator().isEmpty())
		{
			url.append(driverInfo.getPortSeperator()).append(port);
		}

		if (!driverInfo.getDbSeperator().isEmpty())
		{
			url.append(driverInfo.getDbSeperator()).append(databaseName);
		}
		else
		{
			url.append(";databaseName=").append(databaseName);
		}

		url.append(";integratedSecurity=").append(integratedSecurity).append(";");

		return url.toString();
	}
}
