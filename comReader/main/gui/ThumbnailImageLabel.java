package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import utilities.Utilities;

public class ThumbnailImageLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public ThumbnailImageLabel() {
		
		BufferedImage myPicture = null;
		try {
			String path = this.getClass().getResource("images/sampleMap.png").getPath();
			myPicture = ImageIO.read(new File(path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Image scaledImage = myPicture.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
		BufferedImage scaledImage = Utilities.scaleImageToFitContainer(myPicture, MapItem.MAP_ITEM_WIDTH, MapItem.MAP_ITEM_HEIGHT);
		this.setIcon(new ImageIcon(scaledImage));
		
		// TODO: add onclick listener to open new window with controls for starting and stopping the readings/writings
	}
	

}
