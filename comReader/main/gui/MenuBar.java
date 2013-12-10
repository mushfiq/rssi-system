package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utilities.Utilities;
import main.Application;

public class MenuBar extends JMenuBar{
	
	private static final long serialVersionUID = 1L;
	private JMenu fileMenu, helpMenu;
	private JMenuItem exitMenuItem, aboutMenuItem, helpMenuItem, addMap;
	/** The logger. */
	private Logger logger;
	
	public MenuBar() {
		
		initialize();
		addListenersToComponents();
		logger = Utilities.initializeLogger(this.getClass().getName());
		logger.info("MenuBar initialized.");
	}

	private void initialize() {
		
		/** File menu */
		fileMenu = new JMenu("File");
		
		// Add map menu item
		addMap = new JMenuItem("Add map");
		fileMenu.add(addMap);
		
		// Exit menu item
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		this.add(fileMenu);
		
		/** Help menu */
		helpMenu = new JMenu("Help");
		
		// About menu item
		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);
		
		helpMenuItem = new JMenuItem("Manual");
		helpMenu.add(helpMenuItem);
		
		this.add(helpMenu);
	}
	
	
	private void addListenersToComponents() {
		
		this.addMap.addActionListener(new AddMapActionListener());
		this.exitMenuItem.addActionListener(new ExitActionListener());
	}

	private class AddMapActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			new AddMapDialog();	
		}	
	}
	
	private class ExitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Application.getApplication().getMainFrame().dispose();
			
		}
		
		
	}
	
}
