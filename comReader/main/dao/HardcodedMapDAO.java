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
		
		if(allMaps == null) {
			allMaps = new ArrayList<RoomMap>();
		}

		// add sample maps
		RoomMap map = null;
		BufferedImage image = null;
		double zeroOffsetXInMeters = 0;
		double zeroOffsetYInMeters = 0;
		String title = "";
		ArrayList<Receiver> receivers = null;
		Receiver receiver = null;
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map1.png");
		zeroOffsetXInMeters = 2;
		zeroOffsetYInMeters = 3;
		title = "Room 301";
		receivers = new ArrayList<Receiver>();
		
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map2.png");
		zeroOffsetXInMeters = 1;
		zeroOffsetYInMeters = 2;
		title = "Room 501";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map3.png");
		zeroOffsetXInMeters = 2;
		zeroOffsetYInMeters = 1;
		title = "Room 433";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map5.png");
		zeroOffsetXInMeters = 1;
		zeroOffsetYInMeters = 1;
		title = "Room 049";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map6.png");
		zeroOffsetXInMeters = 0;
		zeroOffsetYInMeters = 1;
		title = "Room 301";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map7.png");
		zeroOffsetXInMeters = 1;
		zeroOffsetYInMeters = 0;
		title = "Room 012";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map8.png");
		zeroOffsetXInMeters = 2;
		zeroOffsetYInMeters = 2;
		title = "Laboratory";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map9.png");
		zeroOffsetXInMeters = 3;
		zeroOffsetYInMeters = 3;
		title = "";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
		
		// add sample map
		image = (BufferedImage) Utilities.loadImage("images/map10.png");
		zeroOffsetXInMeters = 4;
		zeroOffsetYInMeters = 4;
		title = "Ground floor";
		receivers = new ArrayList<Receiver>();
		map = new RoomMap(image, zeroOffsetXInMeters, zeroOffsetYInMeters, title, receivers);
		allMaps.add(map);
				
		return allMaps;
	}

	@Override
	public void setAllMaps(List<RoomMap> allMaps) {
			
		this.allMaps = allMaps;
	}

	@Override
	public void refreshMaps() {
		
	}

	@Override
	public void uploadMap(RoomMap newMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMap(RoomMap mapToDelete) {
		// TODO Auto-generated method stub
		
	}
	

	
}
