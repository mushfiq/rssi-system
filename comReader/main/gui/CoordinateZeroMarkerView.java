package gui;

import gui.enumeration.CoordinateZeroMarkerViewType;
import gui.observer.Observable;
import gui.observer.Observer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import utilities.Utilities;

public class CoordinateZeroMarkerView extends JComponent implements Observable {

	private static final long serialVersionUID = 1L;

	/** The image. */
	private BufferedImage image;

	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int ZERO_COORDINATE_MARKER_VIEW_WIDTH = 20;

	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int ZERO_COORDINATE_MARKER_VIEW_HEIGHT = 20;

	private static final String LOWER_LEFT_IMAGE_PATH = "images/lowerLeftMarker.png";
	private static final String UPPER_RIGHT_IMAGE_PATH = "images/upperRightMarker.png";

	private double xInMeters;
	private double yInMeters;
	private CoordinateZeroMarkerViewType type;
	private MapPreviewPanel parent;
	private List<Observer> observers;

	public CoordinateZeroMarkerView(CoordinateZeroMarkerViewType type, MapPreviewPanel parent) {

		setSize(ZERO_COORDINATE_MARKER_VIEW_WIDTH, ZERO_COORDINATE_MARKER_VIEW_HEIGHT);
		setPreferredSize(new Dimension(ZERO_COORDINATE_MARKER_VIEW_WIDTH,
				ZERO_COORDINATE_MARKER_VIEW_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addComponentListener(new CoordinateZeroMarkerListener());
		setDoubleBuffered(true);
		this.type = type;
		this.parent = parent;
		observers = new ArrayList<Observer>();
		observers.add(parent);

		String pathToImage = (type == CoordinateZeroMarkerViewType.LOWER_LEFT) ? LOWER_LEFT_IMAGE_PATH
				: UPPER_RIGHT_IMAGE_PATH;
		BufferedImage myPicture = (BufferedImage) Utilities
				.loadImage(pathToImage);

		image = Utilities.scaleImageToFitContainer(myPicture,
				ZERO_COORDINATE_MARKER_VIEW_WIDTH, ZERO_COORDINATE_MARKER_VIEW_HEIGHT);
		setOpaque(true);
	}

	private class CoordinateZeroMarkerListener implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			
		    	/* There is no need to react to resizing of component,
		    	 * but the method from super type must be overridden. 
			 * */
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		    	CoordinateZeroMarkerView view = (CoordinateZeroMarkerView) e.getSource();
		    	notifyObservers(view);
		}

		@Override
		public void componentShown(ComponentEvent e) {
		    	/* There is no need to react to showing of component,
		    	 * but the method from super type must be overridden. 
			 * */
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		    	/* There is no need to react when component is hidden,
		    	 * but the method from super type must be overridden. 
			 * */
		}
	}

	@Override
	public void paint(Graphics g) {

		super.paintComponent(g);

		g.drawImage(this.image, 0, 0, this);
	}

	@Override
	public void registerObserver(Observer observer) {
		
		observers.add(observer);
	}

	@Override
	public void deregisterObserver(Observer observer) {
		
		if(observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	@Override
	public void notifyObservers(Observable observable) {
	    
	    for (Observer observer : observers) {
		observer.update(observable);
	    }
	}
}
