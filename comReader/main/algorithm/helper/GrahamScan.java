package algorithm.helper;

import java.util.ArrayList;

public class GrahamScan {
    
        private ArrayList<Point_ProbabilityMap> p;
    private int n;
    private int h;
        
    public ArrayList<Point_ProbabilityMap> computeHull(ArrayList<Point_ProbabilityMap> p) {
            this.p = p;
            n = p.size();
            if(n < 3) return null;
            h = 0;
            grahamScan();
            
            ArrayList<Point_ProbabilityMap> convexHull = new ArrayList<Point_ProbabilityMap>();
            for(int i = 0; i < h; i++) {
                    convexHull.add(p.get(i));
            }
            return convexHull;
    }
    
    public int computeHull2(ArrayList<Point_ProbabilityMap> p) {
            this.p = p;
            n = p.size();
            if(n < 3) return n;
            h = 0;
            grahamScan();
            return h;
    }
    
    private void grahamScan() {
            exchange(0, indexOfLowestPoint());
            Point_ProbabilityMap p1 = new Point_ProbabilityMap(p.get(0));
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
            Point_ProbabilityMap t = p.get(i);
            p.set(i, p.get(j));
            p.set(j, t);
    }
    
    private void makeRelTo(Point_ProbabilityMap p0) {
            Point_ProbabilityMap p1 = new Point_ProbabilityMap(p0);
            for(int i = 0; i < n; i++)
                    p.get(i).makeRelTo(p1);
    }
    
    private int indexOfLowestPoint() {
            int min = 0;
            for(int i = 1; i < n; i++)
                    if(p.get(i).y < p.get(min).y || p.get(i).y == p.get(min).y && p.get(i).x < p.get(min).x)
                            min = i;
            return min;
    }
    
    private boolean isConvex(int i) {
            return p.get(i).isConvex(p.get(i-1), p.get(i+1));
    }
    
    private void sort() {
            quicksort(1, n-1);
    }
    
    private void quicksort(int lo, int hi) {
            int i = lo, j = hi;
            Point_ProbabilityMap q = p.get((lo+hi)/2);
            while(i <= j) {
                    while(p.get(i).isLess(q)) i++;
                    while(q.isLess(p.get(j))) j--;
                    if(i <= j) exchange(i++, j--);
            }
            if(lo < j) quicksort(lo,  j);
            if(i < hi) quicksort(i, hi);
    }
}