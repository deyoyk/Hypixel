package com.deyo.hypixel.type.hub.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointSlayer;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerDefinition;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerQuest;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerRegistry;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerService;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUISlayer extends HypixelInventoryGUI {
    private static final int[] TYPE_SLOTS = {20, 21, 22, 23, 24, 25};

    public GUISlayer() {
        super("Slayer", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStacks.filler(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointSlayer.SlayerData data = player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.SLAYER, DatapointSlayer.class).getValue();

        SlayerType[] types = SlayerType.values();
        for (int index = 0; index < types.length && index < TYPE_SLOTS.length; index++) {
            SlayerType type = types[index];
            SlayerDefinition definition = SlayerRegistry.get(type);
            int slot = TYPE_SLOTS[index];

            if (definition == null || !definition.enabled()) {
                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        return ItemStacks.item(Material.GRAY_DYE, 1, type.coloredName(), List.of(
                                Text.of("<7>This slayer is not available yet.")));
                    }
                });
                continue;
            }

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer clicker) {
                    new GUISlayerTier(type).open(clicker);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    DatapointSlayer.SlayerData viewerData = ((SkyBlockPlayer) viewer).getSkyblockDataHandler()
                            .get(SkyBlockDataHandler.Data.SLAYER, DatapointSlayer.class).getValue();
                    int xp = viewerData.progress(type).getXp();
                    int level = definition.levelForXp(xp);
                    return ItemStacks.item(type.menuMaterial(), 1, type.coloredName(), List.of(
                            Text.of("<7>Slayer Level: <e>{}", level),
                            Text.of("<7>Slayer XP: <d>{:,}", xp),
                            Text.empty(),
                            Text.of("<e>Click to view quests!")));
                }
            });
        }

        SlayerQuest activeQuest = data.getActiveQuest();
        if (activeQuest != null) {
            SlayerDefinition activeDefinition = SlayerRegistry.get(activeQuest.type());
            String bossName = activeDefinition != null && activeDefinition.tiers().containsKey(activeQuest.tier())
                    ? activeDefinition.tiers().get(activeQuest.tier()).displayName(activeQuest.type())
                    : activeQuest.type().displayName();

            set(new GUIClickableItem(39) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer clicker) {
                    SlayerService.cancelQuest((SkyBlockPlayer) clicker);
                    new GUISlayer().open(clicker);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.item(Material.BARRIER, 1, Text.of("<c>Cancel Quest"), List.of(
                            Text.of("<7>Active: <e>{}", bossName),
                            Text.empty(),
                            Text.of("<c>Click to cancel.")));
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }
}
