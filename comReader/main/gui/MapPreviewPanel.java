/*
 * 
 * 
 */
package gui;

import gui.enumeration.CoordinateZeroMarkerViewType;
import gui.observer.Observable;
import gui.observer.Observer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
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

import main.Application;
import utilities.ComponentMover;
import utilities.Utilities;
import components.Receiver;
import components.RoomMap;

/**
 * Displays an image of the map when adding new map to the data source or when editing an existing one.<br>
 * <br>
 * 
 * It contains <code>ReceiverView</code> items. <code>CoordinateZeroView</code> item is also added to this panel. <br>
 * <br>
 * 
 * If the image is larger than the panel itself, it will be scaled to fit the panel, maintaining original aspect ratio.
 * 
 * @author Danilo
 * @see ReceiverView
 * @see CoordinateZeroMarkerView
 */
public class MapPreviewPanel extends JPanel implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant PANEL_WIDTH. */
	private static final int PANEL_WIDTH = 950;

	/** The Constant PANEL_HEIGHT. */
	private static final int PANEL_HEIGHT = 550;

	/** The Constant NO_IMAGE_STRING. */
	private static final String NO_IMAGE_STRING = "Click on 'Choose image' button on the right side to show a new map.";

	/** The Constant NO_IMAGE_STRING_LEFT_PADDING. */
	private static final int NO_IMAGE_STRING_LEFT_PADDING = 3;

	/** The Constant NO_IMAGE_STRING_TOP_PADDING. */
	private static final int NO_IMAGE_STRING_TOP_PADDING = 2;

	/** The Constant NO_IMAGE_ERROR_MESSAGE. */
	private static final String NO_IMAGE_ERROR_MESSAGE = "Map must have an image.";

	/** The Constant INVALID_POSITION_OF_MARKERS_ERROR_MESSAGE. */
	private static final String INVALID_POSITION_OF_MARKERS_ERROR_MESSAGE = "Map markers are placed incorrectly.";

	/** The Constant RECEIVERS_OUT_OF_MAP_ERROR_MESSAGE. */
	private static final String RECEIVERS_OUT_OF_MAP_ERROR_MESSAGE = "Receivers cannot be placed outside the map.";

	/** The Constant GRAY_COLOUR. */
	private static final int GRAY_COLOUR = 230;

	/**
	 * The scaling ratio of image (if necessary) to fit container. Initially it is 1.
	 */
	private double scalingRatioToFitContainer;

	/** <code>Logger</code> object. */
	private Logger logger;

	/** Image of a map drawn as a panel background. */
	private BufferedImage backgroundImage;

	/** The original background image. */
	private BufferedImage originalBackgroundImage;

	/** The receiver views. */
	private List<ReceiverView> receiverViews;

	/** The map. */
	private RoomMap map;

	/** Helper object that handles moving JComponents around a panel. */
	private ComponentMover componentMover;

	/** The receiver view in focus. */
	private ReceiverView receiverViewInFocus;

	/** The lower left marker. */
	private CoordinateZeroMarkerView lowerLeftMarker;

	/** The upper right marker. */
	private CoordinateZeroMarkerView upperRightMarker;

	/** The parent. */
	private AddMapDialog parent;

	/**
	 * Instantiates a new <code>MapPreviewPanel</code>. This constructor is used when we open <code>AddMapDialog</code>
	 * in <code>AddMapDialogMode.EDIT</code> mode.
	 * 
	 * @param map
	 *            <code>RoomMap</code> object
	 * @param parent
	 *            <code>AddMapDialog</code> parent object
	 * @see AddMapDialog
	 * @see gui.enumeration.AddMapDialogMode
	 */
	public MapPreviewPanel(RoomMap map, AddMapDialog parent) {

		receiverViews = new ArrayList<ReceiverView>();
		scalingRatioToFitContainer = 1.0;
		this.map = map;
		// TODO: added code
		Application.getApplication().setRoomMap(this.map);
		this.originalBackgroundImage = Utilities.deepCopy((BufferedImage) map.getImage());
		this.backgroundImage = Utilities.deepCopy((BufferedImage) map.getImage());
		this.parent = parent;
		initializeGui();
	}

	/**
	 * Instantiates a new <code>MapPreviewPanel</code>. This constructor is used when we open <code>AddMapDialog</code>
	 * in <code>AddMapDialogMode.ADD</code> mode.
	 * 
	 * @param parent
	 *            <code>AddMapDialog</code> parent object
	 */
	public MapPreviewPanel(AddMapDialog parent) {

		receiverViews = new ArrayList<ReceiverView>();
		scalingRatioToFitContainer = 1.0;
		this.map = new RoomMap();
		this.parent = parent;
		initializeGui();
	}

	/**
	 * Initializes graphical user interface. Layout of this panel is set to <code>null</code> (no
	 * <code>LayoutManager</code>) so that <code>ReceiverView</code> items can be positioned absolutely.
	 * 
	 * @see ReceiverView
	 * @see LayoutManager
	 */
	private void initializeGui() {

		logger = Utilities.initializeLogger(this.getClass().getName());
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(new Color(GRAY_COLOUR, GRAY_COLOUR, GRAY_COLOUR));
		setLayout(null); // in order to position ReceiverViews absolutely
		refreshPreviewImage();
		// register all receiver views to the ComponentMover
		componentMover = new ComponentMover();
		// add zero coordinate marker views
		addCoordinateZeroMarkerViewsToMap();

		for (Receiver receiver : map.getReceivers()) {

			ReceiverView receiverView = new ReceiverView(receiver, this);
			receiverViews.add(receiverView);
			this.add(receiverView);
			// TODO: location should be calculated with offsets for scaling
			// the image and pixel/meter scaling

			double receiverPositionInMetersX = receiver.getXPos();
			double receiverPositionInMetersY = receiver.getYPos();
			double mapRatioWidth = map.getRatioWidth();
			double mapRatioHeight = map.getRatioHeight();
			int lowerLeftMarkerOffsetXInPixels = map.getLowerLeftMarkerOffsetXInPixels();
			int lowerLeftMarkerOffsetYInPixels = map.getLowerLeftMarkerOffsetYInPixels();

			int receiverPositionInPixelsX = calculateReceiverPositionInPixelsX(receiverPositionInMetersX,
				lowerLeftMarkerOffsetXInPixels, mapRatioWidth);
			int receiverPositionInPixelsY = calculateReceiverPositionInPixelsY(receiverPositionInMetersY,
				lowerLeftMarkerOffsetYInPixels, mapRatioHeight);

			receiverView.setLocation(receiverPositionInPixelsX, receiverPositionInPixelsY);
			componentMover.registerComponent(receiverView);
		}

	}

	/**
	 * Changes the panel image. When changed, any <code>ReceiverView</code>s placed on the map will be removed and
	 * <code>CoordinateZeroMarkerView</code>'s will be set to (0,0) position.
	 * 
	 * @param file
	 *            <code>File</code> Image file
	 * @see <code>ReceiverView</code>
	 * */
	public void setPreviewImage(File file) {

		try {
			this.backgroundImage = ImageIO.read(file);
			this.originalBackgroundImage = ImageIO.read(file);
			this.scalingRatioToFitContainer = Utilities.getScalingRatioToFitContainer(this.originalBackgroundImage,
				PANEL_WIDTH, PANEL_HEIGHT);
			this.backgroundImage = Utilities.scaleImageToFitContainer(this.backgroundImage, PANEL_WIDTH, PANEL_HEIGHT);
			this.map.setImage(originalBackgroundImage);
		} catch (IOException e) {
			logger.severe("Reading of the image failed.\n" + e.getMessage());
		}
	}

	/**
	 * Refreshes preview image.
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
	 * determined by <code>PANEL_WIDTH</code> and <code>PANEL_HEIGHT</code>, it will be resized to fit the panel,
	 * maintaining the original aspect ratio.
	 * 
	 * @param g
	 *            <code>Graphics</code> object
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
	 * Creates a <code>ReceiverView</code> object from <code>Receiver</code> object and adds it to the
	 * <code>MapPreviewPanel</code>.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> object
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
	 * Removes the <code>ReceiverView</code> object from <code>RoomMap</code>.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> object
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
	 * @return <code>BufferedImage</code> background image
	 */
	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * Sets one of the <code>ReceiverView</code>s in focus. <code>ReceiverView</code> in focus is denoted by black
	 * border around itself (<code>ReceiverView</code>s that are not in focus don't have this border). When focused,
	 * <code>ReceiverView</code>'s <code>angle</code> property can be changed through <code>ParametersPanel</code>.
	 * 
	 * @param receiverViewInFocus
	 *            <code>ReceiverView</code> object in focus
	 * @see ReceiverView
	 * @see ParametersPanel
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
	 * Rotates <code>ReceiverView</code> in focus.
	 * 
	 * @param rotateAmount
	 *            <code>double</code> rotate amount
	 */
	public void rotateReceiverViewInFocus(double rotateAmount) {

		if (receiverViewInFocus == null) {
			return;
		} else {
			receiverViewInFocus.rotate(rotateAmount);
		}
	}

	/**
	 * Adds <code>CoordinateZeroMarkerView</code>s to <code>RoomMap</code>.
	 */
	public void addCoordinateZeroMarkerViewsToMap() {

		if (lowerLeftMarker == null || upperRightMarker == null) {
			lowerLeftMarker = new CoordinateZeroMarkerView(CoordinateZeroMarkerViewType.LOWER_LEFT, this);
			upperRightMarker = new CoordinateZeroMarkerView(CoordinateZeroMarkerViewType.UPPER_RIGHT, this);

			if (map.getLowerLeftMarkerOffsetXInPixels() != 0 && map.getLowerLeftMarkerOffsetYInPixels() != 0) {
				lowerLeftMarker
						.setLocation(
							(int) (map.getLowerLeftMarkerOffsetXInPixels() * scalingRatioToFitContainer),
							 ((int) (map.getLowerLeftMarkerOffsetYInPixels() * scalingRatioToFitContainer) - (int) (CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_HEIGHT * scalingRatioToFitContainer)));
			}

			if (map.getUpperRightMarkerOffsetXInPixels() != 0 && map.getUpperRightMarkerOffsetYInPixels() != 0) {
				upperRightMarker
						.setLocation(
							((int)(map.getUpperRightMarkerOffsetXInPixels() * scalingRatioToFitContainer)
									- (int)(CoordinateZeroMarkerView.ZERO_COORDINATE_MARKER_VIEW_WIDTH * scalingRatioToFitContainer)),
							(int) (map.getUpperRightMarkerOffsetYInPixels() * scalingRatioToFitContainer));
			}

			add(lowerLeftMarker);
			add(upperRightMarker);
			lowerLeftMarker.repaint();
			upperRightMarker.repaint();
			revalidate();
			componentMover.registerComponent(lowerLeftMarker);
			componentMover.registerComponent(upperRightMarker);
		} else {
			// don't add another pair of zero coordinate map marker views, just set position to zero and repaint the old
			// ones
			lowerLeftMarker.setLocation(0, 0);
			lowerLeftMarker.repaint();
			upperRightMarker.setLocation(0, 0);
			upperRightMarker.repaint();
			revalidate();
		}
	}

	/**
	 * Sets the status message. Call is delegated to <code>AddMapDialog</code>.
	 * 
	 * @param message
	 *            <code>String</code> new status message
	 * @see AddMapDialog
	 */
	public void setStatus(String message) {
		// delegate call to AddMapDialog instance
		parent.setStatus(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.observer.Observer#update(gui.observer.Observable)
	 */
	@Override
	public void update(Observable observable) {

		if (observable instanceof JComponent) {

			JComponent component = (JComponent) observable;
			String scaledPositionMessage = "Scaled position (px): ";
			scaledPositionMessage += "x = " + component.getLocation().x + ", y = " + component.getLocation().y + ".";
			String actualPositionMessage = "Actual position (px): ";
			actualPositionMessage += "x = " + (int) (component.getLocation().x / scalingRatioToFitContainer) + ", y = "
					+ (int) (component.getLocation().y / scalingRatioToFitContainer) + ".";
			String messageToDisplay = scaledPositionMessage + " " + actualPositionMessage;

			parent.setStatus(messageToDisplay);
		}
	}

	/**
	 * Gets the validation error messages.
	 * 
	 * @return <code>List</code> of validation error messages
	 */
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
				validationErrors.add(RECEIVERS_OUT_OF_MAP_ERROR_MESSAGE + " Please check receiver " + receiverView.getReceiver().getID());
				break;
			}
		}

		return validationErrors;
	}

	/**
	 * Getter for <code>RoomMap</code> object with all its properties, including <code>Receiver</code>s and
	 * <code>CoordinateZeroMarkerView</code>s .
	 * 
	 * @return the map
	 * @see Receiver
	 * @see ReceiverView
	 * @see CoordinateZeroMarkerView
	 */
	public RoomMap getMap() {

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
		map.getReceivers().clear(); // first we remove all present receivers
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
		}
		
		// set map title
		String titleFromInput = parent.getRoomTitle();
		map.setTitle(titleFromInput.equals("") ? "Unkown" : titleFromInput);

		// set map xFrom, xTo, yFrom and yTo values
		map.setxFrom(0);
		map.setxTo(map.getWidthInMeters());
		map.setyFrom(0);
		map.setyTo(map.getHeightInMeters());

		Application.getApplication().setRoomMap(map);
		
		return map;
	}

	/**
	 * Calculates <code>Receiver</code>'s <b>X</b> coordinate in meters, for given position in pixels, zero coordinate
	 * point and map width ratio.
	 * 
	 * @param zeroPointLowerLeftX
	 *            <code>int</code> zero point lower left <b>X</b> coordinate
	 * @param receiverViewXInPixels
	 *            <code>int</code> receiver view <b>X</b> in pixels
	 * @param mapWidthRatio
	 *            <code>double</code> map width ratio
	 * @return the double
	 */
	private double calculateReceiverPositionInMetersX(int zeroPointLowerLeftX, int receiverViewXInPixels,
			double mapWidthRatio) {

		double result = (receiverViewXInPixels - zeroPointLowerLeftX) / mapWidthRatio;
		return result;
	}

	/**
	 * Calculates <code>Receiver</code>'s <b>Y</b> coordinate in meters, for given position in pixels, zero coordinate
	 * point and map width ratio.
	 * 
	 * @param zeroPointLowerLeftY
	 *            <code>int</code> zero point lower left <b>Y</b> coordinate
	 * @param receiverViewYInPixels
	 *            <code>int</code> receiver view <b>Y</b> in pixels
	 * @param mapHeightRatio
	 *            <code>double</code> map height ratio
	 * @return the double
	 */
	private double calculateReceiverPositionInMetersY(int zeroPointLowerLeftY, int receiverViewYInPixels,
			double mapHeightRatio) {

		double result = (zeroPointLowerLeftY - receiverViewYInPixels) / mapHeightRatio;
		return result;
	}

	/**
	 * Calculates <code>Receiver</code>'s <b>X</b> coordinate in pixels, for given <code>Receiver</code> position in
	 * meters, zero coordinate point in pixels and map width ratio.
	 * 
	 * @param receiverPositionInMetersX
	 *            <code>double</code> receiver position in meters x
	 * @param lowerLeftMarkerOffsetXInPixels
	 *            <code>int</code> lower left marker offset x in pixels
	 * @param mapRatioWidth
	 *            <code>double</code> map width ratio
	 * @return the int
	 */
	private int calculateReceiverPositionInPixelsX(double receiverPositionInMetersX,
			int lowerLeftMarkerOffsetXInPixels, double mapRatioWidth) {

		// calculate offset position in pixels
		int offsetReceiverPositionInPixelsX = ((int) (receiverPositionInMetersX * mapRatioWidth))
				+ lowerLeftMarkerOffsetXInPixels;

		// calculate position in pixels without the offset, taking into account that ReceiverView has a center-point
		int positionFromTopLeftPoint = offsetReceiverPositionInPixelsX - (ReceiverView.RECEIVER_ITEM_WIDTH / 2);

		// calculate scaled position in pixels
		int scaledPositionFromTopLeftPoint = (int) (positionFromTopLeftPoint * scalingRatioToFitContainer);

		return scaledPositionFromTopLeftPoint;
	}

	/**
	 * Calculates <code>Receiver</code>'s <b>Y</b> coordinate in pixels, for given <code>Receiver</code> position in
	 * meters, zero coordinate point in pixels and map width ratio.
	 * 
	 * @param receiverPositionInMetersY
	 *            <code>double</code> receiver position in meters y
	 * @param lowerLeftMarkerOffsetYInPixels
	 *            <code>int</code> lower left marker offset y in pixels
	 * @param mapRatioHeight
	 *            <code>double</code> map height ratio
	 * @return the int
	 */
	private int calculateReceiverPositionInPixelsY(double receiverPositionInMetersY,
			int lowerLeftMarkerOffsetYInPixels, double mapRatioHeight) {

		// calculate offset position in pixels
		int offsetReceiverPositionInPixelsY = ((lowerLeftMarkerOffsetYInPixels - (int) (receiverPositionInMetersY * mapRatioHeight)));

		// calculate position in pixels without the offset, taking into account that ReceiverView has a center-point
		int positionFromTopLeftPoint = offsetReceiverPositionInPixelsY - (ReceiverView.RECEIVER_ITEM_HEIGHT / 2);

		// calculate scaled position in pixels
		int scaledPositionFromTopLeftPoint = (int) (positionFromTopLeftPoint * scalingRatioToFitContainer);

		return scaledPositionFromTopLeftPoint;
	}
}
