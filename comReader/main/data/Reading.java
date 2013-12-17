/*
 * 
 * 
 */
package data;

import java.util.ArrayList;
import java.util.logging.Logger;

import utilities.Utilities;

/**
 * Reading represents a single set of data obtained from one line from COM port, i.e. it contains information obtained
 * from one receiver. This data includes: watchId, receiverId and four signal strengths.
 * 
 * @author Danilo
 */
public class Reading {

	/** <code>Logger</code> object. */
	@SuppressWarnings("unused")
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

	/** The empty. */
	private boolean empty;

	/**
	 * Instantiates a new <code>Reading</code>.
	 */
	public Reading() {
		logger = Utilities.initializeLogger(this.getClass().getName());
		signalStrengths = new ArrayList<Double>();
		this.empty = true;
		signalStrengths = new ArrayList<Double>();
	}

	/**
	 * Gets the receiver id.
	 * 
	 * @return int receiver id
	 */
	public int getReceiverId() {
		return receiverId;
	}

	/**
	 * Sets the receiver id.
	 * 
	 * @param receiverId
	 *            int id
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
	 * @param watchId
	 *            the new watch id
	 */
	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	/**
	 * Gets the signal strengths.
	 * 
	 * @return <code>List</code> of signal strengths
	 */
	public ArrayList<Double> getSignalStrengths() {
		return signalStrengths;
	}

	/**
	 * Sets the signal strengths.
	 * 
	 * @param signalStrengths
	 *            <code>List</code> of new signal strengths
	 */
	public void setSignalStrengths(ArrayList<Double> signalStrengths) {
		this.signalStrengths = signalStrengths;
	}

	/**
	 * Instantiates a new <code>Reading</code>.
	 * 
	 * @param receiverId
	 *            int receiver id
	 * @param watchId
	 *            int watch id
	 * @param signalStrengths
	 *            <code>List</code> of signal strengths
	 */
	public Reading(int receiverId, int watchId, ArrayList<Double> signalStrengths) {
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
	 * @param signalStrength
	 *            double signal strength
	 */
	public void addSignalStrength(Double signalStrength) {
		this.signalStrengths.add(signalStrength);
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * @return double average strength value
	 */
	public double getAverageStrengthValue() {
		return averageStrengthValue;
	}

	/**
	 * Sets the average strength value.
	 * 
	 * @param averageStrengthValue
	 *            double new average strength value
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
	 * @param rssiDbm
	 *            the new rssi dbm
	 */
	public void setRssiDbm(double rssiDbm) {
		this.rssiDbm = rssiDbm;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Sets the empty.
	 *
	 * @param value the new empty
	 */
	public void setEmpty(boolean value) {
		this.empty = value;
	}

}
