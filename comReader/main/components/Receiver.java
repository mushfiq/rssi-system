/*
 * File: Receiver.java
 * Date				Author				Changes
 * 08 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 */
package components;

/**
 * The class Receiver. A receiver represents the its location, its angle and id.
 * 
 * @version 1.0 08 Nov 2013
 * @author Tommy Griese
 */
public class Receiver {

	/** The angle of the receiver. */
	private double angle;
	
	/** The x-coordinate of the receiver. */
	private double xPos;
	
	/** The y-coordinate of the receiver. */
	private double yPos;
	
	/** The id of the receiver. */
	private int id;

private boolean onMap;
	
	/**
	 * Instantiates a new receiver.
	 *
	 * @param id the id of the receiver
	 * @param xPos the x-coordinate of the receivers position
	 * @param yPos the y-coordinate of the receivers position
	 * @param angle the angle of the receiver
	 */
	public Receiver(int id, double xPos, double yPos, double angle) {
		this.id = id;
		this.xPos = xPos;
		this.yPos = yPos;
		this.angle = angle;
		this.onMap = false;
	}

	public Receiver(int id, double xPos, double yPos, double angle ,
			boolean onMap) {
		super();
		this.angle = angle;
		this.xPos = xPos;
		this.yPos = yPos;
		this.id = id;
		this.onMap = onMap;
	}



	public Receiver(int id) {
		super();
		this.id = id;
		this.onMap = false;
	}
	
	/**
	 * Gets the id of the receiver.
	 *
	 * @return the id
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Gets the x-coordinate of the receiver.
	 *
	 * @return the x-coordinate
	 */
	public double getXPos() {
		return this.xPos;
	}
	
	/**
	 * Gets the y-coordinate of the receiver.
	 *
	 * @return the y-coordinate
	 */
	public double getYPos() {
		return this.yPos;
	}
	
	/**
	 * Gets the angle of the receiver.
	 *
	 * @return the angle of the receiver
	 */
	public double getAngle() {
		return this.angle;
	}
	
	public boolean isOnMap() {
		return onMap;
	}


	public void setOnMap(boolean onMap) {
		this.onMap = onMap;
	}
	
	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}
	
}
