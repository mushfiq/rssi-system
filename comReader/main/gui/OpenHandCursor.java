package gui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class OpenHandCursor {

	public OpenHandCursor() {
		// TODO Auto-generated constructor stub
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Image image = toolkit.getImage("img.gif");  
		Cursor brokenCursor = toolkit.createCustomCursor(image , new Point(0,0), "img");  
		//Component.setCursor(brokenCursor);  
	}

}
