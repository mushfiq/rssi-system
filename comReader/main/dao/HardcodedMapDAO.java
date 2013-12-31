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
	@SuppressWarnings("unused")
	private Logger logger;

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

		// TODO Update maps with valid sample data
		
		// sample maps initialization values
		RoomMap map = null;
		BufferedImage image = null;
		double roomWidth = 0.0;
		double roomHeight = 0.0;
		String title = "";
		ArrayList<Receiver> receivers = null;

		// add sample map, id 0
		image = (BufferedImage) Utilities.loadImage("images/map1.png");
		title = "Room 301";
		roomWidth = 3.0;
		roomHeight = 10.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(2, 0.2926829268, 9.299363057324841, 315.0, true));
		receivers.add(new Receiver(4, 2.7219512195121953, 9.363057324840765, 225.0, true));
		receivers.add(new Receiver(5, 0.32195121951219513, 0.7006369426751593, 45.0, true));
		receivers.add(new Receiver(1, 2.707317073170732, 0.7324840764331211, 135.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(0);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(177);
		map.setLowerLeftMarkerOffsetYInPixels(325);
		map.setUpperRightMarkerOffsetXInPixels(380);
		map.setUpperRightMarkerOffsetYInPixels(11);
		map.setRatioWidth(67.66666666666667);
		map.setRatioHeight(31.4);
		allMaps.add(map);
		
		// add sample map, id 1
		image = (BufferedImage) Utilities.loadImage("images/map2.png");
		title = "Room 501";
		roomWidth = 6.0;
		roomHeight = 6.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 0.5344129554655871, 3.26027397260274, 0.0, true));
		receivers.add(new Receiver(2, 5.441295546558705, 3.315068493150685, 180.0, true));
		receivers.add(new Receiver(3, 3.0607287449392713, 0.6027397260273972, 90.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(1);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(54);
		map.setLowerLeftMarkerOffsetYInPixels(275);
		map.setUpperRightMarkerOffsetXInPixels(301);
		map.setUpperRightMarkerOffsetYInPixels(56);
		map.setRatioWidth(41.166666666666664);
		map.setRatioHeight(36.5);
		allMaps.add(map);

		// add sample map, id 2
		image = (BufferedImage) Utilities.loadImage("images/map3.png");
		title = "Room 433";
		roomWidth = 9.5;
		roomHeight = 4.5;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 0.8475836431226766, 3.9789473684210526, 315.0, true));
		receivers.add(new Receiver(2, 8.864312267657994, 4.073684210526316, 225.0, true));
		receivers.add(new Receiver(3, 0.741635687732342, 0.5447368421052632, 45.0, true));
		receivers.add(new Receiver(4, 4.873605947955391, 4.002631578947368, 270.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(3);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(33);
		map.setLowerLeftMarkerOffsetYInPixels(267);
		map.setUpperRightMarkerOffsetXInPixels(302);
		map.setUpperRightMarkerOffsetYInPixels(77);
		map.setRatioWidth(28.31578947368421);
		map.setRatioHeight(42.22222222222222);
		allMaps.add(map);

		// add sample map, id 3
		image = (BufferedImage) Utilities.loadImage("images/map5.png");
		title = "Room 049";
		roomWidth = 3.0;
		roomHeight = 5.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 1.490566037735849, 4.097222222222222, 270.0, true));
		receivers.add(new Receiver(2, 1.509433962264151, 1.134259259259259, 90.0, true));
		receivers.add(new Receiver(3, 0.32075471698113206, 2.5925925925925926, 0.0, true));
		receivers.add(new Receiver(4, 2.792452830188679, 2.6157407407407405, 180.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(3);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(23);
		map.setLowerLeftMarkerOffsetYInPixels(229);
		map.setUpperRightMarkerOffsetXInPixels(182);
		map.setUpperRightMarkerOffsetYInPixels(13);
		map.setRatioWidth(53.0);
		map.setRatioHeight(43.2);
		allMaps.add(map);

		// add sample map, id 4
		image = (BufferedImage) Utilities.loadImage("images/map6.png");
		title = "Room 305";
		roomWidth = 20.0;
		roomHeight = 10.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(2, 1.566820276497696, 8.669354838709678, 315.0, true));
		receivers.add(new Receiver(3, 18.29493087557604, 8.709677419354838, 225.0, true));
		receivers.add(new Receiver(4, 1.2903225806451613, 1.2096774193548387, 45.0, true));
		receivers.add(new Receiver(5, 18.202764976958527, 1.3709677419354838, 135.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(4);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(69);
		map.setLowerLeftMarkerOffsetYInPixels(321);
		map.setUpperRightMarkerOffsetXInPixels(503);
		map.setUpperRightMarkerOffsetYInPixels(73);
		map.setRatioWidth(21.7);
		map.setRatioHeight(24.8);
		allMaps.add(map);

		// add sample map, id 5
		image = (BufferedImage) Utilities.loadImage("images/map7.png");
		title = "Room 012";
		roomWidth = 6.0;
		roomHeight = 4.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(2, 0.6509433962264151, 3.424, 315.0, true));
		receivers.add(new Receiver(4, 3.2547169811320753, 0.64, 90.0, true));
		receivers.add(new Receiver(5, 5.4905660377358485, 3.392, 225.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(5);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(40);
		map.setLowerLeftMarkerOffsetYInPixels(259);
		map.setUpperRightMarkerOffsetXInPixels(252);
		map.setUpperRightMarkerOffsetYInPixels(134);
		map.setRatioWidth(35.333333333333336);
		map.setRatioHeight(31.25);
		allMaps.add(map);

		// add sample map, id 6
		image = (BufferedImage) Utilities.loadImage("images/map8.png");
		title = "Laboratory";
		roomWidth = 2.0;
		roomHeight = 5.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(2, 0.1837837837837838, 3.3053691275167782, 0.0, true));
		receivers.add(new Receiver(4, 1.827027027027027, 0.2684563758389262, 135.0, true));
		receivers.add(new Receiver(5, 1.2972972972972974, 4.76510067114094, 270.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(6);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(23);
		map.setLowerLeftMarkerOffsetYInPixels(330);
		map.setUpperRightMarkerOffsetXInPixels(208);
		map.setUpperRightMarkerOffsetYInPixels(32);
		map.setRatioWidth(92.5);
		map.setRatioHeight(59.6);
		allMaps.add(map);

		// add sample map, id 7
		image = (BufferedImage) Utilities.loadImage("images/map9.png");
		title = "Room 210";
		roomWidth = 4.0;
		roomHeight = 5.0;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 0.28936170212765955, 3.2386363636363638, 0.0, true));
		receivers.add(new Receiver(2, 3.7106382978723405, 3.200757575757576, 180.0, true));
		receivers.add(new Receiver(3, 1.6680851063829787, 4.261363636363637, 270.0, true));
		receivers.add(new Receiver(4, 1.3617021276595744, 0.32196969696969696, 90.0, true));
		receivers.add(new Receiver(5, 2.519148936170213, 0.34090909090909094, 90.0, true));
		map = new RoomMap(image, title, receivers);
		map.setId(7);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(52);
		map.setLowerLeftMarkerOffsetYInPixels(287);
		map.setUpperRightMarkerOffsetXInPixels(287);
		map.setUpperRightMarkerOffsetYInPixels(23);
		map.setRatioWidth(58.75);
		map.setRatioHeight(52.8);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map10.png");
		title = "Ground floor";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 1, 2, 45, true));
		receivers.add(new Receiver(2, 7, 3.5, 135, true));
		roomWidth = 8;
		roomHeight = 4;
		map = new RoomMap(image, title, receivers);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(247);
		map.setLowerLeftMarkerOffsetYInPixels(872);
		map.setUpperRightMarkerOffsetXInPixels(1315);
		map.setUpperRightMarkerOffsetYInPixels(131);
		map.setRatioWidth(133.875);
		map.setRatioHeight(185.75);
		map.setxFrom(0);
		map.setyFrom(0);
		map.setxTo(8);
		map.setyTo(4);
		map.setId(8);
		map.setGranularity(0.25);
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
				return;
			}
		}
		
		// send the single map to the server
		uploadMapToServer(newMap);
	}

	private void uploadMapToServer(RoomMap newMap) {
		
		Mongo mongo = null;
		DB database;
		DBCollection sampleData;
		
		try {
			mongo = new Mongo("127.0.0.1");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		database = mongo.getDB("rssiSystem");
		sampleData = database.getCollection("map_records");
		// remove all maps
		//sampleData.drop();
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String strDate = simpledateformat.format(new Date());
		
		// -------------------
		//Load our image
        byte[] imageBytes = null;
		try {
			imageBytes = Utilities.LoadImageAsBytes("images/map10.png");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        // ---------------------
		
		//Create GridFS object
        GridFS fs = new GridFS( database );
        //Save image into database
        GridFSInputFile in = fs.createFile( imageBytes );
        in.save();
        Object mapIdObject = in.getId();
        System.out.println(mapIdObject);
		
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
