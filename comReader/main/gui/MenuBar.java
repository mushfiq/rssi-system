/*
 * 
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.Application;
import utilities.Utilities;

import components.Receiver;

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
	private JMenu mapsMenu;

	/** The 'Help' menu. */
	private JMenu helpMenu;

	/** 'Receivers' menu */
	private JMenu receiversMenu;

	/** The exit menu item. */
	private JMenuItem exitMenuItem;

	/** The about menu item. */
	private JMenuItem aboutMenuItem;

	/** The help menu item. */
	private JMenuItem helpMenuItem;

	/** The add map menu item. */
	private JMenuItem addMapMenuItem;

	private JMenuItem addReceiverMenuItem;

	private JMenuItem deleteReceiverMenuItem;

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

		mapsMenu = new JMenu("Maps");

		// Add map menu item
		addMapMenuItem = new JMenuItem("Add map");
		mapsMenu.add(addMapMenuItem);

		// separator
		mapsMenu.addSeparator();

		// Exit menu item
		exitMenuItem = new JMenuItem("Exit");
		mapsMenu.add(exitMenuItem);

		// Add receivers menu
		receiversMenu = new JMenu("Receivers");

		// Add receiver menu item
		addReceiverMenuItem = new JMenuItem("Add receiver");
		receiversMenu.add(addReceiverMenuItem);

		// Delete receiver menu item
		deleteReceiverMenuItem = new JMenuItem("Delete receiver");
		receiversMenu.add(deleteReceiverMenuItem);

		this.add(mapsMenu);
		this.add(receiversMenu);

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
		this.addReceiverMenuItem.addActionListener(new AddReceiverActionListener());
		this.deleteReceiverMenuItem.addActionListener(new DeleteReceiverActionListener());
		this.aboutMenuItem.addActionListener(new AboutActionListener());
		this.helpMenuItem.addActionListener(new HelpActionListener());
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

	private class AddReceiverActionListener implements ActionListener {

		
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame mainFrame = Application.getApplication().getMainFrame();
			// TODO get all receiver numbers, create a message and prompt for input (number only)
			List<Receiver> receivers = Application.getApplication().getReceiverDAO().getAllReceivers();
			StringBuilder builder = new StringBuilder();
			builder.append("Existing receivers: " + ", ");
			for (Receiver receiver : receivers) {
				builder.append(receiver.getID());
			}
			builder.append("\n\nPlease enter new receiver number: ");
			
			String userInput = JOptionPane.showInputDialog(builder.toString());
			if (userInput == null || userInput.equals("")) {
				return; // user canceled the action
			}
			int newReceiverNumber = -1;
			try {
				newReceiverNumber = Integer.parseInt(userInput.trim());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(mainFrame, "Please enter numbers only.");
				return;
			}
			
			if (newReceiverNumber < 0) {
				JOptionPane.showMessageDialog(mainFrame, "Please enter numbers only.");
				return;
			} else {
				// input is a number, try check if it already exists
				for (Receiver receiver : receivers) {
					if (receiver.getID() == newReceiverNumber) {
						JOptionPane.showMessageDialog(mainFrame, "Receiver already exists in the system. Please choose a different receiver number.");
						return;
					} 
				}
				// receiver number doesn't exist, call ReceiverDAO method to save it
				Application.getApplication().getReceiverDAO().addReceiver(new Receiver(newReceiverNumber));
				return;
			}
		}

	}

	private class DeleteReceiverActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame mainFrame = Application.getApplication().getMainFrame();
			// TODO get all receiver numbers, create a message and prompt for input (number only)
			List<Receiver> receivers = Application.getApplication().getReceiverDAO().getAllReceivers();
			StringBuilder builder = new StringBuilder();
			builder.append("Existing receivers: ");
			for (Receiver receiver : receivers) {
				builder.append(receiver.getID() + ", ");
			}
			builder.append("\n\nPlease enter number of receiver to delete: ");
			
			String userInput = JOptionPane.showInputDialog(builder.toString());
			if (userInput == null || userInput.equals("")) {
				return; // user canceled the action
			}
			int newReceiverNumber = -1;
			try {
				newReceiverNumber = Integer.parseInt(userInput.trim());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(mainFrame, "Please enter numbers only.");
				return;
			}
			
			if (newReceiverNumber < 0) {
				JOptionPane.showMessageDialog(mainFrame, "Please enter numbers only.");
				return;
			} else {
				// input is a number, check if it exists
				for (Receiver receiver : receivers) {
					if (receiver.getID() == newReceiverNumber) {
						Application.getApplication().getReceiverDAO().deleteReceiver(new Receiver(newReceiverNumber));
						JOptionPane.showMessageDialog(mainFrame, "Receiver deleted.");
						return;
					} 
				}
				// receiver number doesn't exist
				JOptionPane.showMessageDialog(mainFrame, "Receiver doesn't exists in the system. Please choose a different receiver number.");
			}
		}

	}
	
	private class HelpActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private class AboutActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
