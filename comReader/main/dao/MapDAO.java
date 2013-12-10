package dao;

import java.util.List;

import components.RoomMap;

public interface MapDAO {
	
	abstract List<RoomMap> getAllMaps();

	abstract void setAllMaps(List<RoomMap> allMaps);
	
	abstract void refreshMaps();
	
	abstract void uploadMap(RoomMap newMap);
	
	abstract void deleteMap(RoomMap mapToDelete);
	
	
	
}
