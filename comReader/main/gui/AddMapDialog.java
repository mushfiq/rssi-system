package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;

import main.Application;

import components.Receiver;
import components.RoomMap;


/**
 * Dialog window used for editing an existing map or adding a new one.
 * 
 */
public class AddMapDialog extends JDialog{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant ADD_MAP_WINDOW_WIDTH. */
	private static final int ADD_MAP_WINDOW_WIDTH = 1200;
	
	/** The Constant ADD_MAP_WINDOW_HEIGHT. */
	private static final int ADD_MAP_WINDOW_HEIGHT = 650;
	
	/** The map preview panel. */
	private MapPreviewPanel mapPreviewPanel; 
	
	/** The status panel. */
	private StatusPanel statusPanel;
	
	/** The parameters panel. */
	private ParametersPanel parametersPanel;
	
	/** The receivers panel. */
	private ReceiversPanel receiversPanel;
	
	/** The all receivers. */
	private List<Receiver> allReceivers;
	
	/**
	 * Instantiates a new adds the map dialog.
	 */
	public AddMapDialog() {
		
		allReceivers = Application.getApplication().getReceiverDAO().getAllReceivers();
		mapPreviewPanel = new MapPreviewPanel();
		receiversPanel = new ReceiversPanel(this, allReceivers);
		statusPanel = new StatusPanel();
		parametersPanel = new ParametersPanel(this);
		initializeGui();
	}
	
	public AddMapDialog(RoomMap map) {
		
		allReceivers = Application.getApplication().getReceiverDAO().getAllReceivers();
		List<Receiver> receiversOnMap = Application.getApplication().getReceiverDAO().getAllReceiversForMap(map);
		mapPreviewPanel = new MapPreviewPanel(receiversOnMap, map);
		receiversPanel = new ReceiversPanel(this, allReceivers);
		statusPanel = new StatusPanel();
		parametersPanel = new ParametersPanel(this);
		initializeGui();
	}
	
	private void initializeGui() {
		
		setSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setPreferredSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setLayout(new GridBagLayout());
		setBackground(new Color(247, 247, 247));
		
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
	
	public void setPreviewImage(File file) {
		
		mapPreviewPanel.setPreviewImage(file);
		mapPreviewPanel.repaint();
	}

	// package visibility
	MapPreviewPanel getMapPreviewPanel() {
		return mapPreviewPanel;
	}


	/**
	 * Adds the receiver to map.
	 *
	 * @param receiver the receiver
	 */
	public void addReceiverToMap(Receiver receiver) {
		
		// delegate call to MapPreviewPanel
		mapPreviewPanel.addReceiverViewToMap(receiver);
	}
	
	/**
	 * Removes the receiver from map.
	 *
	 * @param receiver the receiver
	 */
	public void removeReceiverFromMap(Receiver receiver) {
		
		// delegate call to MapPreviewPanel
		mapPreviewPanel.removeReceiverViewFromMap(receiver);
	}


	
}
