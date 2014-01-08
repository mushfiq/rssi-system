package algorithm;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;


import algorithm.helper.Point;

import components.Receiver;
import components.RoomMap;


/**
 * The Class ProximityAlgorithmTest. This test is testing the functional principle of the 
 * {@link ProximityAlgorithm}. 
 * 
 * @version 1.1 21 Dec 2013
 * @author Yentran Tran
 */
public class ProximityAlgorithmTest {

	/** Main path of the tests. */
	private static final String FOLDER_PROXIMITY = "comReader\\JUnitTests\\algorithm\\ProximityAlgorithm\\";
	
	/** Additional path for the test with one receiver. */
	private static final String FOLDER_STRONGESTRECEIVER = FOLDER_PROXIMITY + "testOneReceiver\\";
	
	/** Additional path for the weight function test. */
	private static final String FOLDER_WEIGHTFUNCTION = FOLDER_PROXIMITY + "testWeightFunction\\";
	

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
	private static final int RECEIVERID_12 = 12; // To simulate a receiver that has a signal but is not configured in the algorithm
	
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
	
	
	private static final double RECEIVERPOSITION_MINUS25 = -25.0;
	private static final double RECEIVERPOSITION_PLUS25 = +25.0;
	private static final double RECEIVERPOSITION_MINUS5 = -5.0;
	private static final double RECEIVERPOSITION_PLUS5 = +5.0;
	private static final double RECEIVERPOSITION_ZERO = 0.0;
	
	private static final double RECEIVERANGLE_ZERO = 0.0;
//	private static final double RECEIVERANGLE_PLUS45 = +45.0;
//	private static final double RECEIVERANGLE_MINUS45 = -45.0;
//	private static final double RECEIVERANGLE_PLUS90 = +90.0;
//	private static final double RECEIVERANGLE_MINUS90 = -90.0;
//	private static final double RECEIVERANGLE_PLUS225 = +225.0;
//	
//	private static final double PROBMAP_SIGNALPROPAGATIONCONSTANT = 3.0;
//	private static final double PROBMAP_SIGNALSTRENGTHONEMETER = 51.0;
//	private static final double PROBMAP_XFROM = -20.0;
//	private static final double PROBMAP_XTO = 20.0;
//	private static final double PROBMAP_YFROM = -20.0;
//	private static final double PROBMAP_YTO = 20.0;
//	private static final double PROBMAP_GRANULARITY = 0.25;
//	private static final double PROBMAP_LENGTHHALFAXISX100 = 1.0;
//	private static final double PROBMAP_LENGTHHALFAXISY075 = 0.75;
//	private static final double PROBMAP_LENGTHHALFAXISY025 = 0.25;
	
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
            
        
//        deleteDirectory(new File(FOLDER_PROXIMITY));
//        
//        createDirectory(new File(FOLDER_PROXIMITY));
//        createDirectory(new File(FOLDER_STRONGESTRECEIVER));
       
	}

	/**
	 * Case 1: Exactly one receiver has the strongest signal 
	 * This test is testing the results of the method {@link ProximityAlgorithm#giveStrongestSignal (HashMap)} 
	 * which should return the position of the receiver with the strongest signal
	 */
//	@Ignore
	@Test
	public void testCalculateStrongestSignal() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		
		ProximityAlgorithm algorithm = 
				new ProximityAlgorithm(roommap,receivers);
       
		
		// -- start test01 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_0, -60.0);
        readings.put(RECEIVERID_1, -30.0);
        readings.put(RECEIVERID_2, -10.0);
        readings.put(RECEIVERID_3, -50.0);
        readings.put(RECEIVERID_4, -23.0);

        pExpected = new Point(getReceiver(RECEIVERID_2).getXPos(), getReceiver(RECEIVERID_2).getYPos());
        System.out.println(pExpected.x+";"+pExpected.y);
        pCalculated = algorithm.calculate(readings);
//        assertTrue(testPoints(pCalculated, pExpected));
        assertEquals(pExpected.x, pCalculated.x, 0);
        assertEquals(pExpected.y, pCalculated.y,0);
        
        // -- ende test01 --
        
        
	}
	
	/**
	 * Case 2: three receivers have the same strongest signal. The first position of all receivers with the same signal strength should return.
	 * This test is testing the results of the method {@link ProximityAlgorithm#giveStrongestSignal (HashMap)} 
	 * which should return the position of the receiver with the strongest signal. 
	 * 
	 */
//	@Ignore
	@Test
	public void testCalculateStrongestSignal2() {
		HashMap<Integer, Double> readings;
		Point pExpected;
		Point pCalculated;
		
		ProximityAlgorithm algorithm = 
				new ProximityAlgorithm(roommap,receivers);
       
		
		// -- start test2 --
		readings = new HashMap<Integer, Double>();
        readings.put(RECEIVERID_0, -10.0);
        readings.put(RECEIVERID_1, -30.0);
        readings.put(RECEIVERID_2, -10.0);
        readings.put(RECEIVERID_3, -65.0);
        readings.put(RECEIVERID_4, -23.0);
        readings.put(RECEIVERID_5, -65.0);
        readings.put(RECEIVERID_6, -10.0);

        pExpected = new Point(getReceiver(RECEIVERID_0).getXPos(), getReceiver(RECEIVERID_0).getYPos());
        System.out.println(pExpected.x+";"+pExpected.y);
        pCalculated = algorithm.calculate(readings);
//        assertTrue(testPoints(pCalculated, pExpected));
        assertEquals(pExpected.x, pCalculated.x, 0);
        assertEquals(pExpected.y, pCalculated.y,0);
        
        // -- ende test2 --
        
        
	}


	
	/**
	 * Case 3: Not the position with the strongest signal is return. Expected an exception
	 * This test is testing the results of the method {@link ProximityAlgorithm#giveStrongestSignal (HashMap)} 
	 * which should return the position of the receiver with the strongest signal. 
	 */
//	@Ignore
	@Test
	public void testCalculateStrongestSignal3(){
				HashMap<Integer, Double> readings;
	Point pExpected;
	Point pCalculated;
	
	ProximityAlgorithm algorithm = 
			new ProximityAlgorithm(roommap,receivers);
   
	
	// -- start test01 --
	readings = new HashMap<Integer, Double>();
    readings.put(RECEIVERID_0, -100.0);
    readings.put(RECEIVERID_1, null);
    readings.put(RECEIVERID_2, null);
    readings.put(RECEIVERID_3, null);
    readings.put(RECEIVERID_4, null);

    pExpected = null;

    pCalculated = algorithm.calculate(readings);
    System.out.println("Calc "+pCalculated);
//    assertTrue(testPoints(pCalculated, pExpected));
    assertEquals(pExpected, pCalculated);
    
        // -- ende test2 --
	
	}


	/**
	 * Compares two points.
	 * 
	 * @param pCalculated a point to test
	 * @param pExpected a point to test
	 * @return true if the points are equal, false otherwise
	 */
	public Boolean testPoints(Point pCalculated, Point pExpected) {
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
