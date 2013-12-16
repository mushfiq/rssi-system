
package algorithm.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;
import algorithm.helper.Point;
/**
 * 
 * This class KalmanFilter aims to recursively provide better and better estimates of the position of the watch.
 * 
 * 
 * @version 1.0 22 Nov 2013
 * @author Yentran Tran
 *
 */	
public class KalmanFilterOneDim extends Filter {
	private static final double COVARIANCE_DEFAULT = 0.4;
	private static final double STATEVARIANCE_DEFAULT = 0.05;
	
	
	/**
	 * prioreEstimate is the prior Point which we get from the variable estimate
	 * estimate is the new Point that is calculated 
	 * rawValue is used in order to store the lastPosition which is displayed
	 * priorErrorVariance is the prior ErrorVariance which we get from the variable errorCovariance
	 * errorCovariance 
	 * firstTimeRunning check if the Filter is used first time
	 * COVARIANCE
	 * STATEVARIANCE
	 * kalmanGain 
	 * 
	 */
	private Point priorEstimate, estimate, rawValue;
	private static double priorErrorVariance, errorCovariance; // why static
	private static double kalmanGain; // why static
	private boolean firstTimeRunning = true;
	private static boolean firstTimeRun = true; // why static
	private static double covariance, statevariance; // why static

	/** The logger. */
    private Logger logger;
	
	public KalmanFilterOneDim() {
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
        readConfigParameters();
	}
	
	public KalmanFilterOneDim (double covariance, double statevariance){
		super();
		
		// instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
		this.covariance = covariance;
		this.statevariance = statevariance;
	}
	
	
	/**
	 * 
	 * @param lastPosition is the position we want to estimate better
	 * @return A new point that represents the position of the watch
	 */
	@Override
	public Point applyFilter(Point lastPosition) {
		
		if(firstTimeRunning) {
			priorEstimate = lastPosition;          		//estimate is the old one here
		    priorErrorVariance = 1.2;        		//errorCovariance is the old one
		    firstTimeRunning = false;
		}
		
		//Prediction Part
		
		
	    else {
	    	priorEstimate = estimate;              	//estimate is the old one here
	     	priorErrorVariance = errorCovarRSSI;  	//errorCovariance is the old one
	    }
		//Correction Part
		
		
		rawValue = lastPosition;          				//lastPosition is the newest Position recieved
		kalmanGain = priorErrorVariance / (priorErrorVariance + covariance);
		estimate = new Point (priorEstimate.getX() + (kalmanGain * (rawValue.getX() - priorEstimate.getX())),priorEstimate.getY() + (kalmanGain * (rawValue.getY() - priorEstimate.getY())));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorVariance + statevariance;
		
		lastPosition = new Point (estimate.getX() + statevariance,estimate.getY() + statevariance);   //posistion is the variable I want to update which will be lastPosition next time
		
		return lastPosition;	
	}
	
	
	private double priorRSSI;
	private double estimateRSSI;
	private double rawRSSI;
	private double priorErrorVarRSSI;
	private double errorCovarRSSI;

	
	/**
	 * 
	 * @param lastPosition is the position we want to estimate better
	 * @return A new point that represents the position of the watch
	 */
	public double updateRSSI (double rssi) {
		
		if(firstTimeRun) {
			priorRSSI = rssi;          		//estimate is the old one here
			priorErrorVarRSSI = 1.2;        		//errorCovariance is the old one
		    firstTimeRun = false;
		}
		
		//Prediction Part
		
		
	    else {
	    	priorRSSI = estimateRSSI;              	//estimate is the old one here
	     	priorErrorVarRSSI = errorCovarRSSI;  	//errorCovariance is the old one
	    }
		//Correction Part
		
		
		rawRSSI = rssi;          				//lastPosition is the newest Position recieved
		kalmanGain = priorErrorVarRSSI / (priorErrorVarRSSI + covariance);
		estimateRSSI = priorRSSI + (kalmanGain * (rawRSSI - priorRSSI));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorVarRSSI + covariance;
		
		
		rssi = estimateRSSI + statevariance;   //posistion is the variable I want to update which will be lastPosition next time
		
		return rssi;	
	}
	
	private void readConfigParameters() {
		String res = "";
		double value = KalmanFilterOneDim.COVARIANCE_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("kalman_filter.covariance");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading kalman_filter.covariance failed, default value was set.");
		}
		this.covariance = value;
		
		value = KalmanFilterOneDim.STATEVARIANCE_DEFAULT;
		try {
			res = Utilities.getConfigurationValue("kalman_filter.statevariance");
			value = Double.parseDouble(res);
		} catch(NumberFormatException e) {
			this.logger.log(Level.WARNING, "Reading kalman_filter.statevariance failed, default value was set.");
		}
		this.statevariance = value;
	}
}
