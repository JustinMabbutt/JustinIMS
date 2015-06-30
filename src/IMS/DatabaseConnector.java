package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.DayOfWeek;
import java.util.Random;
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
	
	private Random randomGen = new Random();
	private Connection imsConnector = null;
	private Statement imsStatement = null;
	private ResultSet imsResultSet = null;
	private PreparedStatement updateStock, addStock;
	private Product[] products;
	private int rowCount = 0;
	private float randomStock;
	private float[] diffStock, timeToDelivery, prevStock;	
	private DefaultTableModel tempTableModel;
	private String updateQuery, orderPrediction, dayOrDays, addQuery;
	
	public DatabaseConnector()
	{
		logger.entering(getClass().getName(), "DatabaseConnector");
		try
		{
			Class.forName("com.mysql.jdbc.Driver");		
			imsConnector = DriverManager.getConnection(databaseURL, username, password);
			System.out.println("Database connection successful");
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
			prevStock = new float[getAmountOfProducts()];
			
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
				randomStock = randomGen.nextFloat() * 20.f;
				prevStock[i] = 60.f + randomStock;
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
			updateStock.setInt(1, stockChange);
			updateStock.setInt(2, productID);
			updateStock.executeUpdate();
			tempTableModel.setValueAt(stockChange, productID - 1, 2);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public void addNewProduct(String name, int quantity, int crit, float price)
	{
		try 
		{
			addQuery = "insert into product(ProductName, StockLevel, CriticalThreshold, Price) values (?, ?, ?, ?)";
			addStock = imsConnector.prepareStatement(addQuery);
			addStock.setString(1, name);
			addStock.setInt(2, quantity);
			addStock.setInt(3, crit);
			addStock.setFloat(4, price);
			addStock.executeUpdate();
			tempTableModel.addRow(new Object[]{null, name, Integer.toString(quantity), Integer.toString(crit),  Float.toString(price)});
		} 
		catch (SQLException se) 
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
			if((int)Math.ceil(timeToDelivery[i] * 7.f) == 1)
			{
				dayOrDays = " day";
			}
			else
			{
				dayOrDays = " days";
			}
			orderPrediction += "Stock level of product with ID " + (i + 1) + " will drop below critical threshold in " + (int)Math.ceil(timeToDelivery[i] * 7.f) + dayOrDays + " at current rate of sale." + "\n";
		}
		return orderPrediction;
	}
}