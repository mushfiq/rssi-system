package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import utilities.Utilities;

import components.Receiver;
import components.RoomMap;

	public class HardcodedReceiverDAO implements ReceiverDAO {
	
		/** The logger. */
		private Logger logger;
		private List<Receiver> allReceivers;
		private boolean isDirty; // if data has been written to the database, e.g. we have old copy of data
		
		public HardcodedReceiverDAO() {
			logger = Utilities.initializeLogger(this.getClass().getName()); 
			isDirty = true;
		}
	
		@Override
		public List<Receiver> getAllReceivers() {
			
			if(isDirty){
				loadReceivers();
				return allReceivers;
			}
			
			return allReceivers;
		}
	
		@Override
		public void setAllReceivers(List<Receiver> allReceivers) {
			
			this.allReceivers = allReceivers;
		}
		
		public void loadReceivers() {
			
			allReceivers = new ArrayList<Receiver>();
			
			Receiver receiver1 = new Receiver(1);
			Receiver receiver2 = new Receiver(2);
			Receiver receiver3 = new Receiver(3);
			Receiver receiver4 = new Receiver(4);
			Receiver receiver5 = new Receiver(5);
			
			allReceivers.add(receiver1);
			allReceivers.add(receiver2);
			allReceivers.add(receiver3);
			allReceivers.add(receiver4);
			allReceivers.add(receiver5);
			
			isDirty = false;
		}

		@Override
		public void addReceiverToMap(Receiver receiver, RoomMap map) {
			
			isDirty = true;
		}

		@Override
		public void removeReceiverFromMap(Receiver receiver, RoomMap map) {
			
			isDirty = true;
		}

		@Override
		public void deleteReceiver(Receiver receiverToDelete) {
			
			isDirty = true;
		}

		@Override
		public void addReceiver(Receiver newReceiver) {
			
			isDirty = true;
		}

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
		
		
}
