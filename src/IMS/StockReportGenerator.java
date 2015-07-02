package IMS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JTable;

public class StockReportGenerator 
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	private DateFormat dateFormat;
	private Date date;
	private File stockReport;
	
	public void CreateStockReport(JTable stockTable)
	{
		logger.entering(getClass().getName(), "CreateStockReport");
		
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		stockReport = new File("C:/Users/jmabbutt/workspace/JustinIMS/StockReport.txt");
		try
		{
			stockReport.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(stockReport));
			
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