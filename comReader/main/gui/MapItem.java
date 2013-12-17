package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import components.RoomMap;

/**
 * The Class MapItem.
 */
public class MapItem extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The thumbnail image label. */
	private ThumbnailImagePanel thumbnailImagePanel;
	
	/** The edit button. */
	private JButton editButton;
	
	/** The delete button. */
	private JButton deleteButton;
	
	/** The map. */
	private RoomMap map;
	
	/** The Constant MAP_ITEM_WIDTH. */
	public static final int MAP_ITEM_WIDTH = 150;
	
	/** The Constant MAP_ITEM_HEIGHT. */
	public static final int MAP_ITEM_HEIGHT = 210;
	
	/** The maps panel. */
	private MapsPanel mapsPanel;
	

	/**
	 * Instantiates a new map item.
	 */
	public MapItem() {
		super();
		initialize();
	}
	
	/**
	 * Instantiates a new map item.
	 *
	 * @param mapTitle the map title
	 * @param mapsPanel the maps panel
	 */
	public MapItem(String mapTitle, MapsPanel mapsPanel) {
		
		this.mapsPanel = mapsPanel;
		initialize();
	}
	
	/**
	 * Instantiates a new map item.
	 *
	 * @param map the map
	 * @param mapsPanel the maps panel
	 */
	public MapItem(RoomMap map, MapsPanel mapsPanel) {
		
		this.map = map;
		this.mapsPanel = mapsPanel;
		initialize();
	}
	
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setSize(MAP_ITEM_WIDTH, MAP_ITEM_HEIGHT);
		this.setPreferredSize(new Dimension(MAP_ITEM_WIDTH, MAP_ITEM_HEIGHT));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Map title
		JLabel mapTitleLabel = new JLabel(map.getTitle());
		mapTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(mapTitleLabel);
		
		// Thumbnail image panel
		thumbnailImagePanel = new ThumbnailImagePanel(map.getImage());
		thumbnailImagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(thumbnailImagePanel);  // add first panel to the main container
		
		// Panel for 'Delete' button
		JPanel editAndDeleteButtonsPanel = new JPanel();
		editAndDeleteButtonsPanel.setLayout(new GridBagLayout());
		
		// Edit button
		editButton = new JButton("Edit");
		editButton.addActionListener(new EditButtonListener());
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.anchor = GridBagConstraints.LINE_START;
		
		editAndDeleteButtonsPanel.add(editButton, gbc1);
		
		// Delete button
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteButtonListener(this));
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.anchor = GridBagConstraints.LINE_END;
		
		editAndDeleteButtonsPanel.add(deleteButton, gbc2);
		
		this.add(editAndDeleteButtonsPanel);
	}

	
	
	/**
	 * The listener interface for receiving editButton events.
	 * The class that is interested in processing a editButton
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addEditButtonListener</code> method. When
	 * the editButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see EditButtonEvent
	 */
	private class EditButtonListener implements ActionListener {

		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			new AddMapDialog(map);
		}
	}
	
	/**
	 * The listener interface for receiving deleteButton events.
	 * The class that is interested in processing a deleteButton
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDeleteButtonListener</code> method. When
	 * the deleteButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DeleteButtonEvent
	 */
	private class DeleteButtonListener implements ActionListener {

		/** The parent. */
		private MapItem parent;
		
		/**
		 * Instantiates a new delete button listener.
		 *
		 * @param parent the parent
		 */
		public DeleteButtonListener(MapItem parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int dialogResult = JOptionPane.showConfirmDialog(mapsPanel, "Are you sure you want to delete the map from the system? This action cannot be undone.");
			if (dialogResult == JOptionPane.YES_OPTION) {
				// TODO try to delete map from the server. On success, remove this MapItem from the MapsPanel
				mapsPanel.remove(parent);
				mapsPanel.revalidate();
			} 
		}
	}
	

}
