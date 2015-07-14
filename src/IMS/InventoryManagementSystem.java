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
	private static SystemState systemState;
	private static IMSGUI imsgui = new IMSGUI();
	private static SplashScreen splashScreen = new SplashScreen();
	
    public static void main(String[] args)
    {
    	logger.entering(InventoryManagementSystem.class.getName(), "main");
    	System.out.println("System startup...");
    	systemState = SystemState.Starting;
    	switch(systemState)
    	{
    	case Starting:
    		splashScreen.displaySplash();
    		systemState = SystemState.Running;
    		break;
    	case Running:
    		imsgui.showUI();
    		break;
    	default:
			break;
    	}
    	logger.exiting(InventoryManagementSystem.class.getName(), "main");
    }
}