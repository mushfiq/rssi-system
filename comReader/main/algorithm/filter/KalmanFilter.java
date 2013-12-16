
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
public class KalmanFilter extends Filter {
	/**
	 * Process noise: value between 0 and 1
	 */
	private static final double SENSORNOISE_DEFAULT = 0.4;
	private static final double PROCESSNOISE_DEFAULT = 0.05;
	
	
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
	private static double priorErrorVariance;
	private static double kalmanGain;
	private boolean firstTimeRunning = true;
	private static boolean firstTimeRun = true;
	private static double sensorNoise, processNoise;

	/** The logger. */
    private Logger logger;

	/**
	 * Default constructur
	 */
	public KalmanFilter() {
		super();
		
		 // instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
        readConfigParameters();
	}
	
	public KalmanFilter (double processNoise, double sensorNoise) {
		super ();
		
		 // instantiate logger
        this.logger = Utilities.initializeLogger(this.getClass().getName()); 
        
        KalmanFilter.processNoise = processNoise;
        KalmanFilter.sensorNoise = sensorNoise;
	}

	/**
	 * @param lastPosition is the position we want to estimate better
	 * @return A new point that represents the position of the watch
	 */
	@Override
	public Point applyFilter(Point lastPosition) {
		
		if(firstTimeRunning) {
			priorEstimate = lastPosition;          		//estimate is the old one here
		    priorErrorVariance = 0.5;        		//errorCovariance is the old one
		    firstTimeRunning = false;
		}
		
		//Prediction Part
	    else {
	    	priorEstimate = estimate;              	//estimate is the old one here
	     	priorErrorVariance = errorCovarRSSI + processNoise;  	//errorCovariance is the old one
	    }
		//Correction Part
		rawValue = lastPosition;          				//lastPosition is the newest Position recieved
		kalmanGain = priorErrorVariance / (priorErrorVariance + sensorNoise);
		estimate = new Point (priorEstimate.getX() + (kalmanGain * (rawValue.getX() - priorEstimate.getX())),priorEstimate.getY() + (kalmanGain * (rawValue.getY() - priorEstimate.getY())));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorVariance;
		
		lastPosition = new Point (estimate.getX(), 	estimate.getY());   //posistion is the variable I want to update which will be lastPosition next time
		
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
			priorRSSI = rssi;
			priorErrorVarRSSI = 0.5;
		    firstTimeRun = false;
		}

		//Prediction Part	
	    else {
	    	priorRSSI = estimateRSSI;
	     	priorErrorVarRSSI = errorCovarRSSI+processNoise;
	    }
		//Correction Part

		rawRSSI = rssi;
		kalmanGain = priorErrorVarRSSI / (priorErrorVarRSSI + sensorNoise);
		estimateRSSI = priorRSSI + (kalmanGain * (rawRSSI - priorRSSI));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorVarRSSI;

		rssi = estimateRSSI;
		
		return rssi;	
	}
	
	private void readConfigParameters() {
        String res = "";
        double value = KalmanFilter.PROCESSNOISE_DEFAULT;
        try {
                res = Utilities.getConfigurationValue("kalman_filter.process_noise");
                value = Double.parseDouble(res);
        } catch(NumberFormatException e) {
                this.logger.log(Level.WARNING, "Reading kalman_filter.process_noise failed, default value was set.");
        }
        KalmanFilter.processNoise = value;
        
        value = KalmanFilter.SENSORNOISE_DEFAULT;
        try {
                res = Utilities.getConfigurationValue("kalman_filter.sensor_noise");
                value = Double.parseDouble(res);
        } catch(NumberFormatException e) {
                this.logger.log(Level.WARNING, "Reading kalman_filter.sensor_noise failed, default value was set.");
        }
        KalmanFilter.sensorNoise = value;
	}
}