package tests;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.PositionLocalizationAlgorithm;
import algorithm.ProbabilityBasedAlgorithm;
import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;



public class _Test {
        
        public static void main(String[] args) {
                
                Receiver r1 = new Receiver(1, 0.0, 0.0, 45.0);
                Receiver r2 = new Receiver(2, 25.0, 0.0, 135.0);
                Receiver r3 = new Receiver(3, 25.0, 25.0, 225.0);
                Receiver r4 = new Receiver(4, 0.0, 25.0, 315.0);
                
                ArrayList<Receiver> receivers = new ArrayList<Receiver>();
                receivers.add(r1);
                receivers.add(r2);
                receivers.add(r3);
                receivers.add(r4);

                RoomMap roommap = new RoomMap(25, 25);
                
                PositionLocalizationAlgorithm algo = new ProbabilityBasedAlgorithm(roommap, receivers);
                
                HashMap<Integer, Double> readings = new HashMap<Integer, Double>();
                readings.put(1, -81.41); //ca. 24m radius
                readings.put(2, -79.03); //ca. 20m radius
                readings.put(3, -74.38); //ca. 14m radius
                readings.put(4, -79.03); //ca. 20m radius
                System.out.println("Calculation started");
                long startTime = System.currentTimeMillis();
                algo.calculate(readings);
                long endTime = System.currentTimeMillis();
                System.out.println("Time taken: " + (endTime - startTime) + " ms");
        }
}
