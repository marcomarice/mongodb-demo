package it.bitrock.mongodbdemo.controller;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ConnectionController {

    private ConnectionController() {

    }

    private static final String MONGODB_URI = "mongodb+srv://root:Ecko7dHTOAOKkulM@cluster0.qnmving.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE = "sample_mflix";
    private static final String COLLECTION = "movies";

    public static String getMongoDbUri() {
        return MONGODB_URI;
    }

    public static String getDatabase() {
        return DATABASE;
    }

    public static String getCollection() {
        return COLLECTION;
    }

    public static MongoClient openConnection() {
        return MongoClients.create(ConnectionController.mongoClientSettings());
    }

    public static void closeConnection(MongoClient mongoClient) {
        mongoClient.close();
    }

    public static MongoCollection<Document> getDocumentsCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase(ConnectionController.getDatabase())
                .getCollection(ConnectionController.getCollection());
    }

    public static List<Document> showListDatabases() {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URI)) {
            return mongoClient.listDatabases().into(new ArrayList<>());
        }
    }
    private static MongoClientSettings mongoClientSettings() {
        ConnectionString connectionString = new ConnectionString(MONGODB_URI);
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();
    }
}
