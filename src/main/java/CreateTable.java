import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClientFactory; // 用于创建 并管理 MongoDB 客户端实例
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.function.Consumer;

//import com.fasterxml.jackson.*; //引入当前包下所有的方法
import com.fasterxml.jackson.databind.ObjectMapper;
//创建表，并添加索引

public class CreateTable {
    public static void main(String[] args){
        String databaseName = "my_first_json_data";
        String collectName = "articles_test";
//        MongoClient client = MongoClientFactory.build(); // 已经没有 build方法
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectName);

        //创建索引
//        collection.createIndex(Indexes.ascending("type","updateAt"), new IndexOptions().name("art_type_updateAt").background(true));
        //background 表示后台操作 创建索引

        Boolean flag = false;

        if(flag){
            database.listCollectionNames().forEach((Consumer<String>) c -> {
//            System.out.println("collection: " + c);
//            if (c == "test_connect"){
                MongoCollection collo = database.getCollection(c);
                collo.listIndexes().forEach((Consumer<Document>) i ->{
                    System.out.println("\t index: " + i.toJson());
                });
//            }
            } );
        }
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person("zhangsan",34);
        try{
           String jsonS = mapper.writeValueAsString(person);
           System.out.println(jsonS);
        }catch (Exception e){
            e.printStackTrace();
        }

        // 插入文档

        client.close();
    }
}

class Person {
    public String name;
    public int age;
    public Person(String name,int age){
        this.name = name;
        this.age = age;
    }
}
