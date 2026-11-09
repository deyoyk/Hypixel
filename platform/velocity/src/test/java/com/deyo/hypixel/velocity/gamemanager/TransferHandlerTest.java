package com.deyo.hypixel.velocity.gamemanager;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferHandlerTest {

    private final AtomicReference<RegisteredServer> limbo = new AtomicReference<>();

    @BeforeEach
    @AfterEach
    void reset() throws Exception {
        GameManager.clear();
        setLimboServer(null);
    }

    @Test
    void lobbyOriginsArePreservedForAfkReturn() {
        assertEquals(ServerType.PROTOTYPE_LOBBY,
                TransferHandler.resolveAfkOriginType(ServerType.PROTOTYPE_LOBBY));
        assertEquals(ServerType.BEDWARS_LOBBY,
                TransferHandler.resolveAfkOriginType(ServerType.BEDWARS_LOBBY));
        assertEquals(ServerType.SKYWARS_LOBBY,
                TransferHandler.resolveAfkOriginType(ServerType.SKYWARS_LOBBY));
    }

    @Test
    void gameAndMissingOriginsFallBackToPrototypeLobby() {
        assertEquals(ServerType.PROTOTYPE_LOBBY,
                TransferHandler.resolveAfkOriginType(ServerType.SKYBLOCK_HUB));
        assertEquals(ServerType.PROTOTYPE_LOBBY,
                TransferHandler.resolveAfkOriginType(ServerType.BEDWARS_GAME));
        assertEquals(ServerType.PROTOTYPE_LOBBY,
                TransferHandler.resolveAfkOriginType(null));
    }

    @Test
    void afkLimboSendsToLimboAndReturnsToFallbackLobby() throws Exception {
        RegisteredServer limboServer = registerLimboServer();
        RegisteredServer lobby = registerServer(ServerType.PROTOTYPE_LOBBY, 10);

        AtomicReference<Optional<ServerConnection>> currentServer = new AtomicReference<>(Optional.empty());
        AtomicReference<RegisteredServer> requested = new AtomicReference<>();
        Player player = player(UUID.randomUUID(), currentServer, requested);
        TransferHandler handler = new TransferHandler(player);

        try {
            assertTrue(handler.sendToLimboFromAfk(ServerType.SKYBLOCK_HUB).get(5, TimeUnit.SECONDS));
            assertTrue(handler.isInAfkLimbo());
            assertSame(limboServer, requested.get());

            currentServer.set(Optional.of(connection(player, limboServer)));
            requested.set(null);
            handler.returnFromAfkLimbo();

            await(() -> requested.get() != null);
            assertSame(lobby, requested.get());
            assertFalse(handler.isInAfkLimbo());
        } finally {
            TransferHandler.clearPlayer(player);
        }
    }

    @Test
    void sendToLimboDoesNotReconnectAnAlreadyLimboPlayer() throws Exception {
        RegisteredServer limboServer = registerLimboServer();

        AtomicReference<Optional<ServerConnection>> currentServer =
                new AtomicReference<>(Optional.of(connection(null, limboServer)));
        AtomicReference<RegisteredServer> requested = new AtomicReference<>();
        Player player = player(UUID.randomUUID(), currentServer, requested);
        TransferHandler handler = new TransferHandler(player);
        currentServer.set(Optional.of(connection(player, limboServer)));

        try {
            TransferHandler.playersGoalServerType.put(player, ServerType.SKYBLOCK_HUB);

            assertTrue(handler.sendToLimbo().get(5, TimeUnit.SECONDS));
            assertNull(requested.get());
            assertTrue(handler.isInLimbo());
        } finally {
            TransferHandler.clearPlayer(player);
        }
    }

    @Test
    void clearPlayerForgetsAfkReturnState() throws Exception {
        registerLimboServer();
        AtomicReference<Optional<ServerConnection>> currentServer = new AtomicReference<>(Optional.empty());
        AtomicReference<RegisteredServer> requested = new AtomicReference<>();
        Player player = player(UUID.randomUUID(), currentServer, requested);
        TransferHandler handler = new TransferHandler(player);

        assertTrue(handler.sendToLimboFromAfk(ServerType.SKYWARS_GAME).get(5, TimeUnit.SECONDS));
        assertTrue(handler.isInAfkLimbo());

        TransferHandler.clearPlayer(player);

        assertFalse(handler.isInAfkLimbo());
        assertFalse(handler.isInLimbo());
    }

    private RegisteredServer registerLimboServer() {
        RegisteredServer server = server("limbo", new ArrayList<>());
        limbo.set(server);
        try {
            setLimboServer(server);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
        return server;
    }

    private static RegisteredServer registerServer(ServerType type, int maxPlayers) {
        RegisteredServer server = server(UUID.randomUUID().toString(), new ArrayList<>());
        GameManager.registerServer(type, server, maxPlayers);
        return server;
    }

    private static RegisteredServer server(String name, java.util.List<Player> players) {
        ServerInfo info = new ServerInfo(name, new InetSocketAddress("127.0.0.1", 20000));
        return (RegisteredServer) Proxy.newProxyInstance(
                RegisteredServer.class.getClassLoader(),
                new Class<?>[]{RegisteredServer.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getServerInfo" -> info;
                    case "getPlayersConnected" -> players;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> name;
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static Player player(UUID uuid,
                                 AtomicReference<Optional<ServerConnection>> currentServer,
                                 AtomicReference<RegisteredServer> requested) {
        return (Player) Proxy.newProxyInstance(
                Player.class.getClassLoader(),
                new Class<?>[]{Player.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getUniqueId" -> uuid;
                    case "getUsername" -> "tester";
                    case "getCurrentServer" -> currentServer.get();
                    case "createConnectionRequest" -> {
                        requested.set((RegisteredServer) args[0]);
                        yield connectionRequest();
                    }
                    case "sendMessage" -> null;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> "tester";
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static ServerConnection connection(Player player, RegisteredServer server) {
        return (ServerConnection) Proxy.newProxyInstance(
                ServerConnection.class.getClassLoader(),
                new Class<?>[]{ServerConnection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getServer" -> server;
                    case "getPlayer" -> player;
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> server.getServerInfo().getName();
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static ConnectionRequestBuilder connectionRequest() {
        return (ConnectionRequestBuilder) Proxy.newProxyInstance(
                ConnectionRequestBuilder.class.getClassLoader(),
                new Class<?>[]{ConnectionRequestBuilder.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "connect", "connectWithIndication" -> CompletableFuture.completedFuture(null);
                    case "equals" -> proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static void await(BooleanSupplier condition) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
            Thread.sleep(10);
        }
        assertTrue(condition.getAsBoolean());
    }

    private static void setLimboServer(RegisteredServer server) throws Exception {
        Field field = SkyBlockVelocity.class.getDeclaredField("limboServer");
        field.setAccessible(true);
        field.set(null, server);
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == char.class) {
            return '\0';
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0F;
        }
        return 0D;
    }
}
