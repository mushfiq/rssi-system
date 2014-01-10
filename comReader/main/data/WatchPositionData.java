/*
 * 
 * 
 */
package data;

import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.helper.Point;

/**
 * Contains the information about watch x and y coordinates, watch id and the time when the calculation occurred.
 * 
 * @author Danilo
 */
public class WatchPositionData {

	/** <code>Logger</code> object. */
	@SuppressWarnings("unused")
	private Logger logger;

	/** The watch id. */
	private int watchId;

	private int mapId;
	
	public WatchPositionData(int watchId, int mapId, long time, Point position) {
		super();
		this.watchId = watchId;
		this.mapId = mapId;
		this.time = time;
		this.position = position;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * The time when reading was calculated. This is also used in the database as the time when the reading occurred.
	 * 
	 */
	private long time;

	/** The position of the watch. */
	private Point position;

	/**
	 * Instantiates a new watch position data.
	 */
	public WatchPositionData() {
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/**
	 * Gets the watch id.
	 * 
	 * @return the watch id
	 */
	public int getWatchId() {
		return watchId;
	}

	/**
	 * Sets the watch id.
	 * 
	 * @param watchId
	 *            the new watch id
	 */
	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 * 
	 * @param time
	 *            the new time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 * 
	 * @param position
	 *            the new position
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Instantiates a new watch position data.
	 * 
	 * @param watchId
	 *            the watch id
	 * @param time
	 *            the time
	 * @param position
	 *            the position
	 */
	public WatchPositionData(int watchId, long time, Point position) {
		super();
		this.watchId = watchId;
		this.time = time;
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Watch id: " + watchId + ", time: " + time + ", position: " + position.toString();
	}

}
