/*
 * 
 * 
 */
package dao;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

		// add sample maps
		RoomMap map = null;
		BufferedImage image = null;
		double roomWidth = 0.0;
		double roomHeight = 0.0;
		String title = "";
		ArrayList<Receiver> receivers = null;

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map1.png");

		title = "Room 301";
		receivers = new ArrayList<Receiver>();

		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map2.png");
		title = "Room 501";
		roomWidth = 8;
		roomHeight = 4;
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 40, 50, 90, true));
		receivers.add(new Receiver(2, 10, 20, 180, true));
		receivers.add(new Receiver(5, 100, 50, 315, true));
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map3.png");

		title = "Room 433";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 40, 50, 90, true));
		receivers.add(new Receiver(2, 40, 50, 90, true));
		receivers.add(new Receiver(4, 40, 50, 90, true));
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map5.png");
		title = "Room 049";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map6.png");
		title = "Room 301";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map7.png");
		title = "Room 012";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 40, 50, 90, true));
		receivers.add(new Receiver(2, 40, 50, 90, true));
		receivers.add(new Receiver(3, 40, 50, 90, true));
		receivers.add(new Receiver(4, 40, 50, 90, true));
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map8.png");

		title = "Laboratory";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(4, 40, 50, 90, true));
		receivers.add(new Receiver(2, 40, 50, 90, true));
		receivers.add(new Receiver(1, 40, 50, 90, true));
		receivers.add(new Receiver(5, 40, 50, 90, true));
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map9.png");
		title = "Room 210";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 40, 50, 90, true));
		receivers.add(new Receiver(2, 40, 50, 90, true));
		receivers.add(new Receiver(3, 40, 50, 90, true));
		map = new RoomMap(image, title, receivers);
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
		// map didn't already exist, add it to the list
		allMaps.add(newMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#deleteMap(components.RoomMap)
	 */
	@Override
	public void deleteMap(RoomMap mapToDelete) {

		if (allMaps.contains(mapToDelete)) {
			allMaps.remove(mapToDelete);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.MapDAO#addMap(components.RoomMap)
	 */
	@Override
	public void addMap(RoomMap newMap) {
		// TODO Auto-generated method stub

	}

}
