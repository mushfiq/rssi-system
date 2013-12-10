package gui;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import utilities.Utilities;

public class ThumbnailImageLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private static final int THUMBNAIL_IMAGE_WIDTH = 130;
	private static final int THUMBNAIL_IMAGE_HEIGHT = 160;

	public ThumbnailImageLabel() {
		
		BufferedImage myPicture = (BufferedImage) Utilities.loadImage("images/sampleMap.png");
		
		BufferedImage scaledImage = Utilities.scaleImageToFitContainer(myPicture, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
		this.setIcon(new ImageIcon(scaledImage));
	}
	

}
