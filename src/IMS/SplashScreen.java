package IMS;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.logging.Logger;

/**
 * 
 * @author JustinMabbutt
 *
 */

public class SplashScreen 
{
	private BufferedImage splash = null; 
	
	public SplashScreen()
	{
		try 
		{
			splash = ImageIO.read(new File("C:/Users/justi_000/workspace/JustinIMS/images/splash.jpg"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void displaySplash()
	{
		ImageIcon splashIcon = new ImageIcon(splash);
		JLabel splashLabel = new JLabel();
		splashLabel.setIcon(splashIcon);
		JFrame splashFrame = new JFrame();
		splashFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		splashFrame.getContentPane().add(splashLabel, BorderLayout.CENTER);
		
	}
}