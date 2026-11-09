package com.deyo.hypixel.service.auction;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuctionDatabaseTest {

    @Test
    void inactiveInsertOrUpdateUsesAnAtomicUpsert() {
        MongoCollection<Document> original = AuctionInactiveDatabase.collection;
        AtomicInteger updateCalls = new AtomicInteger();
        AtomicReference<UpdateOptions> options = new AtomicReference<>();

        AuctionInactiveDatabase.collection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                updateCalls.incrementAndGet();
                options.set((UpdateOptions) args[2]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            new AuctionInactiveDatabase("auction").insertOrUpdate("end", 1L);

            assertEquals(1, updateCalls.get());
            assertTrue(options.get().isUpsert());
        } finally {
            AuctionInactiveDatabase.collection = original;
        }
    }

    @Test
    void activeInsertOrUpdateUsesAnAtomicUpsert() {
        MongoCollection<Document> original = AuctionActiveDatabase.collection;
        AtomicReference<UpdateOptions> options = new AtomicReference<>();

        AuctionActiveDatabase.collection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                options.set((UpdateOptions) args[2]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            new AuctionActiveDatabase("auction").insertOrUpdate("end", 1L);

            assertTrue(options.get().isUpsert());
        } finally {
            AuctionActiveDatabase.collection = original;
        }
    }

    @Test
    void inactiveRemoveUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = AuctionInactiveDatabase.collection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        AuctionInactiveDatabase.collection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            AuctionInactiveDatabase database = new AuctionInactiveDatabase("auction");

            assertTrue(database.remove("auction"));

            deletedCount.set(0);
            assertFalse(database.remove("auction"));
        } finally {
            AuctionInactiveDatabase.collection = original;
        }
    }

    @Test
    void activeRemoveUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = AuctionActiveDatabase.collection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        AuctionActiveDatabase.collection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            AuctionActiveDatabase database = new AuctionActiveDatabase("auction");

            assertTrue(database.remove("auction"));

            deletedCount.set(0);
            assertFalse(database.remove("auction"));
        } finally {
            AuctionActiveDatabase.collection = original;
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
