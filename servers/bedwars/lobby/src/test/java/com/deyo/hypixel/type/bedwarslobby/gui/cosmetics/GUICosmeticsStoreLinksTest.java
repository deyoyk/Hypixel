package com.deyo.hypixel.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUICosmeticsStoreLinksTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void cosmeticsMenusUseTheirStandardSizes() {
        assertEquals(InventoryType.CHEST_6_ROW, new GUIMyCosmetics().configuration().getInventoryType());
        assertEquals(InventoryType.CHEST_6_ROW, new GUIPrestigeCustomizer().configuration().getInventoryType());
        assertEquals(InventoryType.CHEST_6_ROW, new GUIShopkeeperSkins().configuration().getInventoryType());
    }
}
