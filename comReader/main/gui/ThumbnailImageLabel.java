package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import utilities.Utilities;

public class ThumbnailImageLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private static final int THUMBNAIL_IMAGE_WIDTH = 130;
	private static final int THUMBNAIL_IMAGE_HEIGHT = 160;

	public ThumbnailImageLabel() {
		
		BufferedImage myPicture = null;
		try {
			String path = this.getClass().getResource("images/sampleMap.png").getPath();
			myPicture = ImageIO.read(new File(path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage scaledImage = Utilities.scaleImageToFitContainer(myPicture, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
		this.setIcon(new ImageIcon(scaledImage));
		
		// TODO: add onclick listener to open new window with controls for starting and stopping the readings/writings
	}
	

}
