package unitask;

import com.google.gson.Gson;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service {

    private final DB db;
    private final DBCollection collection;

    public Service(DB db) {
        this.db = db;
        this.collection = db.getCollection("shapes");
    }

    public List<Shape> findAll() {
        List<Shape> shapes = new ArrayList<>();
        DBCursor dbObjects = collection.find();
        while (dbObjects.hasNext()) {
            DBObject dbObject = dbObjects.next();
            shapes.add(new Shape((BasicDBObject) dbObject));
        }
        return shapes;
    }

    public void createNewShape(String body) {
        Shape shape = new Gson().fromJson(body, Shape.class);
        collection.insert(new BasicDBObject("title", shape.getTitle()).append("done", shape.isDone()).append("createdOn", new Date()));
    }

    public Shape find(String id) {
        return new Shape((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public Shape update(String shapeId, String body) {
        Shape shape = new Gson().fromJson(body, Shape.class);
        collection.update(new BasicDBObject("_id", new ObjectId(shapeId)), new BasicDBObject("$set", new BasicDBObject("done", shape.isDone())));
        return this.find(shapeId);
    }
}
