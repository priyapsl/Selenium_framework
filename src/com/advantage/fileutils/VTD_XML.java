package com.advantage.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import org.testng.annotations.*;

import com.advantage.framework.Misc;
import com.advantage.genericexceptions.*;
import com.advantage.reporting.Logs;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

/**
 * This class uses VTD-XML for reading from an XML file. Use this class if having performance issues with the
 * XML class.
 */
public class VTD_XML {
	protected VTDGen vg;
	protected VTDNav vn;
	protected AutoPilot ap;

	// Used in navigation to determine if the parent node was selected
	protected boolean bSetNode = false;

	// Node list to the root xpath
	protected String[] toRootXpath;

	// Node list to each node
	protected String[][] toEachNode;

	/**
	 * Needed for the TestNG unit test in this case.
	 */
	public VTD_XML()
	{
	}

	/**
	 * Constructor for initializing using a file
	 * 
	 * @param sConfigFile - Location of XML file to parse
	 * @throws Exception
	 */
	public VTD_XML(String sConfigFile) throws Exception
	{
		// Open a file and read the content into a byte array
		File f = new File(sConfigFile);
		FileInputStream fis = new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		fis.read(b);

		// Complete initialization
		init(b);
	}

	/**
	 * Constructor for initializing using bytes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If you have a string, then you can convert it to bytes using the method <B>getBytes()<B><BR>
	 * 
	 * @param b - XML as bytes
	 * @throws Exception
	 */
	public VTD_XML(byte[] b) throws Exception
	{
		init(b);
	}

	/**
	 * Initializes the class variables to read the XML
	 * 
	 * @param b - XML as bytes
	 * @throws Exception
	 */
	protected void init(byte[] b) throws Exception
	{
		// Instantiate VTDGen and call parse
		vg = new VTDGen();
		vg.setDoc(b);
		vg.parse(true); // set namespace awareness to true

		// Retrieve the VTDNav object from VTDGen & instantiate the AutoPilot object
		vn = vg.getNav();
		ap = new AutoPilot(vn);
	}

	/**
	 * Initialization required before use of method getNextNode() which is optimized parsing XML files up to
	 * 2GB provided enough memory can be allocated.<BR>
	 * <BR>
	 * 
	 * <B>Notes:</B><BR>
	 * 1) On a 32-bit system, the max memory I was able to allocate was 1.6GB.<BR>
	 * 2) On a 64-bit system, no memory allocation was need but application must be run from command line.
	 * (This may be that eclipse was not running the 64-bit version of Java.)<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) Given following XML and you want to parse values for var1 & var2<BR>
	 * &lt;rootNode&gt;<BR>
	 * &nbsp;&nbsp;&lt;testcase&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;var1&gt;1&lt;/var1&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;nested&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;var2&gt;2&lt;/var2&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/nested&gt;<BR>
	 * &nbsp;&nbsp;&lt;/testcase&gt;<BR>
	 * &nbsp;&nbsp;&lt;testcase&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;var1&gt;abc&lt;/var1&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;nested&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;var2&gt;def&lt;/var2&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/nested&gt;<BR>
	 * &nbsp;&nbsp;&lt;/testcase&gt;<BR>
	 * &lt;/rootNode&gt;<BR>
	 * <BR>
	 * Then, use sRootXpath = "/rootNode/testcase", eachNodesXpath[0] = "/var1", eachNodesXpath[1] =
	 * "/nested/var2"<BR>
	 * 
	 * @param sRootXpath - the root xpath
	 * @param eachNodesXpath - the xpath to each of the nodes to be stored in the array
	 */
	public void initializeForOptimizedParsing(String sRootXpath, String[] eachNodesXpath)
	{
		/*
		 * Break the root xpath into pieces for navigation by toElement
		 */
		if (sRootXpath.startsWith("/"))
			toRootXpath = sRootXpath.substring(1).split("/");
		else
			toRootXpath = sRootXpath.split("/");

		/*
		 * Break each node xpath into pieces for navigation by toElement
		 */
		int nNodes = eachNodesXpath.length;
		toEachNode = new String[nNodes][getMaxArraySize(eachNodesXpath)];
		for (int i = 0; i < nNodes; i++)
		{
			if (eachNodesXpath[i].startsWith("/"))
				toEachNode[i] = eachNodesXpath[i].substring(1).split("/");
			else
				toEachNode[i] = eachNodesXpath[i].split("/");
		}
	}

	/**
	 * Given an array of xpaths, the method determines which xpath is the largest. This can be used to
	 * allocate an array to hold the values after parsing.
	 * 
	 * @param nodes - array of xpaths
	 * @return
	 */
	protected int getMaxArraySize(String[] nodes)
	{
		/*
		 * Step 1: Get the lengths of each array
		 */
		int nLength = nodes.length;
		int[] arraySizes = new int[nLength];
		for (int i = 0; i < nLength; i++)
		{
			if (nodes[i].startsWith("/"))
				arraySizes[i] = nodes[i].substring(1).split("/").length - 1;
			else
				arraySizes[i] = nodes[i].split("/").length - 1;
		}

		/*
		 * Step 2: Find the largest array size from the previous step
		 */
		int nLargestArray = arraySizes[0];
		for (int i = 1; i < nLength; i++)
		{
			if (arraySizes[i] > nLargestArray)
				nLargestArray = arraySizes[i];
		}

		return nLargestArray;
	}

	/**
	 * Gets the first nodes value for xpath as a String.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If node contains child nodes, then default value is returned.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * Sample xml for examples below:<BR>
	 * &nbsp;&nbsp;&lt;test&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;user 1&lt;/user&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
	 * &nbsp;&nbsp;&lt;/test&gt;<BR>
	 * <BR>
	 * 1) //test returns default value<BR>
	 * 2) //test/user returns "user 1"<BR>
	 * 
	 * @param sXpath - xpath to node
	 * @param sDefault - value returned if exception occurs
	 * @return String
	 */
	public String getNodeValue(String sXpath, String sDefault)
	{
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);

			// Evaluate the xpath which returns the node index
			int nNodeIndex = ap.evalXPath();

			// If valid index, continue to find value
			if (nNodeIndex != -1)
			{
				// Get the index of the value for this node
				int nValueIndex = vn.getText();

				// If valid index, return the value
				if (nValueIndex != -1)
					// return vn.toNormalizedString(nValueIndex);
					return vn.toString(nValueIndex);
			}
		}
		catch (Exception ex)
		{
		}

		return sDefault;
	}

	/**
	 * Gets the first nodes name (tag name) for xpath as a String.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If node contains child nodes, then default value is returned.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 
	 * @param sXpath - xpath to node
	 * @param sDefault - value returned if exception occurs
	 * @return String
	 */
	public String getNodeName(String sXpath, String sDefault)
	{
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);

			// Evaluate the xpath which returns the node index
			int nNodeIndex = ap.evalXPath();

			// If valid index, continue to find value
			if (nNodeIndex != -1)
			{
				// Get the index of the value for this node
				return vn.toRawString(vn.getCurrentIndex());
			}
		}
		catch (Exception ex)
		{
		}

		return sDefault;
	}

	/**
	 * Gets the first nodes value for xpath as an integer.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If node contains child nodes, then default value is returned.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * Sample xml for examples below:<BR>
	 * &nbsp;&nbsp;&lt;test&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;1&lt;/user&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
	 * &nbsp;&nbsp;&lt;/test&gt;<BR>
	 * <BR>
	 * 1) //test returns default value<BR>
	 * 2) //test/user returns 1<BR>
	 * 
	 * @param sXpath - xpath to node
	 * @param nDefault - value returned if exception occurs
	 * @return
	 */
	public int getNodeValue(String sXpath, int nDefault)
	{
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);

			// Evaluate the xpath which returns the node index
			int nNodeIndex = ap.evalXPath();

			// If valid index, continue to find value
			if (nNodeIndex != -1)
			{
				// Get the index of the value for this node
				int nValueIndex = vn.getText();

				// If valid index, return the value
				if (nValueIndex != -1)
					return Integer.parseInt(vn.toNormalizedString(nValueIndex));
			}
		}
		catch (Exception ex)
		{
		}

		return nDefault;
	}

	/**
	 * Gets the first nodes value for xpath as a boolean.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If node contains child nodes, then default value is returned.<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * Sample xml for examples below:<BR>
	 * &nbsp;&nbsp;&lt;test&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;true&lt;/user&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
	 * &nbsp;&nbsp;&lt;/test&gt;<BR>
	 * <BR>
	 * 1) //test returns default value<BR>
	 * 2) //test/user returns true<BR>
	 * 
	 * @param sXpath - xpath to node
	 * @param bDefault - value returned if exception occurs or no match to convert to boolean
	 * @return
	 */
	public boolean getNodeValue(String sXpath, boolean bDefault)
	{
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);
			String sConvert = null;

			// Evaluate the xpath which returns the node index
			int nNodeIndex = ap.evalXPath();

			// If valid index, continue to find value
			if (nNodeIndex != -1)
			{
				// Get the index of the value for this node
				int nValueIndex = vn.getText();

				// If valid index, return the value
				if (nValueIndex != -1)
					sConvert = vn.toNormalizedString(nValueIndex);
			}

			if (sConvert != null)
			{
				// Strings that will be considered to be true
				if (sConvert.equalsIgnoreCase("true"))
					return true;

				if (sConvert.equalsIgnoreCase("1"))
					return true;

				// Strings that will be considered to be false
				if (sConvert.equalsIgnoreCase("false"))
					return false;

				if (sConvert.equalsIgnoreCase("0"))
					return false;
			}
		}
		catch (Exception ex)
		{
		}

		return bDefault;
	}

	/**
	 * Gets count of all Nodes for the given xpath.<BR>
	 * <BR>
	 * <B>Note:</B> For performance reasons, this result should be stored in a variable instead of calling the
	 * function again if needed more than 1 time. As the processing time would be <B>n</B> instead of
	 * <B>n</B>*<B>n</B>.
	 * 
	 * @param sXpath - xpath for the nodes to count
	 * @return
	 */
	public int getNodesCount(String sXpath)
	{
		int nCount = 0;
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);
			while (ap.evalXPath() != -1)
			{
				nCount++;
			}
		}
		catch (Exception ex)
		{
			return 0;
		}

		return nCount;
	}

	/**
	 * Reads XML and puts the data into an array. Example, sRootXpath = "/root/testdata" & sEachNode =
	 * {"var1","var2"} => Get data for "/root/testdata[i]/var1" & "/root/testdata[i]/var2" where i is for each
	 * "/root/testdata" node found.
	 * 
	 * @param sRootXpath - The root node that contains multiple sets of data
	 * @param sEachNode - All nodes that make up a specific set of data
	 * @return String[][] with all data
	 */
	public String[][] getAllData(String sRootXpath, String[] sEachNode)
	{
		// step 1: Get the number of nodes
		// step 2: Initialize the array that will hold the data
		// step 3: Use a loop to construct the unique xpath to node with data
		// step 4: Return the data
		int nNodes = getNodesCount(sRootXpath);
		String[][] data = new String[nNodes][sEachNode.length];
		for (int i = 0; i < nNodes; i++)
		{
			for (int j = 0; j < sEachNode.length; j++)
			{
				data[i][j] = getNodeValue(sRootXpath + "[" + (i + 1) + "]/" + sEachNode[j], null);
			}
		}

		return data;
	}

	/**
	 * Gets the attribute to the specified node.<BR>
	 * Note: If xpath returns multiple nodes, only the first is checked for the attribute.
	 * 
	 * @param sXpath - xpath to node
	 * @param sAttribute - attribute to find
	 * @return null if cannot find
	 */
	public String getAttribute(String sXpath, String sAttribute)
	{
		try
		{
			ap.resetXPath();
			ap.selectXPath(sXpath);

			// Evaluate the xpath which returns the node index
			int nNodeIndex = ap.evalXPath();

			// If valid index, continue to find value
			if (nNodeIndex != -1)
			{
				// Does the specific node have the attribute?
				int nAttrIndex = vn.getAttrVal(sAttribute);
				if (nAttrIndex != -1)
					return vn.toString(nAttrIndex);
			}
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Gets the ID corresponding to the xpath if it exists else the same xpath is returned.
	 * 
	 * @param sXpath
	 * @return if ID exists it is returned else sXpath
	 */
	public String getIDfromXpath(String sXpath)
	{
		String id = getAttribute(sXpath, "id");
		if (id == null)
			return sXpath;
		else
			return id;
	}

	/**
	 * This method navigates to the next node and returns the data in an array using the method extractData()<BR>
	 * <BR>
	 * <B>Note: </B>If method cannot parse your XML file, then create a new class which inherits this class
	 * and override this method & the method extractData as necessary. (This may be the case if you need data
	 * from attributes.)
	 * 
	 * @return null if no more data else array of strings
	 */
	public String[] getNextNode()
	{
		try
		{
			if (!bSetNode)
			{
				bSetNode = true;
				if (navigateToNode(toRootXpath, 1, VTDNav.FIRST_CHILD))
				{
					// Need to store state for later after getting data
					vn.push();
				}
				else
				{
					return null;
				}
			}
			else
			{
				// Restore previous state to get next sibling
				vn.pop();

				// Use the last node in toRootXpath, to go to the next sibling
				if (vn.toElement(VTDNav.NEXT_SIBLING, toRootXpath[toRootXpath.length - 1]))
				{
					// Need to store state for later after getting data
					vn.push();
				}
				else
				{
					return null;
				}
			}

			return extractData();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Navigates to the last node in the array starting from the specified node in the particular direction.
	 * 
	 * @param nodes - array of nodes to navigate through
	 * @param nStartAt - index of array node to start with
	 * @param nDirection - Direction supported by VTD-XML
	 * @return true if successfully navigated through the nodes else false
	 */
	protected boolean navigateToNode(String[] nodes, int nStartAt, int nDirection)
	{
		try
		{
			for (int i = nStartAt; i < nodes.length; i++)
			{
				// If unable to navigate to the node, then return false which indicates an error
				if (!vn.toElement(nDirection, nodes[i]))
					return false;
			}

			// Able to navigate to the node
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * This method reads the current node and returns it as an array<BR>
	 * <BR>
	 * <B>Note: </B>If method cannot parse your XML file, then create a new class which inherits this class
	 * and override this method & the method getNextNode as necessary. (This may be the case if you need data
	 * from attributes.)
	 * 
	 * @return String[]
	 */
	protected String[] extractData()
	{
		try
		{
			String[] data = new String[toEachNode.length];

			int nValueIndex;
			for (int nNodes = 0; nNodes < toEachNode.length; nNodes++)
			{
				// Store state for later
				vn.push();

				// Assume no data
				data[nNodes] = null;

				// Need to go to the node & get the data
				if (navigateToNode(toEachNode[nNodes], 0, VTDNav.FIRST_CHILD))
				{
					// Get the index of the value for this node & use this value to get node value
					nValueIndex = vn.getText();
					if (nValueIndex != -1)
						data[nNodes] = vn.toString(nValueIndex);
				}

				// Restore state from before
				vn.pop();
			}

			return data;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Method called by testNG to test getAllData and compare to the XML class performance
	 * 
	 * @throws Exception
	 */
	@Test
	public static void unitTests() throws Exception
	{
		Logs.initializeLoggers();

		Logs.log.info("Parsing Optimized for large files:  START");
		Logs.log.info("Marker file test");

		String sRootXpath3 = "Load/Documents/Document";
		String[] eachNodeXpath2 = new String[] { "DocumentDate", "Language", "StorageKey/Field" };
		VTD_XML myTest2 = new VTD_XML(
				"C:\\transfer\\Cadash\\created markerfiles\\performance\\markerfile - 1K (997).xml");
		myTest2.initializeForOptimizedParsing(sRootXpath3, eachNodeXpath2);
		String[] something2;
		something2 = myTest2.getNextNode();
		while (something2 != null)
		{
			Logs.log.info("'" + something2[0] + "' , '" + something2[1] + "', '" + something2[2] + "'");
			something2 = myTest2.getNextNode();
		}

		Logs.log.info("");
		Logs.log.info("Generic XML file parse test");
		String sRootXpath2 = "data/test";
		String[] eachNodeXpath = new String[] { "user", "password" };
		VTD_XML myTest = new VTD_XML("xmlTest.xml");
		myTest.initializeForOptimizedParsing(sRootXpath2, eachNodeXpath);
		String[] something;
		something = myTest.getNextNode();
		while (something != null)
		{
			Logs.log.info("'" + something[0] + "' , '" + something[1] + "'");
			something = myTest.getNextNode();
		}

		Logs.log.info("");
		Logs.log.info("Changing xpaths (results should not be blank)");
		sRootXpath2 = "/data/node";
		eachNodeXpath = new String[] { "/user", "password", "boolean", "/integer" };
		myTest.initializeForOptimizedParsing(sRootXpath2, eachNodeXpath);
		something = myTest.getNextNode();
		while (something != null)
		{
			Logs.log.info("'" + something[0] + "' , '" + something[1] + "', '" + something[2] + "' , '"
					+ something[3] + "'");
			something = myTest.getNextNode();
		}
		Logs.log.info("");
		Logs.log.info("Parsing Optimized for large files:  COMPLETE");
		Logs.log.info("");

		VTD_XML xml = new VTD_XML("xmlTest.xml");
		String sRootXpath = "/data/test";
		String[] sEachNode = new String[] { "user", "password" };
		String[][] data = xml.getAllData(sRootXpath, sEachNode);

		String output = "";
		for (int i = 0; i < sEachNode.length; i++)
		{
			output += sEachNode[i] + "\t";
		}
		Logs.log.info(output);

		for (int i = 0; i < data.length; i++)
		{
			output = "";
			for (int j = 0; j < data[i].length; j++)
			{
				output += "'" + data[i][j] + "'" + "\t";
			}
			Logs.log.info(output);
		}

		Logs.log.info("");
		Logs.log.info(xml.getNodeValue("//nothing", "n/a"));
		Logs.log.info(xml.getNodeValue("//node", "n/a"));
		Logs.log.info(xml.getNodeValue("//node/user", "n/a"));
		Logs.log.info(xml.getNodeValue("//node[@value='abc']/boolean", false));
		Logs.log.info(xml.getNodeValue("//node[@value='abc']//integer", -1000));
		Logs.log.info(xml.getNodeValue("//node[@value='def']/boolean", true));
		Logs.log.info(xml.getNodeValue("//node[@value='def']//integer", -1000));
		Logs.log.info("'" + xml.getAttribute("//user", "value") + "'");
		Logs.log.info("'" + xml.getAttribute("//node", "value") + "'");
		Logs.log.info("'" + xml.getIDfromXpath("//node/user") + "'");
		Logs.log.info("'" + xml.getIDfromXpath("//node[@value='def']/user") + "'");

		String sConfigFile = "C:\\workspace\\XpressSuite\\data\\orderXPRESS\\FULLPACKAGE.xml";
		VTD_XML perf = new VTD_XML(sConfigFile);
		sRootXpath = "/Data/INTTRF";
		sEachNode = new String[] { "ID", "RUN", "ACCOUNT", "TRANSFER_TO", "ACCOUNT_TO", "WITHDRAW",
				"CONTRIB", "TRANSFER", "SOURCE", "SRC_HOLDINGS", "DEST_HOLDINGS", "TRX_DESC", "LOAD" };
		Logs.log.info("VTD-XML Start Time:  " + new Date());
		String[][] dataPoint1 = perf.getAllData(sRootXpath, sEachNode);
		Logs.log.info("VTD-XML End Time:  " + new Date());
		Logs.log.info("");

		XML compareAgainst = new XML(sConfigFile);
		Logs.log.info("XML Start Time:  " + new Date());
		String[][] dataPoint2 = compareAgainst.getAllData(sRootXpath, sEachNode);
		Logs.log.info("XML End Time:  " + new Date());
		Logs.log.info("");
		Logs.log.info("Comparing data...");
		int[] mismatchRow = new int[] { -2 };
		int[] mismatchColumn = new int[] { -2 };
		if (Misc.equal(dataPoint1, dataPoint2, mismatchRow, mismatchColumn))
		{
			Logs.log.info("Arrays were equal");
		}
		else
		{
			if (mismatchRow[0] == -1)
			{
				Logs.log.error("Read data was not the same (rows count.)  dataPoint1:  " + dataPoint1.length
						+ "; dataPoint2:  " + dataPoint2.length);
				throw new GenericUnexpectedException("");
			}

			if (mismatchColumn[0] == -1)
			{
				Logs.log.error("Columns length was different.   dataPoint1:  "
						+ dataPoint1[mismatchRow[0]].length + "; dataPoint2:  "
						+ dataPoint2[mismatchRow[0]].length);
				throw new GenericUnexpectedException("");
			}

			Logs.log.error("Data was different.   dataPoint1[" + mismatchRow[0] + "][" + mismatchColumn[0]
					+ "]:  " + dataPoint1[mismatchRow[0]][mismatchColumn[0]] + "; dataPoint2["
					+ mismatchRow[0] + "][" + mismatchColumn[0] + "]:  "
					+ dataPoint2[mismatchRow[0]][mismatchColumn[0]]);
			throw new GenericUnexpectedException("");
		}
	}

	/**
	 * This method sets the string representing XPath expression
	 * 
	 * @param sXpath as String
	 * @return false in case of any error else true
	 */
	public boolean setXpath(String sXpath)
	{
		try
		{
			ap.selectXPath(sXpath);
			if (ap.evalXPath() != -1)
				return true;
			else
				return false;
		}
		catch (Exception e)
		{
			Logs.log.error(e.getMessage());
			return false;
		}
	}

	/**
	 * Reset the XPath so the XPath Expression can be reused and re-valuated in anther context position
	 */
	public void resetXpath()
	{
		ap.resetXPath();
	}

	/**
	 * Method to get the current node name i.e. tag name<BR>
	 * This method needs to be called after setting xpath using setXpath method.
	 * 
	 * @return - Name of the node or tag.
	 */
	public String getCurrentNodeName()
	{
		try
		{
			// Get the index of the value for this node
			return vn.toRawString(vn.getCurrentIndex());
		}
		catch (Exception ex)
		{
			return "";
		}

	}

	/**
	 * Method to get the current node value i.e. tag name<BR>
	 * This method needs to be called after setting xpath using setXpath method.
	 * 
	 * @return - Value of the node or tag.
	 */
	public String getCurrentNodeValue()
	{
		try
		{
			int nValueIndex = vn.getText();
			// Get the index of the value for this node
			return vn.toString((nValueIndex));

		}
		catch (Exception ex)
		{
			return "";
		}

	}
}