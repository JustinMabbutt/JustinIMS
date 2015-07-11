package IMS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JTable;

/**
 * 
 * @author JustinMabbutt
 *
 */

public class StockReportGenerator 
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	private DateFormat dateFormat;
	private Date date;
	private String stockReportFilePath, purchaseOrderFilePath;
	BufferedWriter writer;
	
	public void createPurchaseOrder(JTable stockTable)
	{
		logger.entering(getClass().getName(), "createPurchaseOrder");
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		purchaseOrderFilePath = "C:/Users/justi_000/workspace/JustinIMS/PurchaseOrder.txt";
		try
		{
			writer = new BufferedWriter(new FileWriter(purchaseOrderFilePath));
			writer.write("NB Gardens Purchase Order - " + dateFormat.format(date));
			writer.newLine();
			writer.newLine();
			
			for(int i = 0; i < stockTable.getColumnCount(); i++)
			{
				writer.write(stockTable.getColumnName(i));
				writer.write("\t");
			}
			
			for(int i = 0; i < stockTable.getRowCount(); i++)
			{
				if(Integer.parseInt(String.valueOf(stockTable.getValueAt(i, 2))) < Integer.parseInt(String.valueOf(stockTable.getValueAt(i, 3))))
				{
					writer.newLine();
					for(int j = 0; j < stockTable.getColumnCount(); j++)
					{
						writer.write(String.valueOf(stockTable.getValueAt(i, j)));
						if(String.valueOf(stockTable.getValueAt(i, j)).length() >= 16)
						{
							writer.write("\t");
						}
						else
						{
							writer.write("\t"); writer.write("\t");
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		logger.exiting(getClass().getName(), "createPurchaseOrder");
	}
	
	public void createStockReport(JTable stockTable)
	{
		logger.entering(getClass().getName(), "CreateStockReport");
		
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		stockReportFilePath = "C:/Users/justi_000/workspace/JustinIMS/StockReport.txt";
		try
		{
			writer = new BufferedWriter(new FileWriter(stockReportFilePath));
			
			writer.write("NB Gardens Stock Report - " + dateFormat.format(date));
			writer.newLine();
			writer.newLine();
			
			for(int i = 0; i < stockTable.getColumnCount(); i++)
			{
				writer.write(stockTable.getColumnName(i));
				writer.write("\t");
			}
			
			for(int i = 0; i < stockTable.getRowCount(); i++)
			{
				writer.newLine();
				for(int j = 0; j < stockTable.getColumnCount(); j++)
				{
					writer.write(String.valueOf(stockTable.getValueAt(i, j)));
					if(String.valueOf(stockTable.getValueAt(i, j)).length() >= 16)
					{
						writer.write("\t");
					}
					else
					{
						writer.write("\t"); writer.write("\t");
					}
				}
			}
			writer.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		logger.exiting(getClass().getName(), "CreateStockReport");
	}
}