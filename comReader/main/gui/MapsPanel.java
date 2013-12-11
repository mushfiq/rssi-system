package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JPanel;

import main.Application;
import components.RoomMap;

// TODO: Auto-generated Javadoc
/**
 * The Class MapsPanel.
 */
public class MapsPanel extends JPanel{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant MAPS_PANEL_WIDTH. */
	private static final int MAPS_PANEL_WIDTH  = 620;
	
	/** The Constant MAPS_PANEL_HEIGHT. */
	private static final int MAPS_PANEL_HEIGHT = 500;
	
	/** The Constant WRAP_LAYOUT_FIX_HEIGHT. */
	private static final int WRAP_LAYOUT_FIX_HEIGHT = 1;
	
	/**
	 * Instantiates a new maps panel.
	 */
	public MapsPanel() {
		
		setBackground(new Color(230, 230, 230));
		setLayout(new WrapLayout(FlowLayout.LEADING));
		this.setSize(new Dimension(MAPS_PANEL_WIDTH, WRAP_LAYOUT_FIX_HEIGHT));
		// TODO: obtain all maps using MapDAO, iterate over them and add MapItems
		List<RoomMap> allMaps = Application.getApplication().getMapDAO().getAllMaps();
		
		for (RoomMap roomMap : allMaps) {
			MapItem item = new MapItem(roomMap, this);
			this.add(item);
		}
		this.revalidate();
	}

}
