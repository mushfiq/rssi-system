package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import components.Receiver;

public class RESTTestReceiversForMapGET {

	public static void main(String[] args) {
		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&format=json"
																+ "&mapId=0");
		String response = "";
		try {
			Representation representation = resource.get();
			response = representation.getText();
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
				int x = jobject.get("x").getAsInt();
				int y = jobject.get("y").getAsInt();
				int mapId = jobject.get("mapId").getAsInt();
				Receiver receiver = new Receiver(receiverId);
				receiver.setxPos(x);
				receiver.setyPos(y);
				receivers.add(receiver);
				System.out.println(receiver);
				System.out.println(mapId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
