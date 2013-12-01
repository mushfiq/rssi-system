package algorithm;

import java.util.ArrayList;

import algorithm.helper.Point;

public class KalmanFilter {
	
	private Point priorEstimate, estimate, rawValue;
	private double priorErrorVariance, errorCovariance;
	private boolean firstTimeRunning;
	private double COVARIANCE = 0.6;
	private double STATEVARIANCE = 0.5;
	private double kalmanGain = 0.5;
	
	
	public  Point updatePoint (Point lastPosition) {
		
		if(firstTimeRunning) {
			priorEstimate = lastPosition;          		//estimate is the old one here
		    priorErrorVariance = 1.2;        		//errorCovariance is the old one
		    firstTimeRunning = false;
		}
		
	    else {
	    	priorEstimate = estimate;              	//estimate is the old one here
	     	priorErrorVariance = errorCovariance;  	//errorCovariance is the old one
	    }
		
		rawValue = lastPosition;          				//lastPosition is the newest Position recieved
		kalmanGain = priorErrorVariance / (priorErrorVariance + COVARIANCE);
		estimate = new Point (priorEstimate.getX() + (kalmanGain * (rawValue.getX() - priorEstimate.getY())),priorEstimate.getY() + (kalmanGain * (rawValue.getY() - priorEstimate.getY())));
		errorCovariance = (1 - kalmanGain) * priorErrorVariance + STATEVARIANCE;
		lastPosition = new Point (estimate.getX() + STATEVARIANCE,estimate.getY() + STATEVARIANCE);   //posistion is the variable I want to update which will be lastPosition next time
		
		return lastPosition;	
	}
	


}
