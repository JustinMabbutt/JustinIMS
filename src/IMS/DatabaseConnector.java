package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector 
{
	static final String JDBCDriver = "com.mysql.jdbc.Driver";
	static final String databaseURL = "jdbc:mysql://localhost/JustinIMS";
	
	static final String username = "JustinMabbutt";
	static final String password = "wicked";
	
	Connection imsConnector = null;
	
	public DatabaseConnector()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to Database...");
			imsConnector = DriverManager.getConnection(databaseURL, username, password);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}