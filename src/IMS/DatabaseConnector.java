package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

public class DatabaseConnector 
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	static final String JDBCDriver = "com.mysql.jdbc.Driver";
	static final String databaseURL = "jdbc:mysql://localhost/ims";
	
	static final String username = "JustinMabbutt";
	static final String password = "wicked";
	
	private Connection imsConnector = null;
	private Statement imsStatement = null;
	private ResultSet imsResultSet = null;
	private PreparedStatement updateStock;
	private Product[] products;
	private int rowCount = 0;
	private float[] diffStock, timeToDelivery, prevStock = 
		{59.f, 55.f, 30.f, 24.f, 89.f, 50.f, 35.f, 40.f, 52.f, 76.f, 68.f, 50.f, 52.f, 30.f, 74.f, 35.f,
		 53.f, 43.f, 50.f, 33.f, 55.f, 52.f, 83.f, 80.f, 38.f, 52.f, 88.f, 49.f, 57.f, 50.f, 58.f, 32.f, 70.f};
	private DefaultTableModel tempTableModel;
	private String updateQuery, orderPrediction;
	
	public DatabaseConnector()
	{
		logger.entering(getClass().getName(), "DatabaseConnector");
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Database connection successful");
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
		updateTable();
		logger.exiting(getClass().getName(), "DatabaseConnector");
	}
	
	public void closeDatabaseConnection()
	{
		logger.entering(getClass().getName(), "closeDatabaseConnection");
		try
		{
			if(imsConnector != null)
			{
				System.out.println("Database connection closed");
				imsConnector.close();
				imsResultSet.close();
				imsStatement.close();
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		logger.exiting(getClass().getName(), "closeDatabaseConnection");
	}
	
	public int getAmountOfProducts()
	{
		logger.entering(getClass().getName(), "getAmountOfProducts");
		try 
		{
			imsStatement = imsConnector.createStatement();	
			imsResultSet = imsStatement.executeQuery("select count(*) from product");
			imsResultSet.next();
			rowCount = imsResultSet.getInt(1);
		} 
		catch (SQLException se) 
		{
			se.printStackTrace();
		}
		logger.exiting(getClass().getName(), "getAmountOfProducts");
		return rowCount;
	}
	
	public void updateTable()
	{
		try
		{
			imsStatement = imsConnector.createStatement();
			imsResultSet = imsStatement.executeQuery("select count(*) from product");
			imsResultSet.next();
			rowCount = imsResultSet.getInt(1);
			products = new Product[getAmountOfProducts()];
			diffStock = new float[getAmountOfProducts()];
			timeToDelivery = new float[getAmountOfProducts()];
			
			imsResultSet = imsStatement.executeQuery("select * from product");
			tempTableModel = buildTableModel(imsResultSet);
			for(int i = 0; i < getAmountOfProducts(); i++)
			{	
				products[i] = new Product(
						String.valueOf(tempTableModel.getValueAt(i, 0)),
						String.valueOf(tempTableModel.getValueAt(i, 1)),
						String.valueOf(tempTableModel.getValueAt(i, 2)),
						String.valueOf(tempTableModel.getValueAt(i, 3)),
						String.valueOf(tempTableModel.getValueAt(i, 4)));
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public void updateStockLevel(int productID, int stockChange)
	{
		try
		{
			updateQuery = "update product set StockLevel = ? where ProductID = ?";
			updateStock = imsConnector.prepareStatement(updateQuery);
			updateStock.setInt(1, productID);
			updateStock.setInt(2, stockChange);
			updateStock.executeQuery();
			updateTable();
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public DefaultTableModel buildTableModel(ResultSet rs)
	{
		logger.entering(getClass().getName(), "buildTableModel");
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();
		
		try 
		{
			ResultSetMetaData metaData = rs.getMetaData();			
			
			int columnCount = metaData.getColumnCount();
			for(int column = 1; column <= columnCount; column++)
			{
				columnNames.add(metaData.getColumnName(column));
			}		
			while(rs.next())
			{
				Vector<Object> vector = new Vector<Object>();
				for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
				{
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}
		}
		catch (SQLException se) 
		{
			se.printStackTrace();
		}
		logger.exiting(getClass().getName(), "buildTableModel");
		return new DefaultTableModel(data, columnNames);
	}
	
	public DefaultTableModel getTableModel()
	{
		return tempTableModel;
	}
	
	public String getOrderPrediction()
	{
		orderPrediction = "NB Gardens Delivery Predictions" + "\n";
		for(int i = 0; i < getAmountOfProducts(); i++)
		{
			diffStock[i] = prevStock[i] - Float.parseFloat(products[i].getCurrentStock());
			timeToDelivery[i] = Float.parseFloat(products[i].getCurrentStock()) / diffStock[i];
			orderPrediction += "Stock level of product with ID " + i + " will drop below critical threshold in " + Math.ceil(timeToDelivery[i] * 7.f) + " days at current rate of sale." + "\n";
		}
		return orderPrediction;
	}
}