package IMS;

public class Product 
{
	/**
	 * Product class - stores all information regarding products inside the database.
	 * This means that the program could keep running if connection is lost
	 */
	private String productName, price, productID, currentStock, criticalStock;
	private boolean isPorousware;
	
	/**
	 * Create new instance of product
	 * @param tempID
	 * @param tempName
	 * @param tempStock
	 * @param tempCrit
	 * @param tempPrice
	 */
	public Product(String tempID, String tempName, String tempStock, String tempCrit, String tempPrice)
	{
		productID = tempID;
		productName = tempName;
		currentStock = tempStock;
		criticalStock = tempCrit;		
		price = tempPrice;
	}
	
	/**
	 * Set product ID
	 * @param productID
	 */
	public void setProductID(String productID)
	{
		this.productID = productID;
	}
	
	/**
	 * Set current stock level
	 * @param currentStock
	 */
	public void setCurrentStock(String currentStock)
	{
		this.currentStock = currentStock;
	}
	
	/**
	 * Set critical stock level
	 * @param criticalStock
	 */
	public void setCriticalStock(String criticalStock)
	{
		this.criticalStock = criticalStock;
	}
	
	/**
	 * Set product name
	 * @param productName
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	/**
	 * Set product price
	 * @param price
	 */
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	/**
	 * Assign porousware
	 * @param porousware
	 */
	public void assignPorousware(boolean porousware)
	{
		isPorousware = porousware;
	}
	
	/**
	 * Retrieve product ID
	 * @return
	 */
	public String getProductID()
	{
		return productID;
	}
	
	/**
	 * Retrieve current stock level
	 * @return
	 */
	public String getCurrentStock()
	{
		return currentStock;
	}
	
	/**
	 * Retrieve critical stock level
	 * @return
	 */
	public String getCriticalStock()
	{
		return criticalStock;
	}
	
	/**
	 * Retrieve product name
	 * @return
	 */
	public String getProductName()
	{
		return productName;
	}
	
	/**
	 * Retrieve product price
	 * @return
	 */
	public String getPrice()
	{
		return price;
	}
	
	/**
	 * Retrieve porousware status
	 * @return
	 */
	public boolean isPorousware()
	{
		return isPorousware;
	}
}