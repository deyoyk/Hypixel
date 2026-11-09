package com.deyo.hypixel.type.skywarslobby.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKit;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKitRarity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    void remainingCoinMenusStillConstruct() {
        SkywarsKit kit = new SkywarsKit("test", "Test", Material.STONE, "",
                SkywarsKitRarity.COMMON, 0, 0, false, true, "", "", Map.of());

        assertEquals(InventoryType.CHEST_6_ROW, new GUISelectNormalPerks().getSize());
        assertEquals(InventoryType.CHEST_6_ROW, new GUIKitSelector("NORMAL").getSize());
        assertEquals(InventoryType.CHEST_6_ROW, new GUIKitStats(kit, "NORMAL").getSize());
        assertEquals(InventoryType.CHEST_6_ROW, new GUIKitBreakdown(kit, "NORMAL").getSize());
    }
}
