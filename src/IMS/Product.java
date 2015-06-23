package IMS;

public class Product 
{
	private int productID, currentStock, criticalStock;
	private String productName;
	private float price;
	
	public Product(int productID, int currentStock, int criticalStock, String productName, float price)
	{
		this.productID = productID;
		this.currentStock = currentStock;
		this.criticalStock = criticalStock;
		this.productName = productName;
		this.price = price;
	}
}