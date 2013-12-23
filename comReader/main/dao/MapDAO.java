/*
 * 
 * 
 */
package dao;

import java.util.List;

import components.RoomMap;

/**
 * MapDAO interface handles retrieving and storing of map information, <code>RoomMap</code> objects, to and from the
 * data source.
 * 
 * @see components.RoomMap
 * @author Danilo
 */
public interface MapDAO {

	/**
	 * Gets all the <code>RoomMap</code> objects from the data source.
	 * 
	 * @return <code>List</code> of all RoomMap objects
	 * @see components.RoomMap
	 */
	List<RoomMap> getAllMaps();

	/**
	 * Stores all the maps to the data source.
	 * 
	 * @param allMaps
	 *            <code>List</code> of all <code>RoomMap</code> objects that exist in the data source
	 * @see components.RoomMap
	 */
	void setAllMaps(List<RoomMap> allMaps);

	/**
	 * Loads all the maps again from the data source. Method can be used, for example, after adding a new map to the
	 * data source and a refresh is needed to show newly added maps.
	 * 
	 * @see components.RoomMap
	 * @see #getAllMaps()
	 */
	void refreshMaps();

	/**
	 * Stores a single <code>RoomMap</code> object to the data source. This method is used for saving changes to
	 * existing maps. <br><br>
	 * 
	 * To add a new map to the data source, use <code>MapDAO.addMap</code> method instead.
	 * 
	 * @param mapToSave
	 *            Existing <code>RoomMap</code> object
	 * @see components.RoomMap
	 * @see #addMap(RoomMap)
	 */
	void saveMap(RoomMap mapToSave);

	/**
	 * Adds the new <code>RoomMap</code> object to the data source. <br><br> 
	 * 
	 * To save changes to an existing map, use
	 * <code>MapDAO.saveMap</code> method instead.
	 * 
	 * @param newMap
	 *            <code>RoomMap</code> object to add to the data source
	 * @see components.RoomMap
	 * @see #saveMap(RoomMap)
	 */
	void addMap(RoomMap newMap);

	/**
	 * Deletes a <code>RoomMap</code> object from the data source.
	 * 
	 * @param mapToDelete
	 *            <code>RoomMap</code> object to delete
	 * @see components.RoomMap
	 */
	void deleteMap(RoomMap mapToDelete);

}
