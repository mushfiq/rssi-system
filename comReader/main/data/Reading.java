package data;

import java.util.ArrayList;

public class Reading {

	private int receiverId;
	private int watchId;
	private ArrayList<Integer> signalStrengths;
	
	
	public Reading() {
		// TODO Auto-generated constructor stub
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
	
	

}
