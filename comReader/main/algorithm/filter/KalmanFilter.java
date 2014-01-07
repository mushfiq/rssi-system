package algorithm.filter;

import algorithm.helper.Point;
/**
 * This class KalmanFilter aims to recursively provide better
 * and better estimates of the position of the watch. The construction of the Kalman filter is based on the
 * explaination <a href="http://greg.czerniak.info/guides/kalman1/</a>.
 * @version 1.0 22 Nov 2013
 * @author Yentran Tran
 *
 */
public class KalmanFilter extends Filter {
	/**
	 * SensorNoise_DEFAUL is the estimated measurement error covariance as default value
	 */
	private static final double SENSORNOISE_DEFAULT = 0.8;
	/**
	 * Processnoise_DEFAULT is the estimated process error covariance as default value
	 */
	private static final double PROCESSNOISE_DEFAULT = 0.125;



	/**
	 * prioreEstimate is the prior Point which we get from the variable estimate
	 * estimate is the new Point that is calculated
	 * rawValue is used in order to store the lastPosition which is displayed
	 */
	private Point priorEstimate, estimate, rawValue;
	/**.
	 * priorErrorVariance is the prior ErrorVariance which we get from the variable errorCovariance
	 * errorCovarRSSI is the newest estimate of the average error for each part of the state.
	 * kalmanGain
	 */
	private double priorErrorVariance,kalmanGain;
	/**
	 * firstTimeRunning check if the Filter is used first time
	 */
	private boolean firstTimeRunning = true;
	
	/**
	 * sensorNose is the estimated measurement error covariance
	 * processNoise is the estimated process error covariance
	 */
	private static double sensorNoise, processNoise;


	/**
	 * Default constructur
	 */
	public KalmanFilter() {
		super();
		KalmanFilter.processNoise = KalmanFilter.PROCESSNOISE_DEFAULT;
		KalmanFilter.sensorNoise = KalmanFilter.SENSORNOISE_DEFAULT;
	    priorErrorVariance = 1;
	}

	public KalmanFilter (double processNoise, double sensorNoise) {
		super();
		this.processNoise = processNoise;
		this.sensorNoise = sensorNoise;
	    priorErrorVariance = 1;
	}

	/**
	 * @param lastPosition is the position we want to estimate better
	 * @return A new point that represents the position of the watch
	 */
	@Override
	public Point applyFilter(Point lastPosition) {

		if (firstTimeRunning) {
			//estimate is the old one here
			priorEstimate = lastPosition;
			    firstTimeRunning = false;
		}

		//Prediction Part
	    else {
	    	//estimate is the old one here
	    	priorEstimate = estimate;
	    	//errorCovariance is the old one
	     	priorErrorVariance = errorCovarRSSI + processNoise;

	    }
		//Correction Part
		//lastPosition is the newest Position recieved
		rawValue = lastPosition;
		kalmanGain = priorErrorVariance / (priorErrorVariance + sensorNoise);
		estimate = new Point(priorEstimate.getX() + (kalmanGain * (rawValue.getX() - priorEstimate.getX())), priorEstimate.getY() + (kalmanGain * (rawValue.getY() - priorEstimate.getY())));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorVariance;
		//posistion is the variable I want to update which will be lastPosition next time
		lastPosition = new Point(estimate.getX(), 	estimate.getY());

		return lastPosition;
	}

	/**
	 * priorRSSI is the previous calculated RSSI value.
	 * estimateRSSI is the new calculate RSSI value
	 * rawRSSI is temporary parameter to store the estimateRSSI
	 */
	private double priorRSSI, estimateRSSI, rawRSSI;

	/**
	 * prioErrorCovarRSSI is the previous Covariance
	 * errorCovarRSSI is the new calculated Covariance
	 */
	private double priorErrorCovarRSSI,errorCovarRSSI;
	/**
	 * firstTimeRun check if the Filter is used first time
	 */
	private static boolean firstTimeRun = true;


	/**
	 * @param rssi is the RSSI value we want to estimate better
	 * @return A new RSSI value which is calculated with the kalman filter based on the previous RSSI value
	 */
	public double updateRSSI (double rssi) {

		if (firstTimeRun) {
			priorRSSI = rssi;
			priorErrorCovarRSSI = 0.5;
		    firstTimeRun = false;
		}

		//Prediction Part

	    else {
	    	priorRSSI = estimateRSSI;
	     	priorErrorCovarRSSI = errorCovarRSSI + processNoise;
	    }
		//Correction Part

		rawRSSI = rssi;
		kalmanGain = priorErrorCovarRSSI / (priorErrorCovarRSSI + sensorNoise);
		estimateRSSI = priorRSSI + (kalmanGain * (rawRSSI - priorRSSI));
		errorCovarRSSI = (1 - kalmanGain) * priorErrorCovarRSSI;

		rssi = estimateRSSI;

		return rssi;
	}
}
