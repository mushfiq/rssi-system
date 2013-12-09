package gui;

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

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import utilities.Utilities;

import components.Receiver;


/**
 * Graphical representation of a Receiver object on the MapPreviewPanel. 
 * This class serves as a view for Receiver object model. It is added
 * to the MapPreviewPanel when opening the AddMapDialog window (if a map
 * already has receivers placed on it) and on the press of a button (ReceiverButton).
 */
public class ReceiverView extends JComponent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The receiver model. */
	private Receiver receiver;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
	
	/** The image. */
	private BufferedImage image;
	
	/** The Constant RECEIVER_ITEM_WIDTH. */
	public static final int RECEIVER_ITEM_WIDTH  = 30;
	
	/** The Constant RECEIVER_ITEM_HEIGHT. */
	public static final int RECEIVER_ITEM_HEIGHT = 30;
	
	/** The Constant LABEL_X_POSITION. */
	private static final int LABEL_X_POSITION = 10;
	
	/** The Constant LABEL_Y_POSITION. */
	private static final int LABEL_Y_POSITION = 15;
	
	/** Parent object of type MapPreviewPanel. */
	private MapPreviewPanel parent;
	 
	
	/**
	 * Instantiates a new receiver view.
	 *
	 * @param receiver Receiver object used as a model
	 * @param parent Parent panel
	 */
	public ReceiverView(Receiver receiver, MapPreviewPanel parent) {
		
		this.receiver = receiver;
		this.parent = parent;
		initializeGui();
	}
	
	/**
	 * Initialize.
	 */
	private void initializeGui() {

		setSize(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT);
		setPreferredSize(new Dimension(RECEIVER_ITEM_WIDTH, RECEIVER_ITEM_HEIGHT));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new ReceiverViewMouseListener(this));
		addComponentListener(new ReceiverViewComponentListener());
		setDoubleBuffered(true); 
		
		BufferedImage myPicture = null;
		try {
			String path = this.getClass().getResource("images/receiverView.png").getPath();
			myPicture = ImageIO.read(new File(path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		image = Utilities.scaleImageToFitContainer(myPicture, ReceiverView.RECEIVER_ITEM_WIDTH, ReceiverView.RECEIVER_ITEM_HEIGHT);
		setOpaque(true);
	}
	
	
	/**
	 * Gets the receiver.
	 *
	 * @return the receiver
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(this.image, 0, 0, this);
		g.drawString("" + receiver.getID(), LABEL_X_POSITION, LABEL_Y_POSITION);
		
	}

	private void rotate(int angle) {
		
			this.image = (BufferedImage)Utilities.rotate(this.image, angle);
	}
	
	/**
	 * The listener interface for receiving receiverViewComponent events.
	 * The class that is interested in processing a receiverViewComponent
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addReceiverViewComponentListener<code> method. When
	 * the receiverViewComponent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ReceiverViewComponentEvent
	 */
	private class ReceiverViewComponentListener implements ComponentListener {


		@Override
		public void componentResized(ComponentEvent e) {
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			
			// TODO: update values when position is changed (maybe unnecessary)
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			
		}
		
	}
	
	/**
	 * The listener interface for receiving receiverViewMouse events.
	 * The class that is interested in processing a receiverViewMouse
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addReceiverViewMouseListener<code> method. When
	 * the receiverViewMouse event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ReceiverViewMouseEvent
	 */
	private class ReceiverViewMouseListener implements MouseListener {

		/** The receiver view. */
		private ReceiverView receiverView;
		
		/**
		 * Instantiates a new receiver view mouse listener.
		 *
		 * @param receiverView the receiver view
		 */
		public ReceiverViewMouseListener(ReceiverView receiverView) {
			this.receiverView = receiverView;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO: set focus on this receiver
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}		
	}
	
	

	
}
