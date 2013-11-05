package algorithm;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import algorithm.helper.GrahamScan;
import algorithm.helper.Line;
import algorithm.helper.Point;
import algorithm.helper.Point_ProbabilityMap;
import algorithm.helper.Point_RoomMap;
import algorithm.probabilityMap.ProbabilityMap_Empiric;
import algorithm.stubs.Receiver;
import algorithm.stubs.RoomMap;


// TODO: Auto-generated Javadoc
/**
 * The Class ProbabilityBasedAlgorithm.
 */
public class ProbabilityBasedAlgorithm extends PositionLocalizationAlgorithm {

	/** The pic counter. */
	private static int picCounter = 0;
	
	/** The points_room map. */
	private ArrayList<Point_RoomMap> points_roomMap;
	
	/** The receivers. */
	private ArrayList<Receiver> receivers;
	
	/** The points_probability map. */
	private ArrayList<Point_ProbabilityMap> points_probabilityMap;
	
	/** The roommap. */
	private RoomMap roommap;
	
	/** The Constant GRANULARITY_PROBMAP. */
	private static final double GRANULARITY_PROBMAP = 0.25;
	
	/** The Constant GRANULARITY_ROOMMAP. */
	private static final double GRANULARITY_ROOMMAP = 0.25;
	
	private static final double GRAYSCALE_IMAGE_START = 100.0;
	private static final double GRAYSCALE_IMAGE_END = 200.0;
	private static final int GRAYSCALE_IMAGE_BLACK = 0;
	private static final int GRAYSCALE_IMAGE_WHITE = 255;
	
	/**
	 * Instantiates a new probability based algorithm.
	 *
	 * @param roommap the roommap
	 * @param receivers the receivers
	 */
	public ProbabilityBasedAlgorithm(RoomMap roommap, ArrayList<Receiver> receivers) {
		super(roommap, receivers);

		points_probabilityMap = new ProbabilityMap_Empiric(3, 40).getProbabilityMap(-25.0, 25.0, -25.0, 25.0, GRANULARITY_PROBMAP);
		
		this.receivers = receivers;
		this.roommap = roommap;
	}
	
	/* (non-Javadoc)
	 * @see algorithm.PositionLocalizationAlgorithm#calculate(java.util.HashMap)
	 */
	@Override
	public Point calculate(HashMap<Integer, Double> readings) {
		this.points_roomMap = createRoomMap(this.roommap.getFromX(), this.roommap.getToX(),
											this.roommap.getFromY(), this.roommap.getToY(),
											GRANULARITY_ROOMMAP);
		
		// go through each receiver and calculate a weighted map
		for (Map.Entry<Integer, Double> e : readings.entrySet()) {
			
			// find the points in the probability map where the rssi value is below the given value
			ArrayList<Point_ProbabilityMap> new_points_probabilityMap = 
					findValuesBelowRssi(points_probabilityMap, e.getValue());

			// calculate the convex hull of the probability map with the rssi values	
			GrahamScan c2 = new GrahamScan();
			ArrayList<Point_ProbabilityMap> convexHull = 
					c2.computeHull(new_points_probabilityMap);
	
			// transform the convex hull
			ArrayList<Point_ProbabilityMap> convexHullTransformed;
			Receiver receiver = getReceiver(receivers, e.getKey());
			if(receiver != null) {
				convexHullTransformed = transformation(convexHull, receiver.getAngle(), receiver.getXPos(), receiver.getYPos());
			} else {
				return null;
			}
			
			newGrayScaleImageConvexHull(convexHullTransformed, "ConvexHull" + e.getKey());
			
			
			// Test each point from room map if it lies in the transformed convex hull
			// and weight each point
			weightRoomMap(this.points_roomMap, convexHullTransformed);
		}
		// Find points with the highest weighted value
		ArrayList<Point_RoomMap> highestPoints_RoomMap = giveMaxWeightedValue(this.points_roomMap);
					
		// calculate point
		Point p = getPosition(highestPoints_RoomMap);
		System.out.println(p);
		
		newGrayScaleImage(this.points_roomMap, p, this.receivers, "Result" + picCounter);
		picCounter++;
		
		return p;
	}
	
	/**
	 * Find values below rssi.
	 *
	 * @param probabilityMap the probability map
	 * @param rssi the rssi
	 * @return the array list
	 */
	private ArrayList<Point_ProbabilityMap> findValuesBelowRssi(ArrayList<Point_ProbabilityMap> probabilityMap, double rssi) {
		ArrayList<Point_ProbabilityMap> pMap = new ArrayList<Point_ProbabilityMap>();
		
		for(int i = 0; i < probabilityMap.size(); i++) {
			if(probabilityMap.get(i).rssi_value >= rssi) {
				pMap.add(probabilityMap.get(i));
			}
		}
		return pMap;
	}
	
	// This method creates a map of the room
	/**
	 * Creates the room map.
	 *
	 * @param lengthXFrom the length x from
	 * @param lengthXTo the length x to
	 * @param lengthYFrom the length y from
	 * @param lengthYTo the length y to
	 * @param granularity the granularity
	 * @return the array list
	 */
	private ArrayList<Point_RoomMap> createRoomMap(double lengthXFrom, double lengthXTo, 
												   double lengthYFrom, double lengthYTo, 
												   double granularity) {
		
		ArrayList<Point_RoomMap> roomMap = new ArrayList<Point_RoomMap>();
		
		for(double i = lengthXFrom; i <= lengthXTo; i+=granularity) { // x-Axe
			for(double j = lengthYFrom; j <= lengthYTo; j+=granularity) { // y-Axe
				roomMap.add(new Point_RoomMap(i, j));
			}
		}
		
		return roomMap;
	}
		
	/**
	 * Transformation.
	 *
	 * @param p the p
	 * @param angle the angle
	 * @param receiverXpos the receiver xpos
	 * @param receiverYpos the receiver ypos
	 * @return the array list
	 */
	private ArrayList<Point_ProbabilityMap> transformation (ArrayList<Point_ProbabilityMap> p, double angle, double receiverXpos, double receiverYpos ) {
		
		//Define a new List with the transformated coordindates of the Probability Map
		ArrayList<Point_ProbabilityMap> transformation_probMap = new ArrayList<Point_ProbabilityMap>();
		
		//Rotation the coordination of the Probability Map
		for (int i = 0; i < p.size(); i++) {
			
			double xRotation = Math.cos(angle) * p.get(i).x - Math.sin(angle) * p.get(i).y;
			double yRotation = Math.sin(angle) * p.get(i).x + Math.cos(angle) * p.get(i).y;
			transformation_probMap.add(new Point_ProbabilityMap(xRotation,yRotation,p.get(i).rssi_value));
			
		}
		
		//Translation the coordination of the Probability Map
		for (int i = 0; i < p.size(); i++) {
			transformation_probMap.get(i).x = transformation_probMap.get(i).x + receiverXpos; 
			transformation_probMap.get(i).y = transformation_probMap.get(i).y + receiverYpos;
		}
		return transformation_probMap;
	}
	
	/**
	 * Weight room map.
	 *
	 * @param roomMap the room map
	 * @param convexHull the convex hull
	 */
	private void weightRoomMap(ArrayList<Point_RoomMap> roomMap, ArrayList<Point_ProbabilityMap> convexHull) {
		for(int i = 0; i < roomMap.size(); i++) {
			if(liesPointInConvexHull(roomMap.get(i), convexHull)) {
				roomMap.get(i).setNewWeightValue(roomMap.get(i).getWeightValue() * 5.0);
			}
		}
	}
	
	/**
	 * Lies point in convex hull.
	 *
	 * @param pRoomMap the room map
	 * @param convexHull the convex hull
	 * @return true, if successful
	 */
	private boolean liesPointInConvexHull(Point_RoomMap pRoomMap, ArrayList<Point_ProbabilityMap> convexHull) {
		// go along convex hull
		int size = convexHull.size();
		for(int i = 0; i < size; i++) {
			Point p1 = convexHull.get(i%size);
			Point p2 = convexHull.get((i+1)%size);
			Line l = new Line(p1, p2);
			
			if(pRoomMap.isRightOf(l)) {
				return false;
			}
		}		
		return true;
	}
	
	//This method return the points with the highest weight
	/**
	 * Give max weighted value.
	 *
	 * @param roomMap the room map
	 * @return the array list
	 */
	private ArrayList<Point_RoomMap> giveMaxWeightedValue(ArrayList<Point_RoomMap> roomMap) {
		
		ArrayList<Point_RoomMap> maxWeightedValue = new ArrayList<Point_RoomMap>();
		double maxValue = -1000.0;
		
		for (int i = 0; i < roomMap.size(); i++) {
			if(roomMap.get(i).getWeightValue() > maxValue) {
				maxValue = roomMap.get(i).getWeightValue();
				maxWeightedValue.clear();
				maxWeightedValue.add(roomMap.get(i));
				
			} else if(roomMap.get(i).getWeightValue() == maxValue) {
				maxWeightedValue.add(roomMap.get(i));
			}		
		}
		return maxWeightedValue;
	}
	
	// This method calculates the average point of the suitable points
	/**
	 * Gets the position.
	 *
	 * @param roomMap the room map
	 * @return the position
	 */
	private Point getPosition(ArrayList<Point_RoomMap> roomMap) {
		
		double sumX = 0, sumY = 0;
		for (int i = 0; i < roomMap.size(); i++) {
			sumX += roomMap.get(i).x;
			sumY += roomMap.get(i).y;
		}
		
		double x = sumX/roomMap.size();
		double y = sumY/roomMap.size();
		
		return new Point(x,y);
	}
	
	/**
	 * Gets the receiver.
	 *
	 * @param receivers the receivers
	 * @param id the id
	 * @return the receiver
	 */
	private Receiver getReceiver(ArrayList<Receiver> receivers, int id) {
		for(int i = 0; i < receivers.size(); i++) {
			if(receivers.get(i).getID() == id) {
				return receivers.get(i);
			}
		}
		return null;
	}
	
	/**
	 * New gray scale image.
	 *
	 * @param pointsRoomMap the points
	 * @param pointCalculatedPosition the p
	 * @param picName the pic name
	 */
	private void newGrayScaleImage(ArrayList<Point_RoomMap> pointsRoomMap, Point pointCalculatedPosition, ArrayList<Receiver> receivers, String picName) {
		double smallestWeightedValue = 1.0; 	// smallest weighted value in room map
		double highestWeightedValue = 0.0; 		// highest weighted value in room map
		
		double smallestX = pointsRoomMap.get(0).x;		// smallest position in x in room map
		double smallestY = pointsRoomMap.get(0).y;		// smallest position in y in room map
		
		double highestX = pointsRoomMap.get(0).x;		// highest position in x in room map
		double highestY = pointsRoomMap.get(0).y;		// highest position in y in room map
		
		for(int i = 1; i < pointsRoomMap.size(); i++) {
			if(pointsRoomMap.get(i).x < smallestX) {
				smallestX = pointsRoomMap.get(i).x;
			}
			if(pointsRoomMap.get(i).y < smallestY) {
				smallestY = pointsRoomMap.get(i).y;
			}
			if(pointsRoomMap.get(i).x > highestX) {
				highestX = pointsRoomMap.get(i).x;
			}
			if(pointsRoomMap.get(i).y > highestY) {
				highestY = pointsRoomMap.get(i).y;
			}
			
			if(pointsRoomMap.get(i).getWeightValue() > highestWeightedValue) {
				highestWeightedValue = pointsRoomMap.get(i).getWeightValue();
			}
		}
		
		double factor = 1.0 / GRANULARITY_ROOMMAP;	// determine the factor that is needed to create the picture
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor); // determine the length for the needed picture in x 
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor); // determine the length for the needed picture in y
		
		// create point of origin (for the transformation of each point, here just y value is needed). The coordinates of this point are related to the "old" CS
		Point pointOfOriginNewCoordinateSystem = new Point(0, imageLenghtY);
		
		BufferedImage theImage = new BufferedImage(imageLenghtX + 1, imageLenghtY + 1, BufferedImage.TYPE_BYTE_GRAY); // +1, because there is the need of "0"
	    for(int i = 0; i < pointsRoomMap.size(); i++) {
	    	
	    	int value = linearInterpolation(pointsRoomMap.get(i).getWeightValue(), smallestWeightedValue, highestWeightedValue, GRAYSCALE_IMAGE_START, GRAYSCALE_IMAGE_END);
	    	Color c = new Color(value, value, value);
	    	
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	Point pointAsWholeNumber = new Point((pointsRoomMap.get(i).x - smallestX) * factor, (pointsRoomMap.get(i).y - smallestY) * factor);
	    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	theImage.setRGB((int) Math.round(positionPointInImage.x), (int) Math.round(positionPointInImage.y), c.getRGB());
	    }
	    
	    // the position of the receivers will be displayed as a black pixel in the image
	    for(int i = 0; i < receivers.size(); i++) {
	    	Color c = new Color(GRAYSCALE_IMAGE_BLACK, GRAYSCALE_IMAGE_BLACK, GRAYSCALE_IMAGE_BLACK);
	    	
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	Point pointAsWholeNumber = new Point((receivers.get(i).getXPos() - smallestX) * factor, 
	    										 (receivers.get(i).getYPos() - smallestY) * factor);
	    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	theImage.setRGB((int) Math.round(positionPointInImage.x), (int) Math.round(positionPointInImage.y), c.getRGB());
	    	
	    }
	    
	    // The determined position found by the algorithm will be displayed in white
	    Color c = new Color(GRAYSCALE_IMAGE_WHITE, GRAYSCALE_IMAGE_WHITE, GRAYSCALE_IMAGE_WHITE);
	    Point pointAsWholeNumber = new Point((pointCalculatedPosition.x - smallestX) * factor, (pointCalculatedPosition.y - smallestY) * factor);
    	Point positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    theImage.setRGB((int) Math.round(positionPointInImage.x), (int) Math.round(positionPointInImage.y), c.getRGB());
	    
	    File outputfile = new File(picName + ".bmp");
	    try {
			ImageIO.write(theImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * New gray scale image convex hull.
	 *
	 * @param points the points
	 * @param picName the pic name
	 */
	private void newGrayScaleImageConvexHull(ArrayList<Point_ProbabilityMap> points, String picName) {
		double smallestX = points.get(0).x;
		double smallestY = points.get(0).y;
		
		double highestX = points.get(0).x;
		double highestY = points.get(0).y;
		
		for(int i = 1; i < points.size(); i++) {
			if(points.get(i).x < smallestX) {
				smallestX = points.get(i).x;
			}
			if(points.get(i).y < smallestY) {
				smallestY = points.get(i).y;
			}
			if(points.get(i).x > highestX) {
				highestX = points.get(i).x;
			}
			if(points.get(i).y > highestY) {
				highestY = points.get(i).y;
			}
		}
		
		double factor = 1.0 / GRANULARITY_PROBMAP;
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor) + 1;
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor) + 1;
		
		BufferedImage theImage = new BufferedImage(imageLenghtX, imageLenghtY, BufferedImage.TYPE_BYTE_GRAY);
	    for(int i = 0; i < points.size(); i++) {
	    	int value = 255;
	    	Color c = new Color(value, value, value);
	    	theImage.setRGB((int) Math.round((points.get(i).x - smallestX) * factor), 
							(int) Math.round((points.get(i).y - smallestY) * factor),
							c.getRGB());
	    }
	    File outputfile = new File(picName + ".bmp");
	    try {
			ImageIO.write(theImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// interval A: [a0|a1]
	// interval B: [b0|b1]
	// A -> B; [a0|a1] -> [b0->b1]
	private int linearInterpolation(double value, double intervalA0, double intervalA1, double intervalB0, double intervalB1) {
		double r = (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
		double s = intervalB0 - intervalA0 * (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
				
		int ret = (int) (r * value + s);
		
		return ret;
	}
	private Point pointToImageTransformation(Point pointToTransform, Point pointNewCS) {
		Point negPoint = pointNewCS.neg();
		
		double x = 1.0 * pointToTransform.x + /*0.0 * pointToTransform.y*/ + negPoint.x * 1;
		double y = /*0.0 * pointToTransform.x*/ - 1.0 * pointToTransform.y - negPoint.y * 1;
				
		return new Point(x, y);
	}
}
