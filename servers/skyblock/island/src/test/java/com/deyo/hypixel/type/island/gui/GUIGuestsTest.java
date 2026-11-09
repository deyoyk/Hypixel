package com.deyo.hypixel.type.island.gui;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGuestsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void guestGuideCancelsBottomClicks() {
        GUIGuests gui = new GUIGuests();
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
        assertEquals(InventoryType.CHEST_4_ROW, gui.getSize());
    }
}
