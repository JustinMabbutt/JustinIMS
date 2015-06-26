package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnector 
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	static final String JDBCDriver = "com.mysql.jdbc.Driver";
	static final String databaseURL = "jdbc:mysql://localhost/ims";
	
	static final String username = "JustinMabbutt";
	static final String password = "wicked";
	
	Connection imsConnector = null;
	
	public DatabaseConnector()
	{
		logger.entering(getClass().getName(), "DatabaseConnector");
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to Database...");
			imsConnector = DriverManager.getConnection(databaseURL, username, password);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
			System.out.println("SQLState: " + se.getMessage());
			System.out.println("VendorError: " + se.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		logger.exiting(getClass().getName(), "DatabaseConnector");
	}
}