package IMS;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class IMSGUI extends JFrame
{
	private static final Logger logger = Logger.getLogger(IMSGUI.class.getName());
	
	private JFrame mainFrame = new JFrame();
	private JPanel stockPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JButton add = new JButton();
	private JButton changeQuantity = new JButton();
	private JButton delete = new JButton();
	private DefaultTableModel dtm;
	private DefaultTableCellRenderer tableRenderer;
	
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
        createStockGrid();
        createButtons();
        mainFrame.setTitle("NB Gardens Inventory Management System");
        mainFrame.setSize(800, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        ImageIcon nbLogo = new ImageIcon("images/nb.png");
        Image nbGardensLogo = nbLogo.getImage();
        mainFrame.setIconImage(nbGardensLogo);
        mainFrame.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(3, 1));
        mainFrame.add(buttonPanel, BorderLayout.EAST);
        mainFrame.add(stockPanel); 
        mainFrame.setVisible(true);
        logger.exiting(getClass().getName(), "initGUI");
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
                
            }
        });
        
        ImageIcon helpIcon = new ImageIcon("images/help.jpg");
        JMenuItem displayHelp = new JMenuItem("Display the User Guide", helpIcon);
        displayHelp.setToolTipText("Open the User Guide in Notepad");
        displayHelp.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent event) 
            {
        		try
        	    {
        			Runtime rs = Runtime.getRuntime();
        			rs.exec("Notepad C:\\Users\\jmabbutt\\workspace\\JustinIMS\\UserGuide.txt");
        	    }
        		catch(java.io.IOException e)
        	    {
        	        System.out.println("IOError");
        	    }
            }
        });
        
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
    	add.setText("Add a new product");
    	add.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
    			String name = JOptionPane.showInputDialog(mainFrame, "What is the name of the product you wish to add?", "Add a new product", JOptionPane.PLAIN_MESSAGE);
    		}
    	});
    	
    	delete.setText("Delete a product");
    	delete.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
    			String name = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to delete?", "Delete a product", JOptionPane.PLAIN_MESSAGE);
    		}
    	});
    	
    	changeQuantity.setText("Change the stock level of a product");
    	changeQuantity.addActionListener(new ActionListener()
    	{
    		@Override
    		public void actionPerformed(ActionEvent event)
    		{
    			String name = JOptionPane.showInputDialog(mainFrame, "What is the ID of the product you wish to change the stock level of?", "Change stock level",JOptionPane.PLAIN_MESSAGE);
    		}
    	});
    	
    	buttonPanel.add(add);
    	buttonPanel.add(delete);
    	buttonPanel.add(changeQuantity);
    	logger.exiting(getClass().getName(), "createButtons");
    }
    
    private void createStockGrid()
    {
    	logger.entering(getClass().getName(), "createStockGrid");   
    	tableRenderer = new DefaultTableCellRenderer();
    	String[] columnNames = {"Product ID", "Product Name", "Stock Level", "Critical Threshold"};
    	Object[][] data = {{"1", "Hippy Gnome", "7", "4"},
    					   {"2", "King Gnome", "12", "4"},
    					   {"3", "Queen Gnome", "12", "4"},
    					   {"4", "Nuclear Gnome", "12", "4"},
    					   {"5", "Biohazard Gnome", "12", "4"},
    					   {"6", "Obama Gnome", "12", "4"},
    					   {"7", "Redneck Gnome", "12", "4"},
    					   {"8", "Business Gnome", "12", "4"},
    					   {"9", "Chav Gnome", "12", "4"},
    					   {"10", "Beiber Gnome", "12", "4"},
    					   {"11", "Potter Gnome", "12", "4"},
    					   {"12", "Wolverine Gnome", "12", "4"},
    					   {"13", "Iron Man Gnome", "12", "4"},
    					   {"14", "Voldemort Gnome", "12", "4"},
    					   {"15", "Jedi Gnome", "12", "4"},
    					   {"16", "Sith Gnome", "12", "4"},
    					   {"17", "Picard Gnome", "12", "4"},
    					   {"18", "Angel Gnome", "12", "4"},
    					   {"19", "Gun Gnome", "12", "4"},
    					   {"20", "Big Gun Gnome", "12", "4"},
    					   {"21", "Bazooka Gnome", "12", "4"},
    					   {"22", "Tank Gnome", "12", "4"},
    					   {"23", "Police Gnome", "12", "4"},
    					   {"24", "French Gnome", "12", "4"},
    					   {"25", "Australian Gnome", "12", "4"},
    					   {"26", "Insane Gnome", "12", "4"},
    					   {"27", "Demon Gnome", "12", "4"},
    					   {"28", "Samurai Gnome", "12", "4"},
    					   {"29", "Time Lord Gnome", "12", "4"},
    					   {"30", "Chewbacca Gnome", "12", "4"},
    					   {"31", "Roman Gnome", "12", "4"},
    					   {"32", "Greek Gnome", "12", "4"},
    					   {"33", "Other Gnome", "23", "4"}};
    	dtm = new DefaultTableModel(data, columnNames);
    	JTable productTable = new JTable(dtm)
    	{
    		@Override
    		public boolean isCellEditable(int row, int column)
        	{
                return false;
            }
    	};
    	productTable.getTableHeader().setReorderingAllowed(false);
    	productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
    	productTable.getColumnModel().getColumn(2).setPreferredWidth(120);
    	productTable.getColumnModel().getColumn(3).setPreferredWidth(120);
    	productTable.getTableHeader().setResizingAllowed(false);
    	tableRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    	productTable.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
    	productTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
    	productTable.getColumnModel().getColumn(3).setCellRenderer(tableRenderer);
    	JScrollPane scrollPane = new JScrollPane(productTable);
    	stockPanel.add(scrollPane);
    	logger.exiting(getClass().getName(), "createStockGrid");
    }    
}