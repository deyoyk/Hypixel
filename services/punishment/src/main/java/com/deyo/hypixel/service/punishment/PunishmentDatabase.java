package com.deyo.hypixel.service.punishment;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
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

public record PunishmentDatabase(String playerId) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> punishmentCollection;

    @Override
    public @NonNull MongoDB connect(@NonNull String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        punishmentCollection = database.getCollection("punishments");
        return this;
    }

    @Override
    public void set(@NonNull String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public boolean exists() {
        Document query = new Document("_id", playerId);
        Document found = punishmentCollection.find(query).first();
        return found != null;
    }

    @Override
    public Object get(@NonNull String key, Object def) {
        Document doc = punishmentCollection.find(Filters.eq("_id", playerId)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(@NonNull String key, Object value) {
        punishmentCollection.updateOne(
                Filters.eq("_id", playerId),
                Updates.set(key, value),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public boolean remove(@NonNull String id) {
        return punishmentCollection.deleteOne(Filters.eq("_id", id)).getDeletedCount() > 0;
    }
}
