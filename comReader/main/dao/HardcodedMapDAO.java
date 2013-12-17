package dao;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import utilities.Utilities;

import components.Receiver;
import components.RoomMap;

public class HardcodedMapDAO implements MapDAO {

	private List<RoomMap> allMaps;

	public HardcodedMapDAO() {

	}

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
		map = new RoomMap(image, title, receivers);
		allMaps.add(map);

		return allMaps;
	}

	@Override
	public void setAllMaps(List<RoomMap> allMaps) {

		this.allMaps = allMaps;
	}

	@Override
	public void refreshMaps() {
		// TODO Auto-generated method stub
	}

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

	@Override
	public void deleteMap(RoomMap mapToDelete) {

		if (allMaps.contains(mapToDelete)) {
			allMaps.remove(mapToDelete);
		}

	}

}
