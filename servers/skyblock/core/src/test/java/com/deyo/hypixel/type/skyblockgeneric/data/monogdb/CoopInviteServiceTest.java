package com.deyo.hypixel.type.skyblockgeneric.data.monogdb;

import com.deyo.hypixel.proxyapi.ProxyPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CoopInviteServiceTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void rejectsSelfInvites() {
        SkyBlockPlayer player = player(UUID.randomUUID());

        assertFalse(CoopInviteService.invite(player,
                List.of(new ProxyPlayer(player.getUuid())), List.of(player.getUsername())));
        assertFalse(CoopInviteService.invite(player, player));
    }

    @Test
    void rejectsDuplicateTargets() {
        SkyBlockPlayer player = player(UUID.randomUUID());
        UUID target = UUID.randomUUID();

        assertFalse(CoopInviteService.invite(player,
                List.of(new ProxyPlayer(target), new ProxyPlayer(target)),
                List.of("Target", "Target")));
    }

    @Test
    void rejectsTooManyTargets() {
        SkyBlockPlayer player = player(UUID.randomUUID());

        assertFalse(CoopInviteService.invite(player,
                List.of(new ProxyPlayer(UUID.randomUUID()), new ProxyPlayer(UUID.randomUUID()),
                        new ProxyPlayer(UUID.randomUUID()), new ProxyPlayer(UUID.randomUUID()),
                        new ProxyPlayer(UUID.randomUUID())),
                List.of("One", "Two", "Three", "Four", "Five")));
    }

    @Test
    void rejectsMissingNames() {
        SkyBlockPlayer player = player(UUID.randomUUID());

        assertFalse(CoopInviteService.invite(player,
                List.of(new ProxyPlayer(UUID.randomUUID())), List.of()));
    }

    private static SkyBlockPlayer player(UUID uuid) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new SkyBlockPlayer(new GameProfile(uuid, "Test"), connection);
    }
}
