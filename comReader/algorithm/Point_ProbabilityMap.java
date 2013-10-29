import GeometryObjects.*;

import HelperClass_Math.*;


public class Point_ProbabilityMap extends Point {

	public double rssi_value;
	
	public Point_ProbabilityMap(double x, double y, double rssi_value) {
		super(x, y);

		this.rssi_value = rssi_value;
	}
	
	// Copy-Konstruktor
    public Point_ProbabilityMap(Point_ProbabilityMap p) {
    	this(p.x, p.y, p.rssi_value);
    }
    
    public Point_ProbabilityMap reversed() {
    	return new Point_ProbabilityMap(-this.x, -this.y, this.rssi_value);
    }
    
    public String toString() {
        return "x = " + super.x + " y = " + super.y + " rssi = " + this.rssi_value;
    }
}
