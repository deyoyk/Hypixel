package com.deyo.hypixel.service.itemtracker;

import com.deyo.hypixel.commons.TrackedItem;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrackedItemsDatabaseTest {

    @Test
    @SuppressWarnings("unchecked")
    void insertOrUpdateUsesASingleAtomicUpsert() {
        AtomicInteger replaceCalls = new AtomicInteger();
        AtomicReference<ReplaceOptions> options = new AtomicReference<>();
        MongoCollection<Document> original = TrackedItemsDatabase.collection;

        MongoCollection<Document> proxy = (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> {
                    if (method.getName().equals("replaceOne")) {
                        replaceCalls.incrementAndGet();
                        options.set((ReplaceOptions) args[2]);
                        return null;
                    }
                    throw new AssertionError("Unexpected collection call: " + method.getName());
                });

        TrackedItemsDatabase.collection = proxy;
        try {
            TrackedItem item = new TrackedItem(
                    UUID.randomUUID(), 0L, new ArrayList<>(), "TEST_ITEM", 1);

            new TrackedItemsDatabase(item.getItemUUID()).insertOrUpdate(item);

            assertEquals(1, replaceCalls.get());
            assertTrue(options.get().isUpsert());
        } finally {
            TrackedItemsDatabase.collection = original;
        }
    }
}
