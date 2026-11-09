package com.deyo.hypixel.service.auction;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.deyo.hypixel.service.generic.MongoDB;
import org.bson.Document;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record AuctionInactiveDatabase(String auctionId) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    @Override
    public @NonNull MongoDB connect(@NonNull String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("inactive-auctions");
        return this;
    }

    @Override
    public void set(@NonNull String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(@NonNull String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", auctionId)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    public List<Document> getAll() {
        FindIterable<Document> results = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : results) {
            list.add(doc);
        }
        return list;
    }

    public Document getDocument() {
        Document query = new Document("_id", auctionId);
        return collection.find(query).first();
    }

    @Override
    public boolean remove(@NonNull String id) {
        return collection.deleteOne(new Document("_id", id)).getDeletedCount() > 0;
    }

    public void insertOrUpdate(@NonNull String key, Object value) {
        collection.updateOne(
                new Document("_id", auctionId),
                Updates.set(key, value),
                new UpdateOptions().upsert(true)
        );
    }

    public boolean exists() {
        Document query = new Document("_id", auctionId);
        Document found = collection.find(query).first();
        return found != null;
    }
}

