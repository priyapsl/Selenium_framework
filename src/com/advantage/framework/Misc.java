package com.advantage.framework;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Random;
//import java.util.zip.*;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.*;

import com.advantage.genericexceptions.*;
import com.advantage.reporting.Logs;

/**
 * This class is for miscellaneous functions (i.e. they would be out of place in other classes.)
 */
public class Misc {
	// Default Special characters that cannot be changed
	private static final String SPECIAL = "`~!@#$%^&*()_+-=,./;'[]<>?:{}|\"\\";

	// Special characters to use by default
	private static String sUsedSpecialCharacterSet = SPECIAL;

	// Special token to generate a numeric string within a range
	private static String sRange = "{RANDOM_RANGE(";

	/*
	 * Supported Tokens for replacement (Starts with to indicate token and must end with '}') which map to
	 * { Alphanumeric, Letters Only, Numbers Only, Uppercase Only, Lowercase Only, Special Characters Only,
	 * Boolean (true or false), Password that contains 1 uppercase, 1 lowercase & 1 number character, Password
	 * that contains 1 uppercase, 1 lowercase, 1 number & 1 special character}
	 */
	private static String[] TokensMap = new String[] { "{RANDOM_ALPHA=", "{RANDOM_LETTERS=", "{RANDOM_NUM=",
			"{RANDOM_UPPERCASE=", "{RANDOM_LOWERCASE=", "{RANDOM_SPECIAL=", "{RANDOM_BOOLEAN",
			"{RANDOM_PASSWORD=", "{RANDOM_PASSWORD_ALL=", sRange };

	/**
	 * Gets the default special characters.
	 * 
	 * @return
	 */
	public static String getSpecialDefaults()
	{
		return SPECIAL;
	}

	/**
	 * Resets the used special characters back to the original values
	 */
	public static void resetUsedSpecialToDefaults()
	{
		sUsedSpecialCharacterSet = SPECIAL;
	}

	/**
	 * Set the special characters to be a specific values
	 * 
	 * @param sValue - String that contains the special characters you wish to use
	 */
	public static void setUsedSpecial(String sValue)
	{
		sUsedSpecialCharacterSet = sValue;
	}

	/**
	 * Gets the currently used special characters.
	 * 
	 * @return sUsedSpecialCharacterSet
	 */
	public static String getUsedSpecial()
	{
		return sUsedSpecialCharacterSet;
	}

	/**
	 * Adds the property to the System properties.<BR>
	 * <BR>
	 * Note: Only unique properties will be added.
	 * 
	 * @param sKey - Property to add
	 * @param sValue - Value for the property
	 * @return true if property added else false
	 */
	public static boolean addProperty(String sKey, String sValue)
	{
		String sExistingPropertyValue = System.getProperty(sKey);
		if (sExistingPropertyValue == null)
		{
			Properties applicationProps = new Properties(System.getProperties());
			applicationProps.put(sKey, sValue);
			System.setProperties(applicationProps);
			return true;
		}
		else
			return false;
	}

	/**
	 * This function will check & creates the folders as necessary.
	 * 
	 * @param sFolder - Checks/Creates these folders
	 * @return true if folder(s) exist after execution else false
	 */
	public static boolean bCheckCreateFolder(String sFolder)
	{
		// Check if the folder(s) already exist
		boolean bExists = (new File(sFolder)).exists();
		if (bExists)
			return true;

		// Folder(s) do not exist. So, create them.
		boolean bCreate = (new File(sFolder)).mkdirs();
		if (bCreate)
			return true;
		else
		{
			Logs.log.error("Could not create all folders");
			return false;
		}
	}

	/**
	 * Check if any of the tokens to be replaced exist in a specified string.
	 * 
	 * @param sSearch - String to search
	 * @param Tokens - Tokens to look for
	 * @return
	 */
	public static boolean bTokensToReplace(String sSearch, String[] Tokens)
	{
		for (int i = 0; i < Tokens.length; i++)
		{
			if (sSearch.contains(Tokens[i]))
				return true;
		}

		return false;
	}

	/**
	 * Replaces all tokens in the text with corresponding replacement.
	 * 
	 * @param sText - Text to replace all tokens
	 * @param Tokens - Tokens to look for
	 * @param Replacements - Replacement text for the token
	 * @return
	 */
	public static String replaceTokens(String sText, String[] Tokens, String[] Replacements)
	{
		try
		{
			/*
			 * If number of tokens & what to replace with do not match just return the text without
			 * modification
			 */
			if (Tokens.length != Replacements.length)
				return sText;

			// Go through each token and replace
			String sTemp = sText;
			for (int i = 0; i < Tokens.length; i++)
			{
				sTemp = sTemp.replace(Tokens[i], Replacements[i]);
			}

			return sTemp;
		}
		catch (Exception ex)
		{
			return sText;
		}
	}

	/**
	 * Replaces only the 1st occurrence of each token in the text with corresponding token.<BR>
	 * <BR>
	 * Note: Each token is considered as a regular expression. So, escaping of special characters is required
	 * to achieve expected results.<BR>
	 * <BR>
	 * Commonly used special characters:
	 * 
	 * <PRE>
	 *     ? (question mark) => \?
	 *     * (star)          => \*
	 *     . (period)        => \.
	 * </PRE>
	 * 
	 * <BR>
	 * 
	 * @param sText - Text to replace only the 1st occurrence of each token
	 * @param Tokens - Tokens as regular expressions to look for
	 * @param Replacements - Replacement text for the token
	 * @return
	 */
	public static String replace1stMatch(String sText, String[] Tokens, String[] Replacements)
	{
		try
		{
			/*
			 * If number of tokens & what to replace with do not match just return the text without
			 * modification
			 */
			if (Tokens.length != Replacements.length)
				return sText;

			// Go through each token and replace only the 1st occurrence
			String sTemp = sText;
			for (int i = 0; i < Tokens.length; i++)
			{
				sTemp = sTemp.replaceFirst(Tokens[i], Replacements[i]);
			}

			return sTemp;
		}
		catch (Exception ex)
		{
			return sText;
		}
	}

	/**
	 * This function changes the given xpath to uppercase/lowercase but leaves the strings in the xpath
	 * unchanged.<BR>
	 * <BR>
	 * This function is necessary when using an HTML parser and xpaths fail due to case sensitivity. (I have
	 * found this to be the case when using Cobra HTML.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * If there is a opening delimiter but no matching close delimiter, then only part of the (invalid) xpath
	 * will be converted
	 * properly. The reason for this is I am using a flag to know whether to conversion is necessary which is
	 * switched on/off after each delimiter.<BR>
	 * <BR>
	 * <B>Related Function:</B><BR>
	 * Framework.getText(WebDriver driver, String sXpath) to be used in conjuction with this function.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) You have a lowercase xpath <B>//div[@id='test']</B> but the actual HTML has all uppercase. Use
	 * xpathChangeCase("//div[@id='test']", "'", true) to get the uppercase xpath of <B>//DIV[@ID='test']</B> <BR>
	 * 2) You have an uppercase xpath <B>//DIV[@ID='test']</B> but the actual HTML has all lowercase. Use
	 * xpathChangeCase("//DIV[@ID='test']", "'", false) to get the lowercase xpath of <B>//div[@id='test']</B> <BR>
	 * 3) You have a lowercase xpath <B>//div[@id="test"]</B> but the actual HTML has all uppercase. Use
	 * xpathChangeCase("//div[@id=\"test\"]", "\"", true) to get the uppercase xpath of
	 * <B>//DIV[@ID="test"]</B> <BR>
	 * 4) You have an uppercase xpath <B>//DIV[@ID="test"]</B> but the actual HTML has all lowercase. Use
	 * xpathChangeCase("//DIV[@ID=\"test\"]", "\"", false) to get the lowercase xpath of
	 * <B>//div[@id="test"]</B> <BR>
	 * 
	 * @param sXpath - xpath to make uppercase/lowercase
	 * @param sDelimiter - Delimiter that indicates start/end of a string
	 * @param bToUppercase - true - uppercase; false - lowercase
	 * @return xpath string converted to uppercase/lowercase
	 */
	public static String xpathChangeCase(String sXpath, String sDelimiter, boolean bToUppercase)
	{
		String sConverted = "";
		int nBeginIndex = 0;
		int nEndIndex = sXpath.indexOf(sDelimiter, 0);

		// If cannot initially find the delimiter, then convert the entire string to uppercase/lowercase
		if (nEndIndex < 0)
		{
			if (bToUppercase)
				return sXpath.toUpperCase();
			else
				return sXpath.toLowerCase();
		}

		/*
		 * Need to convert on the xpath parts and not the strings. For example, //div[@id='test'] should
		 * become //DIV[@ID='test']
		 */
		boolean bXpathPart = true;
		boolean bStop = false;
		while (!bStop)
		{
			// Substring that may need to be made uppercase/lowercase
			String sValue = sXpath.substring(nBeginIndex, nEndIndex);

			/*
			 * 1) If xpath part of string, then convert to uppercase/lowercase.
			 * 2) Switch xpath part flag
			 */
			if (bXpathPart)
			{
				if (bToUppercase)
					sValue = sValue.toUpperCase();
				else
					sValue = sValue.toLowerCase();

				bXpathPart = false;
			}
			else
				bXpathPart = true;

			// Add converted string plus delimiter to string to return
			sConverted += sValue + sDelimiter;

			// Move the beginning index to the place where the next substring will be found
			nBeginIndex = nEndIndex + 1;

			// Find the next index of the delimiter
			nEndIndex = sXpath.indexOf(sDelimiter, nBeginIndex);

			// If we cannot find another index of the delimiter use the string length
			if (nEndIndex < 0)
				nEndIndex = sXpath.length();

			// If the beginning index >= string length, then we are complete
			if (nBeginIndex >= sXpath.length())
			{
				/*
				 * The delimiter is always added. However, this can cause the converted string to have an
				 * unwanted quote. So, we will remove if necessary.
				 */
				if (!sXpath.endsWith(sDelimiter))
					sConverted = sConverted.substring(0, sConverted.length() - 1);

				bStop = true;
			}
		}

		return sConverted;
	}

	/**
	 * Compares 2 arrays to determine if they are equal.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Comparison returns false immediately upon finding 1st mismatch (it does not continue to find any
	 * additional mismatches.)<BR>
	 * <BR>
	 * <B>Mismatching data:</B><BR>
	 * If the arrays are not the same, then the parameters mismatchRow[0] & mismatchColumn[0] will be set with
	 * the 1st item to fail.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Array size is different (rows) then mismatchRow[0] = -1 & mismatchColumn[0] = 0<BR>
	 * 2) If there are a different number of columns on a row 5 then mismatchRow[0] = 5 & mismatchColumn[0] =
	 * -1<BR>
	 * 3) If the content of (3,9) is the 1st mismatch found then mismatchRow[0] = 3 & mismatchColumn[0] = 9
	 * 
	 * @param data1 - First array
	 * @param data2 - Second array to compare to the 1st array
	 * @param mismatchRow - First mismatching row set only if mismatch
	 * @param mismatchColumn - First mismatching column set only if mismatch
	 * @return true the content of the arrays are the same, false different content and mismatchRow &
	 *         mismatchColumn are set
	 */
	public static boolean equal(String[][] data1, String[][] data2, int[] mismatchRow, int[] mismatchColumn)
	{
		// Array size the same?
		if (data1.length != data2.length)
		{
			mismatchRow[0] = -1;
			mismatchColumn[0] = 0;
			return false;
		}

		// Check each row
		int nRows = data1.length;
		for (int i = 0; i < nRows; i++)
		{
			// Same number of columns on the row?
			if (data1[i].length != data2[i].length)
			{
				mismatchRow[0] = i;
				mismatchColumn[0] = -1;
				return false;
			}

			// Check each column of the current row
			int nColumns = data1[i].length;
			for (int j = 0; j < nColumns; j++)
			{
				// Data matches?
				if (!data1[i][j].equals(data2[i][j]))
				{
					mismatchRow[0] = i;
					mismatchColumn[0] = j;
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Compares 2 arrays to determine if they are equal.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Comparison returns false immediately upon finding 1st mismatch (it does not continue to find any
	 * additional mismatches.)<BR>
	 * <BR>
	 * <B>Mismatching data:</B><BR>
	 * If the arrays are not the same, then the parameters mismatchColumn[0] will be set with the 1st item to
	 * fail.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Array size is different then mismatchColumn[0] = -1<BR>
	 * 2) If the content of column 5 is the 1st mismatch found then mismatchColumn[0] = 5
	 * 
	 * @param data1 - First array
	 * @param data2 - Second array to compare to the 1st array
	 * @param mismatchColumn - First mismatching column set only if mismatch
	 * @return true the content of the arrays are the same, false different content and mismatchColumn are set
	 */
	public static boolean equal(String[] data1, String[] data2, int[] mismatchColumn)
	{
		// Array size the same?
		if (data1.length != data2.length)
		{
			mismatchColumn[0] = -1;
			return false;
		}

		// Check each row
		int nRows = data1.length;
		for (int i = 0; i < nRows; i++)
		{
			// Data matches?
			if (!data1[i].equals(data2[i]))
			{
				mismatchColumn[0] = i;
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns a random String that meets the requirements.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) <B>nLength</B> needs to be at least 1 or enough characters to meet the additional requirements<BR>
	 * 2) IF <B>nLength</B> < 1 THEN returned String is min characters to meet the additional requirements or
	 * 1<BR>
	 * 3) Use <B>setUsedSpecial</B> if you need to change the special characters set <BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) random(1, true, true, true, true) returns a random String of <B>4</B> characters that has 1
	 * uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 2) random(-1, false, false, false, false) returns a random String of <B>1</B> character<BR>
	 * 3) random(20, true, true, true, true) returns a random String of <B>20</B> characters that has 1
	 * uppercase letter, 1 lowercase letter, 1 number & 1 special character.<BR>
	 * 
	 * @param nLength - Length of String to return
	 * @param bUpper - true ensures that returned String has at least 1 uppercase letter
	 * @param bLower - true ensures that returned String has at least 1 lowercase letter
	 * @param bNumber - true ensures that returned String has at least 1 number
	 * @param bSpecial - true ensures that returned String has at least 1 special character
	 * @return random String that meets parameter requirements
	 */
	public static String random(int nLength, boolean bUpper, boolean bLower, boolean bNumber, boolean bSpecial)
	{
		/*
		 * 1) Start by generating a String Builder of the specified length
		 * 2) Insert an uppercase letter if necessary at a random position
		 * 3) Insert an lowercase letter if necessary at a random position
		 * 4) Insert a number if necessary at a random position
		 * 5) Insert a special character if necessary at a random position
		 */

		/*
		 * Ensure that String Builder is at least enough characters to make random String meet the
		 * requirements.
		 */
		int nMinLength = 0;
		if (bUpper)
			nMinLength++;

		if (bLower)
			nMinLength++;

		if (bNumber)
			nMinLength++;

		if (bSpecial)
			nMinLength++;

		if (nMinLength < 1)
			nMinLength = 1;

		if (nLength > nMinLength)
			nMinLength = nLength;

		// Generate the String Builder
		StringBuilder rest = new StringBuilder(RandomStringUtils.randomAlphanumeric(nMinLength));

		// Get 4 unique positions in the String Builder
		int[] nUniqueSet = uniqueRandom(nMinLength, nMinLength);
		int nIndex = 0;

		// Insert an uppercase letter if necessary at a random position
		if (bUpper)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomAlphabetic(1).toUpperCase());
			nIndex++;
		}

		// Insert an lowercase letter if necessary at a random position
		if (bLower)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomAlphabetic(1).toLowerCase());
			nIndex++;
		}

		// Insert a number if necessary at a random position
		if (bNumber)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.randomNumeric(1));
			nIndex++;
		}

		// Insert a special character if necessary at a random position
		if (bSpecial)
		{
			rest.delete(nUniqueSet[nIndex], nUniqueSet[nIndex] + 1);
			rest.insert(nUniqueSet[nIndex], RandomStringUtils.random(1, sUsedSpecialCharacterSet));
			nIndex++;
		}

		return rest.substring(0, nMinLength);
	}

	/**
	 * Determines if value exists in the specified array.
	 * 
	 * @param nValues - array of integers
	 * @param nFind - specific value to find
	 * @return true specified integer exists in the array, false could not find
	 */
	public static boolean exists(int[] nValues, int nFind)
	{
		try
		{
			int nLength = nValues.length;
			for (int i = 0; i < nLength; i++)
			{
				if (nValues[i] == nFind)
					return true;
			}
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	/**
	 * Generates an array of unique integers.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) nSize needs to be greater than 0 else defaults to array of size 1<BR>
	 * 2) nUpperBound needs be greater than or equal to nSize else defaults to value of nSize<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) IF nSize = -1, nUpperBound = 0 THEN array will be size 1 and have value of 0<BR>
	 * 2) IF nSize = -1, nUpperBound = 100 THEN array will be size 1 and have a value between 0 and 99
	 * inclusive<BR>
	 * 3) IF nSize = 3, nUpperBound = 1 THEN array will be size 3 and all values will be between 0 and 2
	 * inclusive<BR>
	 * 4) IF nSize = 5, nUpperBound = 100 THEN array will be size 5 and all values will be between 0 and 99
	 * inclusive<BR>
	 * 
	 * @param nSize - number of unique integers
	 * @param nUpperBound - generate numbers below this value
	 * @return array of unique integers
	 */
	public static int[] uniqueRandom(int nSize, int nUpperBound)
	{
		Random randomGenerator = new Random();
		int nRandomInt;
		int nUniqueCount = 0;

		// Need to ensure that min length is 1
		int nLength = 1;
		if (nSize > nLength)
			nLength = nSize;

		// Need to ensure that we can generate all unique integers
		int nMaxValue = nLength;
		if (nUpperBound > nMaxValue)
			nMaxValue = nUpperBound;

		// Generate the 1st unique integer
		int[] uniqueSet = new int[nLength];

		// Initialize array values to -1 which cannot be generated
		for (int i = 0; i < nLength; i++)
			uniqueSet[i] = -1;

		uniqueSet[0] = randomGenerator.nextInt(nMaxValue);
		nUniqueCount++;

		/*
		 * Generate the remaining unique integers.
		 * Note: This may take some time depending upon the upper bound for random integers.
		 */
		while (nUniqueCount < nLength)
		{
			nRandomInt = randomGenerator.nextInt(nMaxValue);
			if (!exists(uniqueSet, nRandomInt))
			{
				uniqueSet[nUniqueCount] = nRandomInt;
				nUniqueCount++;
			}
		}

		return uniqueSet;
	}

	/**
	 * Replaces the supported tokens with randomized data.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Replacement is based on entire token. So, if there are multiple of the same token then the same
	 * randomized token will be used for all the same tokens.<BR>
	 * <BR>
	 * <B>Supported Tokens:</B><BR>
	 * 1) {RANDOM_ALPHA=X} - Replaced with X alphanumeric characters<BR>
	 * 2) {RANDOM_LETTERS=X} - Replaced with X letters that could be uppercase or lowercase<BR>
	 * 3) {RANDOM_NUM=X} - Replaced with X numbers<BR>
	 * 4) {RANDOM_UPPERCASE=X} - Replaced with X uppercase letters<BR>
	 * 5) {RANDOM_LOWERCASE=X} - Replaced with X lowercase letters<BR>
	 * 6) {RANDOM_SPECIAL=X} - Replaced with X special characters<BR>
	 * 7) {RANDOM_BOOLEAN} - Replaced with 'true' or 'false'<BR>
	 * 8) {RANDOM_PASSWORD=X} - Replaced with X characters with at least 1 uppercase, 1 lowercase & 1 number<BR>
	 * 9) {RANDOM_PASSWORD_ALL=X} - Replaced with X characters with at least 1 uppercase, 1 lowercase, 1
	 * number & 1 special character<BR>
	 * 10) {RANDOM_RANGE(X,Y)} - Replaced with number that satisfies criteria X &lt;= number &lt;= Y<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) {RANDOM_ALPHA=3} => <B>aB1</B><BR>
	 * 2) {RANDOM_ALPHA=3} => <B>1qa</B><BR>
	 * 3) {RANDOM_LETTERS=5} => <B>dJukL</B><BR>
	 * 4) {RANDOM_LETTERS=5} => <B>Kuhjg</B><BR>
	 * 5) {RANDOM_NUM=3} => <B>658</B><BR>
	 * 6) {RANDOM_NUM=3} => <B>432</B><BR>
	 * 7) {RANDOM_UPPERCASE=4} => <B>DIEK</B><BR>
	 * 8) {RANDOM_UPPERCASE=4} => <B>NYHF</B><BR>
	 * 9) {RANDOM_LOWERCASE=4} => <B>bdgh</B><BR>
	 * 10) {RANDOM_LOWERCASE=4} => <B>npje</B><BR>
	 * 11) {RANDOM_SPECIAL=2} => <B>&^</B><BR>
	 * 12) {RANDOM_SPECIAL=2} => <B>$#</B><BR>
	 * 13) {RANDOM_LETTERS=1}-{RANDOM_NUM=3}-{RANDOM_ALPHA=3} => <B>x-083-Wdi</B><BR>
	 * 14) {RANDOM_ALPHA=3}-{RANDOM_ALPHA=3} => <B>Ab1-Ab1</B><BR>
	 * 15) {RANDOM_NUM=3}-{RANDOM_NUM=3} => <B>123-123</B><BR>
	 * 16) {RANDOM_NUM=3}-{RANDOM_NUM=5} => <B>123-63538</B><BR>
	 * 
	 * @param sValue - String that contains Tokens which to be randomized
	 * @return String
	 */
	public static String replaceWithRandomizedTokens(String sValue)
	{
		String sRandom = sValue;
		int nSize = TokensMap.length;
		for (int i = 0; i < nSize; i++)
		{
			int nStartToken = 0;
			int nEndToken = 0;
			while (nStartToken != -1 && nEndToken != -1)
			{
				try
				{
					/*
					 * 1) Get the index for the start of the token
					 * 2) Calculate end of the start token indicator
					 * 3) Get the index of the token end
					 * 4) Get the desired length for the random string
					 * 5) Get the entire token
					 * 6) Replace the entire token with the desired length and type of random string
					 */
					nStartToken = sRandom.indexOf(TokensMap[i]);
					if (nStartToken == -1)
						continue;

					int nStartTokenIndicatorEnd = nStartToken + TokensMap[i].length();
					nEndToken = sRandom.indexOf("}", nStartTokenIndicatorEnd);
					if (nEndToken == -1)
						continue;

					String sLengthForRandomString = sRandom.substring(nStartTokenIndicatorEnd, nEndToken);
					String sEntireToken = sRandom.substring(nStartToken, nEndToken + 1);

					if (TokensMap[i] == sRange)
					{
						// Range is separated by a comma
						String[] parse = sLengthForRandomString.split(",");

						// Max Range should end with a )
						if (parse[1].endsWith(")"))
							parse[1] = parse[1].substring(0, parse[1].length() - 1);

						sRandom = sRandom.replace(
								sEntireToken,
								String.valueOf(randomRange(Conversion.nParseInt(parse[0], 0),
										Conversion.nParseInt(parse[1], 0))));
					}
					else
					{
						sRandom = sRandom
								.replace(
										sEntireToken,
										randomizeToken(TokensMap[i],
												Conversion.nParseInt(sLengthForRandomString, 1)));
					}
				}
				catch (Exception ex)
				{
				}
			}
		}

		return sRandom;
	}

	/**
	 * Returns a randomized string corresponding to the token mapping
	 * 
	 * @param sToken - A supported token
	 * @param nLength - Length of String to return
	 * @return
	 */
	public static String randomizeToken(String sToken, int nLength)
	{
		/*
		 * Match Token to corresponding random function
		 */
		String sRandomized = sToken;

		// Random Alphanumeric String
		if (sToken.equals(TokensMap[0]))
		{
			sRandomized = RandomStringUtils.randomAlphanumeric(nLength);
		}

		// Random Letters Only String
		if (sToken.equals(TokensMap[1]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength);
		}

		// Random Numbers Only String
		if (sToken.equals(TokensMap[2]))
		{
			sRandomized = RandomStringUtils.randomNumeric(nLength);
		}

		// Random Uppercase Only String
		if (sToken.equals(TokensMap[3]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength).toUpperCase();
		}

		// Random Lowercase Only String
		if (sToken.equals(TokensMap[4]))
		{
			sRandomized = RandomStringUtils.randomAlphabetic(nLength).toLowerCase();
		}

		// Random Special Characters Only String
		if (sToken.equals(TokensMap[5]))
		{
			sRandomized = RandomStringUtils.random(nLength, sUsedSpecialCharacterSet);
		}

		// Random Boolean String (true or false)
		if (sToken.equals(TokensMap[6]))
		{
			Random r = new Random();
			if ((r.nextInt(1000) % 2) == 0)
				sRandomized = "true";
			else
				sRandomized = "false";
		}

		// Random Password with 1 uppercase, 1 lowercase & 1 number character String
		if (sToken.equals(TokensMap[7]))
		{
			sRandomized = random(nLength, true, true, true, false);
		}

		// Random Password with 1 uppercase, 1 lowercase, 1 number & 1 special character String
		if (sToken.equals(TokensMap[8]))
		{
			sRandomized = random(nLength, true, true, true, true);
		}

		return sRandomized;
	}

	/**
	 * Returns a number in the specified range.<BR>
	 * <B>Examples:</B><BR>
	 * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
	 * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4, 5}<BR>
	 * 
	 * @param nMin - Minimum value
	 * @param nMax - Maximum value
	 * @return
	 */
	public static int randomRange(int nMin, int nMax)
	{
		int nOffset, nUseRangeMin, nUseRangeMax, nRange;

		// Set the default values for the range
		nUseRangeMin = nMin;
		nUseRangeMax = nMax;

		// Get a valid range
		if (nUseRangeMin > nUseRangeMax)
		{
			int nTemp = nUseRangeMin;
			nUseRangeMin = nUseRangeMax;
			nUseRangeMax = nTemp;
		}

		/*
		 * Need to shift the range to be 0 to Max for random number generation. The offset will be used after
		 * to random number in range.
		 */
		nOffset = nUseRangeMin;

		// Need to ensure range is positive
		if (nUseRangeMin < 0)
		{
			nUseRangeMin += -1 * nOffset;
			nUseRangeMax += -1 * nOffset;
		}

		Random r = new Random();
		nRange = nOffset + r.nextInt(nUseRangeMax - nUseRangeMin + 1);
		return nRange;
	}

	/**
	 * Create a single line string that represents a node that spans 1 line. (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * createNode("RunID","8976") will return<BR>
	 * &lt;RunID>8976&lt;/RunID>
	 * 
	 * @param sNode - Node Name
	 * @param sValue - Value of node
	 * @return
	 */
	public static String createNode(String sNode, String sValue)
	{
		return "<" + sNode + ">" + sValue + "</" + sNode + ">";
	}

	/**
	 * Create a single line string that represents a node that spans 1 line. (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * createNode("Field","name","STATEMENT_DATE","2011-10-27") will return<BR>
	 * &lt;Field name="STATEMENT_DATE">2011-10-27&lt;/Field>
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - Attribute Name
	 * @param sAttrValue - Attribute Value
	 * @param sValue - Value of node
	 * @return
	 */
	public static String createNode(String sNode, String sAttr, String sAttrValue, String sValue)
	{
		return "<" + sNode + " " + sAttr + "=\"" + sAttrValue + "\"" + ">" + sValue + "</" + sNode + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * startNode("RunID") will return<BR>
	 * &lt;RunID>
	 * 
	 * @param sNode - Node Name
	 * @return
	 */
	public static String startNode(String sNode)
	{
		return "<" + sNode + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * startNode("Field","name","STATEMENT_DATE") will return<BR>
	 * &lt;Field name="STATEMENT_DATE">
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - Attribute Name
	 * @param sAttrValue - Attribute Value
	 * @return
	 */
	public static String startNode(String sNode, String sAttr, String sAttrValue)
	{
		return "<" + sNode + " " + sAttr + "=\"" + sAttrValue + "\"" + ">";
	}

	/**
	 * Starts the node but does not complete the node. Use if there are child nodes. (Simplifies creating XML
	 * files.)<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) startNode("Field", new String[][]{ {"a","1"}, {"b","2"}, false) will return<BR>
	 * &lt;Field a="1" b="2"><BR>
	 * 2) startNode("Field", new String[][]{ {"a","1"}, {"b","2"}, true) will return<BR>
	 * &lt;Field a="1" b="2"/><BR>
	 * 
	 * @param sNode - Node Name
	 * @param sAttr - For each attribute contains Attribute Name (sAttr[X][0]) & Attribute Value (sAttr[X][1])
	 * @param bNodeComplete - true to close the node using slash
	 * @return
	 */
	public static String startNode(String sNode, String[][] sAttr, boolean bNodeComplete)
	{
		String sAllAttributes = "";
		int nAttributes = sAttr.length;

		// For each attribute construct the xml
		for (int i = 0; i < nAttributes; i++)
		{
			String sAnotherAttribute = " " + sAttr[i][0] + "=\"" + sAttr[i][1] + "\"";
			sAllAttributes += sAnotherAttribute;
		}

		// Decide whether to close the tag on the same line using slash
		if (bNodeComplete)
			return "<" + sNode + sAllAttributes + "/>";
		else
			return "<" + sNode + sAllAttributes + ">";
	}

	/**
	 * Completes the node (Simplifies creating XML files.)<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * completeNode("RunID") will return<BR>
	 * &lt;/RunID>
	 * 
	 * @param sNode - Node Name
	 * @return
	 */
	public static String completeNode(String sNode)
	{
		return "</" + sNode + ">";
	}

	/**
	 * Gets a random enumeration option<BR>
	 * <BR>
	 * <B>Example: </B><BR>
	 * Languages randomLang = (Languages) randomEnum(Languages.English, 10000);<BR>
	 * 
	 * @param e - Any Enumeration value
	 * @param nRangeMultiplier - Increases the range of values before getting the remainder when divided by
	 *            number of enumeration values for the enumeration
	 * @return enumeration value
	 */
	public static Enum<?> randomEnum(Enum<?> e, int nRangeMultiplier)
	{
		Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
		int nSize = options.length;
		int nRandom = Misc.randomRange(0, nRangeMultiplier * nSize) % nSize;
		return options[nRandom];
	}

	/**
	 * Removes specified string from the end of the given string<BR>
	 * <BR>
	 * <B>Note:</B> If the given string has multiple sequences of the string only the last occurrence is
	 * removed<BR>
	 * <BR>
	 * <B>Examples: </B><BR>
	 * <table border="1">
	 * <tr>
	 * <td><B>Original String</B></td>
	 * <td><B>Ends With String to be removed</B></td>
	 * <td><B>Returned String</B></td>
	 * </tr>
	 * <tr>
	 * <td>//test/</td>
	 * <td>/</td>
	 * <td>//test</td>
	 * </tr>
	 * <tr>
	 * <td>something//</td>
	 * <td>/</td>
	 * <td>something/</td>
	 * </tr>
	 * <tr>
	 * <td>First NameTest</td>
	 * <td>Test</td>
	 * <td>First Name</td>
	 * </tr>
	 * <tr>
	 * <td>anotherTestTest</td>
	 * <td>Test</td>
	 * <td>anotherTest</td>
	 * </tr>
	 * </table>
	 * 
	 * @param sValue - String to work with
	 * @param sEndsWith - String to be removed from end of the given string
	 * @return
	 */
	public static String removeEndsWith(String sValue, String sEndsWith)
	{
		if (sValue == null || sEndsWith == null)
			return sValue;

		// Does the string end with what we want to remove?
		if (sValue.endsWith(sEndsWith))
		{
			return sValue.substring(0, sValue.length() - sEndsWith.length());
		}
		else
		{
			// No work to be done just return string
			return sValue;
		}
	}

	@Test
	public static void unitTest()
	{
		Logs.initializeLoggers();

		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_RANGE(-5,5)}"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_RANGE(5,-5)}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_RANGE(-5,5)}z"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_RANGE(5,-5)}z"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_RANGE(1,12)}"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_RANGE(1,12)}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_RANGE(1,12)}z"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_RANGE(1,12)}z"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_BOOLEAN}"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_BOOLEAN}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_BOOLEAN}z"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_BOOLEAN}z"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD=8}"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_PASSWORD_ALL=8}"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_NUM=3}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_NUM=7}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_NUM=5}z"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_NUM=6}z"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_ALPHA=3}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=7}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=5}z"));
		Logs.log.info(replaceWithRandomizedTokens("a{RANDOM_ALPHA=6}z"));
		Logs.log.info("");
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_UPPERCASE=5}-{RANDOM_LOWERCASE=3}"));
		Logs.log.info("");

		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=5}, " + "{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}, " + "{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}, "
				+ "{RANDOM_SPECIAL=5}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=5}, " + "{RANDOM_LETTERS=4}, "
				+ "{RANDOM_NUM=3}, " + "{RANDOM_UPPERCASE=2}, " + "{RANDOM_LOWERCASE=1}, "
				+ "{RANDOM_SPECIAL=6}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"
				+ "  REPEATS:  {RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, " + "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, "
				+ "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=5}{RANDOM_LETTERS=5}, "
				+ "{RANDOM_NUM=5}{RANDOM_UPPERCASE=5}, " + "{RANDOM_LOWERCASE=5}{RANDOM_SPECIAL=5}"
				+ "  NEXT SET:  {RANDOM_ALPHA=3}{RANDOM_LETTERS=3}, "
				+ "{RANDOM_NUM=3}{RANDOM_UPPERCASE=3}, " + "{RANDOM_LOWERCASE=3}{RANDOM_SPECIAL=3}"));
		Logs.log.info(replaceWithRandomizedTokens("{RANDOM_ALPHA=1}{RANDOM_LETTERS=1}, "
				+ "{RANDOM_NUM=1}{RANDOM_UPPERCASE=1}, " + "{RANDOM_LOWERCASE=1}{RANDOM_SPECIAL=1}"
				+ "  NEXT SET:  {RANDOM_ALPHA=11}{RANDOM_LETTERS=11}, "
				+ "{RANDOM_NUM=11}{RANDOM_UPPERCASE=11}, " + "{RANDOM_LOWERCASE=11}{RANDOM_SPECIAL=11}"));
		Logs.log.info("");

		/*
		 * Random string generation tests
		 */
		int nLength = 8;
		for (int i = 0; i < nLength; i++)
		{
			Logs.log.info("Random String of length (" + nLength + "):  " + RandomStringUtils.random(nLength));
			Logs.log.info("Random Alphabetic String of length (" + nLength + "):  "
					+ RandomStringUtils.randomAlphabetic(nLength));
			Logs.log.info("Random Alphanumeric String of length (" + nLength + "):  "
					+ RandomStringUtils.randomAlphanumeric(nLength));
			Logs.log.info("Random Numeric String of length (" + nLength + "):  "
					+ RandomStringUtils.randomNumeric(nLength));
			Logs.log.info("Random String of length (" + nLength + ") that always starts with a letter:  "
					+ RandomStringUtils.randomAlphabetic(1)
					+ RandomStringUtils.randomAlphanumeric(nLength - 1));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains an uppercase letter:  "
					+ random(nLength, true, false, false, false));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains a lowercase letter:  "
					+ random(nLength, false, true, false, false));
			Logs.log.info("Random String of length (" + nLength + ") that always contains a number:  "
					+ random(nLength, false, false, true, false));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains a special character:  "
					+ random(nLength, false, false, false, true));
			Logs.log.info("Random String of length (" + nLength
					+ ") that always contains an uppercase, lowercase, number & special character:  "
					+ random(nLength, true, true, true, true));
			Logs.log.info("Random String of length (4) that always contains an uppercase, lowercase, number & special character:  "
					+ random(1, true, true, true, true));
			Logs.log.info("Random String of length (1):  " + random(-1, false, false, false, false));
			Logs.log.info("Random String of length (3) that always contains an uppercase, number & special character:  "
					+ random(1, true, false, true, true));
			Logs.log.info("Random String of length (3) that always contains a lowercase, number & special character:  "
					+ random(1, false, true, true, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & special character:  "
					+ random(1, true, true, false, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & number:  "
					+ random(1, true, true, true, false));

			setUsedSpecial("&#");
			Logs.log.info("Special character set to \"&#\"");
			Logs.log.info("Random String of length (3) that always contains an uppercase, number & special character:  "
					+ random(1, true, false, true, true));
			Logs.log.info("Random String of length (3) that always contains a lowercase, number & special character:  "
					+ random(1, false, true, true, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & special character:  "
					+ random(1, true, true, false, true));
			Logs.log.info("Random String of length (3) that always contains an uppercase, lowercase & number:  "
					+ random(1, true, true, true, false));
			resetUsedSpecialToDefaults();
			Logs.log.info("Special characters reset");
			Logs.log.info("");
		}

		Logs.log.info("");

		int[] mismatchRow = new int[] { -2 };
		int[] mismatchColumn = new int[] { -2 };

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		String[][] test1 = new String[][] { { "a", "1" }, { "b", "2" } };
		String[][] test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Misc.equal(test1, test2, mismatchRow, mismatchColumn))
			Logs.log.info("Arrays were equal as expected");
		else
		{
			Logs.log.error("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);
			throw new GenericUnexpectedException("");
		}

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1" }, { "b", "2" } };
		test2 = new String[][] { { "a", "1" }, { "d", "2" } };
		if (Misc.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1" }, { "b", "2" }, { "c", "3" } };
		test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Misc.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		mismatchRow[0] = -2;
		mismatchColumn[0] = -2;
		test1 = new String[][] { { "a", "1", "a1" }, { "b", "2", "b2" } };
		test2 = new String[][] { { "a", "1" }, { "b", "2" } };
		if (Misc.equal(test1, test2, mismatchRow, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but there should have been a mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Row:  " + mismatchRow[0] + "; Column:  " + mismatchColumn[0]);

		Logs.log.info("");
		mismatchColumn[0] = -2;
		String[] test3 = new String[] { "a", "1", "b", "2" };
		String[] test4 = new String[] { "a", "1", "b", "2" };
		if (Misc.equal(test3, test4, mismatchColumn))
			Logs.log.info("Arrays were equal as expected");
		else
		{
			Logs.log.error("Column:  " + mismatchColumn[0]);
			throw new GenericUnexpectedException("");
		}

		mismatchColumn[0] = -2;
		test3 = new String[] { "a", "1", "b", "2" };
		test4 = new String[] { "a", "1", "c", "2" };
		if (Misc.equal(test3, test4, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but expected mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Column:  " + mismatchColumn[0]);

		mismatchColumn[0] = -2;
		test3 = new String[] { "a", "1", "b", "2", "c" };
		test4 = new String[] { "a", "1", "b", "2" };
		if (Misc.equal(test3, test4, mismatchColumn))
		{
			Logs.log.error("Arrays were equal but expected mismatch");
			throw new GenericUnexpectedException("");
		}
		else
			Logs.log.info("Column:  " + mismatchColumn[0]);
	}

	/**
	 * Get the specified System Property
	 * 
	 * @param sKey - the name of the system property
	 * @param sDefaultValue - Default value to return if error or property not found
	 * @return - sDefaultValue if System Property does not exist or is the empty string else the actual System
	 *         Property value
	 */
	public static String getProperty(String sKey, String sDefaultValue)
	{
		try
		{
			String sPropertyValue = System.getProperty(sKey);
			if (sPropertyValue == null || sPropertyValue.equals(""))
				return sDefaultValue;
			else
				return sPropertyValue;
		}
		catch (SecurityException s)
		{
			Logs.log.error(s);
			throw s;
		}
		catch (Exception ex)
		{
		}

		return sDefaultValue;
	}
	
//	public static void createZipFolder(String sFolderPath) throws Exception 
//	{
//		byte[] buffer = new byte[1024];
//		 
//    	try{
// 
//    		FileOutputStream fos = new FileOutputStream(sFolderPath);
//    		ZipOutputStream zos = new ZipOutputStream(fos);
//    		ZipEntry ze= new ZipEntry(sFolderPath);
//    		zos.putNextEntry(ze);
//    		FileInputStream in = new FileInputStream(sFolderPath);
// 
//    		int len;
//    		while ((len = in.read(buffer)) > 0) {
//    			zos.write(buffer, 0, len);
//    		}
// 
//    		in.close();
//    		zos.closeEntry();
// 
//    		//remember close it
//    		zos.close();
// 
//    		System.out.println("Done");
// 
//    	}catch(IOException ex){
//    	   ex.printStackTrace();
//    	} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
