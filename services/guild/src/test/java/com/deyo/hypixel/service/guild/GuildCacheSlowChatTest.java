package com.deyo.hypixel.service.guild;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildCacheSlowChatTest {

    @Test
    void prunesExpiredSlowChatTimestamps() {
        UUID expired = UUID.randomUUID();
        UUID active = UUID.randomUUID();
        Map<UUID, Long> timestamps = new HashMap<>();
        timestamps.put(expired, 80_000L);
        timestamps.put(active, 95_000L);

        int removed = GuildCache.pruneSlowChat(timestamps, 100_000L, 10_000L);

        assertEquals(1, removed);
        assertFalse(timestamps.containsKey(expired));
        assertTrue(timestamps.containsKey(active));
    }
}
