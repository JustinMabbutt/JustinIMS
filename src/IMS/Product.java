package IMS;

public class Product 
{
	/*
	 * Product class - stores all information regarding products inside the database.
	 * This means that the program could keep running if connection is lost
	 */
	private String productName, price, productID, currentStock, criticalStock;
	private boolean isPorousware;
	
	public Product(String tempID, String tempName, String tempStock, String tempCrit, String tempPrice)
	{
		productID = tempID;
		productName = tempName;
		currentStock = tempStock;
		criticalStock = tempCrit;		
		price = tempPrice;
	}
	
	public void setProductID(String productID)
	{
		this.productID = productID;
	}
	
	public void setCurrentStock(String currentStock)
	{
		this.currentStock = currentStock;
	}
	
	public void setCriticalStock(String criticalStock)
	{
		this.criticalStock = criticalStock;
	}
	
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	public void assignPorousware(boolean porousware)
	{
		isPorousware = porousware;
	}
	
	public String getProductID()
	{
		return productID;
	}
	
	public String getCurrentStock()
	{
		return currentStock;
	}
	
	public String getCriticalStock()
	{
		return criticalStock;
	}
	
	public String getProductName()
	{
		return productName;
	}
	
	public String getPrice()
	{
		return price;
	}
	
	public boolean isPorousware()
	{
		return isPorousware;
	}
}