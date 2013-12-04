package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import components.Receiver;


public class AddMapDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final int ADD_MAP_WINDOW_WIDTH = 1200;
	private static final int ADD_MAP_WINDOW_HEIGHT = 650;
	private MapPreviewPanel mapPreviewPanel; 
	private StatusPanel statusPanel;
	private ParametersPanel parametersPanel;
	private ReceiversPanel receiversPanel;
	private List<Receiver> allReceivers;
	
	public AddMapDialog() {
		
		setSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setPreferredSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setLayout(new GridBagLayout());
		setBackground(new Color(247, 247, 247));
		
		// TODO: load receivers from data source
		// allReceivers = ReceiverDAO.getAllReceivers();
		
		// XXX: hardcoded creation of receivers and receivers that are on the map
		allReceivers = new ArrayList<Receiver>();
		
		Receiver receiver1 = new Receiver(1, 25, 25, 45, true);
		Receiver receiver2 = new Receiver(2, 30, 30, 90, true);
		Receiver receiver3 = new Receiver(3, 50, 50, 100, true);
		Receiver receiver4 = new Receiver(4);
		Receiver receiver5 = new Receiver(5);
		
		allReceivers.add(receiver1);
		allReceivers.add(receiver2);
		allReceivers.add(receiver3);
		allReceivers.add(receiver4);
		allReceivers.add(receiver5);
		
		List<Receiver> receiversOnMap = new ArrayList<Receiver>();
		
		receiversOnMap.add(receiver1);
		receiversOnMap.add(receiver2);
		receiversOnMap.add(receiver3);
		
		// end of hardcoded creation for receivers 
		
		// Add MapPreviewPanel
		mapPreviewPanel = new MapPreviewPanel(receiversOnMap);
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
		receiversPanel = new ReceiversPanel(this, allReceivers);
		
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
		statusPanel = new StatusPanel();
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 6;
		gbc3.gridwidth = 4;
		gbc3.gridheight = 1;
		gbc3.weightx = 1;
		gbc3.anchor = GridBagConstraints.FIRST_LINE_START;
		
		this.add(statusPanel, gbc3);
		
		
		// Add parameters panel
		parametersPanel = new ParametersPanel(this);
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
		
		// TODO: perform button enabling/disabling actions after changing the image
	}

	// package visibility
	MapPreviewPanel getMapPreviewPanel() {
		return mapPreviewPanel;
	}


	public void addReceiverToMap(Receiver receiver) {
		
		// delegate call to MapPreviewPanel
		
		mapPreviewPanel.addReceiverViewToMap(receiver);
	}
	
	public void removeReceiverFromMap(Receiver receiver) {
		
		// delegate call to MapPreviewPanel
		
		mapPreviewPanel.removeReceiverViewFromMap(receiver);
	}


	
	
	

	
}
