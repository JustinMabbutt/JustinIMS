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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author JustinMabbutt
 *
 */

public class DatabaseConnector 
{
	/**
	 * Database Connector handles retrieval and processing of information from the database
	 */
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	static final String JDBCDriver = "com.mysql.jdbc.Driver";
	static final String databaseURL = "jdbc:mysql://10.50.20.14:3306/ims";
	//static final String databaseURL = "jdbc:mysql://localhost/ims";
	static final String username = "JustinMabbutt";
	static final String password = "wicked";
	
	private Random randomGen = new Random();
	private Connection imsConnector = null;
	private Statement imsStatement = null;
	private ResultSet imsResultSet = null, generatedKeys = null;
	private PreparedStatement updateStock, addStock;
	private ArrayList<Product> products = new ArrayList<Product>();
	private int rowCount = 0;
	private float randomStock;
	private float[] diffStock, timeToDelivery, prevStock;
	Object[] newProduct = new Object[6];
	private DefaultTableModel tempTableModel = new DefaultTableModel();
	private String updateQuery, orderPrediction, dayOrDays, addQuery;
	
	/**
	 * Initialise database connection
	 */
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
	
	/**
	 * Close database connection at system exit
	 */
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
	
	/**
	 * Retrieve amount of products (rows)
	 * @return amount of products
	 */
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
			logger.log(Level.SEVERE, se.getMessage(), se);
		}
		logger.exiting(getClass().getName(), "getAmountOfProducts");
		return rowCount;
	}
	
	/**
	 * Load product information from database into table model
	 */
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
				if(String.valueOf(tempTableModel.getValueAt(i, 5)) == "true")
				{
					products.get(i).assignPorousware(true);
				}
				else
				{
					products.get(i).assignPorousware(false);
				}
				randomStock = randomGen.nextFloat() * 20.f;
				prevStock[i] = 60.f + randomStock;
			}
		}
		catch(SQLException se)
		{
			logger.log(Level.SEVERE, se.getMessage(), se);
		}
		logger.exiting(getClass().getName(), "loadData");
	}
	
	/**
	 * Update stock level of a product
	 * @param productID
	 * @param stockChange
	 */
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
			logger.log(Level.SEVERE, se.getMessage(), se);
		}
		logger.exiting(getClass().getName(), "updateStockLevel");
	}
	
	/**
	 * Add new product to database and GUI table
	 * @param name
	 * @param quantity
	 * @param crit
	 * @param price
	 */
	public void addNewProduct(String name, int quantity, int crit, float price, int porousware)
	{
		logger.entering(getClass().getName(), "addNewProduct");
		try 
		{
			addQuery = "insert into product(ProductName, StockLevel, CriticalThreshold, Price, Porousware) values (?, ?, ?, ?, ?)";
			addStock = imsConnector.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
			addStock.setString(1, name); newProduct[1] = name;
			addStock.setInt(2, quantity); newProduct[2] = quantity;
			addStock.setInt(3, crit); newProduct[3] = crit;
			addStock.setFloat(4, price); newProduct[4] = price;
			addStock.setFloat(5, porousware);
			if(porousware == 1)
			{
				newProduct[5] = "false";
			}
			else
			{
				newProduct[5] = "true";
			}
			addStock.executeUpdate();
			generatedKeys = addStock.getGeneratedKeys();
			while(generatedKeys.next())
			{
				if(generatedKeys.last())
				{
					newProduct[0] = generatedKeys.getInt(1);
				}
			}
			tempTableModel.insertRow(getAmountOfProducts() - 1, newProduct);
		} 
		catch (SQLException se) 
		{
			logger.log(Level.SEVERE, se.getMessage(), se);
		}
		logger.exiting(getClass().getName(), "addNewProduct");
	}
	
	/**
	 * Build table model from result set
	 * @param rs the result set of the query
	 * @return the table model for use in the GUI
	 */
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
			logger.log(Level.SEVERE, se.getMessage(), se);
		}
		logger.exiting(getClass().getName(), "buildTableModel");
		return new DefaultTableModel(data, columnNames);
	}
	
	/**
	 * Get table from database
	 * @return table model for use in GUI
	 */
	public DefaultTableModel getTableModel()
	{
		return tempTableModel;
	}
	
	/**
	 * Get predictions of delivery requirements using rate of sale calculation
	 * @return String of delivery predictions
	 */
	public String getDeliveryPrediction()
	{
		logger.entering(getClass().getName(), "getOrderPrediction");
		orderPrediction = "NB Gardens Delivery Predictions" + "\n";
		for(int i = 0; i < getAmountOfProducts(); i++)
		{
			diffStock[i] = prevStock[i] - Float.parseFloat(products.get(i).getCurrentStock());
			if(diffStock[i] > 0.f)
			{
				timeToDelivery[i] = Float.parseFloat(products.get(i).getCurrentStock()) / diffStock[i];
				if((int)Math.ceil(timeToDelivery[i] * 7.f) == 1)
				{
					dayOrDays = " day";
				}
				else
				{
					dayOrDays = " days";
				}
				orderPrediction += "Stock level of product with ID " + (i + 1) + " has decreased by " + (int)Math.floor(diffStock[i]) + " since the last stock check. \n"
				+ "This means that a delivery will be required in " + ((int)Math.ceil(timeToDelivery[i] * 7.f)) + dayOrDays + " at current rate of sale.";
				if((i + 1) != getAmountOfProducts())
				{
					orderPrediction += "\n \n";
				}
			}
			else
			{
				orderPrediction += "Stock level of product with ID " + (i + 1) + " has increased by " + (int)Math.floor(diffStock[i]) + " since the last stock check. \n"
						+ "This means that a delivery will not be not required for the time being.";
			}
		}
		logger.exiting(getClass().getName(), "getOrderPrediction");
		return orderPrediction;
	}
}