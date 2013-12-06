/*
 * File: ProbabilityBasedAlgorithmTest.java
 * Date				Author				Changes
 * 20 Nov 2013		Tommy Griese		initial test version 1.0 (one receiver)
 * 21 Nov 2013		Tommy Griese		create test for two, three and four receiver
 * 01 Dec 2013		Tommy Griese		Just testing the second room map function and ProbabilityMapElliptic
 */
package algorithm;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import algorithm.helper.Point;
import algorithm.probabilityMap.ProbabilityMapPathLossCircle;
import algorithm.probabilityMap.ProbabilityMapPathLossElliptic;
import algorithm.weightFunction.WeightFunctionExtended;
import algorithm.weightFunction.WeightFunctionSimple;
import components.Receiver;
import components.RoomMap;

public class ProbabilityBasedAlgorithmTest {

	private static ArrayList<Receiver> receivers;
	
	private static ProbabilityBasedAlgorithm algorithm;
	
	private static final String FOLDER_ONERECEIVER = "comReader\\JUnitTests\\algorithm\\testOneReceiver\\";
	private static final String FOLDER_TWORECEIVER = "comReader\\JUnitTests\\algorithm\\testTwoReceiver\\";
	private static final String FOLDER_THREERECEIVER = "comReader\\JUnitTests\\algorithm\\testThreeReceiver\\";
	private static final String FOLDER_FOURRECEIVER = "comReader\\JUnitTests\\algorithm\\testFourReceiver\\";
	
	private static final String FOLDER_WEIGHTFUNCTION = "comReader\\JUnitTests\\algorithm\\testWeightFunction\\";
	
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
	
	private static final double ROOMMAP_XFROM = -25.0;
	private static final double ROOMMAP_XTO = 25.0;
	private static final double ROOMMAP_YFROM = -25.0;
	private static final double ROOMMAP_YTO = 25.0;
	
	private static final double IMPOSSIBLE_POSITION_X = 1000.0;
	private static final double IMPOSSIBLE_POSITION_Y = 1000.0;
	
	private static final double DELTA = 0.1;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Receiver r0 = new Receiver(RECEIVERID_0, -25.0, 25.0, 0.0);
		Receiver r1 = new Receiver(RECEIVERID_1, -25.0, 0.0, 0.0);
		Receiver r2 = new Receiver(RECEIVERID_2, -25.0, -25.0, 0.0);
		Receiver r3 = new Receiver(RECEIVERID_3, -5, 5, 0.0);
		Receiver r4 = new Receiver(RECEIVERID_4, -5, -5, 0.0);
		Receiver r5 = new Receiver(RECEIVERID_5, 0.0, 25.0, 0.0);
		Receiver r6 = new Receiver(RECEIVERID_6, 0.0, 0.0, 0.0);
		Receiver r7 = new Receiver(RECEIVERID_7, 0.0, -25.0, 0.0);
		Receiver r8 = new Receiver(RECEIVERID_8, 5, 5, 0.0);
		Receiver r9 = new Receiver(RECEIVERID_9, 5, -5, 0.0);
		Receiver r10 = new Receiver(RECEIVERID_10, 25.0, 25.0, 0.0);
		Receiver r11 = new Receiver(RECEIVERID_11, 25.0, 0.0, 0.0);
		Receiver r12 = new Receiver(RECEIVERID_12, 25.0, -25.0, 0.0);

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
        
        algorithm = new ProbabilityBasedAlgorithm(
        		new RoomMap(ROOMMAP_XFROM, ROOMMAP_XTO, ROOMMAP_YFROM, ROOMMAP_YTO, ""), receivers, 
        		new ProbabilityMapPathLossCircle(3.0, 51.0, -20.0, 20.0, -20.0, 20.0, 0.25),
        		new WeightFunctionSimple());  
        
        algorithm.setGrayscaleDebugInformation(true);
        algorithm.setGrayscaleDebugInformationExtended(true);
        algorithm.setConvexhullDebugInformation(false);
        
        deleteDirectory(new File(FOLDER_ONERECEIVER));
        deleteDirectory(new File(FOLDER_TWORECEIVER));
        deleteDirectory(new File(FOLDER_THREERECEIVER));
        deleteDirectory(new File(FOLDER_FOURRECEIVER));
        deleteDirectory(new File(FOLDER_WEIGHTFUNCTION));
        
        createDirectory(new File(FOLDER_ONERECEIVER));
        createDirectory(new File(FOLDER_TWORECEIVER));
        createDirectory(new File(FOLDER_THREERECEIVER));
        createDirectory(new File(FOLDER_FOURRECEIVER));
        createDirectory(new File(FOLDER_WEIGHTFUNCTION));
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}
	
	@Before
	public void init() {
		algorithm.setWeightFunction(new WeightFunctionSimple());
		algorithm.setProbabilityMap(new ProbabilityMapPathLossCircle(3.0, 51.0, -20.0, 20.0, -20.0, 20.0, 0.25));
	}

	@Test
	public void testCalculateOneReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
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
     	
     	pExpected = new Point(-21.12399, 21.12399);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test06 --
     	
     	// -- start test07 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_2, -60.0);
     	
     	pExpected = new Point(-24.21875, -24.21875);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test07 --
     	
     	// -- start test08 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_10, -85.5);
     	
     	pExpected = new Point(19.06142, 19.06142);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test08 --
     	
     	// -- start test09 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_12, -72.5);
     	
     	pExpected = new Point(22.84349, -22.84349);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test09 --
     	
     	// -- start test10 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_5, -60.0);
     	
     	pExpected = new Point(0.0, 24.22596);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test10 --
     	
     	// -- start test11 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_1, -60.0);
     	
     	pExpected = new Point(-24.22596, 0.0);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test11 --
     	
     	// -- start test12 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_7, -60.0);
     	
     	pExpected = new Point(0.0, -24.22596);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test12 --
     	
     	// -- start test13 --
        readings = new HashMap<Integer, Double>();
     	readings.put(RECEIVERID_11, -60.0);
     	
     	pExpected = new Point(24.22596, 0.0);
     	pCalculated = algorithm.calculate(readings);
     	assertTrue(testPoints(pCalculated, pExpected, DELTA));
     	// -- ende test13 --
	}
	
	@Test
	public void testCalculateTwoReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		algorithm.setGrayscaleImagePath(FOLDER_TWORECEIVER);
		
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
 	 	
 	 	pExpected = new Point(-10.88170, 3.61830);
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
 	 	
 	 	pExpected = new Point(12.5, -21.72543);
 	 	pCalculated = algorithm.calculate(readings);
 	 	assertTrue(testPoints(pCalculated, pExpected, DELTA));
	 	// -- ende test08 --
	}
	
	@Test
	public void testCalculateThreeReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		algorithm.setGrayscaleImagePath(FOLDER_THREERECEIVER);
		
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

        pExpected = new Point(-4.42307, 1.03846);
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

	@Test
	public void testCalculateFourReceiver() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		algorithm.setGrayscaleImagePath(FOLDER_FOURRECEIVER);
		
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
	
	@Test
	public void testNewWeightFunction() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		 
		algorithm.setGrayscaleImagePath(FOLDER_WEIGHTFUNCTION);

		
		
		algorithm.setProbabilityMapForOneReceiver(getReceiver(RECEIVERID_4), new ProbabilityMapPathLossElliptic(
				3.0, 51.0, -20.0, 20.0, -20.0, 20.0, 0.25,
				3.0, 1.0));
		algorithm.setWeightFunctionForOneReceiver(getReceiver(RECEIVERID_4), new WeightFunctionExtended(0.1));
		/*algorithm.setProbabilityMap(new ProbabilityMapElliptic(
				3.0, 51.0, -20.0, 20.0, -20.0, 20.0, 0.25,
				1.0, 1.0));*/
		
		//algorithm.setRoomMapWeightFunction(RoomMapWeightFunction.SIMPLE);
		
		// -- start test01 --
		/*readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -73.00);
        readings.put(RECEIVERID_4, -73.00);
        readings.put(RECEIVERID_8, -73.00);
        readings.put(RECEIVERID_9, -73.00);

        pExpected = new Point(0.0, 0.0);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected));*/
        // -- ende test01 --
        
        // -- start test02 --
      	readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_3, -74.00);
        readings.put(RECEIVERID_8, -82.00);
        readings.put(RECEIVERID_4, -75.00);
        readings.put(RECEIVERID_13, -40.0);

        pExpected = new Point(-4.42307, 1.03846);
        pCalculated = algorithm.calculate(readings);
        assertTrue(testPoints(pCalculated, pExpected, DELTA));
        // -- ende test02 --
	}
	
	private boolean testPoints(Point pCalculated, Point pExpected) {
		Point pImpossible = new Point(IMPOSSIBLE_POSITION_X, IMPOSSIBLE_POSITION_Y);        
        if(pCalculated.equals(pExpected) && !pCalculated.equals(pImpossible)) {
        	return true;
        } else {
        	return false;
        }
	}
	private boolean testPoints(Point pCalculated, Point pExpected, double delta) {
		Point pImpossible = new Point(IMPOSSIBLE_POSITION_X, IMPOSSIBLE_POSITION_Y);        
        if(Math.abs(pExpected.getX() - pCalculated.getX()) < delta &&
           Math.abs(pExpected.getY() - pCalculated.getY()) < delta &&
           !pCalculated.equals(pImpossible)) {
        	return true;
        } else {
        	return false;
        }
	}
	
	private Receiver getReceiver(int id) {
		for(int i = 0; i < receivers.size(); i++) {
			if(receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}
	
	private static boolean deleteDirectory(File path) {
		if(path.exists()) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
	
	private static boolean createDirectory(File path) {
		if(!path.exists()) {
			if(path.mkdir()) {
				return true;
			}
		}
		return false;
	}

}
