package com.deyo.hypixel.commons;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrackedItemTest {

    @Test
    void legacyDocumentsWithoutOptionalFieldsLoad() {
        UUID uuid = UUID.randomUUID();
        Document document = new Document("_id", uuid.toString()).append("item-type", "TEST");

        TrackedItem item = TrackedItem.fromDocument(document);

        assertEquals(uuid, item.getItemUUID());
        assertEquals(0L, item.getCreated());
        assertEquals(0, item.getNumberMade());
        assertTrue(item.getAttachedPlayers().isEmpty());
    }

    @Test
    void cloneDoesNotShareOwnershipLogs() {
        TrackedItem item = TrackedItem.newTrackedItem(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "TEST", 1);

        TrackedItem clone = item.clone();
        clone.addOrUpdateAttachedPlayer(UUID.randomUUID(), UUID.randomUUID());

        assertEquals(1, item.getAttachedPlayers().size());
    }

    @Test
    void documentsRoundTripOwnershipLogs() {
        UUID itemUuid = UUID.randomUUID();
        UUID playerUuid = UUID.randomUUID();
        UUID profileUuid = UUID.randomUUID();
        TrackedItem item = TrackedItem.newTrackedItem(itemUuid, playerUuid, profileUuid, "TEST", 4);

        TrackedItem restored = TrackedItem.fromDocument(item.toDocument());

        assertEquals(itemUuid, restored.getItemUUID());
        assertEquals(5, restored.getNumberMade());
        assertEquals(item.getCreated(), restored.getCreated());
        assertEquals(1, restored.getAttachedPlayers().size());
        assertEquals(playerUuid, restored.getAttachedPlayers().getFirst().playerUUID());
        assertEquals(profileUuid, restored.getAttachedPlayers().getFirst().playerProfileUUID());
    }
}
