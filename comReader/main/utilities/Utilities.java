package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Reading;


/**
 * The Class Utilities.
 */
public final class Utilities {

	/** The Constant RSSI_OFFSET. */
	private static final int RSSI_OFFSET = 77;
	private static final int POSITIVE_NUMBER_LIMIT = 128;
	private static final int SUBTRAHEND = 256;
	
	/**
	 * Instantiates a new utilities.
	 */
	private Utilities() {
		
	}
	
	/**
	 * Convert rssi dec to dbm.
	 *
	 * @param rssiDecimalValue the rssi decimal value
	 * @return the int
	 */
	public static double convertRSSIDecToDbm(double rssiDecimalValue) {
		
		double rssiDbm = 0;
		
		if (rssiDecimalValue >= POSITIVE_NUMBER_LIMIT) {
			rssiDbm = (rssiDecimalValue - SUBTRAHEND) / 2 - RSSI_OFFSET;
		}
		else {
			rssiDbm = (rssiDecimalValue) / 2 - RSSI_OFFSET;
		}
		
		return rssiDbm;
	}
	
	/**
	 * Removes the inappropriate values.
	 *
	 * @param reading the reading
	 * @return the reading
	 */
	public static Reading removeInappropriateValues(Reading reading) {
		
		Reading newReading = null;
		
		return newReading;
	}
	
	/**
	 * Calculate reading average.
	 *
	 * @param reading the reading
	 * @return the int
	 */
	public static double  calculateReadingAverage(Reading reading) {
		double result = 0;
		
		if (reading == null) {
			return 0;
		}
		
		ArrayList<Double> signalStrengths = reading.getSignalStrengths();
		
		for (int i = 0; i < signalStrengths.size(); i++) {
			result += signalStrengths.get(i);
		}
		
		result = result / signalStrengths.size();
		
		return result;
	}
	
	public static HashMap<Integer, HashMap<Integer, Double>> calculateBatchSignalAverages(ArrayList<Reading> batch) {
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> allData = new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>();
		int watchId = 0;
		int receiverId = 0;
		double averageStrengthValue = 0;
		ArrayList<Integer> watchIds = new ArrayList<Integer>(); 
		
		// populate the three-dimensional HashMap with data 
		for (Reading reading : batch) {
			
			watchId = reading.getWatchId();
			receiverId = reading.getReceiverId();
			averageStrengthValue = reading.getAverageStrengthValue();
			
			// add watchId to the list for later use
			if (!watchIds.contains(watchId)) {
				watchIds.add(watchId);
			}
			
			if (allData.get(watchId) == null) {
				allData.put(watchId, new HashMap<Integer, ArrayList<Double>>());
			}
			
			if (allData.get(watchId).get(receiverId) == null) {
				
				allData.get(watchId).put(receiverId, new ArrayList<Double>());
			}
			
			allData.get(watchId).get(receiverId).add(averageStrengthValue);
			
		}
		
		//System.out.println("Number of receivers is: " + allData.get(0).size());
		HashMap<Integer, HashMap<Integer, Double>> averagedAllData = new HashMap<Integer, HashMap<Integer, Double>>();
		// calculate averages
		
		int watchIdsSize = watchIds.size();
		for (int i = 0; i < watchIdsSize; i++) {
			
			HashMap<Integer, ArrayList<Double>> hashMap = allData.get(i);
			
			for (Map.Entry<Integer, ArrayList<Double>> entry : hashMap.entrySet()) {
				int receiverId2 = entry.getKey();
			    //System.out.println(entry.getKey() + "/" + entry.getValue());
			    
			    if (averagedAllData.get(i) == null) {
			    	averagedAllData.put(i, new HashMap<Integer, Double>());
			    }
			    
			    averagedAllData.get(i).put(receiverId2, calculateArrayListAverage(entry.getValue()));
			    //System.out.println(calculateArrayListAverage(entry.getValue()));
			}
		}
		
		return averagedAllData;
	}
	
	
	private static Double calculateArrayListAverage(ArrayList<Double> list) {
		
		double result = 0;
		
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
		}
		
		result = result / list.size();
		
		return result;
	}
	
}
