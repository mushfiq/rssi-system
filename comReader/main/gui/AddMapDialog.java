package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JDialog;


public class AddMapDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final int ADD_MAP_WINDOW_WIDTH = 1200;
	private static final int ADD_MAP_WINDOW_HEIGHT = 650;
	private MapPreviewPanel mapPreviewPanel; 
	private StatusPanel statusPanel;
	private ParametersPanel parametersPanel;
	private ReceiversPanel receiversPanel;
	
	public AddMapDialog() {
		
		setSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setPreferredSize(new Dimension(ADD_MAP_WINDOW_WIDTH, ADD_MAP_WINDOW_HEIGHT));
		setLayout(new GridBagLayout());
		
		// Add receivers panel
		receiversPanel = new ReceiversPanel();
		receiversPanel.setBackground(Color.yellow);
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 4;
		gbc1.gridheight = 1;
		gbc1.weightx = 1;
		gbc1.weighty = 1;
		gbc1.anchor = GridBagConstraints.FIRST_LINE_START;
		
		this.add(receiversPanel, gbc1);
		
		
		// Add MapPreviewPanel
		mapPreviewPanel = new MapPreviewPanel();
		mapPreviewPanel.setBackground(Color.LIGHT_GRAY);
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 4;
		gbc2.gridheight = 5;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
		
		this.add(mapPreviewPanel, gbc2);
		
		
		// Add status panel
		statusPanel = new StatusPanel();
		statusPanel.setBackground(Color.ORANGE);
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
		parametersPanel.setBackground(Color.green);
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
	

}
