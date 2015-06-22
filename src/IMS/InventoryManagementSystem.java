package IMS;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class InventoryManagementSystem extends JFrame
{
	private static final long serialVersionUID = 1L;
	 
	public InventoryManagementSystem() 
	{
        initUI();
    }

    private void initUI() 
    {
        createMenuBar();

        setTitle("Simple menu");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createMenuBar() 
    {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                System.exit(0);
            }
        });

        file.add(eMenuItem);
        menubar.add(file);

        setJMenuBar(menubar);
    }

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
            	InventoryManagementSystem ims = new InventoryManagementSystem();
                ims.setVisible(true);
            }
        });
    }
}