package utilities;

import java.util.ArrayList;

import data.Reading;


/**
 * The Class Utilities.
 */
public final class Utilities {

	/** The Constant RSSI_OFFSET. */
	private static final int RSSI_OFFSET = 77;
	
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
	public static int convertRSSIDecToDbm(int rssiDecimalValue) {
		
		int rssiDbm = 0;
		
		if (rssiDecimalValue >= 128) {
			rssiDbm = (rssiDecimalValue - 256) / 2 - RSSI_OFFSET;
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
	public static int calculateReadingAverage(Reading reading) {
		int result = 0;
		
		if (reading == null) {
			return 0;
		}
		
		ArrayList<Integer> signalStrengths = reading.getSignalStrengths();
		
		for (int i = 0; i < signalStrengths.size(); i++) {
			result += signalStrengths.get(i);
		}
		
		result = result / signalStrengths.size();
		
		return result;
	}
	
	
}
