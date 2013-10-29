

import java.util.ArrayList;

import HelperClass_Math.Point;

import sun.org.mozilla.javascript.internal.ast.WithStatement;


//test comment2
public class PositionLocalization {
	
	private int n = 3;
	private int A = 40;

	public static void main(String[] args) {
		
		PositionLocalization posLoc = new PositionLocalization();
		ArrayList<Point_ProbabilityMap> pList = posLoc.createMap(50, 50, 1);
		
		Point_ProbabilityMap[] p = posLoc.findValuesBelowRssi(pList, -60);
		

		GrahamScan c = new GrahamScan();
		int h = c.computeHull(p);		
	}
	
	// This method creates a map of the room
	private ArrayList<Point_RoomMap> createRoomMap (double width, double height, double granularity) {
		
		double n_width = width / granularity;
		double n_height = height / granularity;
		
		ArrayList<Point_RoomMap> roomMap = new ArrayList<Point_RoomMap>();
		
//		// 1. Quadrant
//		double n_widthHalf = n_width/2;
//		double n_heighthHalf = n_height/2;
		
		for(double i = 0; i <= width; i+=granularity) { // x-Axe
			for(double j = 0; j <= height; j+=granularity) { // y-Axe
				
				roomMap.add(new Point_RoomMap(i, j));
			}
		}
		
		return roomMap;
	}
	
	
	// creates a map with the emprical formula
	private ArrayList<Point_ProbabilityMap> createMap(double width, double height, double granularity) {
		double n_width = width / granularity;
		double n_height = height / granularity;
		
		ArrayList<Point_ProbabilityMap> pMap = new ArrayList<Point_ProbabilityMap>();
		
		// 1. Quadrant
		double n_widthHalf = n_width/2;
		double n_heighthHalf = n_height/2;
		
		for(double i = -n_widthHalf; i <= n_widthHalf; i+=granularity) { // x-Achse
			for(double j = -n_heighthHalf; j <= n_heighthHalf; j+=granularity) { // y-Achse
				
				double distance = 0;
				if(i == 0 && j == 0) {
					distance = granularity;
				} else {
					distance = Math.sqrt(i * i + j * j);
				}
				pMap.add(new Point_ProbabilityMap(i, j, distanceToRSSI(distance)));
				
			}
		}
		
		return pMap;
	}
	
	private double distanceToRSSI(double distance) {
		double rssi = -(10 * this.n * Math.log10(distance) + this.A);
		return rssi;
	}
	
	private Point_ProbabilityMap[] findValuesBelowRssi(ArrayList<Point_ProbabilityMap> probMap, double rssi) {
		ArrayList<Point_ProbabilityMap> probabilityMap = new ArrayList<Point_ProbabilityMap>();
		
		for(int i = 0; i < probMap.size(); i++) {
			if(probMap.get(i).rssi_value >= rssi) {
				probabilityMap.add(probMap.get(i));
			}
		}
		
		
		Point_ProbabilityMap[] pList = new Point_ProbabilityMap[probabilityMap.size()];
		probabilityMap.toArray(pList);
		
		return pList;
	}
	
	//This method return the points with the highest weight
	private ArrayList<Point_RoomMap> giveMaxWeightedValue (ArrayList<Point_RoomMap> roomMap) {
		
		ArrayList<Point_RoomMap> maxWeightedValue = new ArrayList<Point_RoomMap>();
		double maxValue = 0;
		
		
		for (int i = 0; i < roomMap.size(); i++) {
			if (roomMap.get(i).getWeightValue() >= maxValue) {
				maxValue = roomMap.get(i).getWeightValue();
				maxWeightedValue.add(roomMap.get(i));
			}
			else
				maxWeightedValue.clear();
				maxWeightedValue.add(roomMap.get(i));
		}
		return maxWeightedValue;
	}
	
	// This method calculates the average point of the suitable points
	private Point getPosition (ArrayList<Point_RoomMap> roomMap) {
		
		double sumX = 0, sumY = 0;
		for (int i = 0; i < roomMap.size(); i++) {
			sumX = roomMap.get(i).x;
			sumY = roomMap.get(i).y;
		}
		
		double x = sumX/roomMap.size();
		double y = sumY/roomMap.size();
		
		return new Point(x,y);
	}
		
}
