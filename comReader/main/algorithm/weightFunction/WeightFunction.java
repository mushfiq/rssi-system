package algorithm.weightFunction;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import components.Receiver;
import components.RoomMap;


public abstract class WeightFunction {
	
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

	public abstract void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull);	
}
