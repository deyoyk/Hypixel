package com.deyo.hypixel.type.bedwarsgame.gui;

import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIBedWarsTeleporterTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void excludesTheViewerAndEliminatedPlayers() {
        BedWarsPlayer viewer = player("viewer");
        BedWarsPlayer alive = player("alive");
        BedWarsPlayer eliminated = player("eliminated");
        eliminated.setTag(BedWarsGame.ELIMINATED_TAG, true);

        assertEquals(
                List.of(alive),
                GUIBedWarsTeleporter.teleportTargets(List.of(viewer, alive, eliminated), viewer.getUuid())
        );
    }

    @Test
    void teleporterUsesTheStandardMenuSize() {
        GUIBedWarsTeleporter gui = new GUIBedWarsTeleporter(List.of());

        assertEquals(InventoryType.CHEST_4_ROW, gui.configuration().getInventoryType());
    }

    private static BedWarsPlayer player(String name) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new BedWarsPlayer(connection, new GameProfile(UUID.randomUUID(), name));
    }
}
