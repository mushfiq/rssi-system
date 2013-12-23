/*
 * 
 * 
 */
package dao;

import java.util.List;

import components.Receiver;
import components.RoomMap;

/**
 * ReceiverDAO interface handles retrieving and storing of receiver information, <code>Receiver</code> objects, to and
 * from the data source.
 * 
 * @see components.Receiver
 * @author Danilo
 */
public interface ReceiverDAO {

	/**
	 * Gets all the <code>Receiver</code> objects from the data source.
	 * 
	 * @return list of all <code>Receiver</code> objects in the data source
	 * @see components.Receiver
	 */
	List<Receiver> getAllReceivers();

	/**
	 * Stores all <code>Receiver</code> objects to the data source.
	 * 
	 * @param allReceivers
	 *            <code>List</code> of all <code>Receiver</code> objects
	 */
	void setAllReceivers(List<Receiver> allReceivers);

	/**
	 * Adds a single <code>Receiver</code> object to a specified <code>RoomMap</code>'s <code>List</code> of
	 * <code>Receiver</code>s. <br>
	 * <br>
	 * 
	 * To add a <code>Receiver</code> to the data source (not to any particular <code>RoomMap</code>), use
	 * <code>ReceiverDAO.addReceiver</code> method instead.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> to add
	 * @param map
	 *            <code>RoomMap</code> object
	 * @see components.RoomMap
	 * @see #addReceiver(Receiver)
	 */
	void addReceiverToMap(Receiver receiver, RoomMap map);

	/**
	 * Removes the <code>Receiver</code> object from <code>RoomMap</code>'s <code>List</code>. <br>
	 * <br>
	 * 
	 * To delete <code>Receiver</code> from the data source completely, use <code>ReceiverDAO.deleteReceiver</code>
	 * method instead.
	 * 
	 * @param receiver
	 *            <code>Receiver</code> object to remove
	 * @param map
	 *            <code>RoomMap</code> object
	 * @see components.RoomMap
	 * @see #deleteReceiver(Receiver)
	 */
	void removeReceiverFromMap(Receiver receiver, RoomMap map);

	/**
	 * Deletes <code>Receiver</code> from data source. When a <code>Receiver</code> is deleted from the source, it will
	 * be removed from all the <code>RoomMap</code>s in the data source, as well.
	 * 
	 * To remove <code>Receiver</code> from one map only, use <code>ReceiverDAO.removeReceiverFromMap</code> method
	 * instead.
	 * 
	 * @param receiverToDelete
	 *            <code>Receiver</code> to delete
	 * @see #removeReceiverFromMap(Receiver, RoomMap)
	 * @see components.RoomMap
	 */
	void deleteReceiver(Receiver receiverToDelete);

	/**
	 * Adds the <code>Receiver</code> to data source. <br>
	 * <br>
	 * 
	 * To add <code>Receiver</code> to a particular <code>RoomMap</code>, use <code>ReceiverDAO.addReceiverToMap</code>
	 * method instead.
	 * 
	 * @param newReceiver
	 *            <code>Receiver</code> object
	 * @see components.RoomMap
	 * @see #addReceiverToMap(Receiver, RoomMap)
	 */
	void addReceiver(Receiver newReceiver);

	/**
	 * Gets a <code>List</code> of all <code>Receiver</code>s for a particular <code>RoomMap</code> from the data
	 * source.
	 * 
	 * @param map
	 *            <code>RoomMap</code> object
	 * @return <code>List</code> of all <code>Receiver</code> objects
	 * @see components.RoomMap
	 */
	List<Receiver> getAllReceiversForMap(RoomMap map);
}
