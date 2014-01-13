/*
 * 
 * 
 */
package dao;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import utilities.Utilities;
import components.Receiver;
import components.RoomMap;

/**
 * Implementation of <code>dao.MapDAO</code> interface. It retrieves map information from hard coded data, i.e. it has
 * no external data source. This class is used for testing purposes, when using an external data source is not
 * necessary, is quicker or when it is inappropriate (e.g. while testing methods that write do the data source).
 * 
 * @author Danilo
 * @see dao.MapDAO
 */
public class HardcodedMapDAO implements MapDAO {

	/** <code>Logger</code> object. */
	private Logger logger;

	private static final String DATABASE_ADDRESS = "database_address";

	private static final String DEFAULT_DATABASE_ADDRESS = "127.0.0.1";

	/**
	 * <code>List</code> of all <code>RoomMaps</code> in the data source.
	 * 
	 * @see components.RoomMap
	 * */
	private List<RoomMap> allMaps;

	/**
	 * Instantiates a new <code>HardcodedMapDAO</code> object. Although it is not implemented as a singleton (and
	 * therefore multiple instances can be created), only one instance should be used, since multiple instances will
	 * read/write from/to the same data source.
	 * 
	 */
	public HardcodedMapDAO() {
		logger = Utilities.initializeLogger(this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#getAllMaps()
	 */
	@Override
	public List<RoomMap> getAllMaps() {

		if (allMaps != null) {
			return allMaps;
		} else {
			allMaps = new ArrayList<RoomMap>();
		}

		// sample maps initialization values
		RoomMap map = null;
		BufferedImage image = null;
		double roomWidth = 0.0;
		double roomHeight = 0.0;
		String title = "";
		ArrayList<Receiver> receivers = null;

		// add sample map, id 0
		String path1 = "images/room_4m_6m.png";
		image = (BufferedImage) Utilities.loadImage(path1);
		title = "Test Room";
		roomWidth = 4.0;
		roomHeight = 6.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(0, 0.0, 0.0, 45.0, true));
		receivers.add(new Receiver(1, 4.0, 0.0, 135.0, true));
		receivers.add(new Receiver(2, 4.0, 6.0, 225.0, true));
		receivers.add(new Receiver(3, 0.0, 6.0, 315.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(0);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(0);
		map.setLowerLeftMarkerOffsetYInPixels(460);
		map.setUpperRightMarkerOffsetXInPixels(309);
		map.setUpperRightMarkerOffsetYInPixels(0);
		map.setRatioWidth(75.5);
		map.setRatioHeight(75.5);
		map.setPath(path1);
		allMaps.add(map);

		// add sample map, id 1
		String path2 = "images/test_room_fifth_floor.png";
		image = (BufferedImage) Utilities.loadImage(path2);
		title = "Room 501";
		roomWidth = 6.0;
		roomHeight = 6.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(0, 0.0, 6.0, 315.0, true));
		receivers.add(new Receiver(1, 2.0, 6.0, 270.0, true));
		receivers.add(new Receiver(2, 6.0, 6.0, 225.0, true));
		receivers.add(new Receiver(3, 0.0, 2.45, 0.0, true));
		receivers.add(new Receiver(4, 6.0, 2.45, 180.0, true));
		receivers.add(new Receiver(6, 6.0, 0.0, 135.0, true));
		receivers.add(new Receiver(9, 3.60, 2.0, 270.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(1);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(0);
		map.setLowerLeftMarkerOffsetYInPixels(538);
		map.setUpperRightMarkerOffsetXInPixels(538);
		map.setUpperRightMarkerOffsetYInPixels(0);
		map.setRatioWidth(89.67);
		map.setRatioHeight(89.67);
		map.setPath(path2);
		allMaps.add(map);
		
		// add sample map, id 2
		String path3 = "images/room_4m_6m.png";
		image = (BufferedImage) Utilities.loadImage(path3);
		title = "Room 481";
		roomWidth = 5.0;
		roomHeight = 8.30;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(6, 0.7, 0.0, 90.0, true));
		receivers.add(new Receiver(1, 5.0, 0.65, 135.0, true));
		receivers.add(new Receiver(2, 5.0, 4.5, 180.0, true));
		receivers.add(new Receiver(3, 0.0, 2.0, 0.0, true));
		receivers.add(new Receiver(4, 0.0, 7.8, 315.0, true));
		receivers.add(new Receiver(0, 2.5, 8.3, 270.0, true));
		receivers.add(new Receiver(9, 4.80, 8.3, 225.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(2);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(0);
		map.setLowerLeftMarkerOffsetYInPixels(460);
		map.setUpperRightMarkerOffsetXInPixels(309);
		map.setUpperRightMarkerOffsetYInPixels(0);
		map.setRatioWidth(61.80);
		map.setRatioHeight(55.42);
		map.setPath(path3);
		allMaps.add(map);

		return allMaps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#setAllMaps(java.util.List)
	 */
	@Override
	public void setAllMaps(List<RoomMap> allMaps) {

		this.allMaps = allMaps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#refreshMaps()
	 */
	@Override
	public void refreshMaps() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#saveMap(components.RoomMap)
	 */
	@Override
	public void saveMap(RoomMap newMap) {

		// if map already existed and was modified, replace the old map with new one
		int listSize = allMaps.size();
		for (int i = 0; i < listSize; i++) {
			if (allMaps.get(i).getId() == newMap.getId()) {
				allMaps.set(i, newMap);
				break;
			}
		}

		// send the single map to the server
		uploadMapToServer(newMap);
	}

	private void uploadMapToServer(RoomMap newMap) {

		Mongo mongo = null;
		DB database;
		DBCollection sampleData;

		String databaseAddress = DEFAULT_DATABASE_ADDRESS;

		try { // read parameters from the configuration file
			databaseAddress = Utilities.getConfigurationValue(DATABASE_ADDRESS);

		} catch (NumberFormatException exception) { // reading has failed, use default values
			logger.info("Reading parameters from configuration file failed. "
					+ "Using default values for database_address instead.");
		}

		try {
			mongo = new Mongo(databaseAddress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		database = mongo.getDB("rssiSystem");
		sampleData = database.getCollection("map_records");
		// remove all maps
		sampleData.drop();

		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String strDate = simpledateformat.format(new Date());

		// -------------------
		// Load our image
		byte[] imageBytes = null;
		try {
			imageBytes = Utilities.LoadImageAsBytes(newMap.getPath());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// ---------------------

		// Create GridFS object
		GridFS fs = new GridFS(database);
		// Save image into database
		GridFSInputFile in = fs.createFile(imageBytes);
		in.save();
		Object mapIdObject = in.getId();
		// System.out.println(mapIdObject);

		try {

			DBObject documentDetail = new BasicDBObject();

			documentDetail.put("_cls", "mapRecords"); // for mongoEngine ORM users
			documentDetail.put("image", mapIdObject);
			documentDetail.put("mapId", newMap.getId());
			documentDetail.put("width", newMap.getWidthInMeters());
			documentDetail.put("height", newMap.getHeightInMeters());
			documentDetail.put("offsetX", newMap.getLowerLeftMarkerOffsetXInPixels());
			documentDetail.put("offsetY", newMap.getLowerLeftMarkerOffsetYInPixels());
			documentDetail.put("offset2X", newMap.getUpperRightMarkerOffsetXInPixels());
			documentDetail.put("offset2Y", newMap.getUpperRightMarkerOffsetYInPixels());
			documentDetail.put("scalingX", newMap.getRatioWidth());
			documentDetail.put("scalingY", newMap.getRatioHeight());
			documentDetail.put("title", newMap.getTitle());
			documentDetail.put("updateTime", strDate);

			sampleData.insert(documentDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#deleteMap(components.RoomMap)
	 */
	@Override
	public void deleteMap(RoomMap mapToDelete) {

		for (RoomMap map : allMaps) {
			if (map.getId() == mapToDelete.getId()) {
				allMaps.remove(map);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#addMap(components.RoomMap)
	 */
	@Override
	public void addMap(RoomMap newMap) {
		// TODO find the index that doesn't exist and assign it to the map before storing it
		int newMapId = 0;
		int listSize = allMaps.size();
		int highestId = 0;
		for (int i = 0; i < listSize; i++) {
			if (allMaps.get(i).getId() > highestId) {
				highestId = allMaps.get(i).getId();
			}
		}
		newMapId = highestId + 1;
		newMap.setId(newMapId);
		allMaps.add(newMap);

		// send the single map to the server
		uploadMapToServer(newMap);
	}

}
