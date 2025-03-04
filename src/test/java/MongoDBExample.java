import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBExample {
    public static void main(String[] args) {
        // 连接到 MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("my_first_json_data");
        MongoCollection<Document> collection = database.getCollection("test_connect");

        // 插入一条文档
        Document doc = new Document("name", "Alice")
                .append("age", 33)
                .append("city", "San Francisco");
        collection.insertOne(doc);

        System.out.println("Document inserted successfully!");

        // 关闭连接
        mongoClient.close();
    }
}