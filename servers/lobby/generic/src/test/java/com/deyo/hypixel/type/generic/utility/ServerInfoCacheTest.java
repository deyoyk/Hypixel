package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.UnderstandableProxyServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerInfoCacheTest {

    private static final List<UnderstandableProxyServer> SERVERS = List.of(
            server(ServerType.SKYBLOCK_HUB, 5),
            server(ServerType.SKYBLOCK_ISLAND, 3),
            server(ServerType.BEDWARS_LOBBY, 2)
    );

    @AfterEach
    void resetCache() throws Exception {
        setStatic("cachedServers", List.of());
        setStatic("lastCacheTime", 0L);
    }

    @Test
    void typeFilteringUsesTheCachedSnapshot() throws Exception {
        primeCache();

        List<UnderstandableProxyServer> hubs = ServerInfoCache
                .getServersByType(ServerType.SKYBLOCK_HUB)
                .join();

        assertEquals(1, hubs.size());
        assertEquals(ServerType.SKYBLOCK_HUB, hubs.getFirst().type());
        assertFalse(ServerInfoCache.isCacheStale());
    }

    @Test
    void cachedFilteringNeverTriggersARefresh() throws Exception {
        primeCache();

        List<UnderstandableProxyServer> hubs = ServerInfoCache.getCachedServersByType(ServerType.SKYBLOCK_HUB);

        assertEquals(1, hubs.size());
        assertEquals(ServerType.SKYBLOCK_HUB, hubs.getFirst().type());
    }

    @Test
    void playerCountsAreAggregatedFromCachedServers() throws Exception {
        primeCache();

        assertEquals(5, ServerInfoCache.getTotalPlayersForType(ServerType.SKYBLOCK_HUB));
        assertEquals(8, ServerInfoCache.getTotalSkyBlockPlayers());
    }

    @Test
    void cacheIsStaleWhenExpiredOrEmpty() throws Exception {
        primeCache();
        assertFalse(ServerInfoCache.isCacheStale());

        setStatic("lastCacheTime", System.currentTimeMillis() - 60_000);
        assertTrue(ServerInfoCache.isCacheStale());

        setStatic("cachedServers", List.of());
        setStatic("lastCacheTime", System.currentTimeMillis());
        assertTrue(ServerInfoCache.isCacheStale());
    }

    private static void primeCache() throws Exception {
        setStatic("cachedServers", SERVERS);
        setStatic("lastCacheTime", System.currentTimeMillis());
    }

    private static void setStatic(String name, Object value) throws Exception {
        Field field = ServerInfoCache.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(null, value);
    }

    private static UnderstandableProxyServer server(ServerType type, int players) {
        return new UnderstandableProxyServer(
                type.name() + "-" + UUID.randomUUID(),
                UUID.randomUUID(),
                type,
                25565,
                java.util.stream.IntStream.range(0, players)
                        .mapToObj(ignored -> UUID.randomUUID())
                        .toList(),
                100,
                type.formatName()
        );
    }
}
