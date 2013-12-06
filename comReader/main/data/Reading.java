package data;

import java.util.ArrayList;
import java.util.logging.Logger;

import utilities.Utilities;


/**
 * Reading represents a single set of data obtained from one line from COM port, i.e. it
 * contains information obtained from one receiver. This data includes: watchId, receiverId and
 * four signal strengths.
 */
public class Reading {

	/** The logger. */
	private Logger logger;
	
	/** The receiver id. */
	private int receiverId;
	
	/** The watch id. */
	private int watchId;
	
	/** The signal strengths. */
	private ArrayList<Double> signalStrengths;
	
	/** The average strength value. */
	private double averageStrengthValue;
	
	/** The rssi dbm. */
	private double rssiDbm = 0;
	
	private boolean empty;
	
	/**
	 * Instantiates a new reading.
	 */
	public Reading() {
		
		logger = Utilities.initializeLogger(this.getClass().getName());
		signalStrengths = new ArrayList<Double>();
		this.empty = true;
	}

	/**
	 * Gets the receiver id.
	 *
	 * @return the receiver id
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * Sets the receiver id.
	 *
	 * @param receiverId the new receiver id
	 */
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * Gets the watch id.
	 *
	 * @return the watch id
	 */
	public int getWatchId() {
		return watchId;
	}

	/**
	 * Sets the watch id.
	 *
	 * @param watchId the new watch id
	 */
	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	/**
	 * Gets the signal strengths.
	 *
	 * @return the signal strengths
	 */
	public ArrayList<Double> getSignalStrengths() {
		return signalStrengths;
	}

	/**
	 * Sets the signal strengths.
	 *
	 * @param signalStrengths the new signal strengths
	 */
	public void setSignalStrengths(ArrayList<Double> signalStrengths) {
		this.signalStrengths = signalStrengths;
	}

	/**
	 * Instantiates a new reading.
	 *
	 * @param receiverId the receiver id
	 * @param watchId the watch id
	 * @param signalStrengths the signal strengths
	 */
	public Reading(int receiverId, int watchId,
			ArrayList<Double> signalStrengths) {
		super();
		this.receiverId = receiverId;
		this.watchId = watchId;
		this.signalStrengths = signalStrengths;
		
		double average = Utilities.calculateReadingAverage(this);
		this.setAverageStrengthValue(average);
		this.setRssiDbm(average);
		this.setEmpty(false);
	}
	
	/**
	 * Adds the signal strength.
	 *
	 * @param signalStrength the signal strength
	 */
	public void addSignalStrength(Double signalStrength) {
		
		this.signalStrengths.add(signalStrength);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String string = "Watch id: " + watchId + ", receiver id: " + receiverId + ", readings: ";
		StringBuilder builder = new StringBuilder(string);
		for (double signalStrength : signalStrengths) {
			builder.append(signalStrength + " ");
		}
		
		return builder.toString();
	}

	/**
	 * Gets the average strength value.
	 *
	 * @return the average strength value
	 */
	public double getAverageStrengthValue() {
		return averageStrengthValue;
	}

	/**
	 * Sets the average strength value.
	 *
	 * @param averageStrengthValue the new average strength value
	 */
	public void setAverageStrengthValue(double averageStrengthValue) {
		this.averageStrengthValue = averageStrengthValue;
	}

	/**
	 * Gets the rssi dbm.
	 *
	 * @return the rssi dbm
	 */
	public double getRssiDbm() {
		return rssiDbm;
	}

	/**
	 * Sets the rssi dbm.
	 *
	 * @param rssiDbm the new rssi dbm
	 */
	public void setRssiDbm(double rssiDbm) {
		this.rssiDbm = rssiDbm;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean value) {
		this.empty = value;
	}
}
