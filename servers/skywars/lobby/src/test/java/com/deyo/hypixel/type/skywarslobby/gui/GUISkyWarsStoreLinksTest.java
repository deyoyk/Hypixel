package com.deyo.hypixel.type.skywarslobby.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUISkyWarsStoreLinksTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void kitsPerksUsesTheStandardMenuSize() {
        assertEquals(InventoryType.CHEST_5_ROW, new GUIKitsPerks().getSize());
    }

    @Test
    void soulWellUsesTheStandardMenuSize() {
        assertEquals(InventoryType.CHEST_6_ROW, new GUISoulWell().configuration().getInventoryType());
    }
}
