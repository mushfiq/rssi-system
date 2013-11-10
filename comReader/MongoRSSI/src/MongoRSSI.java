
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.DB;

public class MongoRSSI {
	public static void main(String[] args) throws Exception {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("rssiSystem");
		DBCollection rawData = db.getCollection("rawData");
		long currentUnixTime = System.currentTimeMillis() / 1000L;
		try {
			rawData.insert(new BasicDBObject().append("watchPositionData"
					).append("insertedAt",
					currentUnixTime));
		} catch (Exception e) {
			System.out.print(e);
		}
		DBCursor cursor = rawData.find();
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			System.out.println(object);
		}

	}
}