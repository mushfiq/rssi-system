package tests;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import utilities.Utilities;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import components.Receiver;
import components.RoomMap;

public class DirectMapUploadTest {

	
	public static void main(String[] args) {
		
		Mongo mongo = null;
		DB database;
		DBCollection sampleData;
		
		try {
			mongo = new Mongo("127.0.0.1");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		database = mongo.getDB("mydb");
		sampleData = database.getCollection("map_records");
		
		// Sample map
		RoomMap map = null;
		BufferedImage image = null;
		double roomWidth = 0.0;
		double roomHeight = 0.0;
		String title = "";
		ArrayList<Receiver> receivers = null;
		
		title = "Ground floor";
		receivers = new ArrayList<Receiver>();
		receivers.add(new Receiver(1, 1, 2, 45, true));
		receivers.add(new Receiver(2, 7, 3.5, 135, true));
		roomWidth = 8;
		roomHeight = 4;
		map = new RoomMap(image, title, receivers);
		map.setWidthInMeters(roomWidth);
		map.setHeightInMeters(roomHeight);
		map.setLowerLeftMarkerOffsetXInPixels(247);
		map.setLowerLeftMarkerOffsetYInPixels(872);
		map.setUpperRightMarkerOffsetXInPixels(1315);
		map.setUpperRightMarkerOffsetYInPixels(131);
		map.setRatioWidth(133.875);
		map.setRatioHeight(185.75);
		map.setxFrom(0);
		map.setyFrom(0);
		map.setxTo(8);
		map.setyTo(4);
		map.setId(8);
		map.setGranularity(0.25);
		// end of sample map
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String strDate = simpledateformat.format(new Date());
		
		// -------------------
		//Load our image
        byte[] imageBytes = null;
		try {
			imageBytes = Utilities.LoadImageAsBytes("images/map10.png");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        // ---------------------
		
		//Create GridFS object
        GridFS fs = new GridFS( database );
        //Save image into database
        GridFSInputFile in = fs.createFile( imageBytes );
        in.save();
        Object mapIdObject = in.getId();
        System.out.println(mapIdObject);
		
		try {
			DBObject documentDetail = new BasicDBObject();

			//documentDetail.put("_cls", "mapRecords"); // for mongoEngine ORM users
			documentDetail.put("image", in.getId());
			documentDetail.put("mapId", map.getId());
			documentDetail.put("width", map.getWidthInMeters());
			documentDetail.put("height", map.getHeightInMeters());
			documentDetail.put("offsetX", map.getLowerLeftMarkerOffsetXInPixels());
			documentDetail.put("offsetY", map.getLowerLeftMarkerOffsetYInPixels());
			documentDetail.put("offset2X", map.getUpperRightMarkerOffsetXInPixels());
			documentDetail.put("offset2Y", map.getUpperRightMarkerOffsetYInPixels());
			documentDetail.put("scalingX", map.getRatioWidth());
			documentDetail.put("scalingY", map.getRatioHeight());
			documentDetail.put("title", map.getTitle());
			documentDetail.put("updateTime", strDate);

			sampleData.insert(documentDetail);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
}
