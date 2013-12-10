package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import utilities.ComponentMover;
import utilities.Utilities;
import components.Receiver;
import components.RoomMap;


/**
 * Displays an image of the map when adding new map to the system 
 * or when editing an existing one. 
 * 
 * It contains ReceiverView items. 
 * 
 * CoordinateZeroView item is also added to this panel.
 *  
 * If the image is larger than the panel itself, it will be scaled
 * to fit the panel, maintaining original aspect ratio. 
 */
public class MapPreviewPanel extends JPanel {

	/** The logger. */
	private Logger logger;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 950;
	
	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 550;
	
	/** Image of a map drawn as a panel background. */
	private BufferedImage backgroundImage;
	
	/** The original background image. */
	private BufferedImage originalBackgroundImage; 
	
	/** The scaling ratio of image (if necessary) to fit container. Initially it is 1. */
	private double scalingRatioToFitContainer;
	
	/** The Constant DEFAULT_RECEIVER_VIEW_X_IN_METERS. */
	private static final int DEFAULT_RECEIVER_VIEW_X_IN_METERS = 1;
	
	/** The Constant DEFAULT_RECEIVER_VIEW_Y_IN_METERS. */
	private static final int DEFAULT_RECEIVER_VIEW_Y_IN_METERS = 1;
	
	/** The Constant NO_IMAGE_STRING. */
	private static final String NO_IMAGE_STRING = "Click on 'Upload' button on the right side to show a new map.";
	
	/** The Constant NO_IMAGE_STRING_LEFT_PADDING. */
	private static final int NO_IMAGE_STRING_LEFT_PADDING = 3;
	
	/** The Constant NO_IMAGE_STRING_TOP_PADDING. */
	private static final int NO_IMAGE_STRING_TOP_PADDING = 2;
	
	/** The receiver views. */
	private List<ReceiverView> receiverViews;
	
	/** The receivers. */
	private List<Receiver> receivers;
	
	private RoomMap map;
	
	/** Helper object that handles moving JComponents around a panel. */
	private ComponentMover componentMover;
	
	private ReceiverView receiverViewInFocus;
	
	private CoordinateZeroView coordinateZeroView;
	
	/**
	 * Instantiates a new map preview panel.
	 *
	 * @param receiversOnMap List of receivers that are already put on the map.
	 */
	public MapPreviewPanel(List<Receiver> receiversOnMap, RoomMap map) {
		
		receiverViews = new ArrayList<ReceiverView>();
		scalingRatioToFitContainer = 1.0;
		
		this.map = map;
		
		if (receiversOnMap == null) {
			this.receivers = new ArrayList<Receiver>();
		} else {
			this.receivers = receiversOnMap;
		}
		
		initializeGui();
	}
	
	public MapPreviewPanel() {
		
		receiverViews = new ArrayList<ReceiverView>();
		receivers = new ArrayList<Receiver>();
		initializeGui();
	}
	
	/**
	 * Initializes GUI. Layout is set to null (no LayoutManager) so that ReceiverView
	 * items can be positioned absolutely. 
	 */
	private void initializeGui() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(new Color(230, 230, 230));
		setLayout(null); // in order to position ReceiverViews absolutely
		
		
		// register all receiver views to the ComponentMover
		componentMover = new ComponentMover();
		
		for (Receiver receiver : this.receivers) {
			
			if (receiver.isOnMap()) {
				ReceiverView receiverView = new ReceiverView(receiver, this);
				receiverViews.add(receiverView);
				this.add(receiverView);
				// TODO: location should be calculated with offsets for scaling the image and pixel/meter scaling
				receiverView.setLocation((int) receiver.getXPos(), (int) receiver.getYPos());
				componentMover.registerComponent(receiverView);
			}	
		}
	}
	
	/**
	 * Changes the panel image. When changed, any receivers placed on the map
	 *  will be removed and a (0,0) offset point of the map will be reset to the 
	 *  (0,0) offset point of the picture.  
	 * 
	 * @param file File image
	 * 
	 * */
	public void setPreviewImage(File file) {
		
		try {
			this.backgroundImage = ImageIO.read(file);
			this.originalBackgroundImage = ImageIO.read(file);
			this.scalingRatioToFitContainer = Utilities.getScalingRatioToFitContainer(this.originalBackgroundImage, PANEL_WIDTH, PANEL_HEIGHT);
			this.backgroundImage = Utilities.scaleImageToFitContainer(this.backgroundImage, PANEL_WIDTH, PANEL_HEIGHT);
			
		} catch (IOException e) {
			logger.severe("Reading of the image failed.\n" + e.getMessage());
		}
		
	}
	
	/** 
	 * Overridden method in order to display image as a panel background.
	 * If the image is larger than the panel, determined by PANEL_WIDTH
	 * and PANEL_HEIGHT, it will be resized to fit the panel, maintaining
	 * the original aspect ratio.
	 * 
	 * @param g Graphics object
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (backgroundImage == null) { // if there is no image, draw message string
			
			g.drawString(NO_IMAGE_STRING, PANEL_WIDTH / NO_IMAGE_STRING_LEFT_PADDING, PANEL_HEIGHT / NO_IMAGE_STRING_TOP_PADDING);
			this.scalingRatioToFitContainer = 1;
			
			return;
		}
		
		// Draw the background image.
	    g.drawImage(this.backgroundImage, 0, 0, this);
	}

	
	/**
	 * Creates a ReceiverView object from Receiver object
	 * and adds it to the MapPreviewPanel. 
	 *
	 * @param receiver Receiver object
	 */
	public void addReceiverViewToMap(Receiver receiver) {
		
		ReceiverView receiverView = new ReceiverView(receiver, this);
		receiverViews.add(receiverView);
		this.add(receiverView);
		receiverView.setLocation((int) receiver.getXPos(), (int) receiver.getYPos());
		componentMover.registerComponent(receiverView);
		repaint();
	}
	
	/**
	 * Removes the ReceiverView object from map.
	 *
	 * @param receiver Receiver object
	 */
	public void removeReceiverViewFromMap(Receiver receiver) {
		
		for (ReceiverView receiverView : receiverViews) {
			if (receiverView.getReceiver().getID() == receiver.getID()) {
				
				// remove receiver from map and receiverViews list
				this.remove(receiverView);
				receiverViews.remove(receiverView);
				componentMover.deregisterComponent(receiverView);
				repaint();
				
				// set receivers coordinates to 0,0
				for (Receiver receiverItem : receivers) {
					if (receiverItem.getID() == receiver.getID()) {
						receiverItem.setOnMap(false);
						receiverItem.setxPos(0.0);
						receiverItem.setyPos(0.0);
					}
				}
				return;
			}
		}
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public void focusReceiverView(ReceiverView receiverViewInFocus) {
		
		this.receiverViewInFocus = receiverViewInFocus;
		
		for (ReceiverView receiverView : receiverViews) {
			receiverView.setBorder(BorderFactory.createLineBorder(Color.black, 0)); // remove border from all reciverViews
			receiverView.repaint();
			if(receiverView.equals(receiverViewInFocus)) {
				receiverView.setBorder(BorderFactory.createLineBorder(Color.black, 2));
				receiverView.repaint();
			}
		}
	}
	
	public void rotateReceiverViewInFocus(double rotateAmount) {
		
		if (receiverViewInFocus == null) {
			return;
		} else {
			receiverViewInFocus.rotate(rotateAmount);
		}
		
	}

	public void addCoordinateZeroViewToMap() {
		
		if (coordinateZeroView == null) {
			coordinateZeroView = new CoordinateZeroView();
			add(coordinateZeroView);
			componentMover.registerComponent(coordinateZeroView);
		} else {
			
			// don't add the second zero coordinate view, just repaint the old one
			coordinateZeroView.repaint();
			revalidate();
		}
	}
	
}
