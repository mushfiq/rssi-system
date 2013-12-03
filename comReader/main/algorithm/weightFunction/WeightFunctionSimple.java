package algorithm.weightFunction;

import java.util.ArrayList;

import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import components.Receiver;
import components.RoomMap;

public class WeightFunctionSimple extends WeightFunction {

	/** Factor that is used to determine the weighted points of a room map. */
	public static final double FACTOR_FOR_WEIGHTING_ROOMMAP = 2.0;
	
	public WeightFunctionSimple() {
	}

	@Override
	public void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		ArrayList<PointRoomMap> pointsRoomMap = roommap.getRoomMapPoints();
		
		for (int i = 0; i < pointsRoomMap.size(); i++) {
			if (liesPointInConvexHull(pointsRoomMap.get(i), convexHull)) {
				pointsRoomMap.get(i).setNewWeightValue(pointsRoomMap.get(i).getWeightValue() * WeightFunctionSimple.FACTOR_FOR_WEIGHTING_ROOMMAP);
			}
		}
	}

}
