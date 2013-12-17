/*
 * 
 * 
 */
package gui;

import gui.observer.Observable;
import gui.observer.Observer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import utilities.Utilities;
import components.Receiver;

/**
 * Graphical representation of a <code>Receiver</code> object on the <code>MapPreviewPanel</code>. This class serves as
 * a view for <code>Receiver</code> object model. It is added to the <code>MapPreviewPanel</code> when opening the
 * <code>AddMapDialog</code> window (if a map already has <code>Receiver</code>s placed on it) and on the press of a
 * <code>ReceiverButton</code>.
 * 
 * @see ReceiverButton
 * @see AddMapDialog
 * @see MapPreviewPanel
 * @author Danilo
 */
public class ReceiverView extends JComponent implements Observable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int RECEIVER_ITEM_WIDTH = 30;

	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int RECEIVER_ITEM_HEIGHT = 30;

	/** The Constant LABEL_X_POSITION. */
	private static final int LABEL_X_POSITION = 10;

	/** The Constant LABEL_Y_POSITION. */
	private static final int LABEL_Y_POSITION = 20;

	/** The receiver model. */
	private Receiver receiver;

	/** The image. */
	private BufferedImage image;

	/** Parent object of type MapPreviewPanel. */
	private MapPreviewPanel parent;

	/** The observers. */
	private List<Observer> observers;

	/**
	 * Instantiates a new <code>ReceiverView</code>.
	 * 
	 * @param receiver
	 *            Receiver object used as a model
	 * @param parent
	 *            Parent panel
	 */
	public ReceiverView(Receiver receiver, MapPreviewPanel parent) {

		this.receiver = receiver;
		this.parent = parent;
		observers = new ArrayList<Observer>();
		observers.add(parent);
		initializeGui();
	}

	/**
	 * Initializes the <code>ReceiverView</code>.
	 */
	private void initializeGui() {

		setSize(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT);
		setPreferredSize(new Dimension(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new ReceiverViewMouseListener(this));
		addComponentListener(new ReceiverViewComponentListener(this));
		setDoubleBuffered(true);
		BufferedImage myPicture = (BufferedImage) Utilities.loadImage("images/receiverView" + (int) receiver.getAngle()
				+ ".png");
		image = Utilities.scaleImageToFitContainer(myPicture, ReceiverView.RECEIVER_ITEM_WIDTH,
			ReceiverView.RECEIVER_ITEM_HEIGHT);
		setOpaque(true);
	}

	/**
	 * Gets the <code>Receiver</code>.
	 * 
	 * @return <code>Receiver</code> object
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	/**
	 * Paint component.
	 * 
	 * @param g
	 *            <code>Graphics</code> object
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(this.image, 0, 0, this);
		g.drawString("" + receiver.getID(), LABEL_X_POSITION, LABEL_Y_POSITION);
	}

	/**
	 * Rotates a <code>Receiver</code> for a specified <code>angle</code>.
	 * 
	 * @param angle
	 *            <code>double</code> amount to rotate
	 */
	public void rotate(double angle) {

		receiver.setAngle(receiver.getAngle() + angle);

		BufferedImage myPicture = null;
		try {
			String path = Utilities.class.getResource("images/receiverView" + (int) receiver.getAngle() + ".png")
					.getPath();
			myPicture = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image = Utilities.scaleImageToFitContainer(myPicture, ReceiverView.RECEIVER_ITEM_WIDTH,
			ReceiverView.RECEIVER_ITEM_HEIGHT);

		this.repaint();
	}

	/**
	 * The listener interface for receiving receiverViewComponent events. The class that is interested in processing a
	 * receiverViewComponent event implements this interface, and the object created with that class is registered with
	 * a component using the component's <code>addReceiverViewComponentListener</code> method. When the
	 * receiverViewComponent event occurs, that object's appropriate method is invoked.
	 * 
	 * @see ReceiverViewComponentEvent
	 */
	private class ReceiverViewComponentListener implements ComponentListener {

		/** The receiver view. */
		private ReceiverView receiverView;

		/**
		 * Instantiates a new receiver view component listener.
		 * 
		 * @param receiverView
		 *            the receiver view
		 */
		public ReceiverViewComponentListener(ReceiverView receiverView) {
			this.receiverView = receiverView;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentMoved(ComponentEvent e) {
			parent.focusReceiverView(receiverView);
			notifyObservers(receiverView);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentShown(ComponentEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
		 */
		@Override
		public void componentHidden(ComponentEvent e) {

		}

	}

	/**
	 * The listener interface for receiving receiverViewMouse events. The class that is interested in processing a
	 * receiverViewMouse event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addReceiverViewMouseListener</code> method. When the receiverViewMouse
	 * event occurs, that object's appropriate method is invoked.
	 * 
	 * @see ReceiverViewMouseEvent
	 */
	private class ReceiverViewMouseListener implements MouseListener {

		/** The receiver view. */
		private ReceiverView receiverView;

		/**
		 * Instantiates a new receiver view mouse listener.
		 * 
		 * @param receiverView
		 *            the receiver view
		 */
		public ReceiverViewMouseListener(ReceiverView receiverView) {
			this.receiverView = receiverView;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			parent.focusReceiverView(receiverView);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	/**
	 * Registers an observer.
	 * 
	 * @param observer
	 *            <code>Observer</code> object
	 */
	@Override
	public void registerObserver(Observer observer) {

		observers.add(observer);
	}

	/**
	 * Unregisters an observer.
	 * 
	 * @param observer
	 *            <code>Observer</code> object
	 */
	@Override
	public void unregisterObserver(Observer observer) {

		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/**
	 * Notifies observers.
	 * 
	 * @param observable
	 *            <code>Observable</code> self object. <code>Observer</code>s need <code>Observable</code> object itself
	 *            to track changes.
	 */
	@Override
	public void notifyObservers(Observable observable) {

		for (Observer observer : observers) {
			observer.update(observable);
		}
	}

}
