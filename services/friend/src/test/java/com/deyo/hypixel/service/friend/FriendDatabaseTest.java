package com.deyo.hypixel.service.friend;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.deyo.hypixel.commons.friend.PendingFriendRequest;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FriendDatabaseTest {

    @Test
    void insertOrUpdateUsesAnAtomicUpsert() {
        MongoCollection<Document> original = FriendDatabase.friendDataCollection;
        AtomicReference<UpdateOptions> options = new AtomicReference<>();

        FriendDatabase.friendDataCollection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                options.set((UpdateOptions) args[2]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            new FriendDatabase("player").insertOrUpdate("friends", "Test");

            assertTrue(options.get().isUpsert());
        } finally {
            FriendDatabase.friendDataCollection = original;
        }
    }

    @Test
    void addPendingRequestUsesAnAtomicUpsert() {
        MongoCollection<Document> original = FriendDatabase.pendingRequestsCollection;
        AtomicReference<ReplaceOptions> options = new AtomicReference<>();
        AtomicReference<Document> stored = new AtomicReference<>();

        FriendDatabase.pendingRequestsCollection = collection((method, args) -> {
            if (method.equals("replaceOne")) {
                options.set((ReplaceOptions) args[2]);
                stored.set((Document) args[1]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            UUID from = UUID.randomUUID();
            UUID to = UUID.randomUUID();
            PendingFriendRequest request = PendingFriendRequest.create(from, to, "Sender", "Target");

            new FriendDatabase(null).addPendingRequest(request);

            assertTrue(options.get().isUpsert());
            assertEquals(from + "_" + to, stored.get().getString("_id"));
            assertEquals(from.toString(), stored.get().getString("from"));
            assertEquals(to.toString(), stored.get().getString("to"));
            assertNotNull(stored.get().getString("data"));
        } finally {
            FriendDatabase.pendingRequestsCollection = original;
        }
    }

    @Test
    void removeUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = FriendDatabase.friendDataCollection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        FriendDatabase.friendDataCollection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            FriendDatabase database = new FriendDatabase("player");

            assertTrue(database.remove("player"));

            deletedCount.set(0);
            assertFalse(database.remove("player"));
        } finally {
            FriendDatabase.friendDataCollection = original;
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
