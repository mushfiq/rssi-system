/*
 * File: KalmanFilterTest.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create test file with just one test up to now to look if the output
 * 										of the grayscale image is right (testOutputGrayscaleImage)
 */
package algorithm;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import algorithm.filter.KalmanFilter;
import algorithm.helper.Point;
import algorithm.images.GrayscaleImages;
import components.RoomMap;
/**
 * /**
 * The Class KalmanFilterTest. This test is testing the functional principle of the 
 * {@link KalmanFilter}. For each test a sum of images is going to be created so that
 * it is easier to verify the correct results.
 * 
 * @version 1.1 21 Dec 2013
 * @author Yentran Tran
 *
 */
public class KalmanFilterTest {

	private static final String FOLDER_KALMANFILTER = "comReader\\JUnitTests\\algorithm\\kalmanfilter\\";
	private static final String FOLDER_KFTEST = FOLDER_KALMANFILTER + "testOutputGrayscaleImage\\";
	
	
	@BeforeClass
	public static void setUp() throws Exception {        
        deleteDirectory(new File(FOLDER_KALMANFILTER));
        
        createDirectory(new File(FOLDER_KALMANFILTER));
        createDirectory(new File(FOLDER_KFTEST));
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}
	
	@Before
	public void init() {
	}
	
	/**
	 * 
	 */
	@Test
	public void testOutputGrayscaleImage() {
		KalmanFilter kf = new KalmanFilter(0.125,0.8);
		KalmanFilter kf2 = new KalmanFilter(0.125,0.8);
		RoomMap rm = new RoomMap(0.0, 5.0, 0.0, 5.0, null);
		
		ArrayList<Point> rawPoints = new ArrayList<Point>();
		
		ArrayList<Point> rawPoints2 = new ArrayList<Point>();
		
		//Test case 1: walk path looks like the letter U
		rawPoints.add(new Point(0.0, 0.0));
		rawPoints.add(new Point(0.0, 1.0));
		rawPoints.add(new Point(0.0, 2.0));
		rawPoints.add(new Point(0.0, 3.0));
		rawPoints.add(new Point(0.0, 4.0));
		rawPoints.add(new Point(0.0, 5.0));
		rawPoints.add(new Point(1.0, 5.0));
		rawPoints.add(new Point(2.0, 5.0));
		rawPoints.add(new Point(3.0, 5.0));
		rawPoints.add(new Point(4.0, 5.0));
		rawPoints.add(new Point(5.0, 5.0));
		rawPoints.add(new Point(5.0, 4.0));
		rawPoints.add(new Point(5.0, 3.0));
		rawPoints.add(new Point(5.0, 2.0));
		rawPoints.add(new Point(5.0, 1.0));
		rawPoints.add(new Point(5.0, 0.0));
		
		// Test case 2: walk path has a zick zack form
		rawPoints2.add(new Point(0, 0));
		rawPoints2.add(new Point(1, 1));
		rawPoints2.add(new Point(2, 2));
		rawPoints2.add(new Point(3, 1));
		rawPoints2.add(new Point(4, 2));
		rawPoints2.add(new Point(5, 1));
		rawPoints2.add(new Point(6, 2));
		rawPoints2.add(new Point(7, 1));
		

		//Calculate the walking estimated points with the kalman filter
		ArrayList<Point> filteredPoints = new ArrayList<Point>();
		for(int i = 0; i < rawPoints.size(); i++) {
			filteredPoints.add(kf.applyFilter(rawPoints.get(i)));
			System.out.println(kf.applyFilter(rawPoints.get(i)));
		}
		
		ArrayList<Point> filteredPoints2 = new ArrayList<Point>();
		for(int i = 0; i < rawPoints2.size(); i++) {
			filteredPoints2.add(kf2.applyFilter(rawPoints2.get(i)));
			System.out.println(kf2.applyFilter(rawPoints2.get(i)));
		}
		
		
		//Draw the path of the calcutlated points
		GrayscaleImages img = new GrayscaleImages(FOLDER_KFTEST);
		img.newGrayScaleImage(rm, rawPoints, filteredPoints);
		img.newGrayScaleImage(rm, rawPoints2, filteredPoints2);
	
		
		assertTrue(true);
	}
	
	/**
	 * Deletes the structure below the given path and the folder itself.
	 * 
	 * @param path the path to delete
	 * @return true if and only if the file or directory is successfully deleted; false otherwise 
	 */
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
	
	/**
	 * Creates a new directory.
	 * 
	 * @param path the path where the directory should be created
	 * @return true if the directory was created, false otherwise
	 */
	private static boolean createDirectory(File path) {
		if(!path.exists()) {
			if(path.mkdir()) {
				return true;
			}
		}
		return false;
	}
}
