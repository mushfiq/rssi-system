package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import utilities.Utilities;

public class CoordinateZeroView extends JComponent {

	private static final long serialVersionUID = 1L;
	
	/** The image. */
	private BufferedImage image;
	
	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int ZERO_COORDINATE_WIDTH  = 20;
	
	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int ZERO_COORDINATE_HEIGHT = 20;
	
	private double xInMeters;
	private double yInMeters;
	
	
	public CoordinateZeroView() {
		
		setSize(ZERO_COORDINATE_WIDTH, ZERO_COORDINATE_HEIGHT);
		setPreferredSize(new Dimension(ZERO_COORDINATE_WIDTH, ZERO_COORDINATE_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addComponentListener(new ZeroCoordinateViewComponentListener());
		setDoubleBuffered(true); 
		
		BufferedImage myPicture = (BufferedImage) Utilities.loadImage("images/zeroCoordinateView.png");
		
		image = Utilities.scaleImageToFitContainer(myPicture, ZERO_COORDINATE_WIDTH, ZERO_COORDINATE_HEIGHT);
		setOpaque(true);
	}
	
	private class ZeroCoordinateViewComponentListener implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO update the zero coordinate position in meters
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public void paint(Graphics g) {
		
		super.paintComponent(g);

		g.drawImage(this.image, 0, 0, this);
	}
	
}
