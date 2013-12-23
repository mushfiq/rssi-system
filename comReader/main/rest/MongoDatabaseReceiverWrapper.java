package rest;

import components.Receiver;

public class MongoDatabaseReceiverWrapper {

	private Receiver receiver;
	private int mongoId;
	public MongoDatabaseReceiverWrapper(Receiver receiver, int mongoId) {
		super();
		this.receiver = receiver;
		this.mongoId = mongoId;
	}
	public Receiver getReceiver() {
		return receiver;
	}
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	public int getMongoId() {
		return mongoId;
	}
	public void setMongoId(int mongoId) {
		this.mongoId = mongoId;
	}
	
	
}
