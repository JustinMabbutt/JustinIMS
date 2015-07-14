package IMS;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * 
 * @author JustinMabbutt
 *
 */

public class IMSGUI extends JFrame
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	StockReportGenerator reportGenerator = new StockReportGenerator();
	DatabaseConnector dbConnect = new DatabaseConnector();
	private JFrame mainFrame = new JFrame(), helpFrame = new JFrame(), predictionsFrame = new JFrame();
	private JPanel stockPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JButton timeSimulation = new JButton();
	private JButton changeQuantity = new JButton();
	private JButton orderPrediction = new JButton();
	private JTable productTable;
	private DefaultTableCellRenderer tableRenderer;
	private TableModel stockChecker;
	private int predictDelivery, startSimulation, randomID;
	private String deliveryPredictions, changeStockLevel, changeStockID, addNewName, addNewQuantity, addNewCrit, addNewPrice;
	private Timer simTime = new Timer();
	private double randomDecrement;
	private Random randomGenerator = new Random();
	private SimulationRunning simRun = SimulationRunning.OFF;
	private Image nbGardensLogo;
	private ImageIcon nbLogo;
	
	public IMSGUI() 
	{
		logger.entering(getClass().getName(), "IMSGUI");
        initUI();
        logger.exiting(getClass().getName(), "IMSGUI");
    }

    private void initUI() 
    {
    	logger.entering(getClass().getName(), "initGUI");
        createMenuBar();
        createStockGrid(dbConnect.getTableModel());
        createButtons();
        mainFrame.setTitle("NB Gardens Inventory Management System");
        mainFrame.setSize(800, 500);
        helpFrame.setTitle("NB Gardens Inventory Management System - User Guide");
        helpFrame.setSize(400, 250);
        predictionsFrame.setTitle("NB Gardens Inventory Management System - Delivery Predictions");
        predictionsFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        predictionsFrame.setLocationRelativeTo(null);
        predictionsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainFrame.setResizable(false);
        helpFrame.setResizable(false);
        predictionsFrame.setResizable(false);
        nbLogo = new ImageIcon("images/nb.png");
        nbGardensLogo = nbLogo.getImage();
        mainFrame.setIconImage(nbGardensLogo);
        helpFrame.setIconImage(nbGardensLogo);
        predictionsFrame.setIconImage(nbGardensLogo);
        mainFrame.setLayout(new BorderLayout());
        helpFrame.setLayout(new BorderLayout());
        predictionsFrame.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(3, 1));
        mainFrame.add(buttonPanel, BorderLayout.EAST);
        mainFrame.add(stockPanel);
        mainFrame.addWindowListener(new WindowAdapter() 
        {
        	public void windowClosing(WindowEvent evt) 
        	{
        		dbConnect.closeDatabaseConnection();
        	    System.exit(0);
        	}
        });
        mainFrame.setVisible(true);
        logger.exiting(getClass().getName(), "initGUI");
    }
    
    public void showUI()
    {
    	mainFrame.setVisible(true);
    }

    private void createMenuBar() 
    {
    	logger.entering(getClass().getName(), "createMenuBar");
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");
		ImageIcon exitIcon = new ImageIcon("images/exit.jpg");
        JMenuItem exit = new JMenuItem("Exit", exitIcon);
        exit.setToolTipText("Exit application");
        exit.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	dbConnect.closeDatabaseConnection();
       	     	System.exit(0);
            }
        });
        
        ImageIcon printIcon = new ImageIcon("images/txt.png");
        JMenuItem printStockList = new JMenuItem("Write Stock List", printIcon);
        printStockList.setToolTipText("Write the current stock list to a text file");
        printStockList.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent event) 
            {
                reportGenerator.createStockReport(productTable);
                JOptionPane.showMessageDialog(mainFrame, "Stock Report generated. It has been saved to solution directory ", "Stock Report", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        ImageIcon addIcon = new ImageIcon("images/plus.png");
        JMenuItem addStockItem = new JMenuItem("Add new product to database", addIcon);
        addStockItem.setToolTipText("Add a new product to the database");
        addStockItem.addActionListener(new ActionListener()
        {	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				addNewName = JOptionPane.showInputDialog(mainFrame, "What is the name of the product you wish to add?", "Add new product",JOptionPane.PLAIN_MESSAGE);
				if(addNewName != null)
				{
	    			addNewQuantity = JOptionPane.showInputDialog(mainFrame, "Please enter the stock level of the new product.", "Add stock level", JOptionPane.PLAIN_MESSAGE);
	    			if(addNewQuantity != null)
	    			{
		    			while(!isNumber(addNewQuantity))
		    			{
		    				addNewQuantity = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to change the stock level of?", "Change stock level",JOptionPane.PLAIN_MESSAGE);
		    			}	
		    			addNewCrit = JOptionPane.showInputDialog(mainFrame, "Please enter the critical threshold of the new product.", "Add critical threshold", JOptionPane.PLAIN_MESSAGE);
		    			if(addNewCrit != null)
		    			{
			    			while(!isNumber(addNewCrit))
			    			{
			    				addNewCrit = JOptionPane.showInputDialog(mainFrame, "Please enter the critical threshold of the new product.", "Add critical threshold", JOptionPane.PLAIN_MESSAGE);
			    			}
			    			addNewPrice = addNewCrit = JOptionPane.showInputDialog(mainFrame, "Please enter the selling price of the new product.", "Add price", JOptionPane.PLAIN_MESSAGE);
			    			if(addNewPrice != null)
			    			{
				    			while(!isFloat(addNewPrice))
				    			{
				    				addNewPrice = JOptionPane.showInputDialog(mainFrame, "Please enter the selling price of the new product.", "Add price", JOptionPane.PLAIN_MESSAGE);
				    			}
								dbConnect.addNewProduct(addNewName, Integer.parseInt(addNewQuantity), Integer.parseInt(addNewCrit), Float.parseFloat(addNewPrice));
								checkStockLevels();
			    			}
		    			}
	    			}
				}
			}
		});
        
        ImageIcon helpIcon = new ImageIcon("images/help.jpg");
        JMenuItem displayHelp = new JMenuItem("Display the User Guide", helpIcon);
        displayHelp.setToolTipText("Open the User Guide");
        displayHelp.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent event) 
            {
        		userGuide();
            }
        });
        file.add(addStockItem);
		file.add(printStockList);
        file.add(exit);
        help.add(displayHelp);
        menuBar.add(file);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(help);
        setJMenuBar(menuBar);
        mainFrame.setJMenuBar(menuBar);
        logger.exiting(getClass().getName(), "createMenuBar");
    }
    
    private void createButtons()
    {  
    	logger.entering(getClass().getName(), "createButtons");
    	timeSimulation.setText("Start simulation");
    	timeSimulation.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
    			startSimulation = JOptionPane.showConfirmDialog(mainFrame, "This will simulate real time stock levels dropping as orders are placed. Simulation is currently " + simRun + ". Are you sure you wish to continue?", "Time Simulation - will not affect database", JOptionPane.YES_NO_OPTION);
    			if(startSimulation == JOptionPane.YES_OPTION)
    			{
    				switch(simRun)
    				{
    				case OFF:
    					simRun = SimulationRunning.ON;				    
    					simTime.schedule(new randomDecrementTask(), 5 * 1000);  
    					break;
    				case ON:
    					simRun = SimulationRunning.OFF;
    					simTime.cancel();
    					createStockGrid(dbConnect.getTableModel());
    				}
    			}
    		}
    	});
    	
    	orderPrediction.setText("Predict when stock deliveries will be required");
    	orderPrediction.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
    			predictDelivery = JOptionPane.showConfirmDialog(mainFrame, "Do you wish to predict when deliveries will be required?", "Predict when deliveries will be required", JOptionPane.YES_NO_OPTION);
    			if(predictDelivery == JOptionPane.YES_OPTION)
    			{
    				deliveryPredictions = dbConnect.getOrderPrediction();
    				deliveryPredictions(deliveryPredictions);
	    		}
	    	}
    	});
    	
    	changeQuantity.setText("Change the stock level of a product");
    	changeQuantity.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
				changeStockID = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to change the stock level of?", "Change stock level",JOptionPane.PLAIN_MESSAGE);
				if(changeStockID != null)
				{
	    			while(!isNumber(changeStockID))
	    			{        					
	    				changeStockID = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to change the stock level of?", "Change stock level",JOptionPane.PLAIN_MESSAGE);
	        		}
	    			changeStockLevel = JOptionPane.showInputDialog(mainFrame, "Please enter the new stock level of the product.", "Change stock level", JOptionPane.PLAIN_MESSAGE);
	    			if(changeStockLevel != null)
	    			{
		    			while(!isNumber(changeStockLevel))
		    			{
		    				changeStockID = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to change the stock level of?", "Change stock level",JOptionPane.PLAIN_MESSAGE);
		    			}	
						dbConnect.updateStockLevel(Integer.parseInt(changeStockID), Integer.parseInt(changeStockLevel));
						checkStockLevels();
	    			}
				}
    		}
    	});
    	buttonPanel.add(timeSimulation);
    	buttonPanel.add(orderPrediction);
    	buttonPanel.add(changeQuantity);
    	logger.exiting(getClass().getName(), "createButtons");
    }
    
    private void createStockGrid(DefaultTableModel tableModel)
    {
    	logger.entering(getClass().getName(), "createStockGrid");
		tableRenderer = new DefaultTableCellRenderer();
		productTable = new JTable(tableModel)
		{
			@Override
			public boolean isCellEditable(int row, int column)
	    	{
	            return false;
	        }
		};
    	checkStockLevels();
    	productTable.getTableHeader().setReorderingAllowed(false);
    	productTable.getTableHeader().setResizingAllowed(false);
    	productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    	productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
    	productTable.getColumnModel().getColumn(2).setPreferredWidth(120);
    	productTable.getColumnModel().getColumn(3).setPreferredWidth(150);  
    	tableRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    	productTable.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
    	productTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
    	productTable.getColumnModel().getColumn(3).setCellRenderer(tableRenderer);
    	productTable.getColumnModel().getColumn(4).setCellRenderer(tableRenderer);
    	JScrollPane scrollPane = new JScrollPane(productTable);
    	stockPanel.add(scrollPane);
    	logger.exiting(getClass().getName(), "createStockGrid");
    }  
    
    private void userGuide()
    {
    	logger.entering(getClass().getName(), "userGuide");
    	JTextPane userGuideText = new JTextPane();
    	StyleContext userGuideStyle = new StyleContext();
    	final DefaultStyledDocument userGuideDoc = new DefaultStyledDocument(userGuideStyle);
    	final Style headingStyle = userGuideStyle.addStyle("Heading2", null);
        headingStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
        headingStyle.addAttribute(StyleConstants.FontFamily, "serif");
        headingStyle.addAttribute(StyleConstants.Bold, new Boolean(true));
        final String userGuide = "NB Gardens Inventory Management System: User Guide" + "\n"
		        + "Welcome the NB Gardens Inventory Management System." + "\n"
		        + "Use the mouse to control the system, the buttons on the right" + "\n"
		        + "";
        try 
        {
			userGuideDoc.insertString(0, userGuide, null);
			userGuideDoc.setParagraphAttributes(0, 1, headingStyle, false);
		} 
        catch (BadLocationException e) 
        {
			logger.log(Level.SEVERE, "Exception when assembling user guide", e);
		}
        userGuideText.setDocument(userGuideDoc);
        userGuideText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userGuideText);
        helpFrame.add(scrollPane);
        helpFrame.setVisible(true);
        logger.exiting(getClass().getName(), "userGuide");
    }
    
    private void deliveryPredictions(String deliveries)
    {
    	logger.entering(getClass().getName(), "deliveryPredictions");
    	JTextPane predictionsText = new JTextPane();
    	StyleContext predictionsStyle = new StyleContext();
    	final DefaultStyledDocument predictionsDoc = new DefaultStyledDocument(predictionsStyle);
    	final Style headingStyle = predictionsStyle.addStyle("Heading2", null);
        headingStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
        headingStyle.addAttribute(StyleConstants.FontFamily, "serif");
        headingStyle.addAttribute(StyleConstants.Bold, new Boolean(true));
        try 
        {
			predictionsDoc.insertString(0, deliveries, null);
			predictionsDoc.setParagraphAttributes(0, 1, headingStyle, false);
		} 
        catch (BadLocationException e) 
        {
        	logger.log(Level.SEVERE, "Exception when assembling delivery predictions", e);
		}
        predictionsText.setDocument(predictionsDoc);
        predictionsText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(predictionsText);
        predictionsFrame.add(scrollPane);
        predictionsFrame.setVisible(true);
        logger.exiting(getClass().getName(), "deliveryPredictions");
    } 	
  
    private class randomDecrementTask extends TimerTask
	{
		public void run()
		{
			randomID = randomGenerator.nextInt(32) + 1;
			randomDecrement = -(randomGenerator.nextInt(2) + 1);
			dbConnect.getTableModel().setValueAt(Double.parseDouble(String.valueOf(dbConnect.getTableModel().getValueAt(randomID, 2))) + randomDecrement, randomID, 2);
			JOptionPane.showMessageDialog(mainFrame, "Stock for Product ID: " + randomID + " has been decremented by " + randomDecrement, "Simulation Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
    
    private void checkStockLevels()
    {
    	logger.entering(getClass().getName(), "checkStockLevels");
    	stockChecker = dbConnect.getTableModel();
    	for(int i = 1; i < dbConnect.getAmountOfProducts(); i++)
    	{
    		if(Integer.parseInt(String.valueOf(stockChecker.getValueAt(i, 2))) < 5)
    		{
    			showStockAlert(Integer.parseInt(String.valueOf(stockChecker.getValueAt(i, 0))));
    		}
    	}
    	logger.exiting(getClass().getName(), "checkStockLevels");
    }
    
    private boolean isNumber(String userEntry)
    { 	
    	try
    	{
    		Integer.parseInt(userEntry);
    		return true;
    	}
    	catch(NumberFormatException nfe)
    	{
    		logger.log(Level.SEVERE, "Number format exception on user input", nfe);
    		return false;
    	}
    }
    
    private boolean isFloat(String userEntry)
    {
    	try
    	{
    		Float.parseFloat(userEntry);
    		return true;
    	}
    	catch(NumberFormatException nfe)
    	{
    		logger.log(Level.SEVERE, "Number format exception on user input", nfe);
    		return false;
    	}
    }
    
    private void showStockAlert(int productID)
    {
    	logger.entering(getClass().getName(), "showStockAlert");
    	JOptionPane.showMessageDialog(mainFrame, "Stock for Product ID: " + productID + " is below the critical threshold", "Stock Level Alert!", JOptionPane.WARNING_MESSAGE);
    	logger.exiting(getClass().getName(), "showStockAlert");
    }
}