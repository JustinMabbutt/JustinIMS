package IMS;

import java.util.logging.Logger;

public class InventoryManagementSystem
{
	/*
	 * Main entry point for program - GUI initialisation
	 */
	private static final Logger logger = Logger.getLogger(InventoryManagementSystem.class.getName());
	private enum SystemState {Starting, Running};
	static IMSGUI imsgui = new IMSGUI();
	
    public static void main(String[] args)
    {
    	System.out.println("System startup...");
    	SystemState systemState = SystemState.Starting;
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