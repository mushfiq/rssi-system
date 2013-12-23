package services;

import dataobjects.Point;

public class Utilities {
	
	public static Point previousLocation;
	public Utilities()
	{
		previousLocation = new Point(0.0f, 0.0f);
	}
	
	public static double getAngleOfLineBetweenTwoPoints(Point p1, Point p2) 
	{ 
		
	double x1 = p1.getX();	
	double y1 = p1.getY();
	
	double x2 = p2.getX();
	double y2 = p2.getY();
	
	double xDiff = x2 - x1; 
	double yDiff = y2 - y1; 
	return Math.toDegrees(Math.atan2(xDiff,yDiff));
	}
	public static String getDirectionByDegree(Double deg){
		String direction=null;
		
		if(deg >= 0 && deg <= 11.25 || deg > 348.75 && deg <= 360) {
			direction = "North";
		}


		if(deg > 11.25 && deg <= 33.75) {
			direction = "North North East";
		}

		if(deg > 33.75 && deg <= 56.25) {
			direction = "North East";
		}

		if(deg > 56.25 && deg <= 78.75) {
			direction = "East North East";
		}

		if(deg > 78.75 && deg <= 101.25) {
			direction = "East";
		}

		if(deg > 101.25 && deg <= 123.75) {
			direction = "East South East";
		}

		if(deg > 123.75 && deg <= 146.25) {
			direction = "South East";
		}

		if(deg > 146.25 && deg <= 168.75) {
			direction = "South South East";
		}

		if(deg > 168.75 && deg <= 191.25) {
			direction = "South";
		}

		if(deg > 191.25 && deg <= 213.75) {
			direction = "South South West";
		}

		if(deg > 213.75 && deg <= 236.25) {
			direction = "South West";
		}

		if(deg > 236.25 && deg <= 258.75) {
			direction = "West South West";
		}

		if(deg > 258.75 && deg <= 281.25) {
			direction = "West";
		}

		if(deg > 281.25 && deg <= 303.75) {
			direction = "West North West";
		}

		if(deg > 303.75 && deg <= 326.25) {
			direction = "North West";
		}

		if(deg > 326.25 && deg <= 348.75) {
			direction = "North North West";
		}

		return direction;
	}

	

	public static String getDirection(Point currentLocation){
	
		Double degree = getAngleOfLineBetweenTwoPoints(previousLocation,currentLocation);
		
		
		if(degree<0){
			degree = 0.0;
		}
		String direction = getDirectionByDegree(degree);
		previousLocation = new Point(currentLocation.getX(), currentLocation.getY());
		String statementForVoice = "Device is moving towards "+direction;
		return statementForVoice;
	}
}