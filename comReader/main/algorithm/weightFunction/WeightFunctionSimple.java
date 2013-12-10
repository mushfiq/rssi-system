/*
 * File: WeightFunctionSimple.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 */
package algorithm.weightFunction;

import java.util.ArrayList;

import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import components.Receiver;
import components.RoomMap;

/**
 * The class WeightFunctionSimple. This class weights the room map in an easy way (see method weight(...)).
 *
 * @version 1.0 08 Dec 2013
 * @author Tommy Griese
 */
public class WeightFunctionSimple extends WeightFunction {

	/** Default factor that is used to determine the weighted points of a room map. */
	public static final double FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT = 2.0;
	
	/** Current applied factor for determining the weighted points of a room map. */
	private double factor;
	
	/**
	 * Instantiates a new WeightFunctionSimple with default 'weight factor'.
	 */
	public WeightFunctionSimple() {
		super();
		
		this.factor = WeightFunctionSimple.FACTOR_FOR_WEIGHTING_ROOMMAP_DEFAULT;
	}
	
	/**
	 * Instantiates a new WeightFunctionSimple with the given 'weight factor'.
	 * 
	 * @param weightFactor the weight factor to use for the weighting (see method weight)
	 */
	public WeightFunctionSimple(double weightFactor) {
		super();
		
		this.factor = weightFactor;
	}

	/**
	 * This is the weight method of the simple weight function class. This method covers the approach that
	 * all points that are located inside the convex hull will get a higher weight than the points that are not
	 * located inside the convex hull, e.g. the weight of a point that is located inside the convex hull will be 
	 * multiplied by a weight factor, the weight of the points not located inside the convex hull remain.
	 * 
	 * @param roommap the room map that should be weighted
	 * @param receiver the current receiver for which this weighting should be done 
	 * @param convexHull the convex hull for the current receiver
	 */
	@Override
	public void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		ArrayList<PointRoomMap> pointsRoomMap = roommap.getRoomMapPoints();
		
		for (int i = 0; i < pointsRoomMap.size(); i++) {
			if (liesPointInConvexHull(pointsRoomMap.get(i), convexHull)) {
				pointsRoomMap.get(i).setNewWeightValue(pointsRoomMap.get(i).getWeightValue() * this.factor);
			}
		}
	}

}