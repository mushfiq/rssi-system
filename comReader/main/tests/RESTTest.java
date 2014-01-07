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

public class RESTTest {

	public static void main(String[] args) {
		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/receiver/?access_key=529a2d308333d14178f5c54d&format=json");

		// Write the response entity on the console
		try {
			// resource.get().write(System.out);
			Representation representation = resource.get();
			String response = representation.getText();
			parse(response);

		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static List<Receiver> parse(String jsonLine) {
		
		List<Receiver> receivers = new ArrayList<Receiver>();
		try {
			JsonElement jelement = new JsonParser().parse(jsonLine);
			JsonObject jobject = jelement.getAsJsonObject();
			// jobject = jobject.getAsJsonObject("data");
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

		}
		return receivers;
	}
}
