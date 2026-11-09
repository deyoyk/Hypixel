package com.deyo.hypixel.velocity.gamemanager.impl;

import com.velocitypowered.api.proxy.Player;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.DoesServerHaveIslandProtocol;
import com.deyo.hypixel.velocity.data.ProfilesDatabase;
import com.deyo.hypixel.velocity.data.UserDatabase;
import com.deyo.hypixel.velocity.gamemanager.BalanceConfiguration;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisClient;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.ToLongFunction;

public class IslandCheck extends BalanceConfiguration {
    private static final long LOOKUP_TIMEOUT_MILLIS = 3000;
    private static final long CACHE_TTL_MILLIS = 10_000;
    private static final Map<UUID, CachedIsland> ISLAND_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, CachedServer> SERVER_CACHE = new ConcurrentHashMap<>();
    private static final AtomicLong LAST_PRUNE = new AtomicLong();

    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        UUID playerId = player.getUniqueId();
        long now = System.currentTimeMillis();
        pruneCaches(now);
        CachedIsland cachedIsland = ISLAND_CACHE.get(playerId);
        if (cachedIsland != null && isFresh(cachedIsland.cachedAt(), now)) {
            return cachedIsland.islandUUID() == null ? null : findServerWithIsland(cachedIsland.islandUUID());
        }

        Document userDatabase = new UserDatabase(playerId).getDocument();
        UUID activeProfile = selectedProfile(userDatabase);
        if (activeProfile == null) {
            ISLAND_CACHE.put(playerId, new CachedIsland(null, now));
            return null;
        }

        Document document = new ProfilesDatabase(activeProfile.toString()).getDocument();
        UUID islandUUID = islandUuid(document);
        if (islandUUID == null) {
            ISLAND_CACHE.put(playerId, new CachedIsland(null, now));
            return null;
        }

        ISLAND_CACHE.put(playerId, new CachedIsland(islandUUID, now));
        return findServerWithIsland(islandUUID);
    }

    static @Nullable UUID selectedProfile(Document userDatabase) {
        return userDatabase == null ? null : parseUuid(userDatabase.getString("selected"));
    }

    static @Nullable UUID islandUuid(Document profileDocument) {
        if (profileDocument == null) return null;

        String island = profileDocument.getString("island_uuid");
        if (island == null) return null;

        String normalized = island.replace("\"", "");
        return normalized.equals("null") ? null : parseUuid(normalized);
    }

    private static @Nullable UUID parseUuid(String value) {
        if (value == null) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    static boolean isFresh(long cachedAt, long now) {
        return now - cachedAt <= CACHE_TTL_MILLIS;
    }

    static <T> void removeExpired(Map<UUID, T> cache, long now, ToLongFunction<T> cachedAt) {
        cache.entrySet().removeIf(entry -> !isFresh(cachedAt.applyAsLong(entry.getValue()), now));
    }

    private static void pruneCaches(long now) {
        long previous = LAST_PRUNE.get();
        if (now - previous < CACHE_TTL_MILLIS || !LAST_PRUNE.compareAndSet(previous, now)) return;

        removeExpired(ISLAND_CACHE, now, CachedIsland::cachedAt);
        removeExpired(SERVER_CACHE, now, CachedServer::cachedAt);
    }

    private static GameManager.GameServer findServerWithIsland(UUID islandUUID) {
        long now = System.currentTimeMillis();
        CachedServer cachedServer = SERVER_CACHE.get(islandUUID);
        if (cachedServer != null
                && isFresh(cachedServer.cachedAt(), now)
                && GameManager.getFromUUID(cachedServer.server().internalID()) != null) {
            return cachedServer.server();
        }

        Map<GameManager.GameServer, CompletableFuture<DoesServerHaveIslandProtocol.Response>> responses = new LinkedHashMap<>();

        for (GameManager.GameServer gameServer : GameManager.getFromType(ServerType.SKYBLOCK_ISLAND)) {
            responses.put(gameServer, RedisClient.requestServer(
                gameServer.internalID(),
                new DoesServerHaveIslandProtocol(),
                new DoesServerHaveIslandProtocol.Request(islandUUID.toString())));
        }

        if (responses.isEmpty()) return null;

        CompletableFuture<?>[] waits = responses.values().stream()
            .map(future -> future.exceptionally(ignored -> null))
            .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(waits)
            .orTimeout(LOOKUP_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .exceptionally(ignored -> null)
            .join();

        GameManager.GameServer toSendTo = null;
        for (Map.Entry<GameManager.GameServer, CompletableFuture<DoesServerHaveIslandProtocol.Response>> entry : responses.entrySet()) {
            DoesServerHaveIslandProtocol.Response response = entry.getValue().getNow(null);
            if (response != null && response.serverHasIt()) {
                toSendTo = entry.getKey();
            }
        }

        if (toSendTo != null) {
            SERVER_CACHE.put(islandUUID, new CachedServer(toSendTo, now));
        }
        return toSendTo;
    }

    private record CachedIsland(UUID islandUUID, long cachedAt) {
    }

    private record CachedServer(GameManager.GameServer server, long cachedAt) {
    }
}
