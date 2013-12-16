/*
 * 
 * 
 */
package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utilities.Utilities;

/**
 * This class extends <code>JPanel</code>. It shows a thumbnail image of a <code>ReceiverMap</code> inside a
 * <code>MapItem</code> object.
 * 
 * @see MapItem
 * @author Danilo
 */
public class ThumbnailImagePanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant THUMBNAIL_IMAGE_WIDTH. */
	private static final int THUMBNAIL_IMAGE_WIDTH = 130;

	/** The Constant THUMBNAIL_IMAGE_HEIGHT. */
	private static final int THUMBNAIL_IMAGE_HEIGHT = 160;

	/** The background image. */
	private Image backgroundImage;

	/**
	 * Instantiates a new <code>ThumbnailImagePanel</code>.
	 * 
	 * @param image
	 *            <code>Image</code> object
	 */
	public ThumbnailImagePanel(Image image) {

		this.setSize(THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
		this.setPreferredSize(new Dimension(THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT));
		this.backgroundImage = Utilities.createThumbnailImageForContainer((BufferedImage) image, THUMBNAIL_IMAGE_WIDTH,
			THUMBNAIL_IMAGE_HEIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the background image at the center of the panel
		int x = (this.getWidth() - backgroundImage.getWidth(null)) / 2;
		int y = (this.getHeight() - backgroundImage.getHeight(null)) / 2;

		g.drawImage(this.backgroundImage, x, y, this);
	}

}
