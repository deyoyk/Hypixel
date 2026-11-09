package com.deyo.hypixel.type.bedwarslobby.npcs;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NPCDoormanDaveTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void opensTheSlumberHotel() {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        HypixelPlayer player = new HypixelPlayer(connection,
                new GameProfile(UUID.randomUUID(), "test"));
        NPCDoormanDave npc = new NPCDoormanDave();

        npc.onClick(new NPCInteractEvent(player, PlayerHand.MAIN, npc));

        Inventory inventory = assertInstanceOf(Inventory.class, player.getOpenInventory());
        assertNotNull(inventory.getTitle());
        assertEquals(InventoryType.CHEST_4_ROW, inventory.getInventoryType());
    }
}
