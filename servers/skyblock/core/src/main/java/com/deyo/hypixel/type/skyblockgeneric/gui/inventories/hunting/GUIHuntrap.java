package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.hunting;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.View;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointHunting;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeRegistry;
import com.deyo.hypixel.type.skyblockgeneric.hunting.HuntrapService;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIHuntrap implements View<GUIHuntrap.State> {
    public record State(DatapointHunting.PlacedHuntrap trap) {
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, context) -> "Shard Trap", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext context) {
        Components.fill(layout);
        SkyBlockPlayer player = (SkyBlockPlayer) context.player();
        DatapointHunting.PlacedHuntrap trap = HuntrapService.current(player, state.trap());
        if (trap == null) {
            player.closeInventory();
            return;
        }
        AttributeDefinition caught = AttributeRegistry.get(trap.caughtShard());
        if (caught == null) {
            long seconds = Math.max(0, (trap.catchAt() - System.currentTimeMillis()) / 1000);
            layout.slot(22, (s, c) -> ItemStacks.item(Material.RED_STAINED_GLASS_PANE, """
                    <c>Empty
                    <7>This trap hasn't caught anything yet!

                    <7>Massive Time: <a>{}""",
                    "%d h %02d m %02d s".formatted(seconds / 3600, seconds / 60 % 60, seconds % 60)));
        } else {
            layout.slot(22, (s, c) -> AttributeGUIItems.shard(caught, 1), (click, c) -> {
                HuntrapService.claim((SkyBlockPlayer) c.player(), trap);
                c.player().closeInventory();
            });
        }
        layout.slot(50, (s, c) -> ItemStacks.item(Material.BEDROCK, """
                <a>Pickup Trap
                <e>Click to pickup"""), (click, c) -> {
            HuntrapService.pickup((SkyBlockPlayer) c.player(), trap);
            c.player().closeInventory();
        });
        Components.close(layout, 49);
    }

    public static void open(SkyBlockPlayer player, DatapointHunting.PlacedHuntrap trap) {
        player.openView(new GUIHuntrap(), new State(trap));
    }
}
