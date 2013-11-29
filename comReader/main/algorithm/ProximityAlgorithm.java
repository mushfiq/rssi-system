package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import algorithm.helper.Point;

import components.Receiver;
import components.RoomMap;

public class ProximityAlgorithm extends PositionLocalizationAlgorithm {
	
	private int id;

	public ProximityAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);
		
	}

	@Override
	public Point calculate(HashMap<Integer, Double> readings) {
		
		return giveStrongestSignal(readings);
		
	}
	
	private Point giveStrongestSignal (HashMap<Integer, Double> readings) {
		double strongestSignalValue = 0;
		
		for (Map.Entry<Integer, Double> e : readings.entrySet()) {
			if (e.getValue() >= strongestSignalValue) {
				strongestSignalValue = e.getValue();
				id = e.getKey();
			}	
		}
		Receiver receiverStrongestSignal = getReceiver(receivers, id);
		
		if(receiverStrongestSignal != null){
			Point strongestSignal = new Point(receiverStrongestSignal.getXPos(), receiverStrongestSignal.getYPos());
			System.out.println(strongestSignal.toString());
			return strongestSignal;
		}
		else
			return null;
	}
	
	private Receiver getReceiver(ArrayList<Receiver> receivers, int id) {
		for (int i = 0; i < receivers.size(); i++) {
			if (receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}
	
	

}
