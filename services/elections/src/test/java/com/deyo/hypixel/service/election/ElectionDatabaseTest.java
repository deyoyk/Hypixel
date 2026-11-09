package com.deyo.hypixel.service.election;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElectionDatabaseTest {

    @Test
    void voteIdsAreScopedToTheElectionYear() {
        assertNotEquals(
                ElectionDatabase.voteId("player", 1),
                ElectionDatabase.voteId("player", 2)
        );
        assertEquals("player#2", ElectionDatabase.voteId("player", 2));
    }

    @Test
    void removeUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = ElectionDatabase.electionCollection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        ElectionDatabase.electionCollection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            ElectionDatabase database = new ElectionDatabase("key");

            assertTrue(database.remove("key"));

            deletedCount.set(0);
            assertFalse(database.remove("key"));
        } finally {
            ElectionDatabase.electionCollection = original;
        }
    }

    @Test
    void castVoteAtomicallyReplacesTheVoteAndMovesTallies() {
        MongoCollection<Document> originalVotes = ElectionDatabase.votesCollection;
        MongoCollection<Document> originalTallies = ElectionDatabase.talliesCollection;
        AtomicReference<FindOneAndReplaceOptions> options = new AtomicReference<>();
        AtomicInteger tallyUpdates = new AtomicInteger();

        ElectionDatabase.votesCollection = collection((method, args) -> {
            if (method.equals("findOneAndReplace")) {
                options.set((FindOneAndReplaceOptions) args[2]);
                return new Document("candidate", "OLD");
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });
        ElectionDatabase.talliesCollection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                tallyUpdates.incrementAndGet();
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            ElectionDatabase.castVote("player", "NEW", 2);

            assertTrue(options.get().isUpsert());
            assertEquals(ReturnDocument.BEFORE, options.get().getReturnDocument());
            assertEquals(2, tallyUpdates.get());
        } finally {
            ElectionDatabase.votesCollection = originalVotes;
            ElectionDatabase.talliesCollection = originalTallies;
        }
    }

    @Test
    void reCastingTheSameVoteDoesNotMoveTallies() {
        MongoCollection<Document> originalVotes = ElectionDatabase.votesCollection;
        MongoCollection<Document> originalTallies = ElectionDatabase.talliesCollection;
        AtomicInteger tallyUpdates = new AtomicInteger();

        ElectionDatabase.votesCollection = collection((method, args) -> {
            if (method.equals("findOneAndReplace")) {
                return new Document("candidate", "SAME");
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });
        ElectionDatabase.talliesCollection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                tallyUpdates.incrementAndGet();
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            ElectionDatabase.castVote("player", "SAME", 2);

            assertEquals(0, tallyUpdates.get());
        } finally {
            ElectionDatabase.votesCollection = originalVotes;
            ElectionDatabase.talliesCollection = originalTallies;
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
