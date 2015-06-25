package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector 
{
	private Connection imsConnector;
	
	public Connection ConnectToDatabase()
	{
		String url = "jdbc:mysql://localhost/JustinIMS" + "user=JustinMabbutt&password=wicked";
		try 
		{
			Class.forName("com.mysql.jdbc.driver").newInstance();
		} 
		catch (InstantiationException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IllegalAccessException e1) 
		{
			e1.printStackTrace();
		} 
		catch (ClassNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			imsConnector = DriverManager.getConnection(url);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return imsConnector;
	}
}