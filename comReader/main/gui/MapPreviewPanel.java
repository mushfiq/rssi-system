package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapPreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int PANEL_WIDTH = 950;
	private static final int PANEL_HEIGHT = 500;
	private Image backgroundImage; // picture of a map drawn as a panel background
	
	public MapPreviewPanel() {
		
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		
	}
	
	public void setPreviewImage(File file) {
		try {
			backgroundImage = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw the background image.
	    g.drawImage(backgroundImage, 0, 0, this);
	    
	    System.out.println("Drawn...");
	}
	
	

}
