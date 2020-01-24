package unitask;

import com.mongodb.*;

import static spark.Spark.setIpAddress;
import static spark.Spark.setPort;
import static spark.SparkBase.staticFileLocation;

public class SparkServer {
    private static final String IP_ADDRESS = System.getenv("DIY_IP") != null ? System.getenv("DIY_IP") : "localhost";
    private static final int PORT = System.getenv("DIY_IP") != null ? Integer.parseInt(System.getenv("DIY_IP")) : 8080;

    public static void main(String[] args) throws Exception {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        new Resource(new Service(mongo()));
    }

    private static DB mongo() throws Exception {
        String host = System.getenv("MONGODB_DB_HOST");
        if (host == null) {
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient.getDB("unitask");
        }
        int port = Integer.parseInt(System.getenv("MONGODB_DB_PORT"));
        String dbname = System.getenv("APP_NAME");
        String username = System.getenv("MONGODB_DB_USERNAME");
        String password = System.getenv("MONGODB_DB_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(20).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB(dbname);
        if (db.authenticate(username, password.toCharArray())) {
            return db;
        } else {
            throw new RuntimeException("Not able to authenticate with MongoDB");
        }
    }
}
