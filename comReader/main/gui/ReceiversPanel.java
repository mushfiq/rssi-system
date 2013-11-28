package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class ReceiversPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 950;
	private static final int PANEL_HEIGHT = 80;
	
	public ReceiversPanel() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
	}

}
