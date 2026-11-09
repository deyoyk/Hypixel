package com.deyo.hypixel.type.bedwarslobby.gui.cosmetics;

import com.deyo.hypixel.type.generic.data.datapoints.DatapointBedWarsHotbar.HotbarItemType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GUIBedWarsGameSettingsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void assignSwapsAnExistingItemType() {
        Map<Byte, HotbarItemType> layout = Map.of(
                (byte) 3, HotbarItemType.MELEE,
                (byte) 4, HotbarItemType.BLOCKS);

        assertEquals(Map.of(
                        (byte) 3, HotbarItemType.BLOCKS,
                        (byte) 4, HotbarItemType.MELEE),
                GUIBedWarsGameSettings.assign(layout, (byte) 4, HotbarItemType.MELEE));
    }

    @Test
    void assignMovesAnItemIntoAnEmptySlot() {
        Map<Byte, HotbarItemType> layout = Map.of((byte) 3, HotbarItemType.MELEE);

        assertEquals(Map.of((byte) 5, HotbarItemType.MELEE),
                GUIBedWarsGameSettings.assign(layout, (byte) 5, HotbarItemType.MELEE));
    }

    @Test
    void assignCanClearASlot() {
        Map<Byte, HotbarItemType> layout = Map.of((byte) 3, HotbarItemType.MELEE);

        assertEquals(Map.of(), GUIBedWarsGameSettings.assign(layout, (byte) 3, null));
    }

    @Test
    void nextCyclesThroughEveryItemTypeAndEmpty() {
        assertEquals(HotbarItemType.BLOCKS, GUIBedWarsGameSettings.next(null));
        assertEquals(HotbarItemType.MELEE, GUIBedWarsGameSettings.next(HotbarItemType.BLOCKS));
        assertNull(GUIBedWarsGameSettings.next(HotbarItemType.COMPASS));
    }

    @Test
    void menuUsesFiveRows() {
        assertEquals(InventoryType.CHEST_5_ROW,
                new GUIBedWarsGameSettings().configuration().getInventoryType());
    }
}
