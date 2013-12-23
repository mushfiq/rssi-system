/*
 * 
 * 
 */
package gui;

import gui.enumeration.AddMapDialogMode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import main.Application;
import components.Receiver;
import components.RoomMap;
import dao.MapDAO;

/**
 * Dialog window used for editing an existing <code>RoomMap</code> or adding a new one. <br>
 * <br>
 * From this window, if opened in <code>AddMapDialogMode.EDIT</code> mode, user can start and stop readings and
 * writings.
 * 
 * @author Danilo
 * @see components.RoomMap
 */
public class AddMapDialog extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant ADD_MAP_WINDOW_WIDTH. */
	private static final int ADD_MAP_WINDOW_WIDTH = 1200;

	/** The Constant ADD_MAP_WINDOW_HEIGHT. */
	private static final int ADD_MAP_WINDOW_HEIGHT = 650;

	/** The Constant DARKER_GRAY_COLOUR. */
	private static final int DARKER_GRAY_COLOUR = 247;

	/** The map preview panel. */
	private MapPreviewPanel mapPreviewPanel;

	/** The status panel. */
	private StatusPanel statusPanel;

	/** The parameters panel. */
	private ParametersPanel parametersPanel;

	/** The receivers panel. */
	private ReceiversPanel receiversPanel;

	/** All receivers available in the system. */
	private List<Receiver> allReceivers;

	/** Opening mode of the window. It can be 'ADD' or 'EDIT'. */
	private AddMapDialogMode openingMode;

	/**
	 * Constructor used when opening the dialog window in 'ADD' mode. We fetch all the available receivers from the
	 * system using <code>ReceiverDAO</code> interface and display available receivers to the user.
	 * 
	 * @see gui.enumeration.AddMapDialogMode
	 */
	public AddMapDialog() {

		openingMode = AddMapDialogMode.ADD;
		allReceivers = Application.getApplication().getReceiverDAO().getAllReceivers();
		/*
		 * In 'ADD' mode, initially there are no receivers on the map, therefore we create an empty list to pass to
		 * receiversPanel.
		 */
		List<Receiver> receiversOnMap = new ArrayList<Receiver>();
		mapPreviewPanel = new MapPreviewPanel(this);
		receiversPanel = new ReceiversPanel(this, allReceivers, receiversOnMap);
		statusPanel = new StatusPanel();
		parametersPanel = new ParametersPanel(this);
		initializeGui();
	}

	/**
	 * Constructor used when opening the dialog window in 'EDIT' mode. We fetch all the available receivers from the
	 * system using <code>ReceiverDAO</code> interface and display available receivers to the user. Additionally, we
	 * pass the method a <code>RoomMap</code> object from which it can obtain map information and display appropriate
	 * settings (such as map image, correct position of map markers, receiver positions (if any on the map), room size
	 * etc).
	 * 
	 * @param map
	 *            <code>RoomMap</code> instance
	 * @see gui.enumeration.AddMapDialogMode
	 */
	public AddMapDialog(RoomMap map) {

		openingMode = AddMapDialogMode.EDIT;
		allReceivers = Application.getApplication().getReceiverDAO().getAllReceivers();
		mapPreviewPanel = new MapPreviewPanel(map, this);
		receiversPanel = new ReceiversPanel(this, allReceivers, map.getReceivers());
		statusPanel = new StatusPanel();
		parametersPanel = new ParametersPanel(this);
		parametersPanel.setRoomWidthAndHeightInMetersSpinners(map.getWidthInMeters(), map.getHeightInMeters());
		parametersPanel.setRoomTitle(map.getTitle());
		initializeGui();
	}

	/**
	 * Initializes graphical user interface for dialog window. Depending on the <code>AddMapDialogMode</code>, some
	 * components may not be visible.
	 * 
	 * @see gui.enumeration.AddMapDialogMode
	 */
	private void initializeGui() {

		setSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setPreferredSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setLayout(new GridBagLayout());
		setBackground(new Color(DARKER_GRAY_COLOUR, DARKER_GRAY_COLOUR, DARKER_GRAY_COLOUR));
		setTitle((openingMode == AddMapDialogMode.ADD) ? "Add map" : "Edit map");

		// Add MapPreviewPanel
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 4;
		gbc2.gridheight = 5;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		gbc2.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(mapPreviewPanel, gbc2);

		// Add receivers panel
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 4;
		gbc1.gridheight = 1;
		gbc1.weightx = 1;
		gbc1.weighty = 1;
		gbc1.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(receiversPanel, gbc1);

		// Add status panel
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 6;
		gbc3.gridwidth = 4;
		gbc3.gridheight = 1;
		gbc3.weightx = 1;
		gbc3.anchor = GridBagConstraints.FIRST_LINE_START;

		this.add(statusPanel, gbc3);

		// Add parameters panel
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 4;
		gbc4.gridy = 0;
		gbc4.gridwidth = 1;
		gbc4.gridheight = 7;
		gbc4.anchor = GridBagConstraints.FIRST_LINE_END;

		this.add(parametersPanel, gbc4);

		this.pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setVisible(true);
	}

	/**
	 * Sets the preview image.
	 * 
	 * @param file
	 *            <code>File</code> object with image
	 */
	public void setPreviewImage(File file) {

		mapPreviewPanel.setPreviewImage(file);
		mapPreviewPanel.repaint();
	}

	/**
	 * Gets the map preview panel. Visibility of this method is 'package'
	 * 
	 * @return <code>MapPreviewPanel</code> instance
	 * @see MapPreviewPanel
	 */
	MapPreviewPanel getMapPreviewPanel() {
		return mapPreviewPanel;
	}

	/**
	 * Adds the receiver to map. Method call is delegated to <code>MapPreviewPanel</code>.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> object that serves as a receiver model
	 * @see MapPreviewPanel
	 * @see Receiver
	 */
	public void addReceiverToMap(Receiver receiver) {
		// delegate call to MapPreviewPanel
		mapPreviewPanel.addReceiverViewToMap(receiver);
	}

	/**
	 * Removes the <code>Receiver</code> object from <code>RoomMap</code>.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> object
	 * @see RoomMap
	 * @see Receiver
	 */
	public void removeReceiverFromMap(Receiver receiver) {
		// delegate call to MapPreviewPanel
		mapPreviewPanel.removeReceiverViewFromMap(receiver);
	}

	/**
	 * Gets the opening mode.
	 * 
	 * @return <code>AddMapDialogMode</code> opening mode
	 * @see gui.enumeration.AddMapDialogMode
	 */
	public AddMapDialogMode getOpeningMode() {
		return openingMode;
	}

	/**
	 * Adds the <code>CoordinateZeroView</code>.
	 * 
	 * @see gui.CoordinateZeroMarkerView
	 */
	public void addCoordinateZeroView() {

		mapPreviewPanel.addCoordinateZeroMarkerViewsToMap();
	}

	/**
	 * Sets the status.
	 * 
	 * @param message
	 *            <code>String</code> new status
	 */
	public void setStatus(String message) {
		this.statusPanel.setMessage(message);
	}

	/**
	 * Saves <code>RoomMap</code> to data source. Method first checks if the <code>RoomMap</code> parameters are valid.
	 * On success, it calls the appropriate method of <code>MapDAO</code> interface to store the <code>RoomMap</code>. After that,
	 * <code>MapsPanel</code> is refreshed to display new changes.<br>
	 * <br>
	 * 
	 * If <code>RoomMap</code> hasn't passed validation, error message is displayed to the user and no changes to the
	 * data source are made. 
	 * 
	 * @see RoomMap
	 * @see MapDAO
	 * @see MapsPanel
	 */
	public void saveMap() {
		// TODO Check if map is valid. On success, call the appropriate method of MapDAO to store the map
		// If there are validation errors, display error message to the user
		ArrayList<String> messages = (ArrayList<String>) mapPreviewPanel.getValidationErrorMessages();

		if (!messages.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (String string : messages) {
				builder.append(string + "\n");
			}
			JOptionPane.showMessageDialog(this, builder.toString(), "Validation errors", JOptionPane.WARNING_MESSAGE);
		} else { // map has passed validation
			RoomMap map = mapPreviewPanel.getMap();
			// TODO call HardcodedMapDAO object's method to store the map in the system
			// On success, refresh the MapsPanel in MainFrame, otherwise notify user that saving failed
			Application.getApplication().getMapDAO().saveMap(map);
			Application.getApplication().getMainFrame().refreshMapsPanel();
		}
	}

	/**
	 * Gets the room width in meters. Call is delegated to <code>ParametersPanel</code>.
	 * 
	 * @return <code>double</code> room width in meters
	 * @see ParametersPanel
	 */
	public double getRoomWidthInMeters() {
		// delegate the call to parameters panel
		return parametersPanel.getRoomWidthInMeters();
	}

	/**
	 * Gets the room height in meters. Call is delegated to <code>ParametersPanel</code>.
	 * 
	 * @return the room height in meters
	 * @see ParametersPanel
	 */
	public double getRoomHeightInMeters() {
		// delegate the call to parameters panel
		return parametersPanel.getRoomHeightInMeters();
	}

	/**
	 * Gets the room title. Call is delegated to <code>ParametersPanel</code>.
	 * 
	 * @return the room title
	 */
	public String getRoomTitle() {
		// delegate call to parameters panel
		return parametersPanel.getRoomTitle();
	}
	
	
}
