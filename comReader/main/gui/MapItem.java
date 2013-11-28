package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapItem extends JPanel{

	private ThumbnailImageLabel thumbnailImageLabel;
	private JButton deleteButton, editButton;
	private String mapTitle;
	private static final int MAP_ITEM_WIDTH = 150;
	private static final int MAP_ITEM_HEIGHT = 100;
	
	public MapItem() {
		
		mapTitle = new SimpleDateFormat("dd.MM.yyyy, HH:ss").format(new Date());
		initialize();
	}
	
	public MapItem(String mapTitle) {
		
		this.mapTitle = mapTitle;
		initialize();
	}
	
	
	private void initialize() {
		
		this.setSize(MAP_ITEM_WIDTH, MAP_ITEM_HEIGHT);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Map title
		JLabel mapTitleLabel = new JLabel(this.mapTitle);
		mapTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(mapTitleLabel);
		
		// Thumbnail image panel
		thumbnailImageLabel = new ThumbnailImageLabel();
		thumbnailImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(thumbnailImageLabel);  // add first panel to the main container
		
		// Panel for 'Delete' button
		JPanel editAndDeleteButtonsPanel = new JPanel();
		editAndDeleteButtonsPanel.setLayout(new GridBagLayout());
		
		// Edit button
		editButton = new JButton("Edit");
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.anchor = GridBagConstraints.LINE_START;
		
		editAndDeleteButtonsPanel.add(editButton, gbc1);
		
		// Delete button
		deleteButton = new JButton("Delete");
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.anchor = GridBagConstraints.LINE_END;
		
		editAndDeleteButtonsPanel.add(deleteButton, gbc2);
		
		this.add(editAndDeleteButtonsPanel);
		
	}

}
