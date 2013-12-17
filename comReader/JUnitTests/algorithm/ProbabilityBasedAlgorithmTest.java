/*
 * File: ProbabilityBasedAlgorithmTest.java
 * Date				Author				Changes
 * 20 Nov 2013		Tommy Griese		initial test version 1.0 (one receiver)
 * 21 Nov 2013		Tommy Griese		create test for two, three and four receiver
 * 01 Dec 2013		Tommy Griese		Just testing the second room map function and ProbabilityMapElliptic
 * 08 Dec 2013		Tommy Griese		Added new tests
 * 											testWeightFunctionExtendAndEllipticalProbMap
 * 											testConvexHullTransformation
 * 											testCautiousOfMethodCalculation
 * 										Added JavaDoc comments
 */
package algorithm;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import algorithm.ProbabilityBasedAlgorithm;
import algorithm.filter.KalmanFilterOneDim;
import algorithm.helper.Point;
import algorithm.probabilityMap.ProbabilityMap;
import algorithm.probabilityMap.ProbabilityMapPathLossCircle;
import algorithm.probabilityMap.ProbabilityMapPathLossElliptic;
import algorithm.weightFunction.WeightFunction;
import algorithm.weightFunction.WeightFunctionExtended;
import algorithm.weightFunction.WeightFunctionSimple;
import components.Receiver;
import components.RoomMap;


/**
 * The Class ProbabilityBasedAlgorithmTest. This test is testing the functional principle of the 
 * {@link ProbabilityBasedAlgorithm}. For each test also a sum of images is going to be created so that
 * it is easier to verify the correct results.
 * 
 * @version 1.1 08 Dec 2013
 * @author Tommy Griese
 */
public class ProbabilityBasedAlgorithmTest {

	/** Main path of the tests. */
	private static final String FOLDER_PROBABILITYBASED = "comReader\\JUnitTests\\algorithm\\ProbabilityBased\\";
	
	/** Additional path for the test with one receiver. */
	private static final String FOLDER_ONERECEIVER = FOLDER_PROBABILITYBASED + "testOneReceiver\\";
	
	/** Additional path for the test with two receiver. */
	private static final String FOLDER_TWORECEIVER = FOLDER_PROBABILITYBASED + "testTwoReceiver\\";
	
	/** Additional path for the test with three receiver. */
	private static final String FOLDER_THREERECEIVER = FOLDER_PROBABILITYBASED + "testThreeReceiver\\";
	
	/** Additional path for the test with four receiver. */
	private static final String FOLDER_FOURRECEIVER = FOLDER_PROBABILITYBASED + "testFourReceiver\\";
	
	/** Additional path for the weight function test. */
	private static final String FOLDER_WEIGHTFUNCTION = FOLDER_PROBABILITYBASED + "testWeightFunction\\";
	
	/** Additional path for the convex hull transformation test. */
	private static final String FOLDER_CONVEXHULLTRANSFORMATION = FOLDER_PROBABILITYBASED + "testConvexHullTransformation\\";
	
	/** Additional path for the behavior test. */
	private static final String FOLDER_BEHAVIORCALCULATION = FOLDER_PROBABILITYBASED + "testBehaviorOfMethodCalculation\\";

	private static final int RECEIVERID_0 = 0;
	private static final int RECEIVERID_1 = 1;
	private static final int RECEIVERID_2 = 2;
	private static final int RECEIVERID_3 = 3;
	private static final int RECEIVERID_4 = 4;
	private static final int RECEIVERID_5 = 5;
	private static final int RECEIVERID_6 = 6;
	private static final int RECEIVERID_7 = 7;
	private static final int RECEIVERID_8 = 8;
	private static final int RECEIVERID_9 = 9;
	private static final int RECEIVERID_10 = 10;
	private static final int RECEIVERID_11 = 11;
	private static final int RECEIVERID_12 = 12;
	private static final int RECEIVERID_13 = 13; // To simulate a receiver that has a signal but is not configured in the algo
	
	/** Room map dimension 'from' in x. */
	private static final double ROOMMAP_XFROM = -25.0;
	
	/** Room map dimension 'to' in x. */
	private static final double ROOMMAP_XTO = 25.0;
	
	/** Room map dimension 'from' in y. */
	private static final double ROOMMAP_YFROM = -25.0;
	
	/** Room map dimension 'to' in y. */
	private static final double ROOMMAP_YTO = 25.0;
	
	/** Impossible value in x that is not being to calculated in this test. */
	private static final double IMPOSSIBLE_POSITION_X = 1000.0;
	
	/** Impossible value in y that is not being to calculated in this test. */
	private static final double IMPOSSIBLE_POSITION_Y = 1000.0;
	
	/** Failure delta. */
	private static final double DELTA = 0.1;
	
	
	
	
	/** A list of receivers used in this test. */
	private static ArrayList<Receiver> receivers;
	
	/** A room map that is used for this test. */
	private static RoomMap roommap;
	
	/** A (path loss circle) probability map used for this test. */
	private static ProbabilityMapPathLossCircle probMapCircle;
	
	/** A (path loss elliptic) probability map used for this test. */
	private static ProbabilityMapPathLossElliptic probMapElliptic;
	
	/** A (path loss elliptic with extreme values) probability map used for this test. */
	private static ProbabilityMapPathLossElliptic probMapEllipticExtreme;
	
	/** A simple weight function used for this test. */
	private static WeightFunction weightFunctionSimple;
	
	/** A extended weight function used for this test. */
	private static WeightFunction weightFunctionExtended;
	
	/** A kalman filter used for this test. */
	private static KalmanFilterOneDim filterKalman;
	
	private static final double RECEIVERPOSITION_MINUS25 = -25.0;
	private static final double RECEIVERPOSITION_PLUS25 = +25.0;
	private static final double RECEIVERPOSITION_MINUS5 = -5.0;
	private static final double RECEIVERPOSITION_PLUS5 = +5.0;
	private static final double RECEIVERPOSITION_ZERO = 0.0;
	
	private static final double RECEIVERANGLE_ZERO = 0.0;
	private static final double RECEIVERANGLE_PLUS45 = +45.0;
	private static final double RECEIVERANGLE_MINUS45 = -45.0;
	private static final double RECEIVERANGLE_PLUS90 = +90.0;
	private static final double RECEIVERANGLE_MINUS90 = -90.0;
	private static final double RECEIVERANGLE_PLUS225 = +225.0;
	
	private static final double PROBMAP_SIGNALPROPAGATIONCONSTANT = 3.0;
	private static final double PROBMAP_SIGNALSTRENGTHONEMETER = 51.0;
	private static final double PROBMAP_XFROM = -20.0;
	private static final double PROBMAP_XTO = 20.0;
	private static final double PROBMAP_YFROM = -20.0;
	private static final double PROBMAP_YTO = 20.0;
	private static final double PROBMAP_GRANULARITY = 0.25;
	private static final double PROBMAP_LENGTHHALFAXISX100 = 1.0;
	private static final double PROBMAP_LENGTHHALFAXISY075 = 0.75;
	private static final double PROBMAP_LENGTHHALFAXISY025 = 0.25;
	
	/**
	 * This method is executed once, before the start the start of all tests.
	 * It is initializing the receivers, a room map, three probability maps,
	 * two weight functions and one filter that are needed for all tests.
	 * Moreover it generates the folder for the images of each test.
	 *
	 * @throws Exception the exception that is thrown when something goes wrong
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		Receiver r0 = new Receiver(RECEIVERID_0, RECEIVERPOSITION_MINUS25, RECEIVERPOSITION_PLUS25, RECEIVERANGLE_ZERO);
		Receiver r1 = new Receiver(RECEIVERID_1, RECEIVERPOSITION_MINUS25, RECEIVERPOSITION_ZERO, RECEIVERANGLE_ZERO);
		Receiver r2 = new Receiver(RECEIVERID_2, RECEIVERPOSITION_MINUS25, RECEIVERPOSITION_MINUS25, RECEIVERANGLE_ZERO);
		Receiver r3 = new Receiver(RECEIVERID_3, RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_PLUS5, RECEIVERANGLE_ZERO);
		Receiver r4 = new Receiver(RECEIVERID_4, RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_MINUS5, RECEIVERANGLE_ZERO);
		Receiver r5 = new Receiver(RECEIVERID_5, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_PLUS25, RECEIVERANGLE_ZERO);
		Receiver r6 = new Receiver(RECEIVERID_6, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_ZERO, RECEIVERANGLE_ZERO);
		Receiver r7 = new Receiver(RECEIVERID_7, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_MINUS25, RECEIVERANGLE_ZERO);
		Receiver r8 = new Receiver(RECEIVERID_8, RECEIVERPOSITION_PLUS5, RECEIVERPOSITION_PLUS5, RECEIVERANGLE_ZERO);
		Receiver r9 = new Receiver(RECEIVERID_9, RECEIVERPOSITION_PLUS5, RECEIVERPOSITION_MINUS5, RECEIVERANGLE_ZERO);
		Receiver r10 = new Receiver(RECEIVERID_10, RECEIVERPOSITION_PLUS25, RECEIVERPOSITION_PLUS25, RECEIVERANGLE_ZERO);
		Receiver r11 = new Receiver(RECEIVERID_11, RECEIVERPOSITION_PLUS25, RECEIVERPOSITION_ZERO, RECEIVERANGLE_ZERO);
		Receiver r12 = new Receiver(RECEIVERID_12, RECEIVERPOSITION_PLUS25, RECEIVERPOSITION_MINUS25, RECEIVERANGLE_ZERO);

        receivers = new ArrayList<Receiver>();
        
        receivers.add(r0);
        receivers.add(r1);
        receivers.add(r2);
        receivers.add(r3);
        receivers.add(r4);
        receivers.add(r5);
        receivers.add(r6);
        receivers.add(r7);
        receivers.add(r8);
        receivers.add(r9);
        receivers.add(r10);
        receivers.add(r11);
        receivers.add(r12);
        
        roommap = new RoomMap(ROOMMAP_XFROM, ROOMMAP_XTO, ROOMMAP_YFROM, ROOMMAP_YTO, null);
        
        probMapCircle = new ProbabilityMapPathLossCircle(
        		PROBMAP_SIGNALPROPAGATIONCONSTANT, PROBMAP_SIGNALSTRENGTHONEMETER,
        		PROBMAP_XFROM, PROBMAP_XTO, PROBMAP_YFROM, PROBMAP_YTO, PROBMAP_GRANULARITY);
        probMapElliptic = new ProbabilityMapPathLossElliptic(
        		PROBMAP_SIGNALPROPAGATIONCONSTANT, PROBMAP_SIGNALSTRENGTHONEMETER,
				PROBMAP_XFROM, PROBMAP_XTO, PROBMAP_YFROM, PROBMAP_YTO, PROBMAP_GRANULARITY,
				PROBMAP_LENGTHHALFAXISX100, PROBMAP_LENGTHHALFAXISY075);
        probMapEllipticExtreme = new ProbabilityMapPathLossElliptic(
        		PROBMAP_SIGNALPROPAGATIONCONSTANT, PROBMAP_SIGNALSTRENGTHONEMETER,
        		PROBMAP_XFROM, PROBMAP_XTO, PROBMAP_YFROM, PROBMAP_YTO, PROBMAP_GRANULARITY,
        		PROBMAP_LENGTHHALFAXISX100, PROBMAP_LENGTHHALFAXISY025);
        
        weightFunctionSimple = new WeightFunctionSimple();
        weightFunctionExtended = new WeightFunctionExtended();
        
        filterKalman = new KalmanFilterOneDim();     
        
        
        deleteDirectory(new File(FOLDER_PROBABILITYBASED));
        
        createDirectory(new File(FOLDER_PROBABILITYBASED));
        createDirectory(new File(FOLDER_ONERECEIVER));
        createDirectory(new File(FOLDER_TWORECEIVER));
        createDirectory(new File(FOLDER_THREERECEIVER));
        createDirectory(new File(FOLDER_FOURRECEIVER));
        createDirectory(new File(FOLDER_WEIGHTFUNCTION));
        createDirectory(new File(FOLDER_CONVEXHULLTRANSFORMATION));
        createDirectory(new File(FOLDER_BEHAVIORCALCULATION));
	}

	/**
	 * This test is testing the results of the method {@link ProbabilityBasedAlgorithm#calculate(HashMap)} 
	 * when the method gets one reading as an input. 
	 */
//	@Ignore
	@Test
	public void testCalculateOneReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
        algorithm.enableFilter(false);
		 
		algorithm.setGrayscaleImagePath(FOLDER_ONERECEIVER);
		
		// -- start test01 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_6, -60.0);

        pExpected = new Point(getReceiver(RECEIVERID_6).getXPos(), getReceiver(RECEIVERID_6).getYPos());
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test01 --
        
        // -- start test02 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_3, -70.0);
     	
     	pExpected = new Point(getReceiver(RECEIVERID_3).getXPos(), getReceiver(RECEIVERID_3).getYPos());
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected));
     	// -- ende test02 --
     	
     	// -- start test03 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_4, -40.0);
     	
     	pExpected = new Point(getReceiver(RECEIVERID_4).getXPos(), getReceiver(RECEIVERID_4).getYPos());
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected));
     	// -- ende test03 --
     	
     	// -- start test04 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_8, -67.0);
     	
     	pExpected = new Point(getReceiver(RECEIVERID_8).getXPos(), getReceiver(RECEIVERID_8).getYPos());
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected));
     	// -- ende test04 --
     	
     	// -- start test05 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_9, -55.0);
     	
     	pExpected = new Point(getReceiver(RECEIVERID_9).getXPos(), getReceiver(RECEIVERID_9).getYPos());
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected));
     	// -- ende test05 --
     	
     	// -- start test06 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_0, -80.0);
     	
     	pExpected = new Point(-21.57505, 21.57505);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test06 --
     	
     	// -- start test07 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_2, -60.0);
     	
     	pExpected = new Point(-24.69403, -24.69403);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test07 --
     	
     	// -- start test08 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_10, -85.5);
     	
     	pExpected = new Point(19.50922, 19.50922);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test08 --
     	
     	// -- start test09 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_12, -72.5);
     	
     	pExpected = new Point(23.30092, -23.30092);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test09 --
     	
     	// -- start test10 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_5, -60.0);
     	
     	pExpected = new Point(0.0, 24.72222);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test10 --
     	
     	// -- start test11 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_1, -60.0);
     	
     	pExpected = new Point(-24.72222, 0.0);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test11 --
     	
     	// -- start test12 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_7, -60.0);
     	
     	pExpected = new Point(0.0, -24.72222);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test12 --
     	
     	// -- start test13 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_11, -60.0);
     	
     	pExpected = new Point(24.72222, 0.0);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test13 --
	}
	
	/**
	 * This test is testing the results of the method {@link ProbabilityBasedAlgorithm#calculate(HashMap)} 
	 * when the method gets two readings as an input. 
	 */
//	@Ignore
	@Test
	public void testCalculateTwoReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_TWORECEIVER);
		algorithm.enableFilter(false);
		
		// -- start test01 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_4, -77.50);
        readings.put(RECEIVERID_8, -77.50);

        pExpected = new Point(0.0, 0.0);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test01 --
        
        // -- start test02--
		readings = new HashMap<Integer, Double>();
	    readings.put(RECEIVERID_4, -70.00);
	    readings.put(RECEIVERID_8, -70.00);
	
	    pExpected = new Point(0.0, 0.0);
	    pCalculated = algorithm.calculate(readings);
	    assertTrue(testPoints(pCalculated, pExpected));
	    // -- ende test02 --
	    
	    // -- start test03--
	 	readings = new HashMap<Integer, Double>();
	 	readings.put(RECEIVERID_4, -70.00);
	 	readings.put(RECEIVERID_9, -70.00);
	 	
	 	pExpected = new Point(0.0, -5.0);
	 	pCalculated = algorithm.calculate(readings);
	 	assertTrue(testPoints(pCalculated, pExpected));
	 	// -- ende test03 --
	 	
	 	// -- start test04--
 	 	readings = new HashMap<Integer, Double>();
 	 	readings.put(RECEIVERID_4, -75.00);
 	 	readings.put(RECEIVERID_9, -75.00);
 	 	
 	 	pExpected = new Point(0.0, -5.0);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected));
	 	// -- ende test04 --
 	 	
 	 	// -- start test05--
 	 	readings = new HashMap<Integer, Double>();
 	 	readings.put(RECEIVERID_3, -69.00);
 	 	readings.put(RECEIVERID_6, -69.00);
 	 	
 	 	pExpected = new Point(-2.5, 2.5);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected));
	 	// -- ende test05 --
 	 	
 	 	// -- start test06--
 	 	readings = new HashMap<Integer, Double>();
 	 	readings.put(RECEIVERID_1, -86.50);
 	 	readings.put(RECEIVERID_3, -76.00);
 	 	
 	 	pExpected = new Point(-10.87168, 3.58850);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected, DELTA));
	 	// -- ende test06 --
 	 	
 	 	// -- start test07--
 	 	readings = new HashMap<Integer, Double>();
 	 	readings.put(RECEIVERID_1, -86.50);
 	 	readings.put(RECEIVERID_8, -86.50);
 	 	
 	 	pExpected = new Point(-10.00, 2.50);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected));
	 	// -- ende test07 --
 	 	
 	 	// -- start test08 --
 	 	readings = new HashMap<Integer, Double>();
 	 	readings.put(RECEIVERID_7, -86.50);
 	 	readings.put(RECEIVERID_12, -86.50);
 	 	
 	 	pExpected = new Point(12.5, -22.28635);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected, DELTA));
	 	// -- ende test08 --
	}
	
	/**
	 * This test is testing the results of the method {@link ProbabilityBasedAlgorithm#calculate(HashMap)} 
	 * when the method gets three readings as an input. 
	 */
//	@Ignore
	@Test
	public void testCalculateThreeReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_THREERECEIVER);
		algorithm.enableFilter(false);
		
		// -- start test01 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -73.00);
        readings.put(RECEIVERID_6, -74.00);
        readings.put(RECEIVERID_4, -73.00);

        pExpected = new Point(-4.6, 0.0);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test01 --
        
        // -- start test02 --
 		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -73.00);
        readings.put(RECEIVERID_8, -73.00);
        readings.put(RECEIVERID_4, -73.00);

        pExpected = new Point(-2.5, 2.5);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test02 --
        
        // -- start test03 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -70.00);
        readings.put(RECEIVERID_8, -82.00);
        readings.put(RECEIVERID_4, -75.00);

        pExpected = new Point(-4.42308, 1.03846);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test03 --
        
        // -- start test04 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_4, -73.00);
        readings.put(RECEIVERID_8, -73.00);
        readings.put(RECEIVERID_9, -73.00);

        pExpected = new Point(2.5, -2.5);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test04 --
        
        // -- start test05 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_4, -70.00);
        readings.put(RECEIVERID_8, -75.00);
        readings.put(RECEIVERID_9, -70.00);

        pExpected = new Point(5.0, -1.02);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test05 --
        
        // -- start test06 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_10, -87.00);
        readings.put(RECEIVERID_8, -87.00);
        readings.put(RECEIVERID_6, -70.00);

        pExpected = new Point(5.40123, 5.40123);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test06 --
        
        // -- start test07 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_10, -89.00);
        readings.put(RECEIVERID_8, -87.00);
        readings.put(RECEIVERID_6, -89.00);

        pExpected = new Point(12.5, 12.5);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test07 --
	}

	/**
	 * This test is testing the results of the method {@link ProbabilityBasedAlgorithm#calculate(HashMap)} 
	 * when the method gets four readings as an input. 
	 */
//	@Ignore
	@Test
	public void testCalculateFourReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_FOURRECEIVER);
		algorithm.enableFilter(false);
		
		// -- start test01 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -73.00);
        readings.put(RECEIVERID_4, -73.00);
        readings.put(RECEIVERID_8, -73.00);
        readings.put(RECEIVERID_9, -73.00);

        pExpected = new Point(0.0, 0.0);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test01 --
        
        // -- start test02 --
        readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -78.00);
        readings.put(RECEIVERID_4, -78.00);
        readings.put(RECEIVERID_8, -78.00);
        readings.put(RECEIVERID_9, -78.00);

        pExpected = new Point(0.0, 0.0);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));
        // -- ende test02 --
	}
	
	/**
	 * This test is testing the results of the method 
	 * {@link ProbabilityBasedAlgorithm#calculate(HashMap)} when this algorithm has to handle 
	 * different {@link WeightFunction}s and {@link ProbabilityMap}s. 
	 */
//	@Ignore
	@Test
	public void testWeightFunctionExtendAndEllipticalProbMap() {
		HashMap<Integer, Double> readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -74.00);
        readings.put(RECEIVERID_8, -82.00);
        readings.put(RECEIVERID_4, -75.00);
        readings.put(RECEIVERID_9, -80.0);
        
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_WEIGHTFUNCTION);
		algorithm.enableFilter(false);

		
		algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_4), weightFunctionExtended);
		// -- start test01 --
        pExpected = new Point(-1.90278, 0.54167);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test01 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_8), weightFunctionExtended);
 		// -- start test02 --
        pExpected = new Point(-2.25, 0.125);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test02 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_3), weightFunctionExtended);
 		// -- start test03 --
        pExpected = new Point(-2.2, 0.05);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test03 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_9), weightFunctionExtended);
 		// -- start test04 --
        pExpected = new Point(-2.29167, 0.08333);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test04 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_4), weightFunctionSimple);
        algorithm.setProbabilityMapForOneReceiver(getReceiver(RECEIVERID_4), probMapElliptic);
        // -- start test05 --
        pExpected = new Point(-3.63393, -0.75893);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test05 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_8), weightFunctionSimple);
        algorithm.setProbabilityMapForOneReceiver(getReceiver(RECEIVERID_8), probMapElliptic);
        // -- start test06 --
        pExpected = new Point(-2.87931, -0.44828);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test06 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_3), weightFunctionSimple);
        algorithm.setProbabilityMapForOneReceiver(getReceiver(RECEIVERID_3), probMapElliptic);
        // -- start test07 --
        pExpected = new Point(-3.625, 0.25);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test07 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_9), weightFunctionSimple);
        algorithm.setProbabilityMapForOneReceiver(getReceiver(RECEIVERID_9), probMapElliptic);
        // -- start test08 --
        pExpected = new Point(-1.17056, -1.73832);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test08 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_4), weightFunctionExtended);
        // -- start test09 --
        pExpected = new Point(-0.80357, -1.32143);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test09 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_8), weightFunctionExtended);
        // -- start test10 --
        pExpected = new Point(-1.27865, -1.90625);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test10 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_3), weightFunctionExtended);
        // -- start test11 --
        pExpected = new Point(-1.875, 0.25);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test11 --
        
        algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_9), weightFunctionExtended);
        // -- start test12 --
        pExpected = new Point(-3.125, -0.0625);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test12 --
	}
	
	/**
	 * This test is testing the results of the method 
	 * {@link ProbabilityBasedAlgorithm#calculate(HashMap)} when this algorithm has to handle 
	 * different rotated {@link Receiver}s. 
	 */
//	@Ignore
	@Test
	public void testConvexHullTransformation() {
		HashMap<Integer, Double> readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -74.00);
        readings.put(RECEIVERID_4, -82.00);
        readings.put(RECEIVERID_5, -75.00);
        readings.put(RECEIVERID_6, -80.0);
        readings.put(RECEIVERID_7, -77.0);
        readings.put(RECEIVERID_8, -77.0);
		 
        Receiver r3 = new Receiver(RECEIVERID_3, RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_PLUS5, RECEIVERANGLE_PLUS45);
		Receiver r4 = new Receiver(RECEIVERID_4, RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_MINUS5, RECEIVERANGLE_MINUS45);
		Receiver r5 = new Receiver(RECEIVERID_5, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_PLUS25, RECEIVERANGLE_ZERO);
		Receiver r6 = new Receiver(RECEIVERID_6, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_ZERO, RECEIVERANGLE_PLUS90);
		Receiver r7 = new Receiver(RECEIVERID_7, RECEIVERPOSITION_ZERO, RECEIVERPOSITION_MINUS25, RECEIVERANGLE_MINUS90);
		Receiver r8 = new Receiver(RECEIVERID_8, RECEIVERPOSITION_PLUS5, RECEIVERPOSITION_PLUS5, RECEIVERANGLE_PLUS225);

		ArrayList<Receiver> rec = new ArrayList<Receiver>();
		rec.add(r3);
		rec.add(r4);
		rec.add(r5);
		rec.add(r6);
		rec.add(r7);
		rec.add(r8);
		
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, rec, probMapEllipticExtreme, weightFunctionExtended, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_CONVEXHULLTRANSFORMATION);
		algorithm.enableFilter(false);

		// -- start test01 --
        pExpected = new Point(-4.45455, 1.65909);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test01 --
        
        // -- start test02 --
        rec.clear();
        r4 = new Receiver(RECEIVERID_4, RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_MINUS5, RECEIVERANGLE_ZERO);
        rec.add(r4);
        
        HashMap<Integer, Double> readings2 = new HashMap<Integer, Double>();
        readings2.put(RECEIVERID_4, -82.00);
        
        algorithm.setGrayscaleDebugInformationExtended(false);
        for(int i = 0; i <= 360; i+=10) {
        	r4.setAngle(i);
            
        	pExpected = new Point(RECEIVERPOSITION_MINUS5, RECEIVERPOSITION_MINUS5);
            pCalculated = algorithm.calculate(readings);
            assertTrue(testPoints(pCalculated, pExpected));
        }
        // -- ende test02 --
	}
	
	/**
	 * This test is testing the results of the method 
	 * {@link ProbabilityBasedAlgorithm#calculate(HashMap)} when this algorithm has to handle 
	 * a different amount of receivers and receivers that are not configurated. 
	 */
//	@Ignore
	@Test
	public void testBehaviorOfMethodCalculation() {
		HashMap<Integer, Double> readings = new HashMap<Integer, Double>();
        
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setGrayscaleDebugImageSettings(true, true, true, true);
		algorithm.setGrayscaleImagePath(FOLDER_BEHAVIORCALCULATION);
		algorithm.enableFilter(false);

		// -- start test01 --
        pExpected = null;
        pCalculated = algorithm.calculate(readings);
        assertTrue(pExpected == pCalculated);
        // -- ende test01 --
        
        readings.put(RECEIVERID_13, -40.0);
        
        // -- start test02 --
        pExpected = null;
        pCalculated = algorithm.calculate(readings);
        assertTrue(pExpected == pCalculated);
        // -- ende test02 --
        
        readings.put(RECEIVERID_3, -74.00);
        readings.put(RECEIVERID_8, -82.00);
        readings.put(RECEIVERID_4, -75.00);
        readings.put(RECEIVERID_9, -80.0);
        
        // -- start test03 --
        pExpected = new Point(-2.29167, 0.125);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test03 --
	}
	
	/**
	 * This test is testing the results of the method 
	 * {@link ProbabilityBasedAlgorithm#checkIfPointIsInRoom(Point)}. 
	 */
//	@Ignore
	@Test
	public void testIfPointIsOutsideRoom() {
		Point pExpected;
		Point pCalculated;
		 
		ProbabilityBasedAlgorithm algorithm = 
				new ProbabilityBasedAlgorithm(roommap, receivers, probMapCircle, weightFunctionSimple, filterKalman);
		
		pExpected = new Point(0.0, ROOMMAP_XTO);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(0.0, ROOMMAP_XTO));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XFROM, ROOMMAP_YFROM);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XFROM, ROOMMAP_YFROM));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XFROM, ROOMMAP_YTO);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XFROM, ROOMMAP_YTO));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(5.0, 4.0);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(5.0, 4.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(-15.0, 20.0);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(-15.0, 20.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XFROM, 0.0);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XFROM - 1.0, 0.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XFROM, ROOMMAP_YFROM);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XFROM - 1.0, ROOMMAP_XFROM - 5.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(1.0, ROOMMAP_YFROM);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(1.0, ROOMMAP_XFROM - 15.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XTO, ROOMMAP_YFROM);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XTO + 5.0, ROOMMAP_XFROM - 1.0));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XTO, 2.5);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XTO + 5.5, 2.5));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XTO, ROOMMAP_YTO);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XTO + 5.5, ROOMMAP_YTO + 2.5));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(24.9, ROOMMAP_YTO);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(24.9, ROOMMAP_YTO + 2.5));
		assertTrue(testPoints(pCalculated, pExpected));
		
		pExpected = new Point(ROOMMAP_XFROM, ROOMMAP_YTO);
		pCalculated = algorithm.checkIfPointIsInRoom(new Point(ROOMMAP_XFROM - 0.01, ROOMMAP_YTO + 0.1));
		assertTrue(testPoints(pCalculated, pExpected));
	}
		
	/**
	 * Compares two points.
	 * 
	 * @param pCalculated a point to test
	 * @param pExpected a point to test
	 * @return true if the points are equal, false otherwise
	 */
	private boolean testPoints(Point pCalculated, Point pExpected) {
		Point pImpossible = new Point(IMPOSSIBLE_POSITION_X, IMPOSSIBLE_POSITION_Y);        
        return pCalculated.equals(pExpected) && !pCalculated.equals(pImpossible);
	}
	
	/**
	 * Compares two points. This method takes a delta into account to compare the
	 * two points.
	 * 
	 * @param pCalculated a point to test
	 * @param pExpected a point to test
	 * @param delta a delta for the comparison
	 * @return true if the points are equal, false otherwise
	 */
	private boolean testPoints(Point pCalculated, Point pExpected, double delta) {
		Point pImpossible = new Point(IMPOSSIBLE_POSITION_X, IMPOSSIBLE_POSITION_Y);        
        return Math.abs(pExpected.getX() - pCalculated.getX()) < delta 
        		&& Math.abs(pExpected.getY() - pCalculated.getY()) < delta 
        		&& !pCalculated.equals(pImpossible);
	}
	
	/**
	 * Returns the receiver with the given id.
	 *  
	 * @param id of the receiver that is looked for
	 * @return the receiver if found, null otherwise
	 */
	private Receiver getReceiver(int id) {
		for (int i = 0; i < receivers.size(); i++) {
			if (receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Deletes the structure below the given path and the folder itself.
	 * 
	 * @param path the path to delete
	 * @return true if and only if the file or directory is successfully deleted; false otherwise 
	 */
	private static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
	
	/**
	 * Creates a new directory.
	 * 
	 * @param path the path where the directory should be created
	 * @return true if the directory was created, false otherwise
	 */
	private static boolean createDirectory(File path) {
		if (!path.exists()) {
			if (path.mkdir()) {
				return true;
			}
		}
		return false;
	}

}
