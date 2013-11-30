package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import data.Reading;

/**
 * Class with helper methods for various tasks.
 */
public final class Utilities {

	/** The Constant RSSI_OFFSET. */
	private static final int RSSI_OFFSET = 77;
	/** Parameters used in formula for converting the RSSIdec value to RSSIdBm. */
	private static final int POSITIVE_NUMBER_LIMIT = 128;
	
	/** The Constant SUBTRAHEND. */
	private static final int SUBTRAHEND = 256;
	
	/** The Constant RADIX. */
	private static final int RADIX = 16;
	
	private static final int SIZE_OF_LOG_FILE = 8096;
	
	private static final int NUMBER_OF_FILES_TO_WRITE_TO = 1;
	
	private static final String PATH_TO_LOG_FILE = "comReader" + File.separator + "main" + File.separator + "resources" + File.separator + "log.log";
	
	private static FileHandler fileHandler;
	
	/** The logger. */
	private static Logger logger;
	
	/**
	 *  All helper methods are static so there is no need for
	 *  instantiation of this class. Therefore, the constructor is private. 
	 */
	private Utilities() {
		
	}
	
	/**
	 * Convert RSSI decimal value to RSSI dBm.
	 *
	 * @param rssiDecimalValue rssi decimal value
	 * @return int RSSI dBm value
	 */
	public static double convertRSSIDecToDbm(double rssiDecimalValue) {
		
		double rssiDbm = 0;
		
		if (rssiDecimalValue >= POSITIVE_NUMBER_LIMIT) {
			rssiDbm = (rssiDecimalValue - SUBTRAHEND) / 2 - RSSI_OFFSET;
		} else {
			rssiDbm = (rssiDecimalValue) / 2 - RSSI_OFFSET;
		}
		
		return rssiDbm;
	}
	
	/**
	 * Some signal strengths in the reading can be too far from the median value.
	 * These values should not be considered for averaging. For example, in the list: 
	 * 32, 33, 25, 32 - value '25' is inappropriate
	 * 
	 * @param reading the reading
	 * @return the reading
	 */
	public static Reading removeInappropriateValues(Reading reading) {
		
		// TODO: remove statistically inappropriate values from the reading
		
		return reading;
	}
	
	/**
	 * Calculates average signal strength of a single reading. Every receiver
	 * takes several samples of signal strength. These values need to be
	 * averaged.
	 * 
	 * For the reading 32, 32 , 33, 32, result will be 32,25
	 *
	 * @param reading the reading
	 * @return double average signal strength
	 */
	public static double  calculateReadingAverage(Reading reading) {
		
		if (reading == null) {
			return 0;
		}
		
		double result = 0;
		
		ArrayList<Double> signalStrengths = reading.getSignalStrengths();
		
		for (int i = 0; i < signalStrengths.size(); i++) {
			result += signalStrengths.get(i);
		}
		
		result = result / signalStrengths.size();
		
		return result;
	}
	
	
	/**
	 * Calculates batch signal averages. When reading from the COM port, during a single time interval
	 * (e.g. 250ms), for every receiver, several signal strengths are obtained. These signal strengths are
	 * averaged before being passed to the position localization algorithm (DataProcessor class). 
	 *
	 * @param batch list of signal strengths for several watches and receivers
	 * @return Hash map with average signal strength for every watch and every receiver
	 */
	public static HashMap<Integer, HashMap<Integer, Double>> calculateBatchSignalAverages(ArrayList<Reading> batch) {
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> allData = new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>();
		int watchId = 0;
		int receiverId = 0;
		double averageStrengthValue = 0;
		ArrayList<Integer> watchIds = new ArrayList<Integer>(); 
		if(batch == null) {
			System.out.println("it is null");
		}
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
		
		// calculate averages
		HashMap<Integer, HashMap<Integer, Double>> averagedAllData = new HashMap<Integer, HashMap<Integer, Double>>();
		
		int watchIdsSize = watchIds.size();
		for (int i = 0; i < watchIdsSize; i++) {
			
			HashMap<Integer, ArrayList<Double>> hashMap = allData.get(i);
			
			for (Map.Entry<Integer, ArrayList<Double>> entry : hashMap.entrySet()) {
				int receiverId2 = entry.getKey();
			    
			    if (averagedAllData.get(i) == null) {
			    	averagedAllData.put(i, new HashMap<Integer, Double>());
			    }
			    
			    averagedAllData.get(i).put(receiverId2, calculateArrayListAverage(entry.getValue()));
			}
		}
		
		return averagedAllData;
	}
	
	/**
	 * Initialize logger.
	 */
	public static Logger initializeLogger(String className) {
	
		Logger logger = Logger.getLogger(className);
		logger.setUseParentHandlers(false);
		fileHandler = Utilities.getFileHandler();  
		
        // This block configures the logger with handler and formatter      	  
        SimpleFormatter formatter = new SimpleFormatter(); 
        fileHandler.setFormatter(formatter); 
        logger.addHandler(fileHandler);

	    return logger;
	}
	
	
	/**
	 * Helper method that calculates average value of signal strengths in the list.
	 *
	 * @param list list with signal strengths
	 * @return average value
	 */
	private static Double calculateArrayListAverage(ArrayList<Double> list) {
		
		double result = 0;
		
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
		}
		
		result = result / list.size();
		
		return result;
	}
	
	/**
	 * Creates the reading.
	 *
	 * @param line the line
	 * @return the reading
	 */
	public static Reading createReading(String line) {
		
		double signalStrength1 = 0;
		double signalStrength2 = 0;
		double signalStrength3 = 0;
		double signalStrength4 = 0;
		Reading reading = new Reading();
		
		try {
		
		StringTokenizer tokenizer = new StringTokenizer(line);
			
			//REP
			tokenizer.nextToken();
			//2
			tokenizer.nextToken();
			//3
			tokenizer.nextToken();
			//4
			tokenizer.nextToken();
			//5
			tokenizer.nextToken();
			//6
			tokenizer.nextToken();
			//7
			tokenizer.nextToken();
			//8
			int receiverId = Integer.parseInt(tokenizer.nextToken());
			//9
			tokenizer.nextToken();
			//10
			tokenizer.nextToken();
			//11
			tokenizer.nextToken();
			//12
			tokenizer.nextToken();
			//13
			tokenizer.nextToken();
			//14
			signalStrength1 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength1 = Utilities.convertRSSIDecToDbm(signalStrength1);
			//15
			signalStrength2 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength2 = Utilities.convertRSSIDecToDbm(signalStrength2);
			//16
			signalStrength3 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength3 = Utilities.convertRSSIDecToDbm(signalStrength3);
			//17
			signalStrength4 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength4 = Utilities.convertRSSIDecToDbm(signalStrength4);
			//18
			tokenizer.nextToken();
			//19
			tokenizer.nextToken();
			
			ArrayList<Double> signalStrengths = new ArrayList<Double>();
			signalStrengths.add(signalStrength1);
			signalStrengths.add(signalStrength2);
			signalStrengths.add(signalStrength3);
			signalStrengths.add(signalStrength4);
			
			reading = new Reading(receiverId, 0, signalStrengths);
			
		} catch (NumberFormatException exception) {
			getLogger().warning("Parsing data from COM port failed. Empty reading will be returned.");
		}
			
		return reading;
	}
	
	private static FileHandler getFileHandler() {
		
		if (fileHandler == null) {
			try {
				fileHandler = new FileHandler(
						PATH_TO_LOG_FILE, 
						SIZE_OF_LOG_FILE, 
						NUMBER_OF_FILES_TO_WRITE_TO, 
						true);
			} catch (SecurityException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fileHandler;
		
	}
	
	private static Logger getLogger () {
		
		if (logger == null) {
			logger = Utilities.initializeLogger(Utilities.class.getName());
		}
		
		return logger;
	}
}
