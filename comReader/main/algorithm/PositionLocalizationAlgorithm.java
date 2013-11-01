package algorithm;
import java.util.ArrayList;
import java.util.HashMap;

import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;

public abstract class PositionLocalizationAlgorithm {

        public ArrayList<Receiver> receivers;
        public RoomMap roommap;
        
        public PositionLocalizationAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
                this.roommap = roommap;
                this.receivers = receivers;
        }
        
        public abstract int calculate(HashMap<Integer, Double> readings);
}
