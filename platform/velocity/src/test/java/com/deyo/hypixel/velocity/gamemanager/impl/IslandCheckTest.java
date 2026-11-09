package com.deyo.hypixel.velocity.gamemanager.impl;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IslandCheckTest {

    @Test
    void cacheEntriesExpireAfterTenSeconds() {
        long now = 100_000;
        assertTrue(IslandCheck.isFresh(now - 10_000, now));
        assertFalse(IslandCheck.isFresh(now - 10_001, now));
    }

    @Test
    void expiredCacheEntriesArePruned() {
        Map<UUID, Long> cache = new HashMap<>();
        UUID fresh = UUID.randomUUID();
        UUID stale = UUID.randomUUID();
        cache.put(fresh, 95_000L);
        cache.put(stale, 80_000L);

        IslandCheck.removeExpired(cache, 100_000, value -> value);

        assertEquals(Map.of(fresh, 95_000L), cache);
    }

    @Test
    void selectedProfileHandlesMissingNullAndMalformedValues() {
        assertNull(IslandCheck.selectedProfile(null));
        assertNull(IslandCheck.selectedProfile(new Document()));
        assertNull(IslandCheck.selectedProfile(new Document("selected", null)));
        assertNull(IslandCheck.selectedProfile(new Document("selected", "not-a-uuid")));

        UUID profile = UUID.randomUUID();
        assertEquals(profile, IslandCheck.selectedProfile(
                new Document("selected", profile.toString())));
    }

    @Test
    void islandUuidHandlesMissingNullAndQuotedNullValues() {
        assertNull(IslandCheck.islandUuid(null));
        assertNull(IslandCheck.islandUuid(new Document()));
        assertNull(IslandCheck.islandUuid(new Document("island_uuid", null)));
        assertNull(IslandCheck.islandUuid(new Document("island_uuid", "null")));
        assertNull(IslandCheck.islandUuid(new Document("island_uuid", "\"null\"")));
        assertNull(IslandCheck.islandUuid(new Document("island_uuid", "not-a-uuid")));

        UUID island = UUID.randomUUID();
        assertEquals(island, IslandCheck.islandUuid(
                new Document("island_uuid", "\"" + island + "\"")));
    }
}
