package com.deyo.hypixel.type.ravengardgeneric.data.monogdb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RavengardRegionDatabaseTest {

    @Test
    void insertOrUpdateUsesAnAtomicUpsert() {
        MongoCollection<Document> original = RavengardRegionDatabase.collection;
        AtomicReference<UpdateOptions> options = new AtomicReference<>();

        RavengardRegionDatabase.collection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                options.set((UpdateOptions) args[2]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            new RavengardRegionDatabase("region").insertOrUpdate("type", "RAVENPORT");

            assertTrue(options.get().isUpsert());
        } finally {
            RavengardRegionDatabase.collection = original;
        }
    }

    @Test
    void removeUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = RavengardRegionDatabase.collection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        RavengardRegionDatabase.collection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            RavengardRegionDatabase database = new RavengardRegionDatabase("region");

            assertTrue(database.remove("region"));

            deletedCount.set(0);
            assertFalse(database.remove("region"));
        } finally {
            RavengardRegionDatabase.collection = original;
        }
    }

    @SuppressWarnings("unchecked")
    private static MongoCollection<Document> collection(Handler handler) {
        return (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> handler.handle(method.getName(), args));
    }

    private interface Handler {
        Object handle(String method, Object[] args);
    }
}
