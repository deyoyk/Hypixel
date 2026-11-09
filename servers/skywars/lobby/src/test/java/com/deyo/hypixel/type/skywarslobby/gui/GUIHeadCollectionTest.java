package com.deyo.hypixel.type.skywarslobby.gui;

import com.deyo.hypixel.type.generic.data.datapoints.DatapointSkywarsKitStats;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIHeadCollectionTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void totalsAndSortsCollectedHeads() {
        DatapointSkywarsKitStats.KitStatistics first = new DatapointSkywarsKitStats.KitStatistics();
        first.addHeadGathered();
        first.addHeadGathered();
        DatapointSkywarsKitStats.KitStatistics second = new DatapointSkywarsKitStats.KitStatistics();
        second.addHeadGathered();
        DatapointSkywarsKitStats.KitStatistics empty = new DatapointSkywarsKitStats.KitStatistics();
        Map<String, DatapointSkywarsKitStats.KitStatistics> stats = Map.of(
                "first", first, "second", second, "empty", empty);

        assertEquals(3, GUIHeadCollection.totalHeads(stats));
        assertEquals(List.of(
                        new GUIHeadCollection.HeadEntry("first", "first", 2),
                        new GUIHeadCollection.HeadEntry("second", "second", 1)),
                GUIHeadCollection.headEntries(stats, id -> null));
    }

    @Test
    void menuUsesFiveRows() {
        assertEquals(InventoryType.CHEST_5_ROW,
                new GUIHeadCollection().configuration().getInventoryType());
    }
}
