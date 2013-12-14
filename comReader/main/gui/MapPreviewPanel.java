package gui;

import gui.enumeration.CoordinateZeroMarkerViewType;
import gui.observer.Observable;
import gui.observer.Observer;

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
import javax.swing.JComponent;
import javax.swing.JPanel;

import utilities.ComponentMover;
import utilities.Utilities;
import components.Receiver;
import components.RoomMap;

/**
 * Displays an image of the map when adding new map to the system or when editing an existing one.
 * 
 * It contains ReceiverView items.
 * 
 * CoordinateZeroView item is also added to this panel.
 * 
 * If the image is larger than the panel itself, it will be scaled to fit the panel, maintaining original aspect ratio.
 */
public class MapPreviewPanel extends JPanel implements Observer {

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

	/**
	 * The scaling ratio of image (if necessary) to fit container. Initially it is 1.
	 */
	private double scalingRatioToFitContainer;

	/** The Constant NO_IMAGE_STRING. */
	private static final String NO_IMAGE_STRING = "Click on 'Upload' button on the right side to show a new map.";

	/** The Constant NO_IMAGE_STRING_LEFT_PADDING. */
	private static final int NO_IMAGE_STRING_LEFT_PADDING = 3;

	/** The Constant NO_IMAGE_STRING_TOP_PADDING. */
	private static final int NO_IMAGE_STRING_TOP_PADDING = 2;

	private static final String NO_IMAGE_ERROR_MESSAGE = "Map must have an image.";
	private static final String INVALID_POSITION_OF_MARKERS_ERROR_MESSAGE = "Map markers are placed incorrectly.";
	private static final String RECEIVERS_OUT_OF_MAP_ERROR_MESSAGE = "Receivers cannot be placed outside the map.";

	/** The receiver views. */
	private List<ReceiverView> receiverViews;

	/** The map. */
	private RoomMap map;

	/** Helper object that handles moving JComponents around a panel. */
	private ComponentMover componentMover;

	/** The receiver view in focus. */
	private ReceiverView receiverViewInFocus;

	private CoordinateZeroMarkerView lowerLeftMarker;
	private CoordinateZeroMarkerView upperRightMarker;
	private AddMapDialog parent;

	/**
	 * Instantiates a new map preview panel.
	 * 
	 * @param map
	 *            the map
	 */
	public MapPreviewPanel(RoomMap map, AddMapDialog parent) {

		receiverViews = new ArrayList<ReceiverView>();
		scalingRatioToFitContainer = 1.0;
		this.map = map;
		this.originalBackgroundImage = (BufferedImage) map.getImage();
		this.backgroundImage = (BufferedImage) map.getImage();
		this.parent = parent;

		initializeGui();
	}

	/**
	 * Instantiates a new map preview panel.
	 */
	public MapPreviewPanel(AddMapDialog parent) {
		this.parent = parent;
		receiverViews = new ArrayList<ReceiverView>();
		initializeGui();
	}

	/**
	 * Initializes GUI. Layout is set to null (no LayoutManager) so that ReceiverView items can be positioned
	 * absolutely.
	 */
	private void initializeGui() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(new Color(230, 230, 230));
		setLayout(null); // in order to position ReceiverViews absolutely
		refreshPreviewImage();

		// register all receiver views to the ComponentMover
		componentMover = new ComponentMover();

		for (Receiver receiver : map.getReceivers()) {

			ReceiverView receiverView = new ReceiverView(receiver, this);
			receiverViews.add(receiverView);
			this.add(receiverView);
			// TODO: location should be calculated with offsets for scaling
			// the image and pixel/meter scaling
			receiverView.setLocation((int) receiver.getXPos() * 100, (int) receiver.getYPos() * 100);
			componentMover.registerComponent(receiverView);
		}
		// add zero coordinate marker views
		addCoordinateZeroMarkerViewsToMap();
	}

	/**
	 * Changes the panel image. When changed, any receivers placed on the map will be removed and a (0,0) offset point
	 * of the map will be reset to the (0,0) offset point of the picture.
	 * 
	 * @param file
	 *            File image
	 * 
	 * */
	public void setPreviewImage(File file) {

		try {
			this.backgroundImage = ImageIO.read(file);
			this.originalBackgroundImage = ImageIO.read(file);
			this.scalingRatioToFitContainer = Utilities.getScalingRatioToFitContainer(this.originalBackgroundImage,
					PANEL_WIDTH, PANEL_HEIGHT);
			this.backgroundImage = Utilities.scaleImageToFitContainer(this.backgroundImage, PANEL_WIDTH, PANEL_HEIGHT);

		} catch (IOException e) {
			logger.severe("Reading of the image failed.\n" + e.getMessage());
		}
	}

	/**
	 * Refresh preview image.
	 */
	private void refreshPreviewImage() {

		if (originalBackgroundImage != null) {
			this.scalingRatioToFitContainer = Utilities.getScalingRatioToFitContainer(this.originalBackgroundImage,
					PANEL_WIDTH, PANEL_HEIGHT);
			this.backgroundImage = Utilities.scaleImageToFitContainer(this.backgroundImage, PANEL_WIDTH, PANEL_HEIGHT);
			this.repaint();
		}
	}

	/**
	 * Overridden method in order to display image as a panel background. If the image is larger than the panel,
	 * determined by PANEL_WIDTH and PANEL_HEIGHT, it will be resized to fit the panel, maintaining the original aspect
	 * ratio.
	 * 
	 * @param g
	 *            Graphics object
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundImage == null) { // if there is no image, draw message string

			g.drawString(NO_IMAGE_STRING, PANEL_WIDTH / NO_IMAGE_STRING_LEFT_PADDING, PANEL_HEIGHT
					/ NO_IMAGE_STRING_TOP_PADDING);
			this.scalingRatioToFitContainer = 1;

		} else {
			// Draw the background image.
			g.drawImage(this.backgroundImage, 0, 0, this);
		}

	}

	/**
	 * Creates a ReceiverView object from Receiver object and adds it to the MapPreviewPanel.
	 * 
	 * @param receiver
	 *            Receiver object
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
	 * @param receiver
	 *            Receiver object
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
				for (Receiver receiverItem : map.getReceivers()) {
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

	/**
	 * Gets the background image.
	 * 
	 * @return the background image
	 */
	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * Focus receiver view.
	 * 
	 * @param receiverViewInFocus
	 *            the receiver view in focus
	 */
	public void focusReceiverView(ReceiverView receiverViewInFocus) {

		this.receiverViewInFocus = receiverViewInFocus;

		for (ReceiverView receiverView : receiverViews) {
			// remove border from all receiverViews
			receiverView.setBorder(BorderFactory.createLineBorder(Color.black, 0));
			receiverView.repaint();
			if (receiverView.equals(receiverViewInFocus)) {
				receiverView.setBorder(BorderFactory.createLineBorder(Color.black, 2));
				receiverView.repaint();
			}
		}
	}

	/**
	 * Rotate receiver view in focus.
	 * 
	 * @param rotateAmount
	 *            the rotate amount
	 */
	public void rotateReceiverViewInFocus(double rotateAmount) {

		if (receiverViewInFocus == null) {
			return;
		} else {
			receiverViewInFocus.rotate(rotateAmount);
		}
	}

	/**
	 * Adds coordinate zero marker views to map.
	 */
	public void addCoordinateZeroMarkerViewsToMap() {

		if ((lowerLeftMarker == null) || (upperRightMarker == null)) {

			lowerLeftMarker = new CoordinateZeroMarkerView(CoordinateZeroMarkerViewType.LOWER_LEFT, this);
			upperRightMarker = new CoordinateZeroMarkerView(CoordinateZeroMarkerViewType.UPPER_RIGHT, this);
			add(lowerLeftMarker);
			add(upperRightMarker);
			componentMover.registerComponent(lowerLeftMarker);
			componentMover.registerComponent(upperRightMarker);
		} else {
			// don't add the second zero coordinate view, just repaint the old one
			lowerLeftMarker.repaint();
			upperRightMarker.repaint();
			revalidate();
		}
	}

	public void setStatus(String message) {
		// delegate call to AddMapDialog instance
		String updateMessage = message;
		parent.setStatus(updateMessage);
	}

	@Override
	public void update(Observable observable) {

		if (observable instanceof JComponent) {

			JComponent component = (JComponent) observable;
			String scaledPositionMessage = "Scaled position (px): ";
			scaledPositionMessage += "x = " + component.getLocation().x + ", y = " + component.getLocation().y + ".";
			String actualPositionMessage = "Actual position (px): ";
			actualPositionMessage += "x = " + (int) (component.getLocation().x / scalingRatioToFitContainer) + ", y = "
					+ (int) (component.getLocation().y / scalingRatioToFitContainer) + ".";

			// TODO calculate position in meters
			String positionInMetersMessage = "Position in meters: ";
			positionInMetersMessage += "x = ";

			String messageToDisplay = scaledPositionMessage + " " + actualPositionMessage + positionInMetersMessage;
			parent.setStatus(messageToDisplay);
		}
	}

	public List<String> getValidationErrorMessages() {

		ArrayList<String> validationErrors = new ArrayList<>();
		// check if map has an image
		if (map.getImage() == null) {
			validationErrors.add(NO_IMAGE_ERROR_MESSAGE);
		}
		// check if map markers are in valid positions

		// we take the lower left corner of the lowerLeftMarker as the marker position point
		int lowerLeftMarkerPositionX = (int) (lowerLeftMarker.getLocation().getX() / scalingRatioToFitContainer);
		int lowerLeftMarkerPositionY = (int) (lowerLeftMarker.getLocation().getY() / scalingRatioToFitContainer)
				+ (CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_HEIGHT);

		// we take the upper right corner of the upperRightMarker as the marker position point
		int upperRightMarkerPositionX = (int) (upperRightMarker.getLocation().getX() / scalingRatioToFitContainer)
				+ (CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_WIDTH);
		int upperRightMarkerPositionY = (int) (upperRightMarker.getLocation().getY() / scalingRatioToFitContainer);

		if ((lowerLeftMarkerPositionX > upperRightMarkerPositionX)
				|| (lowerLeftMarkerPositionY < upperRightMarkerPositionY)) {
			validationErrors.add(INVALID_POSITION_OF_MARKERS_ERROR_MESSAGE);
		}

		// check if receivers are outside the map
		for (ReceiverView receiverView : receiverViews) {
			// we take the middle point of receiverView as the receiver position point
			int receiverViewLocationX = (int) (receiverView.getLocation().getX() / scalingRatioToFitContainer)
					+ (ReceiverView.RECEIVER_ITEM_WIDTH / 2);
			int receiverViewLocationY = (int) (receiverView.getLocation().getY() / scalingRatioToFitContainer)
					+ (ReceiverView.RECEIVER_ITEM_HEIGHT / 2);

			if ((receiverViewLocationX < lowerLeftMarkerPositionX)
					|| (receiverViewLocationX > upperRightMarkerPositionX)
					|| (receiverViewLocationY > lowerLeftMarkerPositionY)
					|| (receiverViewLocationY < upperRightMarkerPositionY)) {
				validationErrors.add(RECEIVERS_OUT_OF_MAP_ERROR_MESSAGE);
				break;
			}
		}

		return validationErrors;
	}

	public RoomMap getMap() {

		// TODO set all map properties and return the map object

		// set zero coordinate marker positions
		int lowerLeftMarkerOffsetXInPixels = (int) (lowerLeftMarker.getLocation().getX() / scalingRatioToFitContainer);
		int lowerLeftMarkerOffsetYInPixels = (int) (lowerLeftMarker.getLocation().getY() / scalingRatioToFitContainer)
				+ (CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_HEIGHT);
		map.setLowerLeftMarkerOffsetXInPixels(lowerLeftMarkerOffsetXInPixels);
		map.setLowerLeftMarkerOffsetYInPixels(lowerLeftMarkerOffsetYInPixels);

		int upperRightMarkerOffsetXInPixels = (int) (upperRightMarker.getLocation().getX() / scalingRatioToFitContainer)
				+ (CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_WIDTH);
		int upperRightMarkerOffsetYInPixels = (int) (upperRightMarker.getLocation().getY() / scalingRatioToFitContainer);
		map.setUpperRightMarkerOffsetXInPixels(upperRightMarkerOffsetXInPixels);
		map.setUpperRightMarkerOffsetYInPixels(upperRightMarkerOffsetYInPixels);

		// set room width and height in meters
		double roomWidthInMeters = parent.getRoomWidthInMeters();
		double roomHeightInMeters = parent.getRoomHeightInMeters();
		map.setWidthInMeters(roomWidthInMeters);
		map.setHeightInMeters(roomHeightInMeters);

		// set width and height ratios
		int mapWidthInPixels = upperRightMarkerOffsetXInPixels - lowerLeftMarkerOffsetXInPixels;
		int mapHeightInPixels = lowerLeftMarkerOffsetYInPixels - upperRightMarkerOffsetYInPixels;

		double mapWidthRatio = (mapWidthInPixels) / roomWidthInMeters;
		double mapHeightRatio = (mapHeightInPixels) / roomHeightInMeters;

		map.setRatioWidth(mapWidthRatio);
		map.setRatioHeight(mapHeightRatio);

		// set receiver positions in meters
		for (ReceiverView receiverView : receiverViews) {

			int receiverViewXInPixels = ((int) (receiverView.getLocation().getX() / scalingRatioToFitContainer))
					+ (ReceiverView.RECEIVER_ITEM_WIDTH / 2);
			int receiverViewYInPixels = ((int) (receiverView.getLocation().getY() / scalingRatioToFitContainer))
					+ (ReceiverView.RECEIVER_ITEM_HEIGHT / 2);

			double receiverPositionInMetersX = calculateReceiverPositionInMetersX(lowerLeftMarkerOffsetXInPixels,
					receiverViewXInPixels, mapWidthRatio);
			double receiverPositionInMetersY = calculateReceiverPositionInMetersY(lowerLeftMarkerOffsetYInPixels,
					receiverViewYInPixels, mapHeightRatio);

			receiverView.getReceiver().setxPos(receiverPositionInMetersX);
			receiverView.getReceiver().setyPos(receiverPositionInMetersY);
			map.addReceiver(receiverView.getReceiver());
			System.out.println("Receiver[" + receiverView.getReceiver().getID() + "] position x (m): "
					+ receiverPositionInMetersX + "\nReceiver position y (m): " + receiverPositionInMetersY);
		}

		System.out.println(map);
		return map;
	}

	private double calculateReceiverPositionInMetersX(int zeroPointLowerLeftX, int receiverViewXInPixels,
			double mapWidthRatio) {

		double result = (receiverViewXInPixels - zeroPointLowerLeftX) / mapWidthRatio;
		return result;
	}

	private double calculateReceiverPositionInMetersY(int zeroPointLowerLeftY, int receiverViewYInPixels,
			double mapHeightRatio) {

		double result = (zeroPointLowerLeftY - receiverViewYInPixels) / mapHeightRatio;
		return result;
	}

}
