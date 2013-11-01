package utilities;

import java.util.ArrayList;

import data.Reading;

public class Utilities {

	private static final int RSSI_OFFSET = 77;
	
	public Utilities() {
		// TODO Auto-generated constructor stub
	}

	public static int convertRSSIDecToDbm(int rssiDecimalValue){
		
		int rssiDbm = 0;
		
		if(rssiDecimalValue >= 128){
			rssiDbm = (rssiDecimalValue - 256)/2 - RSSI_OFFSET;
		}
		else{
			rssiDbm = (rssiDecimalValue)/2 - RSSI_OFFSET;
		}
		
		return rssiDbm;
	}
	
	public static Reading removeInappropriateValues(Reading reading){
		
		Reading newReading = null;
		
		return newReading;
	}
	
	public static int calculateReadingAverage(Reading reading){
		int result = 0;
		
		if(reading == null){
			return 0;
		}
		
		ArrayList<Integer> signalStrengths = reading.getSignalStrengths();
		
		for(int i=0; i<signalStrengths.size(); i++){
			result += signalStrengths.get(i);
		}
		
		result = result/signalStrengths.size();
		
		return result;
	}
	
	
}
