package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import utilities.Utilities;


/**
 * Displays an image of the map when adding new map to the system 
 * or when editing an existing one. It also serves as a drop target for 
 * receivers. (0,0) offset point is also marked in this panel. 
 * If the image is larger than the panel itself, it will be scaled
 * to fit the panel, maintaining the original aspect ratio. 
 */
public class MapPreviewPanel extends JPanel {

	/** The logger. */
	private Logger logger;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 950;
	
	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 500;
	
	/** Image of a map drawn as a panel background. */
	private BufferedImage backgroundImage; 
	
	/**
	 * Instantiates a new map preview panel.
	 */
	public MapPreviewPanel() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
	}
	
	/**
	 * Changes the panel image. When changed, any receivers placed on the map
	 *  will be removed and a (0,0) offset point of the map will be reset to the 
	 *  (0,0) offset point of the picture.  
	 * 
	 * @param file File image
	 * 
	 * */
	public void setPreviewImage(File file) {
		
		try {
			this.backgroundImage = ImageIO.read(file);
		} catch (IOException e) {
			logger.severe("Reading of the image failed.\n" + e.getMessage());
		}
	}
	
	/** 
	 * Overridden method in order to display image as a panel background.
	 * If the image is larger than the panel, determined by PANEL_WIDTH
	 * and PANEL_HEIGHT, it will be resized to fit the panel, maintaining
	 * the original aspect ratio.
	 * 
	 * @param g Graphics object
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (backgroundImage == null) { // if there is no image, finalize painting here
			return; 
		}
		
		// original image dimensions in pixels
		double imageWidthInPixels = backgroundImage.getWidth();
		double imageHeightInPixels = backgroundImage.getHeight();
		
		// scaling ratios, if needed. Resize ratio is the smaller value between widthRatio and heightRatio
		double widthRatio = 0;
		double heightRatio = 0;
		double resizeRatio = 0;
		
		// if image is resized, these will be its new dimensions
		double newImageWidthInPixels = 0;
		double newImageHeightInPixels = 0;
		
		
		if (imageWidthInPixels >= imageHeightInPixels) {
		
			if (imageWidthInPixels <= PANEL_WIDTH && imageHeightInPixels <= PANEL_HEIGHT) {
				
				// do nothing, no resizing needed

			} else { // resizing iz required
				
				widthRatio = PANEL_WIDTH / imageWidthInPixels;
				heightRatio = PANEL_HEIGHT / imageHeightInPixels;	
			}
			
		} else { // imageWidthInPixels < imageHeightInPixels
			
			if (imageWidthInPixels <= PANEL_WIDTH && imageWidthInPixels <= imageHeightInPixels) {
	            
				// no resizing required
	
			} else { // resizing is required
				
				widthRatio = PANEL_HEIGHT / imageWidthInPixels;
		        heightRatio = PANEL_WIDTH / imageHeightInPixels;
			}
		}
		

		if (widthRatio != 0 || heightRatio != 0) { // image should be resized
			
			resizeRatio = Math.min(widthRatio, heightRatio);
			
		    newImageHeightInPixels = imageHeightInPixels * resizeRatio;
		    newImageWidthInPixels = imageWidthInPixels * resizeRatio;
		    
			this.backgroundImage = Utilities.convertImagetoBufferedImage(
					this.backgroundImage.getScaledInstance((int) newImageWidthInPixels, 
					(int) newImageHeightInPixels, 
					Image.SCALE_SMOOTH));
		} 
		// otherwise, no resizing is needed, just use the original image
		
		// Draw the background image.
	    g.drawImage(this.backgroundImage, 0, 0, this);
	}
	
}
