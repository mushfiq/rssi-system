package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import algorithm.helper.Point;

import components.Receiver;
import components.RoomMap;
/**
 * The proximity algorithm aims to locate the receiver which the highest signal. 
 */
public class ProximityAlgorithm extends PositionLocalizationAlgorithm {
	/**
	 * id is the Receiver ID
	 */
	private int id;

	/**
	 * @param roommap defines the room map dimensions (is needed to create a list of weighted room map points)
	 * @param receivers a list of receivers that the algorithm should take into account
	 */
	public ProximityAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);

	}
	/**
	 * Return the position of a point depending of the given readings. The position belongs to the receiver with
	 * highest signal strength
	 * @param reading the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	@Override
	public Point calculate(HashMap<Integer, Double> readings) {

		return giveStrongestSignal(readings);

	}

	/**
	 * With this method it is possible to find out which receiver has the highest RSSI value. Each RSSI value from each receiver is 
	 * compared to each other. The highest value is stored in the parameter strongestSignalValue. 
	 * Also the belonging receiver ID is stored which is used for the method getReceiver 
	 * @param readings the rssi values measured in dBm for all receivers
	 * @return the calculated position in a room will be returned in the form of a point ({@link algorithm.helper.Point})
	 */
	private Point giveStrongestSignal (HashMap<Integer, Double> readings) {
		//get a value from the HashMap
		Map.Entry<Integer, Double> firstValue = readings.entrySet().iterator().next();
			//Check if the RSSI value is equal empty
			for(Map.Entry<Integer, Double> e : readings.entrySet())
				if (firstValue.getValue() == null)
					firstValue = e;
			
			if (firstValue.getValue() != null) {
				double strongestSignalValue = firstValue.getValue();
				
					for (Map.Entry<Integer, Double> e : readings.entrySet()) {
						if (e.getValue() > strongestSignalValue) {
							strongestSignalValue = e.getValue();
							id = e.getKey();
						}
					}
					Receiver receiverStrongestSignal = getReceiver(receivers, id);
			
					if (receiverStrongestSignal != null){
						Point strongestSignal = new Point(receiverStrongestSignal.getXPos(), receiverStrongestSignal.getYPos());
						System.out.println(strongestSignal.toString());
						return strongestSignal; 
					}
					else {
						return null;
					}
			}
			else {
				return null;
			}
	}

	
	


	/**
	 * Searches for the receiver with the given id in the receiver array list.
	 *
	 * @param receivers the list of receivers in that the search will be performed
	 * @param id the id of the possible receiver
	 * @return the receiver if found, null otherwise
	 */
	private Receiver getReceiver(ArrayList<Receiver> receivers, int id) {
		for (int i = 0; i < receivers.size(); i++) {
			if (receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}

}
