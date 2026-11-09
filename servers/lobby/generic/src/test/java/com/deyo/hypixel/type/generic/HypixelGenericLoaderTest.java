package com.deyo.hypixel.type.generic;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class HypixelGenericLoaderTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void findMatchesPlayersByUuidDirectly() {
        UUID uuid = UUID.randomUUID();
        HypixelPlayer player = player(uuid, "Test");

        assertSame(player, HypixelGenericLoader.find(List.of(player), uuid));
        assertNull(HypixelGenericLoader.find(List.of(player), UUID.randomUUID()));
        assertNull(HypixelGenericLoader.find(List.of(), uuid));
    }

    @Test
    void onlyLoadedOnlinePlayersAreResolved() {
        assertNull(HypixelGenericLoader.getFromUUID(UUID.randomUUID()));
        assertNull(HypixelGenericLoader.loadedPlayer(null));
        assertNull(HypixelGenericLoader.loadedPlayer(player(UUID.randomUUID(), "Test")));
    }

    private static HypixelPlayer player(UUID uuid, String name) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new HypixelPlayer(connection, new GameProfile(uuid, name));
    }
}
