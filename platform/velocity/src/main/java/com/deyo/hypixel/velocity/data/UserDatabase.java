package com.deyo.hypixel.velocity.data;

import com.deyo.hypixel.libs.PlayerField;
import com.deyo.hypixel.libs.codec.Codecs;
import com.deyo.hypixel.commons.data.DeyoData;
import org.bson.Document;

import java.util.UUID;

public class UserDatabase {
    private static final PlayerField<String> PROFILES_INDEX =
            PlayerField.create("hypixel", "_profiles_index", Codecs.STRING, null);

    public UUID id;

    public UserDatabase(UUID id) {
        this.id = id;
    }

    public UserDatabase(String id) {
        this.id = UUID.fromString(id);
    }

    public static void connect(String connectionString) {
    }

    public Document getDocument() {
        String stored = read(id);
        if (stored == null || stored.isEmpty()) return null;
        String[] parts = stored.split(";", 2);
        if (parts[0].isEmpty()) return null;
        return new Document("_id", id.toString()).append("selected", parts[0]);
    }

    private static String read(UUID uuid) {
        try {
            return DeyoData.account().get(uuid, PROFILES_INDEX);
        } finally {
            DeyoData.account().unload(uuid);
        }
    }
}
