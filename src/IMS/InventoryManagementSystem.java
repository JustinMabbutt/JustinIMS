package IMS;

import java.util.logging.Logger;

/**
 * 
 * @author JustinMabbutt
 *
 */

public class InventoryManagementSystem
{
	private static final Logger logger = Logger.getLogger(InventoryManagementSystem.class.getName());
	private static IMSGUI imsgui = new IMSGUI();
	
    /**
     * Main entry point for program - loads splash screen
     * @param args
     */
    public static void main(String[] args)
    {
    	logger.entering(InventoryManagementSystem.class.getName(), "main");
    	System.out.println("System startup...");
    	imsgui.displaySplash();
    	logger.exiting(InventoryManagementSystem.class.getName(), "main");
    }
}