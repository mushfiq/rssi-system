/*
 * File: GrayscaleImages.java
 * Date				Author				Changes
 * 08 Dec 2013		Tommy Griese		create version 1.0
 */
package algorithm.images;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import utilities.Utilities;
import algorithm.helper.Point;
import algorithm.helper.PointProbabilityMap;
import algorithm.helper.PointRoomMap;

import components.Receiver;
import components.RoomMap;

/**
 * The class GrayscaledImages can help you to visualize some information (depending on the method). 
 * 
 * @version 1.0 08 Dec 2013
 * @author Tommy Griese
 */
public class GrayscaleImages {
	
	/** The path for the grayscale image. */
	private String grayscaleImagePath;
	
	/** Defines the start intensity of the grayscale image. */
	public static final double GRAYSCALE_IMAGE_START = 75.0;
	
	/** Defines the end intensity of the grayscale image. */
	public static final double GRAYSCALE_IMAGE_END = 225.0;
	
	/** The color black. */
	public static final int GRAYSCALE_IMAGE_BLACK = 0;
	
	/** The color white. */
	public static final int GRAYSCALE_IMAGE_WHITE = 255;
	
	/** The color gray. */
	public static final int GRAYSCALE_IMAGE_GRAY = 105;
	
	/** The scale factor in x to increase the complete image. */
	public static final int GRAYSCALE_IMAGE_SCALE_X = 5;
	
	/** The scale factor in y to increase the complete image. */
	public static final int GRAYSCALE_IMAGE_SCALE_Y = 5;
	
	/** Counts each grayscale picture, so that a new picture gets a new number. */
	private int counter;
	
	/** The logger. */
	private Logger logger;
	
	/**
	 * Instantiates a new GrayscaleImages.
	 *
	 * @param path the path
	 */
	public GrayscaleImages(String path) {
		super();
		logger = Utilities.initializeLogger(this.getClass().getName());
		setPath(path);
	}
	
	/**
	 * Sets the image path for the grayscale images. It also sets the picture counter to one.
	 *
	 * @param path the new path for the grayscale images
	 */
	public void setPath(String path) {
		this.counter = 1;
		if (path.endsWith("\\")) {
			this.grayscaleImagePath = path;
		} else {
			this.grayscaleImagePath = path + "\\";
		}
	}
	
	/**
	 * Creates a new grayscale image. Depending on the parameter-list this method can display the weighted room map 
	 * information, the convex hulls of the receivers, the position of the receivers itself and the calculated
	 * point in one image.
	 *
	 * @param roommap the room map that should be displayed on the image
	 * @param showRoommap flag to enable/disable the information of the weighted room map
	 * @param convexhulls the convex hulls that should be displayed on the image
	 * @param showConvexhulls flag to enable/disable the information of the convex hulls
	 * @param receivers the receivers that should be displayed on the image
	 * @param showReceivers flag to enable/disable the information of the receivers
	 * @param pointCalculatedPosition the calculated position that should be displayed on the image
	 * @param showPoint flag to enable/disable the information of the calculated point
	 */
	public void newGrayScaleImage(RoomMap roommap, boolean showRoommap,
								  HashMap<Integer, ArrayList<PointProbabilityMap>> convexhulls, boolean showConvexhulls,
								  ArrayList<Receiver> receivers, boolean showReceivers,
								  Point pointCalculatedPosition, boolean showPoint) {
	
		ArrayList<PointRoomMap> roomMapPoints = roommap.getRoomMapPoints();
		
		double smallestWeightedValue = roomMapPoints.get(0).getWeightValue(); 	// smallest weighted value in room map
		double highestWeightedValue = roomMapPoints.get(0).getWeightValue(); 	// highest weighted value in room map
		
		double smallestX = roomMapPoints.get(0).getX();		// smallest position in x in room map
		double smallestY = roomMapPoints.get(0).getY();		// smallest position in y in room map
		
		double highestX = roomMapPoints.get(0).getX();		// highest position in x in room map
		double highestY = roomMapPoints.get(0).getY();		// highest position in y in room map
		
		for (int i = 1; i < roomMapPoints.size(); i++) {
			if (roomMapPoints.get(i).getX() < smallestX) {
				smallestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() < smallestY) {
				smallestY = roomMapPoints.get(i).getY();
			}
			if (roomMapPoints.get(i).getX() > highestX) {
				highestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() > highestY) {
				highestY = roomMapPoints.get(i).getY();
			}
			
			if (roomMapPoints.get(i).getWeightValue() > highestWeightedValue) {
				highestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
			if (roomMapPoints.get(i).getWeightValue() < smallestWeightedValue) {
				smallestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
		}
		
		double factor = 1.0 / roommap.getGranularity();	// determine the factor that is needed to create the picture
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor); // determine the length for the needed picture in x 
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor); // determine the length for the needed picture in y
		
		// create point of origin (for the transformation of each point, here just y value is needed). The coordinates of this point are related to the "old" CS
		Point pointOfOriginNewCoordinateSystem = new Point(0, imageLenghtY);
		
		Color cBlack = new Color(GrayscaleImages.GRAYSCALE_IMAGE_BLACK, GrayscaleImages.GRAYSCALE_IMAGE_BLACK, GrayscaleImages.GRAYSCALE_IMAGE_BLACK);
		Color cWhite = new Color(GrayscaleImages.GRAYSCALE_IMAGE_WHITE, GrayscaleImages.GRAYSCALE_IMAGE_WHITE, GrayscaleImages.GRAYSCALE_IMAGE_WHITE);
		Point pointAsWholeNumber = null;
		Point positionPointInImage = null;
		int x = -1;
		int y = -1;
		
		BufferedImage theImage = new BufferedImage(imageLenghtX + 1, imageLenghtY + 1, BufferedImage.TYPE_BYTE_GRAY); // +1, because there is the need of "0"
	    
	    if(showRoommap) {
	    	for (int i = 0; i < roomMapPoints.size(); i++) {
		    	
		    	int value = (int) linearInterpolation(roomMapPoints.get(i).getWeightValue(), smallestWeightedValue, highestWeightedValue, GRAYSCALE_IMAGE_START, GRAYSCALE_IMAGE_END);
		    	Color c = new Color(value, value, value);
		    	
		    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
		    	pointAsWholeNumber = new Point((roomMapPoints.get(i).getX() - smallestX) * factor, (roomMapPoints.get(i).getY() - smallestY) * factor);
		    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
		    	
		    	// rounding is needed because of the "rounding effects" by double calculation
		    	x = (int) Math.round(positionPointInImage.getX());
		    	y = (int) Math.round(positionPointInImage.getY());
		    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
		    		theImage.setRGB(x, y, c.getRGB());
		    	}
		    }
	    }
	    
		if(showConvexhulls) {
			for(Map.Entry<Integer, ArrayList<PointProbabilityMap>> e : convexhulls.entrySet()) {
				for (int i = 0; i < e.getValue().size(); i++) {
					pointAsWholeNumber = new Point((e.getValue().get(i).getX() - smallestX) * factor, (e.getValue().get(i).getY() - smallestY) * factor);
			    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
			    	
			    	// rounding is needed because of the "rounding effects" by double calculation
			    	x = (int) Math.round(positionPointInImage.getX());
			    	y = (int) Math.round(positionPointInImage.getY());
			    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
			    		theImage.setRGB(x, y, cWhite.getRGB());
			    	}
			    }
			}
		}
		
		if(showReceivers) {
			// the position of the receivers will be displayed as a black pixel in the image
		    for (int i = 0; i < receivers.size(); i++) {
		    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
		    	pointAsWholeNumber = new Point((receivers.get(i).getXPos() - smallestX) * factor, 
		    								   (receivers.get(i).getYPos() - smallestY) * factor);
		    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
		    	
		    	// rounding is needed because of the "rounding effects" by double calculation
		    	x = (int) Math.round(positionPointInImage.getX());
		    	y = (int) Math.round(positionPointInImage.getY());
		    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
		    		theImage.setRGB(x, y, cBlack.getRGB());
		    	}
		    }
		}
		   
		if(showPoint) {
		    // The determined position found by the algorithm will be displayed in black
		    pointAsWholeNumber = new Point((pointCalculatedPosition.getX() - smallestX) * factor, (pointCalculatedPosition.getY() - smallestY) * factor);
	    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	x = (int) Math.round(positionPointInImage.getX());
	    	y = (int) Math.round(positionPointInImage.getY());
	    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
	    		theImage.setRGB(x, y, cBlack.getRGB());
	    	}
		}
    		    
	    writeImage(theImage, GrayscaleImages.GRAYSCALE_IMAGE_SCALE_X, GrayscaleImages.GRAYSCALE_IMAGE_SCALE_Y);
	}
	
	/**
	 * Creates a new grayscale image. The dimensions of the image are the room map dimensions. The content of the
	 * image will be two sequences of points.
	 *
	 * @param roommap the room map to define the dimensions of the image
	 * @param rawPointsCalculated the first sequence of points that should be displayed (gray)
	 * @param filterPointsCalculated the second sequence of points that should be displayed (white)
	 */
	public void newGrayScaleImage(RoomMap roommap, ArrayList<Point> rawPointsCalculated, ArrayList<Point> filterPointsCalculated) {
		ArrayList<PointRoomMap> roomMapPoints = roommap.getRoomMapPoints();
		
		double smallestWeightedValue = roomMapPoints.get(0).getWeightValue(); 	// smallest weighted value in room map
		double highestWeightedValue = roomMapPoints.get(0).getWeightValue(); 		// highest weighted value in room map
		
		double smallestX = roomMapPoints.get(0).getX();		// smallest position in x in room map
		double smallestY = roomMapPoints.get(0).getY();		// smallest position in y in room map
		
		double highestX = roomMapPoints.get(0).getX();		// highest position in x in room map
		double highestY = roomMapPoints.get(0).getY();		// highest position in y in room map
		
		for (int i = 1; i < roomMapPoints.size(); i++) {
			if (roomMapPoints.get(i).getX() < smallestX) {
				smallestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() < smallestY) {
				smallestY = roomMapPoints.get(i).getY();
			}
			if (roomMapPoints.get(i).getX() > highestX) {
				highestX = roomMapPoints.get(i).getX();
			}
			if (roomMapPoints.get(i).getY() > highestY) {
				highestY = roomMapPoints.get(i).getY();
			}
			
			if (roomMapPoints.get(i).getWeightValue() > highestWeightedValue) {
				highestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
			if (roomMapPoints.get(i).getWeightValue() < smallestWeightedValue) {
				smallestWeightedValue = roomMapPoints.get(i).getWeightValue();
			}
		}
		
		double factor = 1.0 / roommap.getGranularity();	// determine the factor that is needed to create the picture
		
		int imageLenghtX = (int) Math.round((Math.abs(highestX - smallestX)) * factor); // determine the length for the needed picture in x 
		int imageLenghtY = (int) Math.round((Math.abs(highestY - smallestY)) * factor); // determine the length for the needed picture in y
		
		// create point of origin (for the transformation of each point, here just y value is needed). The coordinates of this point are related to the "old" CS
		Point pointOfOriginNewCoordinateSystem = new Point(0, imageLenghtY);
		
		Color cGray = new Color(GrayscaleImages.GRAYSCALE_IMAGE_GRAY, GrayscaleImages.GRAYSCALE_IMAGE_GRAY, GrayscaleImages.GRAYSCALE_IMAGE_GRAY);
		Color cWhite = new Color(GrayscaleImages.GRAYSCALE_IMAGE_WHITE, GrayscaleImages.GRAYSCALE_IMAGE_WHITE, GrayscaleImages.GRAYSCALE_IMAGE_WHITE);
		Point pointAsWholeNumber = null;
		Point positionPointInImage = null;
		int x = -1;
		int y = -1;
		
		BufferedImage theImage = new BufferedImage(imageLenghtX + 1, imageLenghtY + 1, BufferedImage.TYPE_BYTE_GRAY); // +1, because there is the need of "0"
		
		// the position of the receivers will be displayed as a black pixel in the image
	    for (int i = 0; i < rawPointsCalculated.size(); i++) {
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	pointAsWholeNumber = new Point((rawPointsCalculated.get(i).getX() - smallestX) * factor, 
	    										 (rawPointsCalculated.get(i).getY() - smallestY) * factor);
	    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	x = (int) Math.round(positionPointInImage.getX());
	    	y = (int) Math.round(positionPointInImage.getY());
	    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
	    		theImage.setRGB(x, y, cGray.getRGB());
	    	}
	    }
	    
	    for (int i = 0; i < filterPointsCalculated.size(); i++) {
		    	
	    	// an image (px) has no floating point number. Therefore a calculated integer has to determined (this is where the factor comes in action)
	    	pointAsWholeNumber = new Point((filterPointsCalculated.get(i).getX() - smallestX) * factor, (filterPointsCalculated.get(i).getY() - smallestY) * factor);
	    	positionPointInImage = pointToImageTransformation(pointAsWholeNumber, pointOfOriginNewCoordinateSystem);
	    	
	    	// rounding is needed because of the "rounding effects" by double calculation
	    	x = (int) Math.round(positionPointInImage.getX());
	    	y = (int) Math.round(positionPointInImage.getY());
	    	if(x >= 0 && x <= imageLenghtX && y >= 0 && y <= imageLenghtY) {
	    		theImage.setRGB(x, y, cWhite.getRGB());
	    	}
	    }
    		    
	    writeImage(theImage, GrayscaleImages.GRAYSCALE_IMAGE_SCALE_X, GrayscaleImages.GRAYSCALE_IMAGE_SCALE_Y);
	}
	
	/**
	 * Writes the image into a file. With the scaling it is possible to scale the dimensions of the image
	 * in x and y.
	 *
	 * @param theImage the BufferedImage
	 * @param scaleX the scaling in x
	 * @param scaleY the scaling in y
	 */
	private void writeImage(BufferedImage theImage, int scaleX, int scaleY) {
		AffineTransform tx = new AffineTransform();
	    tx.scale(scaleX, scaleY);

	    AffineTransformOp op = new AffineTransformOp(tx,
	        AffineTransformOp.TYPE_BILINEAR);
	    theImage = op.filter(theImage, null);
	    
	    File outputfile = new File(this.grayscaleImagePath + "grayscaleImage_pic" + this.counter + ".png");
	    try {
			ImageIO.write(theImage, "png", outputfile);
			this.counter++;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "[ProbabilityBasedAlgorithm - writeImage( ... )] Can't write debug image into file " + outputfile.getPath());
		}
	}
	
	/**
	 * Performs a linear interpolation for the given value regarding the two given intervals.
	 * <br><br>
	 * interval A: [a0|a1]<br>
	 * interval B: [b0|b1]<br>
	 * A -> B; [a0|a1] -> [b0->b1]<br>
	 * value should lie in interval A (there is no explicit testing)
	 *
	 * @param value the value to be interpolated
	 * @param intervalA0 the lower border of interval A
	 * @param intervalA1 the upper border of interval A
	 * @param intervalB0 the lower border of interval B
	 * @param intervalB1 the upper border of interval B
	 * @return the interpolated value
	 */
	private double linearInterpolation(double value, double intervalA0, double intervalA1, double intervalB0, double intervalB1) {
		// Check borders
		if(value == intervalA0) {
			return intervalB0;
		} else if(value == intervalA1) {
			return intervalB1;
		}
		double r = (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
		double s = intervalB0 - intervalA0 * (intervalB0 - intervalB1) / (intervalA0 - intervalA1);
		
		return (r * value + s);
	}
	
	/**
	 * Makes a transformation of a point from standard math coordinate system into image coordinate system.<br>
	 * (The point of origin of an image is located at the top left corner, the point of origin of a math coordinate
	 * system is located at the bottom left corner). The transformation results out of an horizontal flip and a
	 * transformation afterwards.
	 *
	 * @param pointToTransform the point to transform
	 * @param pointNewCS the point of the new coordinate system
	 * @return a new instance of the transformed point
	 */
	private Point pointToImageTransformation(Point pointToTransform, Point pointNewCS) {
		Point negPoint = pointNewCS.neg();
		
		double x = 1.0 * pointToTransform.getX() + /*0.0 * pointToTransform.y*/ +negPoint.getX() * 1;
		double y = /*0.0 * pointToTransform.x*/ -1.0 * pointToTransform.getY() - negPoint.getY() * 1;
				
		return new Point(x, y);
	}
}