package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetGameCountsProtocol;
import com.deyo.hypixel.proxyapi.ProxyService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Shared cache for game player counts.
 * Used by lobby NPCs to display per-mode player counts.
 */
public class GameCountCache {
    private static final Map<String, CachedCount> cache = new ConcurrentHashMap<>();
    private static final Set<String> refreshing = ConcurrentHashMap.newKeySet();
    private static final long CACHE_TTL_MS = 5000; // 5 seconds
    private static final long REQUEST_TIMEOUT_SECONDS = 10;

    private static final Function<GetGameCountsProtocol.GetGameCountsMessage,
            CompletableFuture<GetGameCountsProtocol.GetGameCountsResponse>> DEFAULT_REQUEST =
            message -> new ProxyService(ServiceType.ORCHESTRATOR)
                    .<GetGameCountsProtocol.GetGameCountsMessage,
                            GetGameCountsProtocol.GetGameCountsResponse>handleRequest(message);

    private static volatile Function<GetGameCountsProtocol.GetGameCountsMessage,
            CompletableFuture<GetGameCountsProtocol.GetGameCountsResponse>> requestHandler = DEFAULT_REQUEST;

    /**
     * Get the player count for a specific server type and game type.
     * Returns cached value if fresh, otherwise triggers async refresh and returns stale/0.
     *
     * @param type The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName The game mode name (e.g., "CLASSIC", "SOLO"), or null for all modes
     * @return The player count (may be stale during refresh)
     */
    public static int getPlayerCount(ServerType type, String gameTypeName) {
        return getPlayerCount(type, gameTypeName, null);
    }

    /**
     * Get the player count for a specific server type, game type, and map.
     *
     * @param type The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName The game mode name (e.g., "CLASSIC", "SOLO"), or null for all modes
     * @param mapName The map name, or null for all maps
     * @return The player count (may be stale during refresh)
     */
    public static int getPlayerCount(ServerType type, String gameTypeName, String mapName) {
        String key = buildCacheKey(type, gameTypeName, mapName);
        CachedCount cached = cache.get(key);

        long now = System.currentTimeMillis();

        if (cached == null || now - cached.timestamp > CACHE_TTL_MS) {
            // Trigger async refresh
            refreshAsync(type, gameTypeName, mapName, key);

            // Return stale value if available, otherwise 0
            return cached != null ? cached.playerCount : 0;
        }

        return cached.playerCount;
    }

    /**
     * Get the game count for a specific server type and game type.
     *
     * @param type The server type
     * @param gameTypeName The game mode name, or null for all modes
     * @return The game count (may be stale during refresh)
     */
    public static int getGameCount(ServerType type, String gameTypeName) {
        return getGameCount(type, gameTypeName, null);
    }

    /**
     * Get the game count for a specific server type, game type, and map.
     *
     * @param type The server type
     * @param gameTypeName The game mode name, or null for all modes
     * @param mapName The map name, or null for all maps
     * @return The game count (may be stale during refresh)
     */
    public static int getGameCount(ServerType type, String gameTypeName, String mapName) {
        String key = buildCacheKey(type, gameTypeName, mapName);
        CachedCount cached = cache.get(key);

        long now = System.currentTimeMillis();

        if (cached == null || now - cached.timestamp > CACHE_TTL_MS) {
            // Trigger async refresh
            refreshAsync(type, gameTypeName, mapName, key);

            // Return stale value if available, otherwise 0
            return cached != null ? cached.gameCount : 0;
        }

        return cached.gameCount;
    }

    static String buildCacheKey(ServerType type, String gameTypeName, String mapName) {
        StringBuilder key = new StringBuilder(type.name());
        key.append(":").append(gameTypeName != null ? gameTypeName : "ALL");
        key.append(":").append(mapName != null ? mapName : "ALL");
        return key.toString();
    }

    private static void refreshAsync(ServerType type, String gameTypeName, String mapName, String cacheKey) {
        if (!refreshing.add(cacheKey)) {
            return;
        }

        var message = new GetGameCountsProtocol.GetGameCountsMessage(type, gameTypeName, mapName);
        CompletableFuture<GetGameCountsProtocol.GetGameCountsResponse> future;
        try {
            future = requestHandler.apply(message);
        } catch (RuntimeException exception) {
            refreshing.remove(cacheKey);
            return;
        }

        future.orTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .thenAccept(response -> {
                    if (response != null) {
                        cache.put(cacheKey, new CachedCount(
                                response.playerCount(),
                                response.gameCount(),
                                System.currentTimeMillis()
                        ));
                    }
                })
                .whenComplete((_, _) -> refreshing.remove(cacheKey));
    }

    static void setRequestHandler(Function<GetGameCountsProtocol.GetGameCountsMessage,
            CompletableFuture<GetGameCountsProtocol.GetGameCountsResponse>> handler) {
        requestHandler = handler;
    }

    static void resetForTests() {
        cache.clear();
        refreshing.clear();
        requestHandler = DEFAULT_REQUEST;
    }

    private record CachedCount(int playerCount, int gameCount, long timestamp) {}
}
