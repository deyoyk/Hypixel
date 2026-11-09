package com.deyo.hypixel.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.deyo.hypixel.commons.ServerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameManagerTest {

    private final List<RegisteredServer> registered = new ArrayList<>();
    private final Map<RegisteredServer, List<Player>> playersByServer = new IdentityHashMap<>();

    @BeforeEach
    @AfterEach
    void reset() {
        GameManager.clear();
        registered.clear();
        playersByServer.clear();
    }

    @Test
    void indexesServersByIdAndName() {
        RegisteredServer first = server(UUID.randomUUID());
        RegisteredServer second = server(UUID.randomUUID());

        GameManager.GameServer firstGame = GameManager.registerServer(ServerType.SKYBLOCK_HUB, first, 50);
        GameManager.GameServer secondGame = GameManager.registerServer(ServerType.SKYBLOCK_HUB, second, 50);

        assertEquals(firstGame, GameManager.getFromUUID(firstGame.internalID()));
        assertEquals(secondGame, GameManager.getFromRegisteredServer(second));
        assertEquals(ServerType.SKYBLOCK_HUB, GameManager.getTypeFromUUID(firstGame.internalID()));
        assertEquals(ServerType.SKYBLOCK_HUB, GameManager.getTypeFromRegisteredServer(first));
        assertEquals(2, GameManager.getFromType(ServerType.SKYBLOCK_HUB).size());
    }

    @Test
    void typeSnapshotCannotMutateTheRegistry() {
        RegisteredServer server = server(UUID.randomUUID());
        GameManager.registerServer(ServerType.SKYBLOCK_HUB, server, 50);

        List<GameManager.GameServer> snapshot = GameManager.getFromType(ServerType.SKYBLOCK_HUB);
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
        assertEquals(1, GameManager.getFromType(ServerType.SKYBLOCK_HUB).size());
    }

    @Test
    void serverMapSnapshotCannotMutateTheRegistry() {
        RegisteredServer server = server(UUID.randomUUID());
        GameManager.registerServer(ServerType.SKYBLOCK_HUB, server, 50);

        Map<ServerType, List<GameManager.GameServer>> snapshot = GameManager.getServers();
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.get(ServerType.SKYBLOCK_HUB).clear());
        assertEquals(1, GameManager.getFromType(ServerType.SKYBLOCK_HUB).size());
    }

    @Test
    void unknownTypesAndIdsReturnSafeValues() {
        assertFalse(GameManager.isAnyEmpty(ServerType.SKYBLOCK_THE_END));
        assertFalse(GameManager.hasType(ServerType.SKYBLOCK_THE_END));
        assertNull(GameManager.getFromUUID(UUID.randomUUID()));
        assertNull(GameManager.getTypeFromUUID(UUID.randomUUID()));
    }

    @Test
    void emptySlotsReflectConnectedPlayerCount() {
        RegisteredServer server = server(UUID.randomUUID());
        List<Player> players = playersByServer.get(server);

        GameManager.registerServer(ServerType.SKYBLOCK_HUB, server, 2);

        assertTrue(GameManager.isAnyEmpty(ServerType.SKYBLOCK_HUB));
        players.add(null);
        players.add(null);
        assertFalse(GameManager.isAnyEmpty(ServerType.SKYBLOCK_HUB));
    }

    @Test
    void removingAServerClearsEveryIndex() {
        RegisteredServer server = server(UUID.randomUUID());
        GameManager.GameServer gameServer = GameManager.registerServer(ServerType.PROTOTYPE_LOBBY, server, 10);

        GameManager.removeServer(gameServer);

        assertNull(GameManager.getFromUUID(gameServer.internalID()));
        assertNull(GameManager.getFromRegisteredServer(server));
        assertNull(GameManager.getTypeFromUUID(gameServer.internalID()));
        assertTrue(GameManager.getFromType(ServerType.PROTOTYPE_LOBBY).isEmpty());
    }

    @Test
    void clearingTheRegistryAlsoClearsPingState() {
        UUID id = UUID.randomUUID();
        GameManager.pingTracker().recordFailure(id);

        GameManager.clear();

        assertEquals(0, GameManager.pingTracker().missed(id));
    }

    @Test
    void removingAServerForgetsItsPingState() {
        RegisteredServer server = server(UUID.randomUUID());
        UUID id = UUID.fromString(server.getServerInfo().getName());
        GameManager.GameServer gameServer = GameManager.registerServer(ServerType.SKYBLOCK_HUB, server, 10);

        GameManager.pingTracker().recordFailure(id);
        GameManager.removeServer(gameServer);

        assertEquals(0, GameManager.pingTracker().missed(id));
    }

    @Test
    void concurrentRegistrationAndReadsDoNotCorruptTheRegistry() throws Exception {
        int servers = 64;
        int readers = 8;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(servers + readers);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        List<RegisteredServer> serversToRegister = new ArrayList<>();
        for (int index = 0; index < servers; index++) {
            serversToRegister.add(server(UUID.randomUUID()));
        }

        for (int index = 0; index < servers; index++) {
            int serverIndex = index;
            Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    GameManager.registerServer(ServerType.SKYBLOCK_HUB, serversToRegister.get(serverIndex),
                            serverIndex % 2 == 0 ? 10 : 20);
                } catch (Throwable throwable) {
                    failure.compareAndSet(null, throwable);
                } finally {
                    done.countDown();
                }
            });
        }

        for (int index = 0; index < readers; index++) {
            Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    for (int iteration = 0; iteration < 500; iteration++) {
                        GameManager.getFromType(ServerType.SKYBLOCK_HUB).size();
                        GameManager.getServers().values().forEach(List::size);
                    }
                } catch (Throwable throwable) {
                    failure.compareAndSet(null, throwable);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(20, TimeUnit.SECONDS), "workers did not finish");
        assertNull(failure.get());
        assertEquals(servers, GameManager.getFromType(ServerType.SKYBLOCK_HUB).size());
    }

    @Test
    void pingSweepIsCoalescedWithItsTimeout() {
        assertTrue(GameManager.PING_INTERVAL_MS >= GameManager.PING_TIMEOUT_MS);
        assertEquals(5_000, GameManager.PING_INTERVAL_MS);
        assertEquals(5_000, GameManager.PING_TIMEOUT_MS);
    }

    private RegisteredServer server(UUID id) {
        ServerInfo info = new ServerInfo(id.toString(), new InetSocketAddress("127.0.0.1", 20000));
        List<Player> players = new ArrayList<>();
        RegisteredServer server = (RegisteredServer) Proxy.newProxyInstance(
                RegisteredServer.class.getClassLoader(),
                new Class<?>[]{RegisteredServer.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getServerInfo" -> info;
                    case "getPlayersConnected" -> players;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> info.getName();
                    default -> null;
                });
        registered.add(server);
        playersByServer.put(server, players);
        return server;
    }
}
