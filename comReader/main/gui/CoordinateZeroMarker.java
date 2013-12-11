package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import utilities.Utilities;

public class CoordinateZeroMarker extends JComponent {

	private static final long serialVersionUID = 1L;

	/** The image. */
	private BufferedImage image;

	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int ZERO_COORDINATE_WIDTH = 20;

	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int ZERO_COORDINATE_HEIGHT = 20;

	private static final String LOWER_LEFT_IMAGE_PATH = "images/lowerLeftMarker.png";
	private static final String UPPER_RIGHT_IMAGE_PATH = "images/upperRightMarker.png";

	private double xInMeters;
	private double yInMeters;
	private CoordinateZeroMarkerType type;

	public CoordinateZeroMarker(CoordinateZeroMarkerType type) {

		setSize(ZERO_COORDINATE_WIDTH, ZERO_COORDINATE_HEIGHT);
		setPreferredSize(new Dimension(ZERO_COORDINATE_WIDTH,
				ZERO_COORDINATE_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addComponentListener(new CoordinateZeroMarkerListener());
		setDoubleBuffered(true);
		this.type = type;

		String pathToImage = (type == CoordinateZeroMarkerType.LOWER_LEFT) ? LOWER_LEFT_IMAGE_PATH
				: UPPER_RIGHT_IMAGE_PATH;
		BufferedImage myPicture = (BufferedImage) Utilities
				.loadImage(pathToImage);

		image = Utilities.scaleImageToFitContainer(myPicture,
				ZERO_COORDINATE_WIDTH, ZERO_COORDINATE_HEIGHT);
		setOpaque(true);
	}

	private class CoordinateZeroMarkerListener implements ComponentListener {

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
