package algorithm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import algorithm.helper.GrahamScan;
import algorithm.helper.Line;
import algorithm.helper.Point;
import algorithm.helper.Point_ProbabilityMap;
import algorithm.helper.Point_RoomMap;
import algorithm.probabilityMap.ProbabilityMap_Empiric;
import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;


public class ProbabilityBasedAlgorithm extends PositionLocalizationAlgorithm {

        private ArrayList<Point_RoomMap> points_roomMap;
        private ArrayList<Receiver> receivers;
        private ArrayList<Point_ProbabilityMap> points_probabilityMap;
        
        private RoomMap roommap;
        
        public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
                super(roommap, receivers);
                
                points_probabilityMap = new ProbabilityMap_Empiric(3, 40).getProbabilityMap(50, 50, 0.2);
                
                this.receivers = receivers;
                this.roommap = roommap;
        }
        
        @Override
        public int calculate(HashMap<Integer, Double> readings) {
                this.points_roomMap = createRoomMap(this.roommap.getLength(), this.roommap.getWidth(), 1);
                
                // go through each receiver and calculate a weighted map
                for(Map.Entry<Integer, Double> e : readings.entrySet()) {
                        
                        // find the points in the probability map where the rssi value is below the given value
                        ArrayList<Point_ProbabilityMap> new_points_probabilityMap = 
                                        findValuesBelowRssi(points_probabilityMap, e.getValue());

                        // calculate the convex hull of the probability map with the rssi values
                        GrahamScan c2 = new GrahamScan();
                        ArrayList<Point_ProbabilityMap> convexHull = 
                                        c2.computeHull(new_points_probabilityMap);
                        
                        // transform the convex hull
                        ArrayList<Point_ProbabilityMap> convexHullTransformed;
                        Receiver receiver = getReceiver(receivers, e.getKey());
                        if(receiver != null) {
                                convexHullTransformed = transformation(convexHull, receiver.getAngle(), receiver.getXPos(), receiver.getYPos());
                        } else {
                                return -1;
                        }
                        
                        // Test each point from room map if it lies in the transformed convex hull
                        // and weight each point
                        weightRoomMap(this.points_roomMap, convexHullTransformed);
                }
                // Find points with the highest weighted value
                ArrayList<Point_RoomMap> highestPoints_RoomMap = giveMaxWeightedValue(this.points_roomMap);
                                        
                // calculate point
                Point p = getPosition(highestPoints_RoomMap);
                System.out.println(p);
                
                return 0;
        }
        
        private ArrayList<Point_ProbabilityMap> findValuesBelowRssi(ArrayList<Point_ProbabilityMap> probabilityMap, double rssi) {
                ArrayList<Point_ProbabilityMap> pMap = new ArrayList<Point_ProbabilityMap>();
                
                for(int i = 0; i < probabilityMap.size(); i++) {
                        if(probabilityMap.get(i).rssi_value >= rssi) {
                                pMap.add(probabilityMap.get(i));
                        }
                }
                return pMap;
        }
        
        // This method creates a map of the room
        private ArrayList<Point_RoomMap> createRoomMap(double length, double width, double granularity) {
                double n_length = length / granularity;
                double n_width = width / granularity;
                
                ArrayList<Point_RoomMap> roomMap = new ArrayList<Point_RoomMap>();
                
                for(double i = 0; i <= n_length; i+=granularity) { // x-Axe
                        for(double j = 0; j <= n_width; j+=granularity) { // y-Axe                
                                roomMap.add(new Point_RoomMap(i, j));
                        }
                }
                
                return roomMap;
        }
                
        private ArrayList<Point_ProbabilityMap> transformation (ArrayList<Point_ProbabilityMap> p, double angle, double receiverXpos, double receiverYpos ) {
                
                //Define a new List with the transformated coordindates of the Probability Map
                ArrayList<Point_ProbabilityMap> transformation_probMap = new ArrayList<Point_ProbabilityMap>();
                
                //Rotation the coordination of the Probability Map
                for (int i = 0; i < p.size(); i++) {
                        
                        double xRotation = Math.cos(angle) * p.get(i).x - Math.sin(angle) * p.get(i).y;
                        double yRotation = Math.sin(angle) * p.get(i).x + Math.cos(angle) * p.get(i).y;
                        transformation_probMap.add(new Point_ProbabilityMap(xRotation,yRotation,p.get(i).rssi_value));
                        
                }
                
                //Translation the coordination of the Probability Map
                for (int i = 0; i < p.size(); i++) {
                        transformation_probMap.get(i).x = transformation_probMap.get(i).x + receiverXpos; 
                        transformation_probMap.get(i).y = transformation_probMap.get(i).y + receiverYpos;
                }
                return transformation_probMap;
        }
        
        private void weightRoomMap(ArrayList<Point_RoomMap> roomMap, ArrayList<Point_ProbabilityMap> convexHull) {
                for(int i = 0; i < roomMap.size(); i++) {
                        if(liesPointInConvexHull(roomMap.get(i), convexHull)) {
                                roomMap.get(i).setNewWeightValue(roomMap.get(i).getWeightValue() * 5.0);
                        }
                }
        }
        private boolean liesPointInConvexHull(Point_RoomMap pRoomMap, ArrayList<Point_ProbabilityMap> convexHull) {
                // go along convex hull
                int size = convexHull.size();
                for(int i = 0; i < size; i++) {
                        Point p1 = convexHull.get(i%size);
                        Point p2 = convexHull.get((i+1)%size);
                        Line l = new Line(p1, p2);
                        
                        if(pRoomMap.isRightOf(l)) {
                                return false;
                        }
                }                
                return true;
        }
        
        //This method return the points with the highest weight
        private ArrayList<Point_RoomMap> giveMaxWeightedValue(ArrayList<Point_RoomMap> roomMap) {
                
                ArrayList<Point_RoomMap> maxWeightedValue = new ArrayList<Point_RoomMap>();
                double maxValue = -1000.0;
                
                for (int i = 0; i < roomMap.size(); i++) {
                        if(roomMap.get(i).getWeightValue() > maxValue) {
                                maxValue = roomMap.get(i).getWeightValue();
                                maxWeightedValue.clear();
                                maxWeightedValue.add(roomMap.get(i));
                                
                        } else if(roomMap.get(i).getWeightValue() == maxValue) {
                                maxWeightedValue.add(roomMap.get(i));
                        }                
                }
                return maxWeightedValue;
        }
        
        // This method calculates the average point of the suitable points
        private Point getPosition(ArrayList<Point_RoomMap> roomMap) {
                
                double sumX = 0, sumY = 0;
                for (int i = 0; i < roomMap.size(); i++) {
                        sumX += roomMap.get(i).x;
                        sumY += roomMap.get(i).y;
                }
                
                double x = sumX/roomMap.size();
                double y = sumY/roomMap.size();
                
                return new Point(x,y);
        }
        
        private Receiver getReceiver(ArrayList<Receiver> receivers, int id) {
                for(int i = 0; i < receivers.size(); i++) {
                        if(receivers.get(i).getID() == id) {
                                return receivers.get(i);
                        }
                }
                return null;
        }
}