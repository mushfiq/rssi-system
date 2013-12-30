/*
 * 
 * 
 */
package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import utilities.Utilities;

import components.RoomMap;

/**
 * Main window of the graphical user interface. When the application is started, it shows the <code>RoomMap</code>s that
 * exist in the system. It also contains menus for editing receivers and adding maps. When this window is closed,
 * application is terminated as well.
 * 
 * @author Danilo
 * @see RoomMap
 */
public class MainFrame extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant WINDOW_WIDTH. */
	private static final int WINDOW_WIDTH = 1000;

	/** The Constant WINDOW_HEIGHT. */
	private static final int WINDOW_HEIGHT = 700;

	/** The Constant SCROLL_PANE_WIDTH. */
	private static final int SCROLL_PANE_WIDTH = 800; // 20px more than the content so that it will not display scroll
														// bar by default
	/** The Constant SCROLL_PANE_HEIGHT. */
	private static final int SCROLL_PANE_HEIGHT = 520; // 20px more than the content so that it will not display scroll
														// bar by default
	/** The Constant NIMBUS_LOOK_AND_FEEL. */
	private static final String NIMBUS_LOOK_AND_FEEL = "Nimbus";

	/** The logger. */
	private Logger logger;

	/** The menu bar. */
	private MenuBar menuBar;

	/** The maps panel. */
	private MapsPanel mapsPanel;

	/** The status panel. */
	private StatusPanel statusPanel;
	

	/**
	 * Instantiates a new <code>MainFrame</code>.
	 */
	public MainFrame() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		initialize();
		logger.log(Level.INFO, "GUI initialized.");
	}

	/**
	 * Initializes the graphical user interface.
	 */
	private void initialize() {

		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setLayout(new GridBagLayout());

		// add menu bar
		menuBar = new MenuBar();
		this.setJMenuBar(menuBar);

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;

		// add maps panel
		mapsPanel = new MapsPanel();
		JScrollPane mapsScrollPane = new JScrollPane(mapsPanel);
		mapsScrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
		mapsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mapsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weightx = 1;
		gbc2.anchor = GridBagConstraints.LINE_START;
		this.add(mapsScrollPane, gbc2);

		// add status panel
		statusPanel = new StatusPanel();

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.weightx = 1;
		gbc3.fill = GridBagConstraints.HORIZONTAL;

		this.add(statusPanel, gbc3);

		this.pack();
		setLocationRelativeTo(null); // center the window on the screens
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLookAndFeel();
		setVisible(true);
	}

	/**
	 * Sets the look and feel. If the preferred look and feel cannot be found, default one is used.
	 * 
	 * @see LookAndFeel
	 */
	private void setLookAndFeel() {

		try {
			LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
			for (LookAndFeelInfo lookAndFeelInfo : infos) {
				if (lookAndFeelInfo.getName() == NIMBUS_LOOK_AND_FEEL) {
					UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
					SwingUtilities.updateComponentTreeUI(this);
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			logger.warning("Look and feel not supported.\n" + e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.warning("Look and feel class not found.");
		} catch (InstantiationException e) {
			logger.warning(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.warning(e.getMessage());
		}
	}

	/**
	 * Sets the status message. Call is delegated to <code>StatusPanel</code>.
	 * 
	 * @param message
	 *            <code>String</code> new status message
	 * @see StatusPanel
	 */
	public void setStatusMessage(String message) {
		statusPanel.setMessage(message);
	}

	/**
	 * Refreshes maps panel. Call is delegated to <code>MapsPanel</code>.
	 * 
	 * @see MapsPanel
	 */
	public void refreshMapsPanel() {
		// Delegate the call to maps panel
		mapsPanel.refreshMapItems();
	}

	public MapsPanel getMapsPanel() {
		return mapsPanel;
	}
	
	
}
