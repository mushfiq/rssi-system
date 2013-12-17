/*
 * 
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import utilities.Utilities;
import main.Application;

/**
 * Menu bar of the <code>MainFrame</code> window. It holds menus for adding new <code>RoomMap</code>s, editing
 * <code>Receiver</code>s and links to help files and about information.
 * 
 * @author Danilo
 * @see MainFrame
 */
public class MenuBar extends JMenuBar {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The 'File' menu. */
	private JMenu fileMenu;

	/** The 'Help' menu. */
	private JMenu helpMenu;

	/** The exit menu item. */
	private JMenuItem exitMenuItem;

	/** The about menu item. */
	private JMenuItem aboutMenuItem;

	/** The help menu item. */
	private JMenuItem helpMenuItem;

	/** The add map menu item. */
	private JMenuItem addMapMenuItem;

	/** <code>Logger</code> object. */
	private Logger logger;

	/**
	 * Instantiates a new <code>MenuBar</code>.
	 */
	public MenuBar() {

		initialize();
		addListenersToComponents();
		logger = Utilities.initializeLogger(this.getClass().getName());
		logger.info("MenuBar initialized.");
	}

	/**
	 * Initializes <code>MenuBar</code>.
	 */
	private void initialize() {

		/** File menu */
		fileMenu = new JMenu("File");

		// Add map menu item
		addMapMenuItem = new JMenuItem("Add map");
		fileMenu.add(addMapMenuItem);

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

	/**
	 * Adds listeners to components.
	 */
	private void addListenersToComponents() {

		this.addMapMenuItem.addActionListener(new AddMapActionListener());
		this.exitMenuItem.addActionListener(new ExitActionListener());
	}

	/**
	 * The listener interface for receiving addMapAction events. The class that is interested in processing a
	 * addMapAction event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addAddMapActionListener</code> method. When the addMapAction event occurs,
	 * that object's appropriate method is invoked.
	 * 
	 * @see AddMapActionEvent
	 */
	private class AddMapActionListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			new AddMapDialog();
		}
	}

	/**
	 * The listener interface for receiving exitAction events. The class that is interested in processing a exitAction
	 * event implements this interface, and the object created with that class is registered with a component using the
	 * component's <code>addExitActionListener</code> method. When the exitAction event occurs, that object's
	 * appropriate method is invoked.
	 * 
	 * @see ExitActionEvent
	 */
	private class ExitActionListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			Application.getApplication().getMainFrame().dispose();
		}

	}

}
