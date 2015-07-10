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
	static IMSGUI imsgui = new IMSGUI();
	
    public static void main(String[] args)
    {
    	System.out.println("System startup...");
    	systemState = SystemState.Starting;
    	switch(systemState)
    	{
    	case Starting:
    		systemState = SystemState.Running;
    		break;
    	case Running:
    		imsgui.showUI();
    		break;
    	default:
			break;
    	}    	
    }
}