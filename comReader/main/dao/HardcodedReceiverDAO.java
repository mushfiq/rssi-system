/*
 * 
 * 
 */
package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import main.Application;
import utilities.Utilities;
import components.Receiver;
import components.RoomMap;

/**
 * Implementation of <code>dao.ReceiverDAO</code> interface. It retrieves receiver information from hard coded data,
 * i.e. it has no external data source. This class is used for testing purposes, when using an external data source is
 * not necessary, is quicker or when it is inappropriate (e.g. while testing methods that write do the data source).
 */
public class HardcodedReceiverDAO implements ReceiverDAO {

	/** <code>Logger</code> object. */
	private Logger logger;

	/** All receivers in the data source. */
	private List<Receiver> allReceivers;

	/**
	 * Dirty flag. This marker is used for caching purposes. I.e. if we have multiple subsequent reading calls, but no
	 * data has been changed in the meantime, no additional calls to the data source will be made - old data will be
	 * used instead.
	 * */
	@SuppressWarnings("unused")
	private boolean isDirty; // if data has been written to the database, e.g. we have old copy of data

	/**
	 * Instantiates a new <code>HardcodedReceiverDAO</code> object. Initially, data has not been read from the data
	 * source and therefore the <code>isDirty</code> flag is set to <code>true</code>.
	 * 
	 */
	public HardcodedReceiverDAO() {
		logger = Utilities.initializeLogger(this.getClass().getName());
		loadReceivers(); // TODO this line should be removed later
		// isDirty = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#getAllReceivers()
	 */
	@Override
	public List<Receiver> getAllReceivers() {
		// XXX commented code will be used in MongoDBReceiverDAO
		/*
		 * if (isDirty) { loadReceivers();
		 * 
		 * ArrayList<Receiver> newList = new ArrayList<Receiver>(allReceivers.size()); for (Receiver receiver :
		 * allReceivers) { try { newList.add((Receiver) receiver.clone()); } catch (CloneNotSupportedException e) {
		 * logger.severe("Cloning of Receiver object failed." + e.getMessage()); } }
		 * 
		 * return newList; } else {
		 */

		ArrayList<Receiver> newList = new ArrayList<Receiver>(allReceivers.size());
		for (Receiver receiver : allReceivers) {
			try {
				newList.add((Receiver) receiver.clone());
			} catch (CloneNotSupportedException e) {
				logger.severe("Cloning of Receiver object failed." + e.getMessage());
			}
		}
		return newList;
	}

	// XXX commented code will be used in MongoDBReceiverDAO
	/* } */

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#setAllReceivers(java.util.List)
	 */
	@Override
	public void setAllReceivers(List<Receiver> allReceivers) {
		this.allReceivers = allReceivers;
	}

	/**
	 * Helper method that loads <code>Receiver</code> objects from the data source. After reading, it sets the
	 * <code>isDirty</code> flag to <code>false</code>.
	 */
	public void loadReceivers() {

		allReceivers = new ArrayList<Receiver>();

		Receiver receiver1 = new Receiver(1);
		Receiver receiver2 = new Receiver(2);
		Receiver receiver3 = new Receiver(3);
		Receiver receiver4 = new Receiver(4);
		Receiver receiver5 = new Receiver(5);
		Receiver receiver6 = new Receiver(6);
		Receiver receiver7 = new Receiver(7);
		Receiver receiver8 = new Receiver(8);
		Receiver receiver9 = new Receiver(9);
		Receiver receiver10 = new Receiver(10);
		Receiver receiver11 = new Receiver(11);
		Receiver receiver12 = new Receiver(12);
		Receiver receiver13 = new Receiver(13);
		Receiver receiver14 = new Receiver(14);
		Receiver receiver15 = new Receiver(15);
		
		allReceivers.add(receiver1);
		allReceivers.add(receiver2);
		allReceivers.add(receiver3);
		allReceivers.add(receiver4);
		allReceivers.add(receiver5);
		allReceivers.add(receiver6);
		allReceivers.add(receiver7);
		allReceivers.add(receiver8);
		allReceivers.add(receiver9);
		allReceivers.add(receiver10);
		allReceivers.add(receiver11);
		allReceivers.add(receiver12);
		allReceivers.add(receiver13);
		allReceivers.add(receiver14);
		allReceivers.add(receiver15);

		isDirty = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#addReceiverToMap(components.Receiver, components.RoomMap)
	 */
	@Override
	public void addReceiverToMap(Receiver receiver, RoomMap map) {

		isDirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#removeReceiverFromMap(components.Receiver, components.RoomMap)
	 */
	@Override
	public void removeReceiverFromMap(Receiver receiver, RoomMap map) {

		isDirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#deleteReceiver(components.Receiver)
	 */
	@Override
	public void deleteReceiver(Receiver receiverToDelete) {

		for (Receiver receiver : allReceivers) {
			if (receiver.getID() == receiverToDelete.getID()) {
				// TODO remove receiver from all maps
				List<RoomMap> allMaps = Application.getApplication().getMapDAO().getAllMaps();
				for (RoomMap roomMap : allMaps) {
					for (Iterator<Receiver> iterator = roomMap.getReceivers().iterator(); iterator.hasNext();) {
						Receiver value = iterator.next();
						if (value.getID() == receiverToDelete.getID()) {
							iterator.remove();
						}
					}
				}
				// remove receiver from list of receivers
				allReceivers.remove(receiver);
				break;
			}
		}

		isDirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#addReceiver(components.Receiver)
	 */
	@Override
	public void addReceiver(Receiver newReceiver) {

		allReceivers.add(newReceiver);
		isDirty = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.ReceiverDAO#getAllReceiversForMap(components.RoomMap)
	 */
	@Override
	public List<Receiver> getAllReceiversForMap(RoomMap map) {

		ArrayList<Receiver> receiversOnMap = new ArrayList<Receiver>();

		Receiver receiver1 = new Receiver(1);
		Receiver receiver2 = new Receiver(2);
		Receiver receiver3 = new Receiver(3);

		receiversOnMap.add(receiver1);
		receiversOnMap.add(receiver2);
		receiversOnMap.add(receiver3);

		return receiversOnMap;
	}

	@Override
	public void updateReceiverForMap(Receiver receiver, RoomMap map) {
		// TODO Auto-generated method stub
		
	}

	
}
