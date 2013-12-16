/*
 * 
 * 
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JPanel;

import main.Application;
import components.RoomMap;

/**
 * Contains <code>MapItem</code> objects that represent available <code>RoomMap</code>s in the data source.
 * 
 * @author Danilo
 * @see RoomMap
 */
public class MapsPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant MAPS_PANEL_WIDTH. */
	private static final int MAPS_PANEL_WIDTH = 620;

	/** The Constant WRAP_LAYOUT_FIX_HEIGHT. Used instead of <code>MAPS_LAYOUT_HEIGHT</code> */
	private static final int WRAP_LAYOUT_FIX_HEIGHT = 1;

	/** The Constant GRAY_COLOUR. */
	private static final int GRAY_COLOUR = 230;

	/**
	 * Instantiates a new <code>MapsPanel</code>.
	 */
	public MapsPanel() {

		setBackground(new Color(GRAY_COLOUR, GRAY_COLOUR, GRAY_COLOUR));
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

	/**
	 * Refreshes map items. This method is needed when new <code>RoomMap</code>s are added to the data source and we
	 * want to display newly added <code>RoomMap</code>s.
	 */
	public void refreshMapItems() {

		removeAll();
		List<RoomMap> allMaps = Application.getApplication().getMapDAO().getAllMaps();
		for (RoomMap roomMap : allMaps) {
			MapItem item = new MapItem(roomMap, this);
			this.add(item);
		}
		revalidate();
	}

}
