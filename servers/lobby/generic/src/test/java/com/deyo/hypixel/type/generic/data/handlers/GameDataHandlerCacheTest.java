package com.deyo.hypixel.type.generic.data.handlers;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameDataHandlerCacheTest {

    @Test
    void gameDataCachesSupportConcurrentAccess() {
        assertConcurrent(BedWarsDataHandler.bedwarsCache);
        assertConcurrent(SkywarsDataHandler.skywarsCache);
        assertConcurrent(MurderMysteryDataHandler.murderMysteryCache);
        assertConcurrent(ArcadeDataHandler.arcadeCache);
        assertConcurrent(PrototypeLobbyDataHandler.prototypeLobbyCache);
        assertConcurrent(ReplayDataHandler.replayViewerCache);
    }

    private static void assertConcurrent(Map<UUID, ?> cache) {
        assertTrue(cache instanceof ConcurrentHashMap<?, ?>, cache.getClass().getName());
    }
}
