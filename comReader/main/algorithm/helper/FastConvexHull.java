/*
 * File: FastConvexHull.java
 * Date				Author				Changes
 * 09 Nov 2013		Tommy Griese		create version 1.0
 * 					Yentran Tran
 */
package algorithm.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The Class FastConvexHull is derived from
 * <a href="https://code.google.com/p/convex-hull/source/browse/Convex+Hull/src/algorithms/FastConvexHull.java?r=4">FastConvexHull</a>.
 * Little adaptations were made so that we can use it in our code.
 * 
 * @version 1.0 09 Nov 2013
 */
public class FastConvexHull {
	
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	
	/**
	 * This method computes the convex hull.
	 *
	 * @param points the array list of all points to find the convex hull
	 * @return the array list with the convex hull
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PointProbabilityMap> computeHull(ArrayList<PointProbabilityMap> points) {
		
		ArrayList<PointProbabilityMap> xSorted = (ArrayList<PointProbabilityMap>) points.clone();
		Collections.sort(xSorted, new XCompare());

		int n = xSorted.size();

		PointProbabilityMap[] lUpper = new PointProbabilityMap[n];
                
		lUpper[0] = xSorted.get(0);
		lUpper[1] = xSorted.get(1);
                
		int lUpperSize = 2;
                
		for (int i = 2; i < n; i++) {
			lUpper[lUpperSize] = xSorted.get(i);
			lUpperSize++;
                        
			while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - FastConvexHull.THREE], 
											    lUpper[lUpperSize - FastConvexHull.TWO],
											    lUpper[lUpperSize - FastConvexHull.ONE])) {
				// Remove the middle point of the three last
				lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
				lUpperSize--;
			}
		}
                
		PointProbabilityMap[] lLower = new PointProbabilityMap[n];
                
		lLower[0] = xSorted.get(n - 1);
		lLower[1] = xSorted.get(n - 2);
                
		int lLowerSize = 2;
                
		for (int i = n - FastConvexHull.THREE; i >= 0; i--) {
			lLower[lLowerSize] = xSorted.get(i);
			lLowerSize++;
                        
			while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - FastConvexHull.THREE], 
												lLower[lLowerSize - FastConvexHull.TWO], 
												lLower[lLowerSize - FastConvexHull.ONE])) {
				// Remove the middle point of the three last
				lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
				lLowerSize--;
			}
		}
                
		ArrayList<PointProbabilityMap> result = new ArrayList<PointProbabilityMap>();
                
		for (int i = 0; i < lUpperSize; i++) {
			result.add(lUpper[i]);
		}
                
		for (int i = 1; i < lLowerSize - 1; i++) {
			result.add(lLower[i]);
		}
                
		return result;
	}
        
	/**
	 * Right turn.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return true, if successful
	 */
	private boolean rightTurn(Point a, Point b, Point c) {
		return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
	}

	/**
	 * The Class XCompare.
	 */
	private class XCompare implements Comparator<Point> {
		
		/**
		 * compare.
		 *
		 * @param o1 the point
		 * @param o2 the point
		 * @return true, if successful
		 */
		@Override
		public int compare(Point o1, Point o2) {
			return (new Double(o1.getX())).compareTo(new Double(o2.getX()));
		}
	}
}
