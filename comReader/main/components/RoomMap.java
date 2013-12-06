/*
 * File: RoomMap.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 */
package components;

import java.awt.image.BufferedImage;

/**
 * The class RoomMap represents the map of a room.
 * 
 * @version 1.0 08 Nov 2013
 * @author Tommy Griese
 */
public class RoomMap {
	
	/** The start value for the room map in x. */
	private double xFrom;
	
	/** The end value for the room map in x. */
	private double xTo;
	
	/** The start value for the room map in y. */
	private double yFrom;
	
	/** The end value for the room map in y. */
	private double yTo;
	
	/** Map image. */
	private BufferedImage mapImage;
	
	private double widthInMeters;
	private double heightInMeters;
	private double scaling; // pixels/meter
	private String title;
	
	/**
	 * Instantiates a new room map.
	 *
	 * @param xFrom the start value for the room map in x
	 * @param xTo the end value for the room map in x
	 * @param yFrom the start value for the room map in y
	 * @param yTo the end value for the room map in y
	 */
	public RoomMap(double xFrom, double xTo, double yFrom, double yTo) {
		this.xFrom = xFrom;
		this.xTo = xTo;
		
		this.yFrom = yFrom;
		this.yTo = yTo;
	}
	
	/**
	 * Instantiates a new room map.
	 *
	 * @param xFrom the start value for the room map in x
	 * @param xTo the end value for the room map in x
	 * @param yFrom the start value for the room map in y
	 * @param yTo the end value for the room map in y
	 * @param pathToImage the path to the image of the room map
	 */
	public RoomMap(double xFrom, double xTo, double yFrom, double yTo, BufferedImage image) {
		super();
		this.xFrom = xFrom;
		this.xTo = xTo;
		
		this.yFrom = yFrom;
		this.yTo = yTo;
		this.mapImage = image;
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
	 * Gets the path to the image.
	 *
	 * @return the path to the image
	 */
	public BufferedImage getPathToImage() {
		return mapImage;
	}

	/**
	 * Sets the path to the image.
	 *
	 * @param pathToImage the new path to the image
	 */
	public void setPathToImage(BufferedImage image) {
		this.mapImage = image;
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

	public BufferedImage getMapImage() {
		return mapImage;
	}

	public void setMapImage(BufferedImage mapImage) {
		this.mapImage = mapImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
