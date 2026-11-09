package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.UnderstandableProxyServer;
import com.deyo.hypixel.proxyapi.ProxyInformation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A shared cache for server information with 5-second TTL.
 * Used by game menus, lobby selectors, and NPCs to get player counts.
 */
public class ServerInfoCache {
    private static volatile List<UnderstandableProxyServer> cachedServers = List.of();
    private static volatile long lastCacheTime = 0;
    private static final long CACHE_TTL_MS = 5000;
    private static final AtomicBoolean REFRESHING = new AtomicBoolean();
    private static volatile CompletableFuture<List<UnderstandableProxyServer>> inFlight =
            CompletableFuture.completedFuture(List.of());

    /**
     * Get all servers, using cache if available.
     */
    public static CompletableFuture<List<UnderstandableProxyServer>> getServers() {
        if (!isCacheStale()) {
            return CompletableFuture.completedFuture(List.copyOf(cachedServers));
        }
        return refreshCache();
    }

    /**
     * Get servers of a specific type.
     */
    public static CompletableFuture<List<UnderstandableProxyServer>> getServersByType(ServerType type) {
        return getServers().thenApply(servers ->
                servers.stream().filter(s -> s.type() == type).toList()
        );
    }

    /**
     * Get servers of a specific type from the current snapshot without triggering a refresh.
     */
    public static List<UnderstandableProxyServer> getCachedServersByType(ServerType type) {
        return cachedServers.stream().filter(s -> s.type() == type).toList();
    }

    /**
     * Get total player count for a server type (uses cached data).
     */
    public static int getTotalPlayersForType(ServerType type) {
        ensureRefreshedAsync();
        return cachedServers.stream()
                .filter(s -> s.type() == type)
                .mapToInt(s -> s.players().size())
                .sum();
    }

    /**
     * Get total player count across all SkyBlock server types.
     */
    public static int getTotalSkyBlockPlayers() {
        ensureRefreshedAsync();
        return cachedServers.stream()
                .filter(s -> s.type().isSkyBlock())
                .mapToInt(s -> s.players().size())
                .sum();
    }

    public static void ensureRefreshedAsync() {
        if (isCacheStale()) {
            refreshCache();
        }
    }

    /**
     * Force refresh the cache.
     */
    public static synchronized CompletableFuture<List<UnderstandableProxyServer>> refreshCache() {
        if (!REFRESHING.compareAndSet(false, true)) {
            return inFlight;
        }

        inFlight = new ProxyInformation().getAllServersInformation().thenApply(servers -> {
            cachedServers = List.copyOf(servers);
            lastCacheTime = System.currentTimeMillis();
            return cachedServers;
        });
        inFlight.whenComplete((ignored, throwable) -> REFRESHING.set(false));
        return inFlight;
    }

    /**
     * Check if cache is stale.
     */
    public static boolean isCacheStale() {
        return System.currentTimeMillis() - lastCacheTime >= CACHE_TTL_MS || cachedServers.isEmpty();
    }
}
