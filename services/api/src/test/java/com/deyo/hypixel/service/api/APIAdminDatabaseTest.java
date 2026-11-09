package com.deyo.hypixel.service.api;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;

class APIAdminDatabaseTest {

    @Test
    @SuppressWarnings("unchecked")
    void replaceOrInsertUsesOneAtomicUpsert() {
        MongoCollection<Document> original = APIAdminDatabase.collection;
        AtomicReference<ReplaceOptions> options = new AtomicReference<>();

        APIAdminDatabase.collection = (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> {
                    if (method.getName().equals("replaceOne")) {
                        options.set((ReplaceOptions) args[2]);
                        return null;
                    }
                    throw new AssertionError("Unexpected collection call: " + method.getName());
                });

        try {
            APIAdminDatabaseObject object = new APIAdminDatabaseObject(
                    "session", "code", "name", UUID.randomUUID());

            APIAdminDatabase.replaceOrInsert(object);

            assertTrue(options.get().isUpsert());
        } finally {
            APIAdminDatabase.collection = original;
        }
    }
}
