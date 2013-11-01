package algorithm.stubs;

public class RoomMap {

        private double length;
        private double width;
        private String pathToImage;
        
        public RoomMap(double length, double width) {
                this.length = length;
                this.width = width;
        }
        
        
        
        public RoomMap(double length, double width, String pathToImage) {
			super();
			this.length = length;
			this.width = width;
			this.pathToImage = pathToImage;
		}



		public double getWidth() {
                return this.width;
        }
        public double getLength() {
                return this.length;
        }



		public void setLength(double length) {
			this.length = length;
		}



		public void setWidth(double width) {
			this.width = width;
		}



		public String getPathToImage() {
			return pathToImage;
		}



		public void setPathToImage(String pathToImage) {
			this.pathToImage = pathToImage;
		}
        
        
}