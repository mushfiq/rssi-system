/*
 * 
 * 
 */
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

/**
 * The Class CoordinateZeroMarkerView. It implements interface <code>Observable</code>.
 * 
 * @see Observable
 * @author Danilo
 */
public class CoordinateZeroMarkerView extends JComponent implements Observable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The image. */
	private BufferedImage image;

	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int ZERO_COORDINATE_MARKER_VIEW_WIDTH = 20;

	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int ZERO_COORDINATE_MARKER_VIEW_HEIGHT = 20;

	/** The Constant LOWER_LEFT_IMAGE_PATH. */
	private static final String LOWER_LEFT_IMAGE_PATH = "images/lowerLeftMarker.png";

	/** The Constant UPPER_RIGHT_IMAGE_PATH. */
	private static final String UPPER_RIGHT_IMAGE_PATH = "images/upperRightMarker.png";

	/** <code>CoordinateZeroMarkerViewType</code> enumeration. */
	private CoordinateZeroMarkerViewType type;

	/** <code>MapPreviewPanel</code> parent component. */
	private MapPreviewPanel parent;

	/** List of <code>Observer</code>s. */
	private List<Observer> observers;

	/**
	 * Instantiates a new <code>CoordinateZeroMarkerView</code>.
	 * 
	 * @param type
	 *           <code>CoordinateZeroMarkerViewType</code> marker type
	 * @param parent
	 *            <code>MapPreviewPanel</code> parent component
	 */
	public CoordinateZeroMarkerView(CoordinateZeroMarkerViewType type, MapPreviewPanel parent) {

		setSize(ZERO_COORDINATE_MARKER_VIEW_WIDTH, ZERO_COORDINATE_MARKER_VIEW_HEIGHT);
		setPreferredSize(new Dimension(ZERO_COORDINATE_MARKER_VIEW_WIDTH, ZERO_COORDINATE_MARKER_VIEW_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addComponentListener(new CoordinateZeroMarkerListener());
		setDoubleBuffered(true);
		this.type = type;
		this.parent = parent;
		observers = new ArrayList<Observer>();
		observers.add(this.parent);

		String pathToImage = (this.type == CoordinateZeroMarkerViewType.LOWER_LEFT) ? LOWER_LEFT_IMAGE_PATH
				: UPPER_RIGHT_IMAGE_PATH;
		BufferedImage myPicture = (BufferedImage) Utilities.loadImage(pathToImage);

		image = Utilities.scaleImageToFitContainer(myPicture, ZERO_COORDINATE_MARKER_VIEW_WIDTH,
				ZERO_COORDINATE_MARKER_VIEW_HEIGHT);
		setOpaque(true);
	}

	/**
	 * The listener interface for receiving coordinateZeroMarker events. The class that is interested in processing a
	 * coordinateZeroMarker event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addCoordinateZeroMarkerListener</code> method. When the
	 * coordinateZeroMarker event occurs, that object's appropriate method is invoked.
	 * 
	 * @see CoordinateZeroMarkerEvent
	 */
	private class CoordinateZeroMarkerListener implements ComponentListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {

			/*
			 * There is no need to react to resizing of component, but the method from super type must be overridden.
			 */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentMoved(ComponentEvent e) {
			CoordinateZeroMarkerView view = (CoordinateZeroMarkerView) e.getSource();
			notifyObservers(view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentShown(ComponentEvent e) {
			/*
			 * There is no need to react to showing of component, but the method from super type must be overridden.
			 */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentHidden(ComponentEvent e) {
			/*
			 * There is no need to react when component is hidden, but the method from super type must be overridden.
			 */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {

		super.paintComponent(g);

		g.drawImage(this.image, 0, 0, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.observer.Observable#registerObserver(gui.observer.Observer)
	 */
	@Override
	public void registerObserver(Observer observer) {

		observers.add(observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.observer.Observable#unregisterObserver(gui.observer.Observer)
	 */
	@Override
	public void unregisterObserver(Observer observer) {

		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.observer.Observable#notifyObservers(gui.observer.Observable)
	 */
	@Override
	public void notifyObservers(Observable observable) {

		for (Observer observer : observers) {
			observer.update(observable);
		}
	}
}
