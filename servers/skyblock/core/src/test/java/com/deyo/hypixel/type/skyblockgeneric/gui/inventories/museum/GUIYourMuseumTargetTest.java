package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum;

import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIYourMuseumTargetTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void viewingAnotherPlayerUsesTheirMuseumReadOnly() {
        SkyBlockPlayer viewer = player(UUID.randomUUID());
        SkyBlockPlayer target = player(UUID.randomUUID());

        assertSame(target, GUIYourMuseum.owner(viewer, target));
        assertSame(viewer, GUIYourMuseum.owner(viewer, null));
        assertFalse(GUIYourMuseum.canModify(viewer, target));
        assertTrue(GUIYourMuseum.canModify(viewer, viewer));
        assertTrue(GUIYourMuseum.canModify(viewer, null));
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
