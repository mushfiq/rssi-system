package algorithm.helper;

import java.util.ArrayList;



public class GrahamScan_OLD {
    
        private Point_ProbabilityMap[] p;
    private int n;
    private int h;
    
    public ArrayList<Point_ProbabilityMap> computeHull2(Point_ProbabilityMap[] p) {
            this.p = p;
            n = p.length;
            if(n < 3) return null;
            h = 0;
            grahamScan();
            
            ArrayList<Point_ProbabilityMap> pMap = new ArrayList<Point_ProbabilityMap>();
            for(int i = 0; i < h; i++) {
                    pMap.add(p[i]);
            }
            return pMap; 
    }
                            
    
    public int computeHull(Point_ProbabilityMap[] p) {
            this.p = p;
            n = p.length;
            if(n < 3) return n;
            h = 0;
            grahamScan();
            return h;
    }
    
    private void grahamScan() {
            exchange(0, indexOfLowestPoint());
            Point_ProbabilityMap p1 = new Point_ProbabilityMap(p[0]);
            makeRelTo(p1);
            sort();
            
            makeRelTo(p1.reversed());
            int i = 3, k = 3;
            while(k < n) {
                    exchange(i, k);
                    while(!isConvex(i-1))
                            exchange(i-1, i--);
                    k++;
                    i++;
            }
            h = i;
    }
    private void exchange(int i, int j) {
            Point_ProbabilityMap t = p[i];
            p[i] = p[j];
            p[j] = t;
    }
    
    private void makeRelTo(Point_ProbabilityMap p0) {
            Point_ProbabilityMap p1 = new Point_ProbabilityMap(p0);
            for(int i = 0; i < n; i++)
                    p[i].makeRelTo(p1);
    }
    
    private int indexOfLowestPoint() {
            int min = 0;
            for(int i = 1; i < n; i++)
                    if(p[i].y < p[min].y || p[i].y == p[min].y && p[i].x < p[min].x)
                            min = i;
            return min;
    }
    
    private boolean isConvex(int i) {
            return p[i].isConvex(p[i-1], p[i+1]);
    }
    
    private void sort() {
            quicksort(1, n-1);
    }
    
    private void quicksort(int lo, int hi) {
            int i = lo, j = hi;
            Point_ProbabilityMap q = p[(lo+hi)/2];
            while(i <= j) {
                    while(p[i].isLess(q)) i++;
                    while(q.isLess(p[j])) j--;
                    if(i <= j) exchange(i++, j--);
            }
            if(lo < j) quicksort(lo,  j);
            if(i < hi) quicksort(i, hi);
    }
}