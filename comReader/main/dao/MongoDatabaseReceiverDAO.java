package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import utilities.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import components.Receiver;
import components.RoomMap;

public class MongoDatabaseReceiverDAO implements ReceiverDAO {

	/** <code>Logger</code> object. */
	private Logger logger;

	private List<Receiver> allReceivers;
	private boolean isDirty; // if data has been written to the database, e.g. we have old copy of data
	
	
	public MongoDatabaseReceiverDAO() {
		logger = Utilities.initializeLogger(this.getClass().getName());
		loadReceivers();
		isDirty = false;
	}

	@Override
	public List<Receiver> getAllReceivers() {

		if (isDirty) {
			loadReceivers();

			ArrayList<Receiver> newList = new ArrayList<Receiver>(allReceivers.size());
			for (Receiver receiver : allReceivers) {
				try {
					newList.add((Receiver) receiver.clone());
				} catch (CloneNotSupportedException e) {
					logger.severe("Cloning of Receiver object failed." + e.getMessage());
				}
			}

			return newList;

		} else {

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
	}

	@Override
	public void setAllReceivers(List<Receiver> allReceivers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addReceiverToMap(Receiver receiver, RoomMap map) {

		ClientResource resource = new ClientResource("http://shironambd.com/api/v1/receiver/");

		resource.setMethod(Method.POST);
		resource.getReference().addQueryParameter("format", "json");
		resource.getReference().addQueryParameter("access_key", "529a2d308333d14178f5c54d");

		JSONObject obj = new JSONObject();
		try {
			obj.put("receiverId", receiver.getID());
			obj.put("mapId", map.getId()); 
			obj.put("x", 7.5);
			obj.put("y", 11.5);

		} catch (JSONException e) {
			logger.severe("Error while creating json object for receiver." + e.getMessage());
		}

		StringRepresentation stringRep = new StringRepresentation(obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			resource.post(stringRep).write(System.out);
		} catch (ResourceException e) {
			logger.severe("Error while adding receiver number " + receiver.getID() + " to the map " + map.getId() + ". " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Error while adding receiver number " + receiver.getID() + " to the map " + map.getId() + ". " + e.getMessage());
		}
	}

	@Override
	public void removeReceiverFromMap(Receiver receiver, RoomMap map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteReceiver(Receiver receiverToDelete) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addReceiver(Receiver newReceiver) {

		ClientResource resource = new ClientResource("http://shironambd.com/api/v1/receiver/");

		resource.setMethod(Method.POST);
		resource.getReference().addQueryParameter("format", "json");
		resource.getReference().addQueryParameter("access_key", "529a2d308333d14178f5c54d");

		JSONObject obj = new JSONObject();
		try {
			obj.put("receiverId", newReceiver.getID());
			//obj.put("mapId", 2); // XXX which map id does receiver without a map belong to?
			obj.put("x", 7.5);
			obj.put("y", 11.5);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		StringRepresentation stringRep = new StringRepresentation(obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			resource.post(stringRep).write(System.out);
		} catch (ResourceException e) {
			logger.severe("Error while adding receiver. " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Error while adding receiver. " + e.getMessage());
		}

	}

	@Override
	public List<Receiver> getAllReceiversForMap(RoomMap map) {

		
		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&format=json"
																+ "&mapId=" + map.getId());
		String response = "";
		try {
			Representation representation = resource.get();
			response = representation.getText();
		} catch (ResourceException e) {
			logger.severe("Error getting all receivers for particular map. " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Error getting all receivers for particular map. " + e.getMessage());
		}
		List<Receiver> receivers = new ArrayList<Receiver>();
		try {
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("objects");

			int receiverId = 0;
			int x = 0;
			int y = 0;

			int arrayLength = jarray.size();
			for (int i = 0; i < arrayLength; i++) {
				jobject = jarray.get(i).getAsJsonObject();
				receiverId = Integer.parseInt(jobject.get("receiverId").toString().trim());
				x = Integer.parseInt(jobject.get("x").toString().trim());
				y = Integer.parseInt(jobject.get("y").toString().trim());
				Receiver receiver = new Receiver(receiverId);
				receiver.setxPos(x);
				receiver.setyPos(y);
				receivers.add(receiver);
//				System.out.println(receiver);
			}
		} catch (Exception e) {
			logger.severe("Error while obtaining receivers for particular map. " + e.getMessage());
		}

		return receivers;
	}

	private void loadReceivers() {

		
		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&format=json");
		String response = "";
		try {
			Representation representation = resource.get();
			response = representation.getText();
		} catch (ResourceException e) {
			logger.severe("Error getting all receivers. " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Error getting all receivers. " + e.getMessage());
		}
		List<Receiver> receivers = new ArrayList<Receiver>();
		try {
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("objects");
			int receiverId = 0;

			int arrayLength = jarray.size();
			for (int i = 0; i < arrayLength; i++) {
				jobject = jarray.get(i).getAsJsonObject();
				receiverId = Integer.parseInt(jobject.get("receiverId").toString().trim());
				Receiver receiver = new Receiver(receiverId);
				receivers.add(receiver);
			}
		} catch (Exception e) {
			logger.severe("Error parsing response from server while trying to get all receivers. " + e.getMessage());
		}

	}

	@Override
	public void updateReceiverForMap(Receiver receiver, RoomMap map) {
		// TODO Auto-generated method stub
		
	}

	
}
