package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utilities.Utilities;

public class ThumbnailImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int THUMBNAIL_IMAGE_WIDTH = 130;
	private static final int THUMBNAIL_IMAGE_HEIGHT = 100;
	private Image backgroundImage;
	
	public ThumbnailImagePanel(Image image) {
		
		this.setSize(THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
		this.setPreferredSize(new Dimension(THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT));
		this.backgroundImage = Utilities.createThumbnailImageForContainer((BufferedImage) image, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
		
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw the background image.
	    g.drawImage(this.backgroundImage, 0, 0, this);
	}
	

}
