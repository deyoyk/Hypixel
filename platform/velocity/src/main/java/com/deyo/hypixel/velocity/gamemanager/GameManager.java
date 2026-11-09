package com.deyo.hypixel.velocity.gamemanager;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.PingServerProtocol;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.velocity.testflow.TestFlowManager;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManager {
    public static final int SLEEP_TIME = 300;
    public static final int PING_INTERVAL_MS = 5_000;
    public static final int PING_TIMEOUT_MS = 5_000;
    public static final int MISSED_PINGS_BEFORE_REMOVAL = 5;
    private static final Map<ServerType, List<GameServer>> servers = new ConcurrentHashMap<>();
    private static final Map<UUID, GameServer> serversById = new ConcurrentHashMap<>();
    private static final Map<UUID, ServerType> typesById = new ConcurrentHashMap<>();
    private static final Map<String, GameServer> serversByName = new ConcurrentHashMap<>();
    private static final ServerPingTracker pingTracker = new ServerPingTracker(MISSED_PINGS_BEFORE_REMOVAL);
    private static volatile Map<ServerType, List<GameServer>> snapshot = Map.of();

    public static synchronized GameServer addServer(ServerType type, UUID serverID, String host, int port, int maxPlayers) {
        pingTracker.forget(serverID);
        port = port == -1 ? getNextAvailablePort() : port;    // if port is -1 then get next available port
        host = (host == null || host.isEmpty()) ? ConfigProvider.settings().getHostName() : host; // if host is null then get from config
        RegisteredServer registeredServer = SkyBlockVelocity.getServer().registerServer(
                new ServerInfo(serverID.toString(), new InetSocketAddress(host, port))
        );
        return registerServer(type, registeredServer, maxPlayers);
    }

    static synchronized GameServer registerServer(ServerType type, RegisteredServer registeredServer, int maxPlayers) {
        UUID serverID = UUID.fromString(registeredServer.getServerInfo().getName());
        pingTracker.forget(serverID);

        boolean isLobby = type.name().endsWith("_LOBBY");
        String rootName = isLobby ? "L" : (maxPlayers <= 20 ? "mini" : "mega");
        String shortenedRootName = isLobby ? "L" : (maxPlayers <= 20 ? "m" : "M");

        char letter = (char) (java.util.concurrent.ThreadLocalRandom.current().nextInt(26) + (isLobby ? 'a' : 'A'));
        String displayName = getNextAvailableDisplayName() + "" + letter;

        GameServer server = new GameServer(
                rootName + displayName, shortenedRootName + displayName,
                serverID, registeredServer, maxPlayers
        );
        serversById.put(serverID, server);
        typesById.put(serverID, type);
        serversByName.put(registeredServer.getServerInfo().getName(), server);
        servers.computeIfAbsent(type, ignored -> new CopyOnWriteArrayList<>()).add(server);
        refreshSnapshot();

        return server;
    }

    public static synchronized void clear() {
        servers.clear();
        serversById.clear();
        typesById.clear();
        serversByName.clear();
        pingTracker.clear();
        snapshot = Map.of();
    }

    public static synchronized void removeServer(GameServer server) {
        serversById.remove(server.internalID(), server);
        typesById.remove(server.internalID());
        serversByName.remove(server.registeredServer().getServerInfo().getName(), server);
        pingTracker.forget(server.internalID());
        for (List<GameServer> gameServers : servers.values()) {
            gameServers.remove(server);
        }
        refreshSnapshot();
    }

    private static synchronized void refreshSnapshot() {
        Map<ServerType, List<GameServer>> copy = new java.util.EnumMap<>(ServerType.class);
        servers.forEach((type, gameServers) -> copy.put(type, List.copyOf(gameServers)));
        snapshot = Collections.unmodifiableMap(copy);
    }

    static ServerPingTracker pingTracker() {
        return pingTracker;
    }

    public static boolean isAnyEmpty(ServerType type) {
        for (GameServer gameServer : getFromType(type)) {
            if (gameServer.hasEmptySlots()) return true;
        }
        return false;
    }

    public static @Nullable GameServer getFromRegisteredServer(RegisteredServer registeredServer) {
        return serversByName.get(registeredServer.getServerInfo().getName());
    }

    public static @Nullable ServerType getTypeFromRegisteredServer(RegisteredServer registeredServer) {
        GameServer gameServer = getFromRegisteredServer(registeredServer);
        return gameServer != null ? typesById.get(gameServer.internalID()) : null;
    }

    public static @Nullable ServerType getTypeFromUUID(UUID uuid) {
        return typesById.get(uuid);
    }

    public static boolean hasType(ServerType type) {
        return !getFromType(type).isEmpty();
    }

    public static List<GameServer> getFromType(ServerType type) {
        return snapshot.getOrDefault(type, List.of());
    }

    public static @Nullable GameServer getFromUUID(UUID uuid) {
        return serversById.get(uuid);
    }

    public static void loopServers(ProxyServer server) {
        server.getScheduler().buildTask(SkyBlockVelocity.getPlugin(), () -> {
            for (List<GameServer> registeredServers : servers.values()) {
                for (GameServer registeredServer : registeredServers) {
                    RegisteredServer givenServer = registeredServer.registeredServer();
                    AtomicBoolean pingSuccess = new AtomicBoolean(false);
                    long startTime = System.currentTimeMillis();

                    RedisClient.requestServer(registeredServer.internalID(),
                            new PingServerProtocol(), new PingServerProtocol.Request()
                    ).thenRun(() -> {
                        pingSuccess.set(true);
                        pingTracker.recordSuccess(registeredServer.internalID());
                    });

                    server.getScheduler().buildTask(SkyBlockVelocity.getPlugin(), () -> {
                        if (pingSuccess.get()) return;

                        if (!pingTracker.recordFailure(registeredServer.internalID())) {
                            Logger.warn(
                                "Server {} missed a ping ({}/{}, sent at {}, deadline {}ms)",
                                givenServer.getServerInfo().getName(),
                                pingTracker.missed(registeredServer.internalID()),
                                MISSED_PINGS_BEFORE_REMOVAL,
                                startTime,
                                System.currentTimeMillis() - startTime
                            );
                            return;
                        }

                        Logger.warn(
                            "Server {} is offline! Removing from list after {} missed pings (last ping sent at {}, deadline {}ms)",
                            givenServer.getServerInfo().getName(),
                            MISSED_PINGS_BEFORE_REMOVAL,
                            startTime,
                            System.currentTimeMillis() - startTime
                        );
                        removeServer(registeredServer);
                        TestFlowManager.handleServerDisconnect(registeredServer.internalID());
                    }).delay(Duration.ofMillis(PING_TIMEOUT_MS)).schedule();
                }
            }
        }).repeat(Duration.ofMillis(PING_INTERVAL_MS)).schedule();
    }

    private static int getNextAvailableDisplayName() {
        Set<Integer> used = new java.util.HashSet<>();
        for (List<GameServer> list : servers.values()) {
            for (GameServer gs : list) {
                String name = gs.displayName();
                int number = 0;
                boolean found = false;
                for (int i = 0; i < name.length(); i++) {
                    char c = name.charAt(i);
                    if (c >= '0' && c <= '9') {
                        number = number * 10 + (c - '0');
                        found = true;
                    }
                }
                if (found && number > 0) used.add(number);
            }
        }
        int candidate = 1;
        while (used.contains(candidate)) candidate++;
        return candidate;
    }


    private static int getNextAvailablePort() {
        int highestPort = 19999;
        for (List<GameServer> list : servers.values()) {
            for (GameServer gameServer : list) {
                highestPort = Math.max(highestPort,
                        gameServer.registeredServer().getServerInfo().getAddress().getPort());
            }
        }
        return highestPort + 1;
    }

    public static Map<ServerType, List<GameServer>> getServers() {
        return snapshot;
    }

    public record GameServer(String displayName, String shortDisplayName,
                             UUID internalID, RegisteredServer registeredServer,
                             int maxPlayers) {
        public boolean hasEmptySlots() {
            return maxPlayers > registeredServer().getPlayersConnected().size();
        }
    }
}
