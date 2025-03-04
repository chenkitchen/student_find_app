import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpServer;
import org.bson.Document; //输入 MongoDB 的 bson 格式数据
//java.io 是一个标准库中的包，负责操作文件读写
import java.io.IOException; //异常类 用于抛出错误
import java.io.OutputStream; // 抽象类  字节的输出流
import java.net.InetSocketAddress; // 标准库 的网络地址端口

//修改添加json入参
import java.io.InputStream;
import java.nio.charset.StandardCharsets; // 标准库 字符集常量 utf-8
import java.util.Scanner; // 读取输入 （可以是键盘输入）

//处理 response
import java.util.HashMap;
import java.util.Map;

public class MongoDBHttpServer {
    public static void main(String[] args) throws IOException {
        // 创建 HTTP 服务器，监听 8080 端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 定义处理插入数据的接口
        server.createContext("/insert", (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {

                // 读取请求体中的 JSON 数据
                InputStream requestBody = exchange.getRequestBody();
                String jsonString = new Scanner(requestBody, String.valueOf(StandardCharsets.UTF_8)).useDelimiter("\\A").next(); //useDelimiter("\\A") 已这个格式进行扫描
                requestBody.close();

                // 解析 JSON 数据（这里假设请求体是一个简单的 JSON 对象）
                // 例如：{"name": "Alice", "age": 25, "city": "San Francisco"}
                Document doc = Document.parse(jsonString);


                // 连接到 MongoDB
                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("my_first_json_data");
                    MongoCollection<Document> collection = database.getCollection("test_connect");

//                    // 插入一条文档
//                    Document doc = new Document("name", "Alice")
//                            .append("age", 35)
//                            .append("city", "San Francisco")
                    doc.append("createDate",System.currentTimeMillis()); //添加时间戳
                    collection.insertOne(doc);

                    // 构造 JSON 响应
                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap.put("status", "success");
                    responseMap.put("message", "Document inserted successfully!");
                    responseMap.put("insertedId", doc.get("_id").toString()); // 返回插入的文档 ID doc会被 MongoDB 进行修改吗？

                    String jsonResponse = new Document(responseMap).toJson();

                    // 设置响应头
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.length());

                    // 返回响应
//                    String response = "Document inserted successfully!";
//                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
//                        os.write(response.getBytes()); //可以在 curl 中显示该 response
                        os.write(jsonResponse.getBytes(StandardCharsets.UTF_8)); //既可以在 zsh 环境中显示，也可以在浏览器中显示
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String response = "Error: " + e.getMessage();
                    exchange.sendResponseHeaders(500, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }));

        // 启动服务器
        server.start();
        System.out.println("Server started on port 8080");
    }
}