package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.banker;

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

class GUIPersonalVaultTargetTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void viewingAnotherPlayerUsesTheirVaultReadOnly() {
        SkyBlockPlayer viewer = player(UUID.randomUUID());
        SkyBlockPlayer target = player(UUID.randomUUID());

        assertSame(target, GUIPersonalVault.owner(viewer, target));
        assertSame(viewer, GUIPersonalVault.owner(viewer, null));
        assertFalse(GUIPersonalVault.editable(viewer, target));
        assertTrue(GUIPersonalVault.editable(viewer, viewer));
        assertTrue(GUIPersonalVault.editable(viewer, null));
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
