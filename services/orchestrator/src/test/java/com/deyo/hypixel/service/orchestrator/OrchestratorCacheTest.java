package com.deyo.hypixel.service.orchestrator;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.game.game.GameObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrchestratorCacheTest {

    @AfterEach
    void cleanup() {
        OrchestratorCache.clear();
    }

    @Test
    void nullServerTypeCountsTheWholeNetwork() {
        OrchestratorCache.handleHeartbeat(UUID.randomUUID(), "L1a", ServerType.SKYBLOCK_HUB, 100, 5,
                List.of(game(ServerType.SKYBLOCK_HUB, 3)));

        OrchestratorCache.GameCountStats all = OrchestratorCache.getGameCounts(null, null, null);
        assertEquals(3, all.playerCount());
        assertEquals(1, all.gameCount());

        OrchestratorCache.GameCountStats filtered = OrchestratorCache.getGameCounts(ServerType.BEDWARS_GAME, null, null);
        assertEquals(0, filtered.playerCount());
        assertEquals(0, filtered.gameCount());
    }

    @Test
    void filtersByGameTypeAndMap() {
        OrchestratorCache.handleHeartbeat(UUID.randomUUID(), "B1a", ServerType.BEDWARS_GAME, 16, 4,
                List.of(game(ServerType.BEDWARS_GAME, "SOLO", "Lighthouse", 2),
                        game(ServerType.BEDWARS_GAME, "DOUBLES", "Lighthouse", 4)));

        assertEquals(4, OrchestratorCache.getGameCounts(ServerType.BEDWARS_GAME, "DOUBLES", null).playerCount());
        assertEquals(6, OrchestratorCache.getGameCounts(ServerType.BEDWARS_GAME, null, "Lighthouse").playerCount());
        assertEquals(2, OrchestratorCache.getGameCounts(ServerType.BEDWARS_GAME, "SOLO", "Lighthouse").playerCount());
    }

    @Test
    void cleanupIsThrottledButRemovesStaleServers() {
        long now = System.currentTimeMillis();
        OrchestratorCache.handleHeartbeat(UUID.randomUUID(), "S1", ServerType.SKYBLOCK_HUB, 100, 5,
                List.of(game(ServerType.SKYBLOCK_HUB, 3)));

        OrchestratorCache.cleanup(now + 60_000);
        assertEquals(0, OrchestratorCache.getAllServers().size());

        OrchestratorCache.handleHeartbeat(UUID.randomUUID(), "S2", ServerType.SKYBLOCK_HUB, 100, 5,
                List.of(game(ServerType.SKYBLOCK_HUB, 3)));

        OrchestratorCache.cleanup(now + 60_001);
        assertEquals(1, OrchestratorCache.getAllServers().size());

        OrchestratorCache.cleanup(now + 65_000);
        assertEquals(0, OrchestratorCache.getAllServers().size());
    }

    private static GameObject game(ServerType type, int players) {
        return game(type, "SOLO", "Lighthouse", players);
    }

    private static GameObject game(ServerType type, String gameType, String map, int players) {
        List<UUID> involved = java.util.stream.IntStream.range(0, players)
                .mapToObj(ignored -> UUID.randomUUID())
                .toList();
        return new GameObject(involved, List.of(), UUID.randomUUID(), type, map, gameType, true);
    }
}
