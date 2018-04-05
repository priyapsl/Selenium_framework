package com.advantage.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.advantage.reporting.Logs;

//import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

/**
 * This class uses the MICROSOFT SQL Server JDBC DRIVER library (sqljdbc4.jar) to connect to the DB. For
 * integrated security, the DLL sqljdbc_auth.dll is required & it only works on windows OS. From Eclipse there
 * should be no issues once the library is added because the required DLL for integrated security is in a
 * sub-folder. However, if there is an issue using integrated security, then copy the sqljdbc_auth.dll to the
 * project folder and this should resolve the issue.
 */
/**
 * Prerequisite for MySQL - 
 * Installation of mysql and supported version of mysql.jconnector.java.
 * Add mysql.jconnector.java jar file in java project.
 * Provide GRANT ALL permission for all databases. 
 * 
 */

public class Database {

	private int nDefaultPort = 1433;
	private boolean bIntegratedSecurity;
	private String sUser;
	private String sPassword;
	private String sServer;
	private int nPort;
	private String sDatabaseName;
	public Connection dbConnection = null;

	/**
	 * Initializes class with default port, Integrated Security Enabled, username/password are set to null
	 * 
	 * @param sServer - DB Server
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sServer, String sDatabaseName)
	{
		bIntegratedSecurity = true;
		set(null, null, sServer, nDefaultPort, sDatabaseName);
	}

	/**
	 * Initializes class with Integrated Security Enabled, username/password are set to null
	 * 
	 * @param sServer - DB Server
	 * @param nPort - Port to connect to DB Server on
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sServer, int nPort, String sDatabaseName)
	{
		bIntegratedSecurity = true;
		set(null, null, sServer, nPort, sDatabaseName);
	}

	/**
	 * Integrated Security is set based on sUser value and use the default port.<BR>
	 * <BR>
	 * Note: Recommended to use this constructor.
	 * 
	 * @param sUser - Username to use to connect
	 * @param sPassword - Password to use to connect
	 * @param sServer - DB Server
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sUser, String sPassword, String sServer, String sDatabaseName)
	{
		set(sUser, sPassword, sServer, nDefaultPort, sDatabaseName);
		setIntegratedSecurityBasedOnUserValue();
	}

	/**
	 * Integrated Security is set based on sUser value
	 * 
	 * @param sUser - Username to use to connect
	 * @param sPassword - Password to use to connect
	 * @param sServer - DB Server
	 * @param nPort - Port to connect to DB Server on
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sUser, String sPassword, String sServer, int nPort, String sDatabaseName)
	{
		set(sUser, sPassword, sServer, nPort, sDatabaseName);
		setIntegratedSecurityBasedOnUserValue();
	}

	public void set(String sUser, String sPassword, String sServer, int nPort, String sDatabaseName)
	{
		this.sUser = sUser;
		this.sPassword = sPassword;
		this.sServer = sServer;
		this.nPort = nPort;
		this.sDatabaseName = sDatabaseName;
	}

	public int getDefaultPort()
	{
		return nDefaultPort;
	}

	public boolean getIntegratedSecurity()
	{
		return bIntegratedSecurity;
	}

	public String getUser()
	{
		return sUser;
	}

	public String getPassword()
	{
		return sPassword;
	}

	public String getServer()
	{
		return sServer;
	}

	public int getPort()
	{
		return nPort;
	}

	public String getDatabaseName()
	{
		return sDatabaseName;
	}

	/**
	 * Enable Integrated Security. If you use a constructor that sets the username/password, then Integrated
	 * Security is disabled by default.<BR>
	 * <BR>
	 * Note: This required the sqljdbc_auth.dll to be in the java.library.path
	 */
	public void enableIntegratedSecurity()
	{
		bIntegratedSecurity = true;
	}

	/**
	 * Disable Integrated Security. If you use a constructor that does not specify username/password, then
	 * Integrated Security is enabled by default.
	 */
	public void disableIntegratedSecurity()
	{
		bIntegratedSecurity = false;
	}

	/**
	 * Sets bIntegratedSecurity to true if sUser is null or empty string else set to false.
	 */
	public void setIntegratedSecurityBasedOnUserValue()
	{
		if (sUser == null || sUser.equals(""))
			bIntegratedSecurity = true;
		else
			bIntegratedSecurity = false;
	}

	/**
	 * Opens a DB connection
	 * 
	 * @return DB Connection to work with
	 * @throws Exception
	 *             if fails to open connection
	 */
	public Connection openConnection(String providerMnemonic)
	{
		try
		{
			DBConfigurator.loadDriver(providerMnemonic);
		}
		catch (Exception exception)
		{
			Logs.logException("Unable to load DB driver. Exception Occurred", exception);
		}
		Map<String, String> dbProperties = new HashMap<String, String>();
		dbProperties.put(DBConfigurator.SERVER, sServer);
		int serverPort = (nPort == 0) ? nDefaultPort : nPort;
		dbProperties.put(DBConfigurator.PORT, Integer.toString(serverPort));
		dbProperties.put(DBConfigurator.DATABASE_NAME, sDatabaseName);
		//dbProperties.put(DBConfigurator.INTEGRATED_SECURITY, Boolean.toString(bIntegratedSecurity));

		String url = DBConfigurator.buildConnectionUrl(providerMnemonic, dbProperties);
		System.out.println("Connecting to DB : " + providerMnemonic + " through URL : " + url);

		try
		{
			
			dbConnection = DriverManager.getConnection(url, sUser, sPassword);
			System.out.println(dbConnection);
		}
		catch (Exception exception)
		{
			Logs.logException("Unable to connect to DB. Exception Occurred", exception);
		}
		return dbConnection;
	}

	/**
	 * Closes a DB connection
	 * 
	 * @param con - DB connection to close
	 */
	public static void closeConnection(Connection con)
	{
		try
		{
			con.close();
		}
		catch (SQLException e)
		{
		}
	}

	/**
	 * Executes the given query. (There is no parameterization with this method. So, query cannot have any
	 * parameters.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			System.out.println("Exception occurred executing ");
			System.out.println("Query:\t" + sQuery);
			System.out.println("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes the given query that contains parameters (all strings). (Parameters must be represented as
	 * question marks which is Java limitation.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @param sParameterValue - Values to substitute for the parameters in order
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery, String[] sParameterValue)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterValue.length; i++)
			{
				pstmt.setString(i + 1, sParameterValue[i]);
			}

			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			/*
			 * Put all the parameters in a string to log.
			 */
			String sParametersList = "";
			for (int i = 0; i < sParameterValue.length; i++)
			{
				sParametersList += sParameterValue[i];
				if ((i + 1) < sParameterValue.length)
					sParametersList += ", ";
			}

			System.out.println("Exception occurred executing ");
			System.out.println("Query:\t" + sQuery);
			System.out.println("Parameters (in order):\t" + sParametersList);
			System.out.println("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes the given query that contains parameters (any type). (Parameters must be represented as
	 * question marks which is Java limitation.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @param sParameterDetails - array that contains the value and type for each parameter
	 *            ex. { {"1", "int"}, {"true", "boolean"}, {"something", ""} }
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery, String[][] sParameterDetails)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			System.out.println(sParameterDetails.length);
			// Add the parameter values to the query
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				/*
				 * Is the parameter type int, boolean or string (default if no
				 * match)?
				 */
				System.out.println(sParameterDetails[i][1]);
				if (sParameterDetails[i][1].equals("int"))
					pstmt.setInt(i + 1, Integer.parseInt(sParameterDetails[i][0]));
				else if (sParameterDetails[i][1].equals("boolean"))
					pstmt.setBoolean(i + 1, Boolean.parseBoolean(sParameterDetails[i][0]));
				else
					pstmt.setString(i + 1, sParameterDetails[i][0]);
			}
			System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			/*
			 * Put all the parameters in a string to log.
			 */
			String sParametersList = "";
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				sParametersList += sParameterDetails[i][0];
				if ((i + 1) < sParameterDetails.length)
					sParametersList += ", ";
			}

			System.out.println("Exception occurred executing ");
			System.out.println("Query:\t" + sQuery);
			System.out.println("Parameters (in order):\t" + sParametersList);
			System.out.println("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes query that updates the DB (no parameterization)
	 * 
	 * @param con
	 * @param sQuery
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);
			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			System.out.println(ex);
			return false;
		}
	}

	/**
	 * Executes query that updates the DB with all string parameters
	 * 
	 * @param con
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param sParameterValue - values of parameters in order
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery, String[] sParameterValue)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterValue.length; i++)
			{
				pstmt.setString(i + 1, sParameterValue[i]);
			}

			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			System.out.println(ex);
			return false;
		}
	}

	/**
	 * Executes query that updates the DB with all string parameters
	 * 
	 * @param con
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param sParameterDetails - array that contains the value and type for each parameter
	 *            ex. { {"1", "int"}, {"true", "boolean"}, {"something", ""} }
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery, String[][] sParameterDetails)
	{
		try
		{
			System.out.println(sQuery);
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				/*
				 * Is the parameter type int, boolean or string (default if no
				 * match)?
				 */
				if (sParameterDetails[i][1].equals("int"))
					pstmt.setInt(i + 1, Integer.parseInt(sParameterDetails[i][0]));
				else if (sParameterDetails[i][1].equals("boolean"))
					pstmt.setBoolean(i + 1, Boolean.parseBoolean(sParameterDetails[i][0]));
				else
					pstmt.setString(i + 1, sParameterDetails[i][0]);
			}
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			System.out.println(ex);
			return false;
		}
	}

	/**
	 * Run any query (no parameterization) and log the results
	 * 
	 * @param sQuery - Query to run (no parameterization)
	 */
	public void runAdHocQuery(String sQuery)
	{
		Database db = new Database(sUser, sPassword, sServer, nPort, sDatabaseName);
		Connection con = null;
		try
		{
			System.out.println("Ad Hoc Query to be executed:  " + sQuery);
			System.out.println("");

			con = db.openConnection("MSSql");
			ResultSet rs = db.executeQuery(con, sQuery);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			int nRow = 1;
			while (rs.next())
			{
				System.out.println("*****");
				System.out.println("Row:  " + nRow);
				System.out.println("*****");

				// Output data for the row
				for (int i = 1; i < columnCount + 1; i++)
				{
					System.out.println(rsmd.getColumnName(i) + ":  " + rs.getString(i));
				}

				System.out.println("");
				nRow++;
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		finally
		{
			Database.closeConnection(con);
		}
	}

	/**
	 * Returns sServer, sDatabaseName, nPort, sUser, sPassword & bIntegratedSecurity
	 */
	public String toString()
	{
		return "DB Server = '" + sServer + "', DB Name = '" + sDatabaseName + "', DB Port = " + nPort
				+ ", DB User = '" + sUser + "', DB Password = '" + sPassword + "', Integrated Security = "
				+ bIntegratedSecurity;
	}

	/**
	 * Method called by testNG to test this class
	 * 
	 * @throws Exception
	 */
	@Test
	public static void unitTest() throws Exception
	{
		// Logs.initializeLoggers();

		Database db;
		// Non Integrated Security
		// db = new Database("dneill", null, "HDB01", 1433, "HPERF_DB");

		// Integrated Security. Uses logged in person's credentials.
		// db = new Database("HDB01", "HPERF_DB");
		db = new Database("root", "root", "10.222.37.38", "db_webdriver");

		// connecting to Excel.
		// db = new Database("dbtest.xls", "", "Excel");
		// String SQL = "Select * from [Sheet1$]";

		Connection con = db.openConnection("MySql");
		String SQL = "select * from db_webdriver.environment";

		ResultSet rs = db.executeQuery(con, SQL);

		while (rs.next())
		{
			System.out.println(rs.getString("browser") + ", " + rs.getString("osname"));
			// System.out.println(rs.getString("sr_no") + ", " + rs.getString("name"));
		}

		/*System.out.println("*************************");

		SQL = "select top 10 P.IVR_SYSID as 'A', P.ALT_PLN_NUM as 'B' from PLN P (nolock) where P.IVR_SYSID = ? and P.ALT_PLN_NUM = ?";
		String[] sParameter = new String[] { "9035239", "H5847924" };
		rs = db.executeQuery(con, SQL, sParameter);
		while (rs.next())
		{
			System.out.println(rs.getString("A") + ", " + rs.getString("B"));
		}
*/
		Database.closeConnection(con);
	}

	@Test
	public static void unitTest1() throws Exception
	{
		unitTest();
	}
}
