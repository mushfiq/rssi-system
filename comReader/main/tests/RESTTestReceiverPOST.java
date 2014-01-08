package tests;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class RESTTestReceiverPOST {

	public static void main(String[] args) {

		ClientResource resource = new ClientResource("http://shironambd.com/api/v1/receiver/");
		
		resource.setMethod(Method.POST);
		resource.getReference().addQueryParameter("format", "json");
		resource.getReference().addQueryParameter("access_key", "529a2d308333d14178f5c54d");
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("receiverId", 5);
			obj.put("mapId", 2);
			obj.put("x", 12);
			obj.put("y", 14);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		StringRepresentation stringRep = new StringRepresentation(obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		
		try {
			resource.post(stringRep).write(System.out);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
