package algorithm.helper;

/**
 * The Class Point_RoomMap.
 */
public class PointRoomMap extends Point {
	
	/** The weight_value. */
	private double weight_value;

	/**
	 * Instantiates a new point_ room map.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public PointRoomMap(double x, double y) {
		super(x, y);
		weight_value = 1;
	}

	/**
	 * Sets the new weight value.
	 *
	 * @param value the new new weight value
	 */
	public void setNewWeightValue (double value) {
		weight_value = value;
	}
	
	/**
	 * Gets the weight value.
	 *
	 * @return the weight value
	 */
	public double getWeightValue () {
		return weight_value;
	}
    
    /* (non-Javadoc)
     * @see algorithm.helper.Point#toString()
     */
    public String toString() {
        return "x = " + super.x + " y = " + super.y + " weight = " + this.weight_value;
    }	
}
