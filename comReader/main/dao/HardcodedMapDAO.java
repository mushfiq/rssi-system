package dao;

import java.util.ArrayList;
import java.util.List;

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
