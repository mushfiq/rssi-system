package algorithm.weightFunction;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;
import components.Receiver;
import components.RoomMap;

public class WeightFunctionExtended extends WeightFunction {

	private double percentageSteps;
	
	public WeightFunctionExtended(double percentageSteps) {
		this.percentageSteps = percentageSteps;
	}

	@Override
	public void weight(RoomMap roommap, Receiver receiver, ArrayList<PointProbabilityMap> convexHull) {
		ArrayList<PointRoomMap> pointsRoomMap = roommap.getRoomMapPoints();
		
		for (int i = 0; i < pointsRoomMap.size(); i++) {
			
			double distanceConvexhullAndPoint = calcDistanceBtwConvexhullAndPoint(convexHull, pointsRoomMap.get(i));
			double distanceReceiverAndPoint = calcDistanceBtwReceiverAndPoint(receiver, pointsRoomMap.get(i));
			
			double res = -1.0;
			if (liesPointInConvexHull(pointsRoomMap.get(i), convexHull)) {
				res = 1.0 - (distanceReceiverAndPoint / (distanceReceiverAndPoint + distanceConvexhullAndPoint));
			} else {
				res = 1.0 - ((distanceReceiverAndPoint - distanceConvexhullAndPoint) / distanceReceiverAndPoint);
			}
			
			for(double j = this.percentageSteps; j <= 1.0; j += this.percentageSteps) {
				if(res < j) {
					pointsRoomMap.get(i).setNewWeightValue(pointsRoomMap.get(i).getWeightValue() * (2.0 - j));
					break;
				}
			}
		}
	}
	
	private double calcDistanceBtwConvexhullAndPoint(ArrayList<PointProbabilityMap> convexHull, Point p) {
		Point pStart = convexHull.get(0);
		Point pEnd = convexHull.get(1);
		double distance = Line2D.ptSegDist(pStart.x, pStart.y, pEnd.x, pEnd.y, p.x, p.y);
		
		int size = convexHull.size();
		for (int i = 0; i < size; i++) {
			pStart = convexHull.get(i % size);
			pEnd = convexHull.get((i + 1) % size);
			
			double distanceTemp = Line2D.ptSegDist(pStart.x, pStart.y, pEnd.x, pEnd.y, p.x, p.y);
			if(distanceTemp < distance) {
				distance = distanceTemp;
			}
		}
		return distance;
	}
	
	private double calcDistanceBtwReceiverAndPoint(Receiver receiver, Point p) {
		Point vectorReceiverPoint = new Point(p.x - receiver.getXPos(), p.y - receiver.getYPos());
		return Math.sqrt(vectorReceiverPoint.x * vectorReceiverPoint.x + vectorReceiverPoint.y * vectorReceiverPoint.y);
	}
}
