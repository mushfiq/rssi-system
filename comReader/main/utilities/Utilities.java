/*
 * 
 * 
 */
package utilities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;

import algorithm.PositionLocalizationAlgorithm;
import data.DataProcessor;
import data.Reading;

/**
 * Class with helper methods for various tasks.
 * 
 * @author Danilo
 */
public final class Utilities {

	/** The Constant RSSI_OFFSET. */
	private static final int RSSI_OFFSET = 77;
	/** Parameters used in formula for converting the RSSIdec value to RSSIdBm. */
	private static final int POSITIVE_NUMBER_LIMIT = 128;

	/** The Constant SUBTRAHEND. */
	private static final int SUBTRAHEND = 256;

	/** The Constant RADIX. */
	private static final int RADIX = 16;

	/** The Constant SIZE_OF_LOG_FILE. */
	private static final int SIZE_OF_LOG_FILE = 10485760; // 10MB

	/** The Constant NUMBER_OF_FILES_TO_WRITE_TO. */
	private static final int NUMBER_OF_FILES_TO_WRITE_TO = 1;

	/** The Constant PATH_TO_LOG_FILE. */
	private static final String PATH_TO_LOG_FILE = "comReader" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "log.log";

	/** The Constant CONFIGURATION_FILE_COMMENTS. */
	private static final String CONFIGURATION_FILE_COMMENTS = "Configuration file";

	/** The file handler. */
	private static FileHandler fileHandler;

	/** The logger. */
	private static Logger utilitiesLogger;

	/** The configuration file. */
	private static String pathToConfigurationFile = "comReader" + File.separator + "main" + File.separator
			+ "resources" + File.separator + "config.ini";

	/** The configuration file. */
	private static Properties configurationFile;

	/**
	 * All helper methods are static so there is no need for instantiation of this class. Therefore, the constructor is
	 * private.
	 */
	private Utilities() {

	}

	/**
	 * Convert RSSI decimal value to RSSI dBm.
	 * 
	 * @param rssiDecimalValue
	 *            <code>double</code> rssi decimal value
	 * @return <code>int</code> RSSI dBm value
	 */
	public static double convertRSSIDecToDbm(double rssiDecimalValue) {

		double rssiDbm = 0;

		if (rssiDecimalValue >= POSITIVE_NUMBER_LIMIT) {
			rssiDbm = (rssiDecimalValue - SUBTRAHEND) / 2 - RSSI_OFFSET;
		} else {
			rssiDbm = (rssiDecimalValue) / 2 - RSSI_OFFSET;
		}

		return rssiDbm;
	}

	/**
	 * Calculates average signal strength of a single <code>Reading</code>. Every receiver takes several samples of
	 * signal strength. These values need to be averaged.<br>
	 * <br>
	 * 
	 * For example, for the readings 32, 32 , 33, 32, result would be <b>32,25</b>
	 * 
	 * @param reading
	 *            <code>Reading</code>
	 * @return <code>double</code> average signal strength
	 * @see Reading
	 */
	public static double calculateReadingAverage(Reading reading) {

		if (reading == null) {
			return 0;
		}

		double result = 0;

		ArrayList<Double> signalStrengths = reading.getSignalStrengths();

		for (int i = 0; i < signalStrengths.size(); i++) {
			result += signalStrengths.get(i);
		}

		result = result / signalStrengths.size();

		return result;
	}

	/**
	 * Calculates batch signal averages. When reading from the COM port, during a single time interval (e.g. 250ms), for
	 * every <code>Receiver</code>, several signal strengths are obtained. These signal strengths are averaged before
	 * being passed to the <code>PositionLocalizationAlgorithm</code> (in <code>DataProcessor</code> class).
	 * 
	 * @param batch
	 *            <code>HashMap</code> of signal strengths for several watches and receivers
	 * @return <code>HashMap</code> with average signal strength for every watch and every receiver
	 * @see PositionLocalizationAlgorithm
	 * @see DataProcessor
	 */
	public static HashMap<Integer, HashMap<Integer, Double>> calculateBatchSignalAverages(ArrayList<Reading> batch) {
		HashMap<Integer, HashMap<Integer, ArrayList<Double>>> allData = new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>();
		int watchId = 0;
		int receiverId = 0;
		double averageStrengthValue = 0;
		ArrayList<Integer> watchIds = new ArrayList<Integer>();

		if (batch == null) {

			System.out.println("it is null");
		}
		// populate the three-dimensional HashMap with data
		for (Reading reading : batch) {

			watchId = reading.getWatchId();
			receiverId = reading.getReceiverId();
			averageStrengthValue = reading.getAverageStrengthValue();

			// add watchId to the list for later use
			if (!watchIds.contains(watchId)) {
				watchIds.add(watchId);
			}

			if (allData.get(watchId) == null) {
				allData.put(watchId, new HashMap<Integer, ArrayList<Double>>());
			}

			if (allData.get(watchId).get(receiverId) == null) {

				allData.get(watchId).put(receiverId, new ArrayList<Double>());
			}

			allData.get(watchId).get(receiverId).add(averageStrengthValue);

		}

		// calculate averages
		HashMap<Integer, HashMap<Integer, Double>> averagedAllData = new HashMap<Integer, HashMap<Integer, Double>>();

		int watchIdsSize = watchIds.size();
		for (int i = 0; i < watchIdsSize; i++) {

			HashMap<Integer, ArrayList<Double>> hashMap = allData.get(i);

			for (Map.Entry<Integer, ArrayList<Double>> entry : hashMap.entrySet()) {
				int receiverId2 = entry.getKey();

				if (averagedAllData.get(i) == null) {
					averagedAllData.put(i, new HashMap<Integer, Double>());
				}

				averagedAllData.get(i).put(receiverId2, calculateArrayListAverage(entry.getValue()));
			}
		}

		return averagedAllData;
	}

	/**
	 * Initializes <code>Logger</code> object for other classes. Loggers from all the classes should write into single
	 * file and in common format.
	 * 
	 * @param className
	 *            <code>String</code> name of the logger owner class
	 * @return Customized <code>Logger</code> instance
	 */
	public static Logger initializeLogger(String className) {

		Logger logger = Logger.getLogger(className);
		logger.setUseParentHandlers(false); // prevent logger from using default handlers, which include writing to the
											// console
		fileHandler = Utilities.getFileHandler();

		// Configures the logger with handler and formatter
		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);
		logger.addHandler(fileHandler);

		return logger;
	}

	/**
	 * Converts <code>Image</code> to <code>BufferedImage</code>.
	 * 
	 * @param img
	 *            <code>Image</code> to be converted
	 * @return <code>BufferedImage</code> instance
	 */
	public static BufferedImage convertImagetoBufferedImage(Image img) {
		if (img instanceof BufferedImage) { // image is already in appropriate format

			return (BufferedImage) img; // cast it to BufferedImage
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose(); // remove created helper graphics instance

		// Return the buffered image
		return bimage;
	}

	/**
	 * Helper method that calculates average value of signal strengths in the list.
	 * 
	 * @param list
	 *            <code>List</code> with signal strengths
	 * @return <code>double</code> average value
	 */
	private static Double calculateArrayListAverage(ArrayList<Double> list) {

		double result = 0;

		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
		}

		result = result / list.size();

		return result;
	}

	/**
	 * Creates the <code>Reading</code> from a string obtained from COM port. It parses values from the string, converts
	 * them from RSSI to dBm and performs averaging of signal strengths. <br>
	 * <br>
	 * 
	 * If there is an exception during any of the above tasks, it returns an empty <code>Reading</code> object.
	 * 
	 * @param line
	 *            <code>String</code> single line from COM port
	 * @return <code>Reading</code> object
	 */
	public static Reading createReading(String line) {

		double signalStrength1 = 0;
		double signalStrength2 = 0;
		double signalStrength3 = 0;
		double signalStrength4 = 0;
		Reading reading = new Reading();

		try {

			StringTokenizer tokenizer = new StringTokenizer(line);

			// REP
			tokenizer.nextToken();
			// 2
			tokenizer.nextToken();
			// 3
			tokenizer.nextToken();
			// 4
			tokenizer.nextToken();
			// 5
			tokenizer.nextToken();
			// 6
			tokenizer.nextToken();
			// 7
			tokenizer.nextToken();
			// 8
			int receiverId = Integer.parseInt(tokenizer.nextToken());
			// 9
			tokenizer.nextToken();
			// 10
			tokenizer.nextToken();
			// 11
			tokenizer.nextToken();
			// 12
			tokenizer.nextToken();
			// 13
			tokenizer.nextToken();
			// 14
			signalStrength1 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength1 = Utilities.convertRSSIDecToDbm(signalStrength1);
			// 15
			signalStrength2 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength2 = Utilities.convertRSSIDecToDbm(signalStrength2);
			// 16
			signalStrength3 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength3 = Utilities.convertRSSIDecToDbm(signalStrength3);
			// 17
			signalStrength4 = Integer.parseInt(tokenizer.nextToken(), RADIX);
			signalStrength4 = Utilities.convertRSSIDecToDbm(signalStrength4);
			// 18
			tokenizer.nextToken();
			// 19
			tokenizer.nextToken();

			ArrayList<Double> signalStrengths = new ArrayList<Double>();
			signalStrengths.add(signalStrength1);
			signalStrengths.add(signalStrength2);
			signalStrengths.add(signalStrength3);
			signalStrengths.add(signalStrength4);

			reading = new Reading(receiverId, 0, signalStrengths);

		} catch (NumberFormatException exception) {
			getLogger().warning("Parsing data from COM port failed. Empty reading will be returned.");
		}

		return reading;
	}

	/**
	 * Returns the <code>FileHandler</code> for log file. <code>FileHandler</code> contains information about path to
	 * log file, size of log file, number of files to write to and append flag.
	 * 
	 * @return Configured <code>FileHandler</code>
	 */
	private static FileHandler getFileHandler() {

		if (fileHandler == null) {
			try {
				fileHandler = new FileHandler(PATH_TO_LOG_FILE, SIZE_OF_LOG_FILE, NUMBER_OF_FILES_TO_WRITE_TO, true);
			} catch (SecurityException e) {
				// error cannot be logged, since this method is used to initialize logger
				e.printStackTrace();
			} catch (IOException e) {
				// error cannot be logged, since this method is used to initialize logger
				e.printStackTrace();
			}
		}

		return fileHandler;

	}

	/**
	 * Used for accessing the <code>Logger</code> from the <code>Utilities</code> class.
	 * 
	 * @return <code>Logger</code> instance
	 */
	private static Logger getLogger() {

		if (utilitiesLogger == null) {
			utilitiesLogger = Utilities.initializeLogger(Utilities.class.getName());
		}

		return utilitiesLogger;
	}

	/**
	 * Scales <code>Image</code> to fit container.
	 * 
	 * @param image
	 *            <code>BufferedImage</code> image to be scaled
	 * @param containerWidth
	 *            <code>int</code> container width
	 * @param containerHeight
	 *            <code>int</code> container height
	 * @return <code>BufferedImage</code> scaled image
	 */
	public static BufferedImage scaleImageToFitContainer(BufferedImage image, int containerWidth, int containerHeight) {
		// FIXME this method could probably call getScalingRatioToFitContainer() method and thus make this much shorter
		// original image dimensions in pixels
		double imageWidthInPixels = image.getWidth();
		double imageHeightInPixels = image.getHeight();

		// scaling ratios, if needed. Resize ratio is the smaller value between widthRatio and heightRatio
		double widthRatio = 0;
		double heightRatio = 0;
		double resizeRatio = 0;

		// if image is resized, these will be its new dimensions
		double newImageWidthInPixels = 0;
		double newImageHeightInPixels = 0;

		BufferedImage resultImage = image;

		if (imageWidthInPixels >= imageHeightInPixels) {

			if (imageWidthInPixels <= containerWidth && imageHeightInPixels <= containerHeight) {

				// do nothing, no resizing needed

			} else { // resizing iz required

				widthRatio = containerWidth / imageWidthInPixels;
				heightRatio = containerHeight / imageHeightInPixels;
			}

		} else { // imageWidthInPixels < imageHeightInPixels

			if (imageHeightInPixels <= containerHeight && imageWidthInPixels <= imageWidthInPixels) {

				// no resizing required

			} else { // resizing is required

				widthRatio = containerWidth / imageWidthInPixels;
				heightRatio = containerHeight / imageHeightInPixels;
			}
		}

		if (widthRatio != 0 || heightRatio != 0) { // image should be resized

			resizeRatio = Math.min(widthRatio, heightRatio);

			newImageHeightInPixels = imageHeightInPixels * resizeRatio;
			newImageWidthInPixels = imageWidthInPixels * resizeRatio;

			resultImage = Utilities.convertImagetoBufferedImage(image.getScaledInstance((int) newImageWidthInPixels,
				(int) newImageHeightInPixels, Image.SCALE_SMOOTH));
		}

		return resultImage;
	}

	/**
	 * Gets the scaling ratio to fit container.
	 * 
	 * @param image
	 *            <code>Image</code> image
	 * @param containerWidth
	 *            <code>int</code> container width
	 * @param containerHeight
	 *            <code>int</code> container height
	 * @return <code>double</code> scaling ratio to fit container
	 */
	public static double getScalingRatioToFitContainer(BufferedImage image, int containerWidth, int containerHeight) {

		// original image dimensions in pixels
		double imageWidthInPixels = image.getWidth();
		double imageHeightInPixels = image.getHeight();

		// scaling ratios, if needed. Resize ratio is the smaller value between widthRatio and heightRatio
		double widthRatio = 0;
		double heightRatio = 0;
		double resizeRatio = 1;

		if (imageWidthInPixels >= imageHeightInPixels) {

			if (imageWidthInPixels <= containerWidth && imageHeightInPixels <= containerHeight) {

				// do nothing, no resizing needed

			} else { // resizing iz required

				widthRatio = containerWidth / imageWidthInPixels;
				heightRatio = containerHeight / imageHeightInPixels;
			}

		} else { // imageWidthInPixels < imageHeightInPixels

			if (imageHeightInPixels <= containerHeight && imageWidthInPixels <= imageWidthInPixels) {

				// no resizing required

			} else { // resizing is required

				widthRatio = containerWidth / imageWidthInPixels;
				heightRatio = containerHeight / imageHeightInPixels;
			}
		}

		if (widthRatio != 0 || heightRatio != 0) { // image should be resized

			resizeRatio = Math.min(widthRatio, heightRatio);

		}

		return resizeRatio;
	}

	/**
	 * Loads image from a given path.
	 * 
	 * @param relativePath
	 *            <code>String</code> relative path
	 * @return <code>Image</code> object
	 */
	public static Image loadImage(String relativePath) {

		String path = Utilities.class.getResource(relativePath).getPath();
		Image myPicture = null;
		try {
			myPicture = ImageIO.read(new File(path));
		} catch (IOException e) {

			utilitiesLogger.warning("Image could not be loaded. Please check path to image.");
		}

		return myPicture;
	}

	/**
	 * Creates the thumbnail image for container.
	 * 
	 * @param image
	 *            <code>Image</code> image to be scaled
	 * @param containerWidth
	 *            <code>int</code> container width
	 * @param containerHeight
	 *            <code>int</code> container height
	 * @return <code>BufferedImage</code> thumbnail
	 */
	public static BufferedImage createThumbnailImageForContainer(BufferedImage image, int containerWidth,
			int containerHeight) {

		// original image dimensions in pixels
		double imageWidthInPixels = image.getWidth();
		double imageHeightInPixels = image.getHeight();

		// scaling ratios, if needed. Resize ratio is the smaller value between widthRatio and heightRatio
		double widthRatio = 0;
		double heightRatio = 0;
		double resizeRatio = 0;

		// if image is resized, these will be its new dimensions
		double newImageWidthInPixels = 0;
		double newImageHeightInPixels = 0;

		BufferedImage resultImage = image;

		if (imageWidthInPixels >= imageHeightInPixels) {

			// resizing iz required
			widthRatio = containerWidth / imageWidthInPixels;
			heightRatio = containerHeight / imageHeightInPixels;

		} else { // imageWidthInPixels < imageHeightInPixels

			widthRatio = containerWidth / imageWidthInPixels;
			heightRatio = containerHeight / imageHeightInPixels;
		}

		if (widthRatio != 0 || heightRatio != 0) { // image should be resized

			resizeRatio = Math.min(widthRatio, heightRatio);

			newImageHeightInPixels = imageHeightInPixels * resizeRatio;
			newImageWidthInPixels = imageWidthInPixels * resizeRatio;

			resultImage = Utilities.convertImagetoBufferedImage(image.getScaledInstance((int) newImageWidthInPixels,
				(int) newImageHeightInPixels, Image.SCALE_SMOOTH));
		}

		return resultImage;
	}

	/**
	 * Loads configuration file.
	 */
	private static void loadConfigurationFile() {
		FileInputStream stream = null;
		configurationFile = new Properties();
		try {
			stream = new FileInputStream(pathToConfigurationFile);
			configurationFile.load(new FileInputStream(pathToConfigurationFile));
			stream.close();
		} catch (IOException e) {
			utilitiesLogger.log(Level.SEVERE, "Couldn't open configuration file.\n" + e.getMessage());
		}
	}

	/**
	 * Helper method that returns the value from 'config.ini' file for a given parameter.
	 * 
	 * @param key
	 *            the key
	 * @return value for given parameter
	 */
	public static String getConfigurationValue(String key) {

		loadConfigurationFile();

		String configurationValue = configurationFile.getProperty(key);

		return (configurationValue != null) ? configurationValue : "";
	}

	/**
	 * Helper method that returns the value from 'config.ini' file for a given parameter. It also checks if this value
	 * can be parsed into a boolean. If not it throws an exception.
	 * 
	 * @param key
	 *            the key
	 * @return value for given parameter
	 */
	public static String getBooleanConfigurationValue(String key) throws Exception {
		loadConfigurationFile();

		String configurationValue = configurationFile.getProperty(key);
		if (!configurationValue.equals("true") && !configurationValue.equals("false")) {
			throw new Exception();
		}

		return configurationValue;
	}

	/**
	 * Sets the configuration value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public static void setConfigurationValue(String key, String value) {
		configurationFile.setProperty(key, value);
		storeConfigurationFile();
	}

	/**
	 * Stores configuration file.
	 */
	private static void storeConfigurationFile() {

		FileOutputStream stream = null;
		configurationFile = new Properties();
		try {
			stream = new FileOutputStream(pathToConfigurationFile);
			configurationFile.store(new FileOutputStream(pathToConfigurationFile), CONFIGURATION_FILE_COMMENTS);
			stream.close();
		} catch (IOException e) {
			utilitiesLogger.log(Level.SEVERE, "Couldn't open configuration file.\n" + e.getMessage());
		}
	}

	public static BufferedImage deepCopy(BufferedImage bi) {

		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static byte[] LoadImageAsBytes(String filePath) throws Exception {
		String path = filePath;
		if(!(new File(filePath).exists())) {
			path = Utilities.class.getResource(filePath).getPath();
		}
		File file = new File(path);
		int size = (int) file.length();
		byte[] buffer = new byte[size];
		FileInputStream in = new FileInputStream(file);
		in.read(buffer);
		in.close();
		return buffer;
	}
}
