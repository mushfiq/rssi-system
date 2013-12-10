package dao;

import java.util.List;

import components.Receiver;
import components.RoomMap;

	public interface ReceiverDAO {

		abstract List<Receiver> getAllReceivers();
	
		abstract void setAllReceivers(List<Receiver> allReceivers);
		
		abstract void addReceiverToMap(Receiver receiver, RoomMap map);
		
		abstract void removeReceiverFromMap(Receiver receiver, RoomMap map);
		
		abstract void deleteReceiver(Receiver receiverToDelete);
		
		abstract void addReceiver(Receiver newReceiver);
		
		abstract List<Receiver> getAllReceiversForMap(RoomMap map);
}
