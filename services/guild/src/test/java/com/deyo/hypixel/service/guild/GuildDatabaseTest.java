package com.deyo.hypixel.service.guild;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.deyo.hypixel.commons.guild.GuildData;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildDatabaseTest {

    @Test
    void insertOrUpdateUsesAnAtomicUpsert() {
        MongoCollection<Document> original = GuildDatabase.guildCollection;
        AtomicReference<UpdateOptions> options = new AtomicReference<>();

        GuildDatabase.guildCollection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                options.set((UpdateOptions) args[2]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            new GuildDatabase("guild").insertOrUpdate("name", "Test");

            assertTrue(options.get().isUpsert());
        } finally {
            GuildDatabase.guildCollection = original;
        }
    }

    @Test
    void saveGuildUsesAnAtomicUpsert() {
        MongoCollection<Document> original = GuildDatabase.guildCollection;
        AtomicReference<ReplaceOptions> options = new AtomicReference<>();
        AtomicReference<Document> stored = new AtomicReference<>();

        GuildDatabase.guildCollection = collection((method, args) -> {
            if (method.equals("replaceOne")) {
                options.set((ReplaceOptions) args[2]);
                stored.set((Document) args[1]);
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            GuildData guild = new GuildData(UUID.randomUUID(), "Test Guild", UUID.randomUUID());
            GuildDatabase.saveGuild(guild);

            assertTrue(options.get().isUpsert());
            assertEquals(guild.getGuildId().toString(), stored.get().getString("_id"));
            assertEquals("test guild", stored.get().getString("name"));
            assertNotNull(stored.get().getString("data"));
        } finally {
            GuildDatabase.guildCollection = original;
        }
    }

    @Test
    void mapPlayerToGuildUsesAnAtomicUpsert() {
        MongoCollection<Document> original = GuildDatabase.playerGuildCollection;
        AtomicReference<UpdateOptions> options = new AtomicReference<>();
        AtomicReference<BsonDocument> update = new AtomicReference<>();

        GuildDatabase.playerGuildCollection = collection((method, args) -> {
            if (method.equals("updateOne")) {
                options.set((UpdateOptions) args[2]);
                update.set(((Bson) args[1]).toBsonDocument());
                return null;
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            UUID guildId = UUID.randomUUID();
            GuildDatabase.mapPlayerToGuild(UUID.randomUUID(), guildId);

            assertTrue(options.get().isUpsert());
            BsonDocument set = update.get().getDocument("$set");
            assertEquals(guildId.toString(), set.getString("guildId").getValue());
        } finally {
            GuildDatabase.playerGuildCollection = original;
        }
    }

    @Test
    void removeUsesTheDeleteResultWithoutLookup() {
        MongoCollection<Document> original = GuildDatabase.guildCollection;
        AtomicInteger deletedCount = new AtomicInteger(1);

        GuildDatabase.guildCollection = collection((method, args) -> {
            if (method.equals("deleteOne")) {
                return DeleteResult.acknowledged(deletedCount.get());
            }
            throw new AssertionError("Unexpected collection call: " + method);
        });

        try {
            GuildDatabase database = new GuildDatabase("guild");

            assertTrue(database.remove("guild"));

            deletedCount.set(0);
            assertFalse(database.remove("guild"));
        } finally {
            GuildDatabase.guildCollection = original;
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
