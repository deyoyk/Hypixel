package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetGameCountsProtocol;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameCountCacheTest {

    @AfterEach
    void resetCache() {
        GameCountCache.resetForTests();
    }

    @Test
    void concurrentStaleLookupsShareOneRefresh() {
        AtomicInteger requests = new AtomicInteger();
        CompletableFuture<GetGameCountsProtocol.GetGameCountsResponse> pending = new CompletableFuture<>();
        GameCountCache.setRequestHandler(message -> {
            requests.incrementAndGet();
            return pending;
        });

        assertEquals(0, GameCountCache.getPlayerCount(ServerType.BEDWARS_GAME, "SOLO"));
        assertEquals(0, GameCountCache.getPlayerCount(ServerType.BEDWARS_GAME, "SOLO"));
        assertEquals(1, requests.get());

        pending.complete(new GetGameCountsProtocol.GetGameCountsResponse(42, 3, true, null));

        assertEquals(42, GameCountCache.getPlayerCount(ServerType.BEDWARS_GAME, "SOLO"));
        assertEquals(1, requests.get());
    }

    @Test
    void differentKeysRefreshIndependently() {
        AtomicInteger requests = new AtomicInteger();
        GameCountCache.setRequestHandler(message -> {
            requests.incrementAndGet();
            return new CompletableFuture<>();
        });

        GameCountCache.getGameCount(ServerType.BEDWARS_GAME, "SOLO");
        GameCountCache.getGameCount(ServerType.BEDWARS_GAME, "DOUBLES");

        assertEquals(2, requests.get());
    }

    @Test
    void cacheKeyUsesAllModeNamesWhenMissing() {
        assertEquals("BEDWARS_GAME:ALL:ALL",
                GameCountCache.buildCacheKey(ServerType.BEDWARS_GAME, null, null));
        assertEquals("BEDWARS_GAME:SOLO:AQUARIUM",
                GameCountCache.buildCacheKey(ServerType.BEDWARS_GAME, "SOLO", "AQUARIUM"));
    }
}
