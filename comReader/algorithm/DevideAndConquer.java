import java.awt.List;

import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;

import HelperClass_Math.Point;


public class DevideAndConquer {
	
//	Point_ProbabilityMap [] convexhull (Point_ProbabilityMap [] points) {
//		
//		// There must be at least 3 points
//	    if (points.length < 3) 
//	    	return points;
//	    
//	
//	    
//		    // Split up the lists into two sublists
//		    int mid = points.length / 2;
//		    Point_ProbabilityMap [] left_points = new Point_ProbabilityMap [mid];
//		    Point_ProbabilityMap [] right_points = new Point_ProbabilityMap [mid];
//		    
//		    // Recursively call sort on the sublists
//		    sort(left_points);
//		    sort(right_points);
//
//	    
//	    
//	    return merge (left_points, right_points);
//		
//	}
//	
//	Point_ProbabilityMap [] merge (Point_ProbabilityMap [] left_points, Point_ProbabilityMap [] right_points) {
//		 // Loop through all elements of both lists
//
//			Point_ProbabilityMap [] result = new Point_ProbabilityMap [100];
//		    for (int i = 0; i < left_points.length; i++) {
//		    	for (int j = 0; j < right_points.length; j++) {
//		    		while (i < left_points.length && j < right_points.length) {
//		    			if (left_points[i].isLess(right_points[j])	
//		    				result.add(left_points[i]);
//		    			else 
//		    				result.add(right_points[j]);
//		    		}
//		    		while (i < left_points.length)
//		    			result.add(left_points[i]);
//		    		while (i < right_points.length)
//		    			result.add(right_points[j]);
//		    		
//		    		
//		    	}
//		    }
//		
//		    return result;
//	}
	
	//input: the number of points n, and
	//an array of points S, sorted by x coord.
	//output: the convex hull of the points in S.
	 Point_ProbabilityMap [] findHullDC(int n, Point_ProbabilityMap S[]) {
		if (n > 1) {
			int h = S.length/2;
			int m = n-h;
			Point_ProbabilityMap LH[] = null, RH[] = null; //left and right hulls
			for (int i = 0; i < 100; i++){
				System.out.println(S[i]);
				LH[i] = S[i];}
			for (int i = h; i < S.length ; i++)
				RH[i] = S[i];

			return convexHull(LH, LH.length);
		} 
		else {
			return S;
		}
	}
	
	Point_ProbabilityMap [] convexHull(Point_ProbabilityMap[] points, int n) {

	 
	    // Initialize Result
		
	    int []next = new int [n];
	    for (int i = 0; i < n; i++)
	        next[i] = -1;
	 
	    // Find the leftmost point
	    int l = 0;
	    for (int i = 1; i < n; i++)
	        if (points[i].x < points[l].x)
	            l = i;
	 
	    // Start from leftmost point, keep moving counterclockwise
	    // until reach the start point again
	    int p = l, q;
	    do
	    {
	        // Search for a point 'q' such that orientation(p, i, q) is
	        // counterclockwise for all points 'i'
	        q = (p+1)%n;
	        for (int i = 0; i < n; i++)
	          if (orientation(points[p], points[i], points[q]) == 2)
	             q = i;
	 
	        next[p] = q;  // Add q to result as a next point of p
	        p = q; // Set p as q for next iteration
	    } while (p != l);
	 
	    // Print Result
	    for (int i = 0; i < n; i++)
	    {
	        if (next[i] != -1)
	           System.out.println("("+points[i].x+";"+points[i].y+")");
	    }
	    
	    return points;
	}
	
	// To find orientation of ordered triplet (p, q, r).
	// The function returns following values
	// 0 --> p, q and r are colinear
	// 1 --> Clockwise
	// 2 --> Counterclockwise
	int orientation(Point p, Point q, Point r)
	{
	    double val = (q.y - p.y) * (r.x - q.x) -
	              (q.x - p.x) * (r.y - q.y);
	 
	    if (val == 0) return 0;  // colinear
	    return (val > 0)? 1: 2; // clock or counterclock wise
	}
	
	
	
	
	
//	//input: the number of points n, and
//	//an array of points S, sorted by x coord.
//	//output: the convex hull of the points in S.
//	public Point[] findHullDC(int n, Point S[]) {
//		if (n > 1) {
//			int h = S.length/2;
//			int m = n-h;
//			Point LH[] = null, RH[] = null; //left and right hulls
//			for (int i = 0; i < h; i++)
//				LH[i] = S[i];
//			for (int i = h; i < S.length ; i++)
//				RH[i] = S[i];
//
//			return mergeHulls(LH,RH,LH.length, RH.length);
//		} 
//		else {
//			return S;
//		}
//	}
	

	

	
	
}
