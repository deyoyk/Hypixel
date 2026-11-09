package com.deyo.hypixel.service.guild;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.service.generic.MongoDB;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record GuildDatabase(String id) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> guildCollection;
    public static MongoCollection<Document> playerGuildCollection;

    @Override
    public @NonNull MongoDB connect(@NotNull String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        guildCollection = database.getCollection("guilds");
        playerGuildCollection = database.getCollection("player-guilds");

        guildCollection.createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
        playerGuildCollection.createIndex(Indexes.ascending("guildId"));
        return this;
    }

    public static GuildData getGuild(final @NotNull UUID guildId) {
        Document doc = guildCollection.find(Filters.eq("_id", guildId.toString())).first();
        if (doc == null) return null;
        return GuildData.getStaticSerializer().deserialize(doc.getString("data"));
    }

    public static void saveGuild(final @NotNull GuildData guild) {
        String serialized = guild.getSerializer().serialize(guild);
        String guildId = guild.getGuildId().toString();

        Document doc = new Document("_id", guildId)
                .append("data", serialized)
                .append("name", guild.getName().toLowerCase());
        guildCollection.replaceOne(
                Filters.eq("_id", guildId),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    public static void deleteGuild(@NotNull UUID guildId) {
        guildCollection.deleteOne(Filters.eq("_id", guildId.toString()));
        playerGuildCollection.deleteMany(Filters.eq("guildId", guildId.toString()));
    }

    public static void mapPlayerToGuild(@NotNull UUID playerUuid, @NotNull UUID guildId) {
        playerGuildCollection.updateOne(
                new Document("_id", playerUuid.toString()),
                Updates.set("guildId", guildId.toString()),
                new UpdateOptions().upsert(true)
        );
    }

    public static void removePlayerMapping(@NotNull UUID playerUuid) {
        playerGuildCollection.deleteOne(Filters.eq("_id", playerUuid.toString()));
    }

    public static UUID getPlayerGuildId(@NotNull UUID playerUuid) {
        Document doc = playerGuildCollection.find(Filters.eq("_id", playerUuid.toString())).first();
        if (doc == null) return null;
        String guildId = doc.getString("guildId");
        return guildId != null ? UUID.fromString(guildId) : null;
    }

    public static boolean guildNameExists(@NotNull String name) {
        return guildCollection.find(Filters.eq("name", name.toLowerCase())).first() != null;
    }

    @Override
    public void set(@NotNull String key, @Nullable Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(@NotNull String key, @Nullable Object def) {
        Document doc = guildCollection.find(Filters.eq("_id", id)).first();
        if (doc == null) return def;
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(@NonNull String key, Object value) {
        guildCollection.updateOne(
                new Document("_id", id),
                Updates.set(key, value),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public boolean remove(@NonNull String id) {
        return guildCollection.deleteOne(new Document("_id", id)).getDeletedCount() > 0;
    }

    @Override
    public boolean exists() {
        Document query = new Document("_id", id);
        return guildCollection.find(query).first() != null;
    }
}
