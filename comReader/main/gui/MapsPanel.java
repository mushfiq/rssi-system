package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class MapsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final int MAPS_PANEL_WIDTH  = 620;
	private static final int MAPS_PANEL_HEIGHT = 500;
	
	public MapsPanel() {
		
		setSize(MAPS_PANEL_WIDTH, MAPS_PANEL_HEIGHT);
		setPreferredSize(new Dimension(MAPS_PANEL_WIDTH, MAPS_PANEL_HEIGHT));
		setBackground(new Color(230, 230, 230));
		setLayout(new FlowLayout(FlowLayout.LEADING));
		
		// TODO: obtain all maps, iterate over them and add MapItems
		
		
		// add sample map item
		MapItem item1 = new MapItem("Room 433");
		this.add(item1);
		
		// add sample map item
		MapItem item2 = new MapItem("Room 438");
		this.add(item2);
				
		// add sample map item
		MapItem item3 = new MapItem("Room 501");
		this.add(item3);
		
		// add sample map item
		MapItem item4 = new MapItem("Room 401");
		this.add(item4);
		
		// add sample map item
		MapItem item5 = new MapItem();
		this.add(item5);
		
	}

}
