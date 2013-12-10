/*
 * File: PositionLocalizationAlgorithm.java
 * Date				Author				Changes
 * 09 Nov 2013		Tommy Griese		initial: version 0.1
 */
package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import components.Receiver;
import components.RoomMap;
import algorithm.helper.Point;

/**
 * The Class TrilaterationAlgorithm.
 */
public class TrilaterationAlgorithm extends PositionLocalizationAlgorithm {

	/**
	 * Instantiates a new TrilaterationAlgorithm.
	 *
	 * @param roommap defines the room map dimensions
	 * @param receivers a list of receivers that the algorithm shall take into account
	 */
	public TrilaterationAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);
	}

	/**
	 * Calculates the position depending of the given readings. 
	 * Up to now the position will NOT be located within the borders of the room. (future enhancement)
	 * 
	 * @param readings the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	@Override
	public Point calculate(HashMap<Integer, Double> readings) {
		return null;
	}
}
