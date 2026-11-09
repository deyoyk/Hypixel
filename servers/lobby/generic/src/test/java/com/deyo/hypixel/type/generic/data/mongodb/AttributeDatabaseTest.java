package com.deyo.hypixel.type.generic.data.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributeDatabaseTest {

    @Test
    void saveDocumentUsesAnAtomicUpsert() {
        MongoCollection<Document> original = AttributeDatabase.collection;
        AtomicReference<ReplaceOptions> options = new AtomicReference<>();
        AtomicReference<Document> stored = new AtomicReference<>();

        AttributeDatabase.collection = collection((method, args) -> {
            if (method.equals("replaceOne")) {
                options.set((ReplaceOptions) args[2]);
                stored.set((Document) args[1]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            AttributeDatabase.saveDocument(new Document("_id", "key").append("value", 1));

            assertTrue(options.get().isUpsert());
            assertEquals("key", stored.get().getString("_id"));
        } finally {
            AttributeDatabase.collection = original;
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
