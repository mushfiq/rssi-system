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

import algorithm.filter.KalmanFilterOneDim;
import algorithm.helper.Point;
import algorithm.images.GrayscaleImages;
import components.RoomMap;

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
	
	@Test
	public void testOutputGrayscaleImage() {
		KalmanFilterOneDim kf = new KalmanFilterOneDim();
		RoomMap rm = new RoomMap(0.0, 5.0, 0.0, 5.0, null);
		
		ArrayList<Point> rawPoints = new ArrayList<Point>();
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
		
		ArrayList<Point> filteredPoints = new ArrayList<Point>();
		for(int i = 0; i < rawPoints.size(); i++) {
			filteredPoints.add(kf.applyFilter(rawPoints.get(i)));
		}
		
		GrayscaleImages img = new GrayscaleImages(FOLDER_KFTEST);
		img.newGrayScaleImage(rm, rawPoints, filteredPoints);
		
		assertTrue(true);
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
