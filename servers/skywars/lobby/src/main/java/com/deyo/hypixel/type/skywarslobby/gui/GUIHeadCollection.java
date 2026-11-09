package com.deyo.hypixel.type.skywarslobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointSkywarsKitStats;
import com.deyo.hypixel.type.generic.data.handlers.SkywarsDataHandler;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKit;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKitRegistry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GUIHeadCollection extends StatelessView {
    private static final int[] HEAD_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Head Collection", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 39, ctx);
        Components.close(layout, 41);

        SkywarsDataHandler handler = SkywarsDataHandler.getUser(ctx.player());
        if (handler == null) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>Head data unavailable
                    <7>Join a SkyWars lobby and try again."""));
            return;
        }

        DatapointSkywarsKitStats.SkywarsKitStats stats = handler.get(
                SkywarsDataHandler.Data.KIT_STATS, DatapointSkywarsKitStats.class).getValue();
        List<HeadEntry> entries = headEntries(stats.getKitStats(), SkywarsKitRegistry::getKit);

        layout.slot(4, ItemStacks.item(Material.PLAYER_HEAD, 1, """
                <a>Head Collection
                <7>Total Heads: <c>{}

                <7>Collect heads by killing players
                <7>in <5>Corrupted Games<7>!""", totalHeads(stats.getKitStats())));

        if (entries.isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No heads collected yet
                    <7>Kill players in Corrupted Games to
                    <7>start your collection."""));
            return;
        }

        for (int index = 0; index < entries.size() && index < HEAD_SLOTS.length; index++) {
            HeadEntry entry = entries.get(index);
            layout.slot(HEAD_SLOTS[index], ItemStacks.item(Material.PLAYER_HEAD, 1, """
                    <a>{}
                    <7>Heads Gathered: <c>{}""", entry.name(), entry.heads()));
        }
    }

    static int totalHeads(Map<String, DatapointSkywarsKitStats.KitStatistics> stats) {
        int total = 0;
        for (DatapointSkywarsKitStats.KitStatistics entry : stats.values()) {
            total += entry.getHeadsGathered();
        }
        return total;
    }

    static List<HeadEntry> headEntries(
            Map<String, DatapointSkywarsKitStats.KitStatistics> stats,
            Function<String, SkywarsKit> lookup) {
        List<HeadEntry> entries = new ArrayList<>();
        for (Map.Entry<String, DatapointSkywarsKitStats.KitStatistics> entry : stats.entrySet()) {
            int heads = entry.getValue().getHeadsGathered();
            if (heads <= 0) continue;

            SkywarsKit kit = lookup.apply(entry.getKey());
            String name = kit == null ? entry.getKey() : kit.getName();
            entries.add(new HeadEntry(entry.getKey(), name, heads));
        }
        entries.sort(Comparator.comparingInt(HeadEntry::heads).reversed()
                .thenComparing(HeadEntry::id));
        return entries;
    }

    record HeadEntry(String id, String name, int heads) {
    }
}
