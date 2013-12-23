package tests;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class RESTTestReceiverPUT {

	public static void main(String[] args) {
		ClientResource resource = new ClientResource("http://shironambd.com/api/v1/receiver/");
		
		resource.setMethod(Method.PUT);
		resource.getReference().addQueryParameter("format", "json");
		resource.getReference().addQueryParameter("access_key", "529a2d308333d14178f5c54d");
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("receiverId", "5");   // mandatory quotes for 'receiverId' field
			obj.put("mapId", 2); 
			obj.put("x", 15);
			obj.put("y", 18);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		StringRepresentation stringRep = new StringRepresentation(obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		
		try {
			resource.put(stringRep).write(System.out);
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
