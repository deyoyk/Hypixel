package com.deyo.hypixel.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBedWarsHotbar;
import com.deyo.hypixel.type.generic.data.handlers.BedWarsDataHandler;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIBedWarsGameSettings extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Bed Wars Settings", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.backOrClose(layout, 31, ctx);
        Components.close(layout, 35);

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            int slot = hotbarSlot + 10;
            final int index = hotbarSlot;
            layout.slot(slot, (_, c) -> hotbarItem(c.player(), index), (click, c) -> {
                BedWarsDataHandler handler = BedWarsDataHandler.getUser(c.player());
                if (handler == null) return;

                DatapointBedWarsHotbar datapoint = handler.get(
                        BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class);
                Map<Byte, DatapointBedWarsHotbar.HotbarItemType> current = datapoint.getValue();
                DatapointBedWarsHotbar.HotbarItemType selected = current.get((byte) index);
                DatapointBedWarsHotbar.HotbarItemType replacement = click.click() instanceof Click.Right
                        ? null : next(selected);

                datapoint.setValue(assign(current, (byte) index, replacement));
                c.session(DefaultState.class).refresh();
            });
        }

        layout.slot(27, ItemStacks.item(Material.BARRIER, """
                <c>Reset Hotbar Layout
                <7>Restore the default Bed Wars hotbar.

                <e>Click to reset!"""), (_, c) -> {
            BedWarsDataHandler handler = BedWarsDataHandler.getUser(c.player());
            if (handler == null) return;

            handler.get(BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class)
                    .setValue(new HashMap<>(DatapointBedWarsHotbar.defaultHotbar));
            c.session(DefaultState.class).refresh();
        });

        layout.slot(29, ItemStacks.item(Material.COMPARATOR, """
                <a>Hotbar Layout
                <7>Choose where each Bed Wars item
                <7>appears in your hotbar.

                <7>Left click a slot to cycle items.
                <7>Right click to clear a slot."""));
    }

    private static net.minestom.server.item.ItemStack.Builder hotbarItem(
            com.deyo.hypixel.type.generic.user.HypixelPlayer player, int hotbarSlot) {
        BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);
        if (handler == null) {
            return ItemStacks.item(Material.BARRIER, 1, """
                    <c>Hotbar data unavailable
                    <7>Join a Bed Wars lobby and try again.""");
        }

        DatapointBedWarsHotbar.HotbarItemType type = handler.get(
                        BedWarsDataHandler.Data.HOTBAR_LAYOUT, DatapointBedWarsHotbar.class)
                .getValue().get((byte) hotbarSlot);

        if (type == null) {
            return ItemStacks.item(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, """
                    <7>Slot {}
                    <8>Empty""", hotbarSlot + 1);
        }

        return ItemStacks.item(type.getMaterial(), 1, """
                <a>{}
                <7>Slot {}

                <e>Left click to cycle
                <e>Right click to clear""", type.pretty(), hotbarSlot + 1);
    }

    static DatapointBedWarsHotbar.HotbarItemType next(DatapointBedWarsHotbar.HotbarItemType current) {
        DatapointBedWarsHotbar.HotbarItemType[] values = DatapointBedWarsHotbar.HotbarItemType.values();
        if (current == null) return values[0];

        int next = current.ordinal() + 1;
        return next >= values.length ? null : values[next];
    }

    static Map<Byte, DatapointBedWarsHotbar.HotbarItemType> assign(
            Map<Byte, DatapointBedWarsHotbar.HotbarItemType> layout,
            byte slot,
            DatapointBedWarsHotbar.HotbarItemType type) {
        Map<Byte, DatapointBedWarsHotbar.HotbarItemType> updated = new HashMap<>(layout);
        DatapointBedWarsHotbar.HotbarItemType previous = updated.get(slot);

        if (type == null) {
            updated.remove(slot);
            return updated;
        }

        for (Map.Entry<Byte, DatapointBedWarsHotbar.HotbarItemType> entry : new ArrayList<>(updated.entrySet())) {
            if (entry.getValue() == type && entry.getKey() != slot) {
                if (previous == null) {
                    updated.remove(entry.getKey());
                } else {
                    updated.put(entry.getKey(), previous);
                }
                break;
            }
        }

        updated.put(slot, type);
        return updated;
    }
}
