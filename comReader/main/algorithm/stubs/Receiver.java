package algorithm.stubs;


public class Receiver {

        private double angle;
        private double xPos;
        private double yPos;
        private int id;

        public Receiver(int id, double xPos, double yPos, double angle) {
                this.id = id;
                this.xPos = xPos;
                this.yPos = yPos;
                this.angle = angle;
        }
        
        public int getID() {
                return this.id;
        }
        public double getXPos() {
                return this.xPos;
        }
        public double getYPos() {
                return this.yPos;
        }
        public double getAngle() {
                return this.angle;
        }
}