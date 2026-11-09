package com.deyo.hypixel.service.punishment;

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

class PunishmentDatabaseTest {

    @Test
    @SuppressWarnings("unchecked")
    void insertOrUpdateUsesAnAtomicUpsert() {
        AtomicInteger updateCalls = new AtomicInteger();
        AtomicReference<UpdateOptions> options = new AtomicReference<>();
        MongoCollection<Document> original = PunishmentDatabase.punishmentCollection;

        PunishmentDatabase.punishmentCollection = (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> {
                    if (method.getName().equals("updateOne")) {
                        updateCalls.incrementAndGet();
                        options.set((UpdateOptions) args[2]);
                        return null;
                    }
                    throw new AssertionError("Unexpected collection call: " + method.getName());
                });

        try {
            new PunishmentDatabase("player").insertOrUpdate("active", true);

            assertTrue(updateCalls.get() == 1);
            assertTrue(options.get().isUpsert());
        } finally {
            PunishmentDatabase.punishmentCollection = original;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void removeUsesTheDeleteResultWithoutAPrecedingLookup() {
        MongoCollection<Document> original = PunishmentDatabase.punishmentCollection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        PunishmentDatabase.punishmentCollection = (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> {
                    if (method.getName().equals("deleteOne")) {
                        return DeleteResult.acknowledged(deletedCount.get());
                    }
                    throw new AssertionError("Unexpected collection call: " + method.getName());
                });

        try {
            PunishmentDatabase database = new PunishmentDatabase("player");

            assertTrue(database.remove("player"));

            deletedCount.set(0);
            assertFalse(database.remove("player"));
        } finally {
            PunishmentDatabase.punishmentCollection = original;
        }
    }
}
