package algorithm.helper;


public class PointProbabilityMap extends Point {

	public double rssi_value;
        
	public PointProbabilityMap(double x, double y, double rssi_value) {
		super(x, y);
        this.rssi_value = rssi_value;
	}
        
        // Copy-Konstruktor
    public PointProbabilityMap(PointProbabilityMap p) {
            this(p.x, p.y, p.rssi_value);
    }
    
    public PointProbabilityMap reversed() {
            return new PointProbabilityMap(-this.x, -this.y, this.rssi_value);
    }
    
    public String toString() {
        return "x = " + super.x + " y = " + super.y + " rssi = " + this.rssi_value;
    }
}