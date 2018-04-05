package com.advantage.localeutils;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.advantage.fileutils.VTD_XML;
import com.advantage.reporting.Logs;

/**
 * This class reads locale translation xml file to map translate id between locale.
 * @author Administrator
 *
 */
public class Translations {
	private String sLocale;
	private static String sLocaleFileName;
	private String sXpath;
	private static HashMap<String, String> mapTranslations = new HashMap<String, String>();
	public static String sDefaultXpath = "/root/data/translate";
	public static String sXpathIdAttribute = "id";

	/**
	 * This method read locale and locale file
	 * @param locale
	 * @param localeFileName
	 */
	public Translations(String locale, String localeFileName)
	{
		this.sLocale = locale;
		sLocaleFileName = localeFileName;
	}

	/**
	 * This method read loacale, locale file and xpath
	 * @param locale
	 * @param localeFileName
	 * @param sXpathStart
	 */
	public Translations(String locale, String localeFileName, String sXpathStart)
	{
		this.sLocale = locale;
		sLocaleFileName = localeFileName;
		this.sXpath = sXpathStart;
	}

	/**
	 * Method to get locale
	 * @return
	 */
	public String getLocale()
	{
		return sLocale;
	}

	/**
	 * Method to get locale filename
	 * @return
	 */
	public static String getLocaleFileName()
	{
		return sLocaleFileName;
	}

	/**
	 * Method to set Xpath
	 * @param sXpath
	 */
	public void setXpath(String sXpath)
	{
		this.sXpath = sXpath;
	}

	/**
	 * Method to get Xpath
	 * @return
	 */
	public String getXpath()
	{
		return sXpath;
	}

	/**
	 * Method to read xml file to generate map translations relation between attribute and value
	 * @return
	 */
	public HashMap<String, String> readAddValues()
	{

		String sLocaleFileName = getLocaleFileName();
		VTD_XML xml;
		try
		{
			xml = new VTD_XML(sLocaleFileName);
			String sXpath = getXpath();
			sXpath = ((sXpath == null) || (sXpath.equalsIgnoreCase(""))) ? sDefaultXpath : sXpath;
			int iNodesCount = xml.getNodesCount(sXpath);

			for (int iIndex = 1; iIndex <= iNodesCount; iIndex++)
			{
				String sNodeXpath = sXpath + "[" + iIndex + "]";
				String sAttribute = xml.getAttribute(sNodeXpath, sXpathIdAttribute);
				String sValue = xml.getNodeValue(sNodeXpath, "");
				mapTranslations.put(sAttribute, sValue);
			}
		}
		catch (Exception exception)
		{
			Logs.logException("Exception occured while reading translation XML file " + sLocaleFileName
					+ ". \nException: " + exception.getMessage() + "\nPlease check...", exception);
		}

		return mapTranslations;
	}

	/**
	 * Method to get translation of particular keyname
	 * @param sKeyName
	 * @return
	 */
	public static String getTranslation(String sKeyName)
	{
		String sValue = mapTranslations.get(sKeyName);
		if (sValue == null)
		{
			Logs.logWarning("The tag '" + sKeyName + "' does not exist in locale property file '"
					+ getLocaleFileName() + "'");
			return "";
		}
		else
			return sValue;
	}

	@Test
	public static void unitTest()
	{
		Logs.initializeLoggers();
		Translations translations = new Translations("en", System.getProperty("user.dir") + "\\EN.xml",
				sDefaultXpath);
		translations.readAddValues();
		//System.out.println(translations.getTranslation("AX_LanguageLink"));
		//System.out.println(translations.getTranslation("AX_LanguageLink"));

		Translations translationsFR = new Translations("fr", System.getProperty("user.dir") + "\\FR.xml",
				"/root/data/translate");
		translationsFR.readAddValues();
		//System.out.println(translationsFR.mapTranslations.get("AX_LanguageLink"));
		//System.out.println(translationsFR.getTranslation("AX_LanguageLink"));
	}
}
