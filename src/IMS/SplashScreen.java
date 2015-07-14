package IMS;

import java.awt.BorderLayout;
import java.awt.Image;
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
	private static final Logger logger = Logger.getLogger(SplashScreen.class.getName());
	private Image nbGardensLogo;
	private ImageIcon nbLogo;
	
	public SplashScreen()
	{
		logger.entering(getClass().getName(), "SplashScreen");
		try 
		{
			splash = ImageIO.read(new File("C:/Users/justi_000/workspace/JustinIMS/images/splash.jpg"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		logger.exiting(getClass().getName(), "SplashScreen");
	}
	
	public void displaySplash()
	{
		logger.entering(getClass().getName(), "displaySplash");
		ImageIcon splashIcon = new ImageIcon(splash);
		JLabel splashLabel = new JLabel();
		splashLabel.setIcon(splashIcon);
		JFrame splashFrame = new JFrame();
		splashFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		splashFrame.getContentPane().add(splashLabel, BorderLayout.CENTER);
		nbLogo = new ImageIcon("images/nb.png");
        nbGardensLogo = nbLogo.getImage();
        splashFrame.setIconImage(nbGardensLogo);
        splashFrame.setLocationRelativeTo(null);
		splashFrame.setVisible(true);
		logger.exiting(getClass().getName(), "displaySplash");
	}
}