package com.advantage.framework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.advantage.genericexceptions.GenericUnexpectedException;
import com.advantage.reporting.Logs;

/**
 * Class for conversion functions.
 */
public class Conversion {
	/**
	 * Returns converts a string to a boolean value<BR>
	 * <BR>
	 * Note: Uses Boolean.parseBoolean and adds "1" is true & null as false
	 * 
	 * @param sValue - String to convert
	 * @return true if ("1" or "true") else false
	 */
	public static boolean bParseBoolean(String sValue)
	{
		if (sValue == null)
			return false;

		if (sValue.equals("1"))
			return true;

		return Boolean.parseBoolean(sValue);
	}

	/**
	 * Returns the date as a string in specific format
	 * 
	 * @param value - Date to covert to string
	 * @param sToFormat - Format to convert to (ex. "yyyy-MM-dd")
	 * @return null if exception occurs else String
	 */
	public static String sConvertDate(Date value, String sToFormat)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(sToFormat);
			String sConverted = new String(dateformat.format(value));
			return sConverted;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns the string as an integer.<BR>
	 * <BR>
	 * Note: -1 is returned for strings that cannot be converted.
	 * 
	 * @param sValue - String to convert
	 * @return -1 if string cannot be converted else integer value
	 */
	public static int nParseInt(String sValue)
	{
		return nParseInt(sValue, -1);
	}

	/**
	 * Returns the string as an integer.
	 * 
	 * @param sValue - String to convert
	 * @param nDefault - Default value to return if conversion fails
	 * @return nDefault if string cannot be converted else integer value
	 */
	public static int nParseInt(String sValue, int nDefault)
	{
		try
		{
			return Integer.parseInt(sValue);
		}
		catch (Exception ex)
		{
			return nDefault;
		}
	}

	/**
	 * If sValue is null, then it returns the empty string else sValue
	 * 
	 * @param sValue
	 * @return sValue or empty string
	 */
	public static String sEmptyInsteadOfNull(String sValue)
	{
		if (sValue == null)
			return "";
		else
			return sValue;
	}

	/**
	 * Converts a String to a Date
	 * 
	 * @param sValue - String to convert
	 * @param sToFormat - Format to convert to (ex. "yyyy-MM-dd")
	 * @return null if exception else Date
	 */
	public static Date toDate(String sValue, String sToFormat)
	{
		try
		{
			DateFormat formatter = new SimpleDateFormat(sToFormat);
			Date date = (Date) formatter.parse(sValue);
			return date;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Adds/Subtracts specified days to the given Date and returns result as a String formatted as specified<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use the method <B>toDate</B> to convert a String if necessary<BR>
	 * 2) Can be used to convert a date object to a specific string format<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) addDays(new Date(), 1, "MM-dd-yyyy") => Tomorrow in format MM-dd-yyyy<BR>
	 * 2) addDays(toDate("2012-09-11","yyyy-MM-dd") , 1, "MM-dd-yyyy") => "09-12-2012"<BR>
	 * 3) addDays(new Date(), 0, "MM-dd-yyyy") => Today in format MM-dd-yyyy<BR>
	 * 4) addDays(toDate("2012-09-11","yyyy-MM-dd") , 0, "MM-dd-yyyy") => "09-11-2012"<BR>
	 * 
	 * @param workingDate - Date object to add/subtract days to
	 * @param nAmount - days to add (positive) /subtract (negative)
	 * @param sOutputFormat - Format to convert to (ex. "yyyy-MM-dd")
	 * @return <B>null</B> if exception else String formated based on Date object
	 */
	public static String addDays(Date workingDate, int nAmount, String sOutputFormat)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(sOutputFormat);
			Date convert = DateUtils.addDays(workingDate, nAmount);
			return dateFormat.format(convert);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns the string as a float.<BR>
	 * <BR>
	 * Note: Float.valueOf("-1") is returned for strings that cannot be converted.
	 * 
	 * @param sValue - String to convert
	 * @return Float.valueOf("-1") if string cannot be converted else float value
	 */
	public static float parseFloat(String sValue)
	{
		float fDefault = Float.valueOf("-1");
		return parseFloat(sValue, fDefault);
	}

	/**
	 * Returns the string as a float.
	 * 
	 * @param sValue - String to convert
	 * @param fDefault - Default value to return if conversion fails
	 * @return fDefault if string cannot be converted else float value
	 */
	public static float parseFloat(String sValue, float fDefault)
	{
		try
		{
			return Float.parseFloat(sValue);
		}
		catch (Exception ex)
		{
			return fDefault;
		}
	}

	/**
	 * Method used to round fractional number to specified number of decimals.<BR>
	 * 
	 * @param dValue
	 *            - Fractional number.
	 * @param iNumberOfDecimals
	 *            - Number of decimal places to keep after rounding up.
	 * @return - double
	 */
	public static double roundToDecimals(double dValue, int iNumberOfDecimals)
	{
		double dMultiplyDivideFactor = Math.pow(10, iNumberOfDecimals);
		return Math.round(dValue * dMultiplyDivideFactor) / dMultiplyDivideFactor;
	}

	/**
	 * Method rounds a given fractional number in String format to specified
	 * number of decimals and again returns that rounded number as a String.<BR>
	 * 
	 * @param sValue
	 *            - Fractional number in String format.
	 * @param iNumberOfDecimals
	 *            - Number of decimal places to keep after rounding up.
	 * @return - String - If sValue is null or "", then returns "".
	 */
	public static String roundToDecimals(String sValue, int iNumberOfDecimals)
	{
		if (sValue == null || sValue.equals(""))
			return "";

		Double dValue, dRoundedValue;
		try
		{
			dValue = Double.parseDouble(sValue);
			dRoundedValue = roundToDecimals(dValue, iNumberOfDecimals);
		}
		catch (Exception e)
		{
			String sErrorMsg = "Exception occurred while converting String '" + sValue + "' to double.";
			Logs.log.error(sErrorMsg);
			throw new GenericUnexpectedException(sErrorMsg + Framework.getNewLine());
		}
		return String.valueOf(dRoundedValue);
	}

	/**
	 * Method to Convert a number(String format) into a decimal number up to the specified digits.
	 * 
	 * @param sValue - Value to be converted into decimal
	 * @param iPrecision - Specify up to which decimal ,value to be converted.
	 * @return DecimalValue
	 */
	public static String convertToDecimal(String sValue, int iPrecision)
	{
		float sFloatValue = Conversion.parseFloat(sValue);
		String sDecimalValue = String.format("%." + iPrecision + "f", sFloatValue);
		return sDecimalValue;
	}

}
