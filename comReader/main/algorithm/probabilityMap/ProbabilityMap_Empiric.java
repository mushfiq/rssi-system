package algorithm.probabilityMap;

import java.util.ArrayList;

import algorithm.helper.Point_ProbabilityMap;



public class ProbabilityMap_Empiric extends ProbabilityMap {
        
        private double n; // n = signal propagation constant, also named propagation exponent
        private double A; // A = received signal strength at a distance of one meter.
        
        public ProbabilityMap_Empiric(double n, double A) {
                this.n = n;
                this.A = A;
        }
        
        @Override
        public ArrayList<Point_ProbabilityMap> getProbabilityMap(double width, double height, double granularity) {
                double n_width = width / granularity;
                double n_height = height / granularity;
                
                ArrayList<Point_ProbabilityMap> pMap = new ArrayList<Point_ProbabilityMap>();
                
                // 1. Quadrant
                double n_widthHalf = n_width/2;
                double n_heighthHalf = n_height/2;
                
                for(double i = -n_widthHalf; i <= n_widthHalf; i+=granularity) { // x-Achse
                        for(double j = -n_heighthHalf; j <= n_heighthHalf; j+=granularity) { // y-Achse
                                
                                double distance = 0;
                                if(i == 0 && j == 0) {
                                        distance = granularity;
                                } else {
                                        distance = Math.sqrt(i * i + j * j);
                                }
                                pMap.add(new Point_ProbabilityMap(i, j, distanceToRSSI(distance)));
                                
                        }
                }
                System.out.println(pMap.size());
                return pMap;
        }
        
        // n = signal propagation constant, also named propagation exponent
        public void setN(int n) {
                this.n = n;
        }
        // A = received signal strength at a distance of one meter.
        public void setA(int A) {
                this.A = A;
        }
        
        // n = signal propagation constant, also named propagation exponent
        public double getN() {
                return this.n;
        }
        // A = received signal strength at a distance of one meter.
        public double getA() {
                return this.A;
        }
        
        public double distanceToRSSI(double distance) {
                double rssi = -(10 * this.n * Math.log10(distance) + this.A);
                return rssi;
        }
}