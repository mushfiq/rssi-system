package data;

import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.helper.Point;


/**
 * Contains the information about watch x and y coordinates, watch id and the 
 * time when the calculation occurred. 
 */
public class WatchPositionData {

	/** The logger. */
	private Logger logger;
	
	/** The watch id. */
	private int watchId;
	
	/** 
	 * The time when reading was calculated. This is also used in the database as the
	 * time when the reading occurred.
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

	public int getWatchId() {
		return watchId;
	}

	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Instantiates a new watch position data.
	 *
	 * @param watchId the watch id
	 * @param time the time
	 * @param position the position
	 */
	public WatchPositionData(int watchId, long time, Point position) {
		super();
		this.watchId = watchId;
		this.time = time;
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "Watch id: " + watchId + ", time: " + time + ", position: " + position.toString();
	}
	
}
