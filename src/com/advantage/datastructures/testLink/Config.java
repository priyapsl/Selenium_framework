package com.advantage.datastructures.testLink;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.advantage.fileutils.VTD_XML;
import com.advantage.reporting.Logs;

/**
 * This class read config file 
 * @author Administrator
 *
 */
public class Config {
	private String sConfigFilePath;
	private String sBrowserType;
	private String sAppURL;
	private String sDBProvider;
	private String sDBServer;
	private Integer iPort;
	private String sDBName;
	private String sDBUserName;
	private String sDBPassword;
	private String sResultPath;
	private String sLanguage;
	private String sSMTPSever;
	private String sFromEmailId;
	private String sToEmailId;
	private String sSubject;
	private String sMessage;

	private Integer iPageTimeoutInSeconds;
	private Integer iElementTimeoutInSeconds;
	private Integer iPollIntervalInMilliSeconds;
	private boolean bIsSendEmails;
	private boolean bIsCaptureScreenshots;
	private String sTestDataPath;
	private String sSMTPPort;
	
	private String sGridHubURL;
	private String sGridPlatform;
	private String sGridVersion;
	private boolean bIsAPITesting;

	private HashMap<String, String> mapCustomConfig = new HashMap<String, String>();

	/**
	 * Method for config path
	 * @param sConfigPath
	 */	
	public Config(String sConfigPath)
	{
		sConfigFilePath = sConfigPath;
	}

	/**
	 * Method to get Config Filepath
	 * @return
	 */
	public String getsConfigFilePath()
	{
		return sConfigFilePath;
	}

	/**
	 * @return the sBrowserType
	 */
	public String getsBrowserType()
	{
		return sBrowserType;
	}

	/**
	 * @param sBrowserType the sBrowserType to set
	 */
	public void setsBrowserType(String sBrowserType)
	{
		this.sBrowserType = sBrowserType;
	}

	/**
	 * @return the sAppURL
	 */
	public String getsAppURL()
	{
		return sAppURL;
	}
	
	/**
	 * @return the sDBProvider
	 */
	public String getsDBProvider()
	{
		return sDBProvider;
	}

	/**
	 * @return the sDBServer
	 */
	public String getsDBServer()
	{
		return sDBServer;
	}

	/**
	 * @return the iPort
	 */
	public Integer getiPort()
	{
		return iPort;
	}
	/**
	 * @return the sDBName
	 */
	public String getsDBName()
	{
		return sDBName;
	}

	/**
	 * @return the sDBUserName
	 */
	public String getsDBUserName()
	{
		return sDBUserName;
	}

	/**
	 * @return the sDBPassword
	 */
	public String getsDBPassword()
	{
		return sDBPassword;
	}

	/**
	 * @return the sResultPath
	 */
	public String[] getsResultPath()
	{
		String[] path = sResultPath.split("//");
		return path;
	}

	/**
	 * @return the iPageTimeoutInSeconds
	 */
	public Integer getiPageTimeoutInSeconds()
	{
		return iPageTimeoutInSeconds;
	}

	/**
	 * @return the iElementTimeoutInSeconds
	 */
	public Integer getiElementTimeoutInSeconds()
	{
		return iElementTimeoutInSeconds;
	}

	/**
	 * @return the iPollIntervalInMilliSeconds
	 */
	public Integer getiPollIntervalInMilliSeconds()
	{
		return iPollIntervalInMilliSeconds;
	}

	/**
	 * @return the bIsSendEmails
	 */
	public boolean isbIsSendEmails()
	{
		return bIsSendEmails;
	}

	/**
	 * @return the bIsCaptureScreenshots
	 */
	public boolean isbIsCaptureScreenshots()
	{
		return bIsCaptureScreenshots;
	}

	/**
	 * @return the mapCustomConfig
	 */
	public HashMap<String, String> getMapCustomConfig()
	{
		return mapCustomConfig;
	}

	/**
	 * @return the sTestDataPath
	 */
	public String getTestDataPath()
	{
		return sTestDataPath;
	}

	/**
	 * @param sTestDataPath the sTestDataPath to set
	 */
	public void setTestDataPath(String sTestDataPath)
	{
		this.sTestDataPath = sTestDataPath;
	}

	/**
	 * @return the sGridHubURL
	 */
	public String getsGridHubURL()
	{
		return sGridHubURL;
	}

	/**
	 * @param sGridHubURL the sGridHubURL to set
	 */
	public void setsGridHubURL(String sGridHubURL)
	{
		this.sGridHubURL = sGridHubURL;
	}

	/**
	 * @return the sGridPlatform
	 */
	public String getsGridPlatform()
	{
		return sGridPlatform;
	}

	/**
	 * @param sGridPlatform the sGridPlatform to set
	 */
	public void setsGridPlatform(String sGridPlatform)
	{
		this.sGridPlatform = sGridPlatform;
	}

	/**
	 * @return the sGridVersion
	 */
	public String getsGridVersion()
	{
		return sGridVersion;
	}

	/**
	 * @param sGridVersion the sGridVersion to set
	 */
	public void setsGridVersion(String sGridVersion)
	{
		this.sGridVersion = sGridVersion;
	}
	
	/**
	 * @return the sLanguage
	 */
	public String SetLanguage()
	{
		if(sLanguage.equals("")) {
			sLanguage = "EN";
		}		
		return sLanguage;
	}
	
	/**
	 * @return the sSMTPServer
	 */
	public String getsSMTPSever()
	{
		return sSMTPSever;
	}

	/**
	 * @param sSMTPSever the sSMTPSever to set
	 */
	public void setsSMTPSever(String sSMTPSever)
	{
		this.sSMTPSever = sSMTPSever;
	}
	
	/**
	 * @return the sFromEmailId
	 */
	public String getsFromEmailId()
	{
		return sFromEmailId;
	}

	/**
	 * @param sFromEmailId the sFromEmailId to set
	 */
	public void setsFromEmailId(String sFromEmailId)
	{
		this.sFromEmailId = sFromEmailId;
	}
	
	/**
	 * @return the sToEmailId
	 */
	public String[] getsToEmailId()
	{
		String[] arr = sToEmailId.split(",");
		return arr;
	}

	/**
	 * @param sToEmailId the sToEmailId to set
	 */
	public void setsToEmailId(String sToEmailId)
	{
		this.sToEmailId = sToEmailId;
	}
	
	/**
	 * @return the sSubject
	 */
	public String getsSubject()
	{
		return sSubject;
	}

	/**
	 * @param sSubject the sSubject to set
	 */
	public void setsSubject(String sSubject)
	{
		this.sSubject = sSubject;
	}
	
	/**
	 * @return the sMessage
	 */
	public String getsMessage()
	{
		return sMessage;
	}

	/**
	 * @param sMessage the sMessage to set
	 */
	public void setsMessage(String sMessage)
	{
		this.sMessage = sMessage;
	}
	
	/**
	 * @return the sSMTPPort
	 */
	public String getsSMTPPort()
	{
		return sSMTPPort;
	}

	/**
	 * @param sSMTPPort the sSMTPPort to set
	 */
	public void setsSMTPPort(String sSMTPPort)
	{
		this.sSMTPPort = sSMTPPort;
	}
	
	/**
	 * @return the bIsAPITesting
	 */
	public boolean bIsAPITesting()
	{
		return bIsAPITesting;
	}

	/**
	 * THis method read config file and set values for each node of xml file
	 */
	
	public void initialize()
	{
		String sConfigFilePath = getsConfigFilePath();
		VTD_XML xml;
		String sXpathRoot = "//config/Fixed";
		String sXpathRootCustom = "//config/Custom/*";

		try
		{
			xml = new VTD_XML(sConfigFilePath);
			int iNodesCount = xml.getNodesCount(sXpathRootCustom);
			bIsAPITesting = Boolean.parseBoolean(xml.getNodeValue(sXpathRoot + "/AppConfig/APITesting", ""));
			sBrowserType = xml.getNodeValue(sXpathRoot + "/AppConfig/Browser", "");
			sResultPath = xml.getNodeValue(sXpathRoot + "/AppConfig/ResultsPath", "");
			sAppURL = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/URL", "");
			sDBProvider = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/DBProvider", "");
			sDBServer = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/Server", "");
			iPort = Integer.parseInt(xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/Port", ""));
			sDBName = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/Database", "");
			sDBUserName = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/User", "");
			sDBPassword = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/DB/Password", "");
			sLanguage = xml.getNodeValue(sXpathRoot + "/AppConfig/ENV/Language", "");
			
			sGridHubURL = xml.getNodeValue(sXpathRoot + "/AppConfig/Grid/HubURL", "");
			sGridPlatform = xml.getNodeValue(sXpathRoot 	+ "/AppConfig/Grid/Platform", "");
			sGridVersion = xml.getNodeValue(sXpathRoot + "/AppConfig/Grid/Version", "");
			
			sSMTPSever = xml.getNodeValue(sXpathRoot + "/AppConfig/SMTPServer", "");
			sFromEmailId = xml.getNodeValue(sXpathRoot + "/AppConfig/FromEmailAddress", "");
			sToEmailId = xml.getNodeValue(sXpathRoot + "/AppConfig/ToEmailAddress", "");
			sSubject = xml.getNodeValue(sXpathRoot + "/AppConfig/Subject", "");
			sMessage = xml.getNodeValue(sXpathRoot + "/AppConfig/Message", "");
			sSMTPPort = xml.getNodeValue(sXpathRoot + "/AppConfig/SMTPPort", "");
			
			iPageTimeoutInSeconds = Integer.parseInt(xml.getNodeValue(sXpathRoot
					+ "/FrameworkConfig/PageTimeoutInSeconds", ""));
			iElementTimeoutInSeconds = Integer.parseInt(xml.getNodeValue(sXpathRoot
					+ "/FrameworkConfig/ElementTimeoutInSeconds", ""));
			iPollIntervalInMilliSeconds = Integer.parseInt(xml.getNodeValue(sXpathRoot
					+ "/FrameworkConfig/PollIntervalInMilliSeconds", ""));
			bIsSendEmails = Boolean.parseBoolean(xml.getNodeValue(sXpathRoot + "/FrameworkConfig/SendEmails",
					""));
			bIsCaptureScreenshots = Boolean.parseBoolean(xml.getNodeValue(sXpathRoot
					+ "/FrameworkConfig/CaptureScreenshots", ""));
			
			sTestDataPath = System.getProperty("user.dir") + System.getProperty("file.separator")
					+ xml.getNodeValue(sXpathRoot + "/FrameworkConfig/TestDataPath", "")
					+ System.getProperty("file.separator");

			for (int i = 1; i <= iNodesCount; i++)
			{
				String sName = xml.getNodeName(sXpathRootCustom + "[" + String.valueOf(i) + "]", "");
				String sValue = xml.getNodeValue(sXpathRootCustom + "[" + String.valueOf(i) + "]", "");
				mapCustomConfig.put(sName, sValue);
			}
		}
		catch (Exception exception)
		{
			Logs.logException("Exception occured while opening or reading config XML file " + sConfigFilePath
					+ ". \nException: " + exception.getMessage() + "\nPlease check...", exception);
		}
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bIsAPITesting ? 1231 : 1237);
		result = prime * result + (bIsCaptureScreenshots ? 1231 : 1237);
		result = prime * result + (bIsSendEmails ? 1231 : 1237);
		result = prime
				* result
				+ ((iElementTimeoutInSeconds == null) ? 0
						: iElementTimeoutInSeconds.hashCode());
		result = prime
				* result
				+ ((iPageTimeoutInSeconds == null) ? 0 : iPageTimeoutInSeconds
						.hashCode());
		result = prime
				* result
				+ ((iPollIntervalInMilliSeconds == null) ? 0
						: iPollIntervalInMilliSeconds.hashCode());
		result = prime * result + ((iPort == null) ? 0 : iPort.hashCode());
		result = prime * result
				+ ((mapCustomConfig == null) ? 0 : mapCustomConfig.hashCode());
		result = prime * result + ((sAppURL == null) ? 0 : sAppURL.hashCode());
		result = prime * result
				+ ((sBrowserType == null) ? 0 : sBrowserType.hashCode());
		result = prime * result
				+ ((sConfigFilePath == null) ? 0 : sConfigFilePath.hashCode());
		result = prime * result + ((sDBName == null) ? 0 : sDBName.hashCode());
		result = prime * result
				+ ((sDBPassword == null) ? 0 : sDBPassword.hashCode());
		result = prime * result
				+ ((sDBProvider == null) ? 0 : sDBProvider.hashCode());
		result = prime * result
				+ ((sDBServer == null) ? 0 : sDBServer.hashCode());
		result = prime * result
				+ ((sDBUserName == null) ? 0 : sDBUserName.hashCode());
		result = prime * result
				+ ((sFromEmailId == null) ? 0 : sFromEmailId.hashCode());
		result = prime * result
				+ ((sGridHubURL == null) ? 0 : sGridHubURL.hashCode());
		result = prime * result
				+ ((sGridPlatform == null) ? 0 : sGridPlatform.hashCode());
		result = prime * result
				+ ((sGridVersion == null) ? 0 : sGridVersion.hashCode());
		result = prime * result
				+ ((sLanguage == null) ? 0 : sLanguage.hashCode());
		result = prime * result
				+ ((sMessage == null) ? 0 : sMessage.hashCode());
		result = prime * result
				+ ((sResultPath == null) ? 0 : sResultPath.hashCode());
		result = prime * result
				+ ((sSMTPPort == null) ? 0 : sSMTPPort.hashCode());
		result = prime * result
				+ ((sSMTPSever == null) ? 0 : sSMTPSever.hashCode());
		result = prime * result
				+ ((sSubject == null) ? 0 : sSubject.hashCode());
		result = prime * result
				+ ((sTestDataPath == null) ? 0 : sTestDataPath.hashCode());
		result = prime * result
				+ ((sToEmailId == null) ? 0 : sToEmailId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		if (bIsAPITesting != other.bIsAPITesting)
			return false;
		if (bIsCaptureScreenshots != other.bIsCaptureScreenshots)
			return false;
		if (bIsSendEmails != other.bIsSendEmails)
			return false;
		if (iElementTimeoutInSeconds == null) {
			if (other.iElementTimeoutInSeconds != null)
				return false;
		} else if (!iElementTimeoutInSeconds
				.equals(other.iElementTimeoutInSeconds))
			return false;
		if (iPageTimeoutInSeconds == null) {
			if (other.iPageTimeoutInSeconds != null)
				return false;
		} else if (!iPageTimeoutInSeconds.equals(other.iPageTimeoutInSeconds))
			return false;
		if (iPollIntervalInMilliSeconds == null) {
			if (other.iPollIntervalInMilliSeconds != null)
				return false;
		} else if (!iPollIntervalInMilliSeconds
				.equals(other.iPollIntervalInMilliSeconds))
			return false;
		if (iPort == null) {
			if (other.iPort != null)
				return false;
		} else if (!iPort.equals(other.iPort))
			return false;
		if (mapCustomConfig == null) {
			if (other.mapCustomConfig != null)
				return false;
		} else if (!mapCustomConfig.equals(other.mapCustomConfig))
			return false;
		if (sAppURL == null) {
			if (other.sAppURL != null)
				return false;
		} else if (!sAppURL.equals(other.sAppURL))
			return false;
		if (sBrowserType == null) {
			if (other.sBrowserType != null)
				return false;
		} else if (!sBrowserType.equals(other.sBrowserType))
			return false;
		if (sConfigFilePath == null) {
			if (other.sConfigFilePath != null)
				return false;
		} else if (!sConfigFilePath.equals(other.sConfigFilePath))
			return false;
		if (sDBName == null) {
			if (other.sDBName != null)
				return false;
		} else if (!sDBName.equals(other.sDBName))
			return false;
		if (sDBPassword == null) {
			if (other.sDBPassword != null)
				return false;
		} else if (!sDBPassword.equals(other.sDBPassword))
			return false;
		if (sDBProvider == null) {
			if (other.sDBProvider != null)
				return false;
		} else if (!sDBProvider.equals(other.sDBProvider))
			return false;
		if (sDBServer == null) {
			if (other.sDBServer != null)
				return false;
		} else if (!sDBServer.equals(other.sDBServer))
			return false;
		if (sDBUserName == null) {
			if (other.sDBUserName != null)
				return false;
		} else if (!sDBUserName.equals(other.sDBUserName))
			return false;
		if (sFromEmailId == null) {
			if (other.sFromEmailId != null)
				return false;
		} else if (!sFromEmailId.equals(other.sFromEmailId))
			return false;
		if (sGridHubURL == null) {
			if (other.sGridHubURL != null)
				return false;
		} else if (!sGridHubURL.equals(other.sGridHubURL))
			return false;
		if (sGridPlatform == null) {
			if (other.sGridPlatform != null)
				return false;
		} else if (!sGridPlatform.equals(other.sGridPlatform))
			return false;
		if (sGridVersion == null) {
			if (other.sGridVersion != null)
				return false;
		} else if (!sGridVersion.equals(other.sGridVersion))
			return false;
		if (sLanguage == null) {
			if (other.sLanguage != null)
				return false;
		} else if (!sLanguage.equals(other.sLanguage))
			return false;
		if (sMessage == null) {
			if (other.sMessage != null)
				return false;
		} else if (!sMessage.equals(other.sMessage))
			return false;
		if (sResultPath == null) {
			if (other.sResultPath != null)
				return false;
		} else if (!sResultPath.equals(other.sResultPath))
			return false;
		if (sSMTPPort == null) {
			if (other.sSMTPPort != null)
				return false;
		} else if (!sSMTPPort.equals(other.sSMTPPort))
			return false;
		if (sSMTPSever == null) {
			if (other.sSMTPSever != null)
				return false;
		} else if (!sSMTPSever.equals(other.sSMTPSever))
			return false;
		if (sSubject == null) {
			if (other.sSubject != null)
				return false;
		} else if (!sSubject.equals(other.sSubject))
			return false;
		if (sTestDataPath == null) {
			if (other.sTestDataPath != null)
				return false;
		} else if (!sTestDataPath.equals(other.sTestDataPath))
			return false;
		if (sToEmailId == null) {
			if (other.sToEmailId != null)
				return false;
		} else if (!sToEmailId.equals(other.sToEmailId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Config [sConfigFilePath=" + sConfigFilePath + ", sBrowserType="
				+ sBrowserType + ", sAppURL=" + sAppURL + ", sDBProvider="
				+ sDBProvider + ", sDBServer=" + sDBServer + ", iPort=" + iPort
				+ ", sDBName=" + sDBName + ", sDBUserName=" + sDBUserName
				+ ", sDBPassword=" + sDBPassword + ", sResultPath="
				+ sResultPath + ", sLanguage=" + sLanguage + ", sSMTPSever="
				+ sSMTPSever + ", sFromEmailId=" + sFromEmailId
				+ ", sToEmailId=" + sToEmailId + ", sSubject=" + sSubject
				+ ", sMessage=" + sMessage + ", iPageTimeoutInSeconds="
				+ iPageTimeoutInSeconds + ", iElementTimeoutInSeconds="
				+ iElementTimeoutInSeconds + ", iPollIntervalInMilliSeconds="
				+ iPollIntervalInMilliSeconds + ", bIsSendEmails="
				+ bIsSendEmails + ", bIsCaptureScreenshots="
				+ bIsCaptureScreenshots + ", sTestDataPath=" + sTestDataPath
				+ ", sSMTPPort=" + sSMTPPort + ", sGridHubURL=" + sGridHubURL
				+ ", sGridPlatform=" + sGridPlatform + ", sGridVersion="
				+ sGridVersion + ", bIsAPITesting=" + bIsAPITesting
				+ ", mapCustomConfig=" + mapCustomConfig + "]";
	}

	@Test
	public static void unitTest()
	{
		Logs.initializeLoggers();
		System.out.println(System.getProperty("user.dir") + "\\testLink_config.xml");
		Config config = new Config(System.getProperty("user.dir") + "\\testLink_config.xml");
		config.initialize();
		System.out.println(config.toString());
	}



	
}
