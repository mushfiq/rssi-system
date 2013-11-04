package algorithm.probabilityMap;

import java.util.ArrayList;
import algorithm.helper.Point_ProbabilityMap;

public abstract class ProbabilityMap {
	public abstract ArrayList<Point_ProbabilityMap> getProbabilityMap(
			double fromX, double toX, 
			double fromY, double toY,
			double granularity);
}
