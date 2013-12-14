/*
 * File: RoomMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 * 03 Dec 2013		Tommy Griese		Code refactoring
 */
package components;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import algorithm.helper.PointRoomMap;

/**
 * The class RoomMap represents the map of a room.
 * 
 * @version 1.1 03 Dec 2013
 * @author Tommy Griese
 */
public class RoomMap {

	/** The default granularity constant for the roommap. */
	public static final double GRANULARITY_DEFAULT = 0.25;

	/** The actual granularity for the roommap. */
	private double granularity;

	/** The start value for the room map in x. */
	private double xFrom;

	/** The end value for the room map in x. */
	private double xTo;

	/** The start value for the room map in y. */
	private double yFrom;

	/** The end value for the room map in y. */
	private double yTo;

	/** The path to the image of the room map. */
	private Image image;

	/** Each point of this list contains a weighted point in a room ({@link algorithm.helper.PointRoomMap}). */
	private ArrayList<PointRoomMap> pointsRoomMap;
	private int id;
	private String title;
	private List<Receiver> receivers;
	private int lowerLeftMarkerOffsetXInPixels;
	private int lowerLeftMarkerOffsetYInPixels;
	private int upperRightMarkerOffsetXInPixels;
	private int upperRightMarkerOffsetYInPixels;
	private double ratioWidth;
	private double ratioHeight;
	private double widthInMeters;
	private double heightInMeters;

	public RoomMap() {
		pointsRoomMap = new ArrayList<PointRoomMap>();
		title = "Title";
		receivers = new ArrayList<Receiver>();
	}

	public ArrayList<PointRoomMap> getPointsRoomMap() {
		return pointsRoomMap;
	}

	public void setPointsRoomMap(ArrayList<PointRoomMap> pointsRoomMap) {
		this.pointsRoomMap = pointsRoomMap;
	}

	/**
	 * Instantiates a new room map.
	 * 
	 * @param xFrom
	 *            the start value for the room map in x
	 * @param xTo
	 *            the end value for the room map in x
	 * @param yFrom
	 *            the start value for the room map in y
	 * @param yTo
	 *            the end value for the room map in y
	 * @param granularity
	 *            the granularity of the room map
	 * @param pathToImage
	 *            the path to the image
	 */
	public RoomMap(double xFrom, double xTo, double yFrom, double yTo, double granularity, Image image) {
		super();
		this.xFrom = xFrom;
		this.xTo = xTo;

		this.yFrom = yFrom;
		this.yTo = yTo;
		this.image = image;
		this.granularity = granularity;

		initialize();
	}

	/**
	 * Instantiates a new room map.
	 * 
	 * @param xFrom
	 *            the start value for the room map in x
	 * @param xTo
	 *            the end value for the room map in x
	 * @param yFrom
	 *            the start value for the room map in y
	 * @param yTo
	 *            the end value for the room map in y
	 * @param image
	 *            Image object the path to the image of the room map
	 */
	public RoomMap(double xFrom, double xTo, double yFrom, double yTo, Image image) {
		super();
		this.xFrom = xFrom;
		this.xTo = xTo;

		this.yFrom = yFrom;
		this.yTo = yTo;
		this.image = image;

		this.granularity = RoomMap.GRANULARITY_DEFAULT;

		initialize();
	}

	public RoomMap(Image image, String title, List<Receiver> receivers) {
		super();
		this.image = image;

		if (title == null || title == "") {
			this.title = new SimpleDateFormat("dd.MM.yyyy, HH:ss").format(new Date());
		} else {
			this.title = title;
		}

		this.receivers = receivers;
	}

	/**
	 * Gets the start value for the room map in x.
	 * 
	 * @return the x from
	 */
	public double getXFrom() {
		return this.xFrom;
	}

	/**
	 * Gets the end value for the room map in x.
	 * 
	 * @return the x to
	 */
	public double getXTo() {
		return this.xTo;
	}

	/**
	 * Gets the start value for the room map in y.
	 * 
	 * @return the y from
	 */
	public double getYFrom() {
		return this.yFrom;
	}

	/**
	 * Gets the end value for the room map in y.
	 * 
	 * @return the y to
	 */
	public double getYTo() {
		return this.yTo;
	}

	/**
	 * Gets the image.
	 * 
	 * @return image Image object
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image
	 *            Image object
	 */
	public void setPathToImage(Image image) {
		this.image = image;
	}

	/**
	 * Creates an array list of the room map with weighted points ({@link algorithm.helper.PointRoomMap}).
	 */
	public void initialize() {

		this.pointsRoomMap = new ArrayList<PointRoomMap>();
		for (double i = this.xFrom; i <= this.xTo; i += this.granularity) { // x-Axe
			for (double j = this.yFrom; j <= this.yTo; j += this.granularity) { // y-Axe
				this.pointsRoomMap.add(new PointRoomMap(i, j));
			}
		}
	}

	/**
	 * Gets the room map points.
	 * 
	 * @return the room map points
	 */
	public ArrayList<PointRoomMap> getRoomMapPoints() {
		return this.pointsRoomMap;
	}

	/**
	 * Gets the granularity.
	 * 
	 * @return the granularity
	 */
	public double getGranularity() {
		return this.granularity;
	}

	public String getTitle() {
		return title;
	}

	public List<Receiver> getReceivers() {
		return receivers;
	}

	public int getId() {
		return id;
	}

	public int getLowerLeftMarkerOffsetXInPixels() {
		return lowerLeftMarkerOffsetXInPixels;
	}

	public void setLowerLeftMarkerOffsetXInPixels(int lowerLeftMarkerOffsetXInPixels) {
		this.lowerLeftMarkerOffsetXInPixels = lowerLeftMarkerOffsetXInPixels;
	}

	public int getLowerLeftMarkerOffsetYInPixels() {
		return lowerLeftMarkerOffsetYInPixels;
	}

	public void setLowerLeftMarkerOffsetYInPixels(int lowerLeftMarkerOffsetYInPixels) {
		this.lowerLeftMarkerOffsetYInPixels = lowerLeftMarkerOffsetYInPixels;
	}

	public int getUpperRightMarkerOffsetXInPixels() {
		return upperRightMarkerOffsetXInPixels;
	}

	public void setUpperRightMarkerOffsetXInPixels(int upperRightMarkerOffsetXInPixels) {
		this.upperRightMarkerOffsetXInPixels = upperRightMarkerOffsetXInPixels;
	}

	public int getUpperRightMarkerOffsetYInPixels() {
		return upperRightMarkerOffsetYInPixels;
	}

	public void setUpperRightMarkerOffsetYInPixels(int upperRightMarkerOffsetYInPixels) {
		this.upperRightMarkerOffsetYInPixels = upperRightMarkerOffsetYInPixels;
	}

	public double getRatioWidth() {
		return ratioWidth;
	}

	public void setRatioWidth(double ratioWidth) {
		this.ratioWidth = ratioWidth;
	}

	public double getRatioHeight() {
		return ratioHeight;
	}

	public void setRatioHeight(double ratioHeight) {
		this.ratioHeight = ratioHeight;
	}

	public double getWidthInMeters() {
		return widthInMeters;
	}

	public void setWidthInMeters(double widthInMeters) {
		this.widthInMeters = widthInMeters;
	}

	public double getHeightInMeters() {
		return heightInMeters;
	}

	public void setHeightInMeters(double heightInMeters) {
		this.heightInMeters = heightInMeters;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {

		return "Id: " + id + "\ntitle: " + title + "\nlower left marker offset x (px): "
				+ lowerLeftMarkerOffsetXInPixels + "\nlower left marker offset y (px): "
				+ lowerLeftMarkerOffsetYInPixels + "\nupper right marker offset x (px): "
				+ upperRightMarkerOffsetXInPixels + "\nupper right marker offset y (px): "
				+ upperRightMarkerOffsetYInPixels + "\nratio width (px/m): " + ratioWidth
				+ "\nratio height (px/m): " + ratioHeight + "\nmap width (m): " + widthInMeters
				+ "\nmap height (m): " + heightInMeters;
	}
	
	public void addReceiver(Receiver receiver) {
		receivers.add(receiver);
	}
	
	public void removeReceiver(Receiver receiver) {
		
		int listSize = receivers.size();
		for (int i = 0; i < listSize; i++) {
			if (receivers.get(i).getID() == receiver.getID()) {
				receivers.remove(i);
				break;
			}	
		}
	}

}
