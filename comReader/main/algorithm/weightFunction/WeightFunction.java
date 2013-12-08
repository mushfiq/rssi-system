/*
 * File: WeightFunction.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 */
package algorithm.weightFunction;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import components.Receiver;
import components.RoomMap;

/**
 * The class WeightFunction. This abstract class should be inherited by all implemented weight function classes.
 * It defines a method to weight the points in a room map.
 * 
 * @version 1.0 08 Dec 2013
 * @author Tommy Griese
 */
public abstract class WeightFunction {
	
	/**
	 * Instantiates a new weight function.
	 */
	public WeightFunction() {
	}
	
	/**
	 * Test if the given point is located inside the convex hull or not.
	 *
	 * @param pRoomMap the point to be tested
	 * @param convexHull the convex hull
	 * @return true, if point is located inside the convex hull, false otherwise
	 */
	protected boolean liesPointInConvexHull(PointRoomMap pRoomMap, ArrayList<PointProbabilityMap> convexHull) {
		int size = convexHull.size();
		for (int i = 0; i < size; i++) {
			Point pStart = convexHull.get(i % size);
			Point pEnd = convexHull.get((i + 1) % size);
			
			if (Line2D.relativeCCW(pStart.x, pStart.y, pEnd.x, pEnd.y, pRoomMap.x, pRoomMap.y) == 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Abstract method weight. This method can be used to weight the room map.
	 *
	 * @param roommap the room map that should be weighted
	 * @param receiver the current receiver for which this weighting should be done 
	 * @param convexHull the convex hull for the current receiver
	 */
	public abstract void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull);	
}
