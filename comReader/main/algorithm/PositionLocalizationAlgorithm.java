/*
 * File: PositionLocalizationAlgorithm.java
 * Date				Author				Changes
 * 09 Nov 2013		Tommy Griese		create version 1.0
 */
package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.helper.Point;

import components.Receiver;
import components.RoomMap;

/**
 * The class PositionLocalizationAlgorithm. This abstract class should be inherited by all implemented algorithm classes.
 * It defines the interface of the constructor, the method calculate(...) and some needed variables.
 * 
 * @version 1.0 09 Nov 2013
 * @author Tommy Griese
 */
public abstract class PositionLocalizationAlgorithm {

	/** The array list of all receivers. */
	protected ArrayList<Receiver> receivers;
	
	/** The room map. */
	protected RoomMap roommap;
	
	/**
	 * Instantiates a new PositionLocalizationAlgorithm.
	 *
	 * @param roommap the room map
	 * @param receivers the list of receivers
	 */
	public PositionLocalizationAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		this.roommap = roommap;
		this.receivers = receivers;
	}
	
	/**
	 * Abstract method calculate.
	 *
	 * @param readings the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	public abstract Point calculate(HashMap<Integer, Double> readings);
}
