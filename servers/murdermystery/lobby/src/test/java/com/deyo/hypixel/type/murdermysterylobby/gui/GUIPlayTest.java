package com.deyo.hypixel.type.murdermysterylobby.gui;

import com.deyo.hypixel.commons.murdermystery.MurderMysteryGameType;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIPlayTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void bottomClicksAreCancelled() {
        assertCancelsBottomClicks(new GUIPlay(MurderMysteryGameType.CLASSIC));
    }

    private static void assertCancelsBottomClicks(HypixelInventoryGUI gui) {
        Inventory inventory = new Inventory(InventoryType.CHEST_4_ROW, "test");
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        Player player = new Player(connection, new GameProfile(UUID.randomUUID(), "test"));
        InventoryPreClickEvent event = new InventoryPreClickEvent(inventory, player, new Click.Left(0));

        gui.onBottomClick(event);

        assertTrue(event.isCancelled());
    }
}
