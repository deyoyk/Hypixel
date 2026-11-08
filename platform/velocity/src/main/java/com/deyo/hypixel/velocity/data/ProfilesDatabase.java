package com.deyo.hypixel.velocity.data;

import com.deyo.hypixel.libs.PlayerField;
import com.deyo.hypixel.libs.codec.Codecs;
import com.deyo.hypixel.commons.data.NameIndex;
import com.deyo.hypixel.commons.data.DeyoData;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public record ProfilesDatabase(String id) implements MongoDB {
    private static final PlayerField<String> DOCUMENT =
            PlayerField.create("skyblock", "_doc", Codecs.STRING, null);

    @Override
    public MongoDB connect(String connectionString) {
        return this;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = getDocument();
        return doc == null ? def : doc.getOrDefault(key, def);
    }

    public List<Document> getAll() {
        return List.of();
    }

    public Document getDocument() {
        String json = read(UUID.fromString(id));
        return json == null ? null : Document.parse(json);
    }

    @Override
    public boolean remove(String uniqueId) {
        write(UUID.fromString(uniqueId), null);
        return true;
    }

    public void insertOrUpdate(String key, Object value) {
        Document doc = getDocument();
        if (doc == null) doc = new Document("_id", id);
        doc.put(key, value);
        write(UUID.fromString(id), doc.toJson());
    }

    public boolean exists() {
        return read(UUID.fromString(id)) != null;
    }

    public static UUID fetchUUID(String username) {
        return NameIndex.lookup(username);
    }

    public static Document fetchDocument(String uniqueId) {
        String json = read(UUID.fromString(uniqueId));
        return json == null ? null : Document.parse(json);
    }

    private static String read(UUID profileId) {
        try {
            return DeyoData.profile().get(profileId, DOCUMENT);
        } finally {
            DeyoData.profile().unload(profileId);
        }
    }

    private static void write(UUID profileId, String json) {
        try {
            DeyoData.profile().set(profileId, DOCUMENT, json);
        } finally {
            DeyoData.profile().unload(profileId);
        }
    }
}

