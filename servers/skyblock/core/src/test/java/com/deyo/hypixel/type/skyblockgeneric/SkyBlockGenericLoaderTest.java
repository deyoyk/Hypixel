package com.deyo.hypixel.type.skyblockgeneric;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SkyBlockGenericLoaderTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void findMatchesPlayersByUuidDirectly() {
        UUID uuid = UUID.randomUUID();
        SkyBlockPlayer player = player(uuid, "Test");

        assertSame(player, SkyBlockGenericLoader.find(List.of(player), uuid));
        assertNull(SkyBlockGenericLoader.find(List.of(player), UUID.randomUUID()));
        assertNull(SkyBlockGenericLoader.find(List.of(), uuid));
    }

    @Test
    void onlyLoadedOnlinePlayersAreResolved() {
        assertNull(SkyBlockGenericLoader.getFromUUID(UUID.randomUUID()));
        assertNull(SkyBlockGenericLoader.loadedPlayer(null));
        assertNull(SkyBlockGenericLoader.loadedPlayer(player(UUID.randomUUID(), "Test")));
    }

    private static SkyBlockPlayer player(UUID uuid, String name) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new SkyBlockPlayer(new GameProfile(uuid, name), connection);
    }
}
