package dev.myleshenp.contentnotification.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MongoBeans {

    @Value("${spring.data.mongodb.uri}")
    String mongoURI;
    @Value("${spring.data.mongodb.database}")
    String mongoDatabase;

    @Bean
    MongoDatabase getMongoDatabase() {
        MongoClient mongoClient = MongoClients.create(mongoURI);
        MongoDatabase db = mongoClient.getDatabase(mongoDatabase);
        return db;
    }
}
