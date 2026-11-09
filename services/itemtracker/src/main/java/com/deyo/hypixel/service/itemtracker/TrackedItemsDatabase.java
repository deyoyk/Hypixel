package com.deyo.hypixel.service.itemtracker;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.deyo.hypixel.commons.TrackedItem;
import org.bson.Document;

import java.util.UUID;

public record TrackedItemsDatabase(UUID itemUUID) {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    private static final int LOCK_STRIPES = 64;
    private static final Object[] ITEM_LOCKS = new Object[LOCK_STRIPES];

    static {
        for (int index = 0; index < ITEM_LOCKS.length; index++) {
            ITEM_LOCKS[index] = new Object();
        }
    }

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("tracked-items");
    }

    public static Integer getNumberMade(String itemType) {
        return (int) collection.countDocuments(Filters.eq("item-type", itemType));
    }

    public boolean exists() {
        return collection.find(Filters.eq("_id", itemUUID.toString())).first() != null;
    }

    public TrackedItem get() {
        Document document = collection.find(Filters.eq("_id", itemUUID.toString())).first();
        return document == null ? null : TrackedItem.fromDocument(document);
    }

    public void insertOrUpdate(TrackedItem trackedItem) {
        collection.replaceOne(
                Filters.eq("_id", trackedItem.getItemUUID().toString()),
                trackedItem.toDocument(),
                new ReplaceOptions().upsert(true)
        );
    }

    static Object lockFor(UUID itemUUID) {
        return ITEM_LOCKS[Math.floorMod(itemUUID.hashCode(), ITEM_LOCKS.length)];
    }

    public static <T> T withItemLock(UUID itemUUID, java.util.function.Supplier<T> action) {
        synchronized (lockFor(itemUUID)) {
            return action.get();
        }
    }
}

