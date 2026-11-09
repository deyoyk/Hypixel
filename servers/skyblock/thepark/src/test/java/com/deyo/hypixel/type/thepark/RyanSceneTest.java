package com.deyo.hypixel.type.thepark;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RyanSceneTest {

    @Test
    void prunesOnlyExpiredCooldowns() {
        UUID expired = UUID.randomUUID();
        UUID recent = UUID.randomUUID();
        Map<UUID, Long> triggered = new ConcurrentHashMap<>();
        triggered.put(expired, 1_000L);
        triggered.put(recent, 9_000L);

        RyanScene.pruneExpired(triggered, 10_000L, 5_000L);

        assertFalse(triggered.containsKey(expired));
        assertTrue(triggered.containsKey(recent));
    }
}
