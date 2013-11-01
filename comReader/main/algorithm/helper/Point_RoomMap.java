package algorithm.helper;


public class Point_RoomMap extends Point {
        
        private double weight_value;

        public Point_RoomMap(double x, double y) {
                super(x, y);
                weight_value = 1;
        }

        public void setNewWeightValue (double value) {
                weight_value = value;
        }
        
        public double getWeightValue () {
                return weight_value;
        }
    
    public String toString() {
        return "x = " + super.x + " y = " + super.y + " weight = " + this.weight_value;
    }


        
}