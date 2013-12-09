/*
 * File: RandomPositionAlgorithm.java
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
 * The class RandomPositionAlgorithm. This class creates some random numbers in a range of the given room map.
 * This class is just for testing purpose.
 * 
 * @version 1.0 09 Nov 2013
 * @author Tommy Griese
 */
public class RandomPositionAlgorithm extends PositionLocalizationAlgorithm {

    /**
     * Instantiates a RandomPositionAlgorithm.
     *
     * @param roommap the room map, used for the range of the random values
     * @param receivers the receivers (not used)
     */
    public RandomPositionAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
            super(roommap, receivers);
    }

    /**
	 * Calculates a random position.
	 * 
	 * @param readings the rssi values measured in dBm for all receivers (not used)
	 * @return the calculated random position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
    @Override
    public Point calculate(HashMap<Integer, Double> readings) {
    	double x = randomInteger(this.roommap.getXFrom(), this.roommap.getXTo());
    	double y = randomInteger(this.roommap.getYFrom(), this.roommap.getYTo());
    	
        return new Point(x, y);
    }
    
    /**
     * Determines a random integer.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the random position
     */
    private double randomInteger(double min, double max) {
    	return Math.random() * (max - min) + min;
    }
}
