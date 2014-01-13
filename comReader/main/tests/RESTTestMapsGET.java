package tests;

import java.awt.Image;
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

import components.RoomMap;

public class RESTTestMapsGET {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/map/?access_key=529a2d308333d14178f5c54d&format=json");
		String response = "";
		try {
			Representation representation = resource.get();
			response = representation.getText();
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<RoomMap> maps = new ArrayList<RoomMap>();
		try {
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("objects");

			int arrayLength = jarray.size();
			for (int i = 0; i < arrayLength; i++) {
				jobject = jarray.get(i).getAsJsonObject();
				String receivers = jobject.get("receiverId").getAsString();
				int mapId = jobject.get("mapId").getAsInt();
				double scalingX = jobject.get("scalingX").getAsDouble();
				double scalingY = jobject.get("scalingY").getAsDouble();
				double mapWidth = jobject.get("width").getAsDouble();
				double mapHeight = jobject.get("height").getAsDouble();
				String rawImageIdOnServer = jobject.get("image").getAsString();
				String imageIdOnServer = parseImagePath(rawImageIdOnServer);

				RoomMap newMap = new RoomMap();
				newMap.setRatioWidth(scalingX);
				newMap.setRatioHeight(scalingY);
				newMap.setId(mapId);
				newMap.setWidthInMeters(mapWidth);
				newMap.setHeightInMeters(mapHeight);

				System.out.println(newMap);
				System.out.println(receivers);
				System.out.println(imageIdOnServer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String parseImagePath(String imagePath) {

		// <GridFSProxy: 52a04c718333d17fb80e46a9>
		String prefix = "<GridFSProxy: ";
		String parsedPath = "";
		parsedPath = imagePath.substring(prefix.length());
		parsedPath = parsedPath.substring(0, parsedPath.length() - 1);

		return parsedPath;
	}

	@SuppressWarnings("unused")
	private static Image getMapImage(String imageId) {

		// Create the client resource
		ClientResource resource = new ClientResource(
														"http://shironambd.com/api/v1/image/?access_key=529a2d308333d14178f5c54d&format=json&id=" + imageId);
		String response = "";
		try {
			Representation representation = resource.get();
			response = representation.getText();
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<RoomMap> maps = new ArrayList<RoomMap>();
		try {
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("objects");

			int arrayLength = jarray.size();
			for (int i = 0; i < arrayLength; i++) {
				jobject = jarray.get(i).getAsJsonObject();
				String receivers = jobject.get("receiverId").getAsString();
				int mapId = jobject.get("mapId").getAsInt();
				double scalingX = jobject.get("scalingX").getAsDouble();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
