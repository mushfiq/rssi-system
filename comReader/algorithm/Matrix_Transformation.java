


import java.util.ArrayList;



public class Matrix_Transformation {
	
	ArrayList<Point_ProbabilityMap> transformation (ArrayList<Point_ProbabilityMap> p, double angle, double receiverXpos, double receiverYpos ) {
		
		//Define a new List with the transformated coordindates of the Probability Map
		ArrayList<Point_ProbabilityMap> transformation_probMap = new ArrayList<Point_ProbabilityMap>();
		
		//Rotation the coordination of the Probability Map
		for (int i = 0; i < p.size(); i++) {
			
			double xRotation = Math.cos(angle) * p.get(i).x - Math.sin(angle) * p.get(i).y;
			double yRotation = Math.sin(angle) * p.get(i).x + Math.cos(angle) * p.get(i).y;
			transformation_probMap.add(new Point_ProbabilityMap(xRotation,yRotation,p.get(i).rssi_value));
			
		}
		
		//Translation the coordination of the Probability Map
		for (int i = 0; i < p.size(); i++) {
			transformation_probMap.get(i).x = transformation_probMap.get(i).x + receiverXpos; 
			transformation_probMap.get(i).y = transformation_probMap.get(i).y + receiverYpos;
		}
		return transformation_probMap;
	}

}
