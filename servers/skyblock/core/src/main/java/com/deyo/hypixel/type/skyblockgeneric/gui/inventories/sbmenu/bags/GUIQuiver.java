package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.StatefulView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.ViewSession;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.collection.CustomCollectionAward;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointQuiver;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GUIQuiver implements StatefulView<GUIQuiver.QuiverState> {
    private static final List<QuiverUpgrade> UPGRADES = List.of(
            new QuiverUpgrade(CustomCollectionAward.QUIVER, 18),
            new QuiverUpgrade(CustomCollectionAward.QUIVER_UPGRADE_1, 9),
            new QuiverUpgrade(CustomCollectionAward.QUIVER_UPGRADE_2, 9)
    );
    private static final int MAX_SLOTS = 36;

    @Override
    public ViewConfiguration<QuiverState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.bags.quiver.title", InventoryType.CHEST_5_ROW);
    }

    @Override
    public QuiverState initialState() {
        return new QuiverState(0);
    }

    @Override
    public void layout(ViewLayout<QuiverState> layout, QuiverState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 40);
        Components.back(layout, 39, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        List<QuiverSlot> slots = calculateSlots(player::hasCustomCollectionAward);
        int amountOfSlots = 0;

        for (QuiverSlot slot : slots) {
            if (slot.unlocked()) {
                amountOfSlots++;
            } else {
                layout.slot(slot.index(), (s, c) -> ItemStacks.item(Material.RED_STAINED_GLASS_PANE, 1,
                        Text.key("gui_sbmenu.bags.quiver.locked_slot"),
                        Text.keyLines("gui_sbmenu.bags.quiver.locked_slot.lore", slot.upgrade().getDisplay())));
            }
        }

        // Set up editable slots
        DatapointQuiver.PlayerQuiver quiver = player.getQuiver();
        for (int i = 0; i < amountOfSlots; i++) {
            int slotIndex = i;
            SkyBlockItem item = quiver.getInSlot(slotIndex);

            layout.editable(slotIndex, (s, c) -> {
                SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                if (item == null) {
                    return ItemStack.builder(Material.AIR);
                } else {
                    return PlayerItemUpdater.playerUpdate(p, item.getItemStack());
                }
            }, (slot, oldItem, newItem, s) -> {
                SkyBlockItem newSkyBlockItem = new SkyBlockItem(newItem);
                if (!isItemAllowed(newSkyBlockItem)) {
                    ctx.player().sendMessage(Text.key("gui_sbmenu.bags.quiver.msg.cannot_put"));
                }
            });
        }
    }

    @Override
    public void onClose(QuiverState state, ViewContext ctx, ViewSession.CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        save(player, ctx.inventory(), getUnlockedSlots(player));
    }

    private int getUnlockedSlots(SkyBlockPlayer player) {
        int amountOfSlots = 0;
        for (QuiverSlot slot : calculateSlots(player::hasCustomCollectionAward)) {
            if (slot.unlocked()) amountOfSlots++;
        }
        return amountOfSlots;
    }

    static List<QuiverSlot> calculateSlots(Predicate<CustomCollectionAward> hasAward) {
        List<QuiverSlot> slots = new ArrayList<>();
        int index = 0;
        boolean unlocked = true;
        for (QuiverUpgrade upgrade : UPGRADES) {
            unlocked = unlocked && hasAward.test(upgrade.award());
            for (int i = 0; i < upgrade.slots() && index < MAX_SLOTS; i++) {
                slots.add(new QuiverSlot(index++, upgrade.award(), unlocked));
            }
        }
        return slots;
    }

    private boolean isItemAllowed(SkyBlockItem item) {
        if (item.isNA()) return true;
        if (item.getMaterial().equals(Material.AIR)) return true;
        return item.getMaterial() == Material.ARROW;
    }

    private void save(SkyBlockPlayer player, net.minestom.server.inventory.Inventory inventory, int slotToSaveUpTo) {
        DatapointQuiver.PlayerQuiver quiver = player.getQuiver();
        for (int i = 0; i < slotToSaveUpTo; i++) {
            SkyBlockItem item = new SkyBlockItem(inventory.getItemStack(i));
            if (isEmptySlot(item)) {
                quiver.getQuiverMap().remove(i);
            } else {
                quiver.getQuiverMap().put(i, item);
            }
        }
    }

    static boolean isEmptySlot(SkyBlockItem item) {
        return item.isNA() || item.getMaterial() == Material.AIR;
    }

    public record QuiverState(int placeholder) {}

    record QuiverUpgrade(CustomCollectionAward award, int slots) {
    }

    record QuiverSlot(int index, CustomCollectionAward upgrade, boolean unlocked) {
    }
}
