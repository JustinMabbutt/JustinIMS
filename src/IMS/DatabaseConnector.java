package IMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author JustinMabbutt
 *
 */

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
	private ArrayList<Product> products = new ArrayList<Product>();
	private int rowCount = 0, IDCount = 48;
	private float randomStock;
	private float[] diffStock, timeToDelivery, prevStock;
	Object[] newProduct = new Object[5];
	private DefaultTableModel tempTableModel = new DefaultTableModel();
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
		loadData();
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
	
	public void loadData()
	{
		logger.entering(getClass().getName(), "loadData");
		try
		{
			tempTableModel = new DefaultTableModel();
			imsStatement = imsConnector.createStatement();
			imsResultSet = imsStatement.executeQuery("SELECT count(*) FROM product");
			imsResultSet.next();
			rowCount = imsResultSet.getInt(1);
			products.clear();
			diffStock = new float[getAmountOfProducts()];
			timeToDelivery = new float[getAmountOfProducts()];
			prevStock = new float[getAmountOfProducts()];
			
			imsResultSet = imsStatement.executeQuery("SELECT * FROM product");
			tempTableModel = buildTableModel(imsResultSet);
			for(int i = 0; i < getAmountOfProducts(); i++)
			{	
				products.add(new Product(
						String.valueOf(tempTableModel.getValueAt(i, 0)),
						String.valueOf(tempTableModel.getValueAt(i, 1)),
						String.valueOf(tempTableModel.getValueAt(i, 2)),
						String.valueOf(tempTableModel.getValueAt(i, 3)),
						String.valueOf(tempTableModel.getValueAt(i, 4))));
				randomStock = randomGen.nextFloat() * 20.f;
				prevStock[i] = 60.f + randomStock;
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		logger.exiting(getClass().getName(), "loadData");
	}
	
	public void updateStockLevel(int productID, int stockChange)
	{
		logger.entering(getClass().getName(), "updateStockLevel");
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
		logger.exiting(getClass().getName(), "updateStockLevel");
	}
	
	public void addNewProduct(String name, int quantity, int crit, float price)
	{
		logger.entering(getClass().getName(), "addNewProduct");
		try 
		{
			addQuery = "insert into product(ProductName, StockLevel, CriticalThreshold, Price) values (?, ?, ?, ?)";
			addStock = imsConnector.prepareStatement(addQuery); newProduct[0] = IDCount;
			addStock.setString(1, name); newProduct[1] = name;
			addStock.setInt(2, quantity); newProduct[2] = quantity;
			addStock.setInt(3, crit); newProduct[3] = crit;
			addStock.setFloat(4, price); newProduct[4] = price;
			addStock.executeUpdate();
			tempTableModel.insertRow(getAmountOfProducts() - 1, newProduct);
			IDCount++;
		} 
		catch (SQLException se) 
		{
			se.printStackTrace();
		}
		logger.exiting(getClass().getName(), "addNewProduct");
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
		logger.entering(getClass().getName(), "getOrderPrediction");
		orderPrediction = "NB Gardens Delivery Predictions" + "\n";
		for(int i = 0; i < getAmountOfProducts(); i++)
		{
			diffStock[i] = prevStock[i] - Float.parseFloat(products.get(i).getCurrentStock());
			timeToDelivery[i] = Float.parseFloat(products.get(i).getCurrentStock()) / diffStock[i];
			if((int)Math.ceil(timeToDelivery[i] * 7.f) == 1)
			{
				dayOrDays = " day";
			}
			else
			{
				dayOrDays = " days";
			}
			orderPrediction += "Stock level of product with ID " + (i + 1) + " will drop below critical threshold in " + (int)Math.ceil(timeToDelivery[i] * 7.f) + dayOrDays + " at current rate of sale.";
			if((i + 1) != getAmountOfProducts())
			{
				orderPrediction += "\n";
			}
		}
		logger.exiting(getClass().getName(), "getOrderPrediction");
		return orderPrediction;
	}
}