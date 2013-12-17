package gui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import utilities.Utilities;
// FIXME this class may be used to change the cursor when dragging views around the preview panel
// if it is proved to be useless or too time consuming to implement, this class should be deleted
public class OpenHandCursor {

	public OpenHandCursor() {
		// TODO Auto-generated constructor stub 
		Image image = Utilities.loadImage("images/handCursor.png");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor brokenCursor = toolkit.createCustomCursor(image , new Point(0,0), "img");  
		//Component.setCursor(brokenCursor);  
	}

}
