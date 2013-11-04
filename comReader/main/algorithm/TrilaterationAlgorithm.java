package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.helper.Point;
import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;


public class TrilaterationAlgorithm extends
                PositionLocalizationAlgorithm {

        public TrilaterationAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
                super(roommap, receivers);
                
        }

        @Override
        public Point calculate(HashMap<Integer, Double> readings) {
                return null;
        }

}