package data;

import java.util.ArrayList;

public class Reading {

	private int receiverId;
	private int watchId;
	private ArrayList<Integer> signalStrengths;
	private int averageStrengthValue;
	private int rssiDbm = 0;
	
	public Reading() {
		signalStrengths = new ArrayList<Integer>();
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getWatchId() {
		return watchId;
	}

	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	public ArrayList<Integer> getSignalStrengths() {
		return signalStrengths;
	}

	public void setSignalStrengths(ArrayList<Integer> signalStrengths) {
		this.signalStrengths = signalStrengths;
	}

	public Reading(int receiverId, int watchId,
			ArrayList<Integer> signalStrengths) {
		super();
		this.receiverId = receiverId;
		this.watchId = watchId;
		this.signalStrengths = signalStrengths;
	}
	
	public void addSignalStrength(int signalStrength){
		
		this.signalStrengths.add(signalStrength);
	}
	
	@Override
	public String toString() {
		
		String string = "Watch id: " + watchId + ", receiver id: " + receiverId + ", readings: ";
		StringBuilder builder = new StringBuilder(string);
		for (int signalStrength : signalStrengths) {
			builder.append(signalStrength + " ");
		}
		
		return builder.toString();
	}

	public int getAverageStrengthValue() {
		return averageStrengthValue;
	}

	public void setAverageStrengthValue(int averageStrengthValue) {
		this.averageStrengthValue = averageStrengthValue;
	}

	public int getRssiDbm() {
		return rssiDbm;
	}

	public void setRssiDbm(int rssiDbm) {
		this.rssiDbm = rssiDbm;
	}
	
	

}
