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
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerRegistry;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerService;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerTierDefinition;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUISlayerTier extends HypixelInventoryGUI {
    private static final int[] TIER_SLOTS = {20, 21, 22, 23, 24, 25};

    private final SlayerType type;

    public GUISlayerTier(SlayerType type) {
        super(type.displayName() + " Slayer", InventoryType.CHEST_5_ROW);
        this.type = type;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStacks.filler(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(40));
        set(new GUIClickableItem(36) {
            @Override
            public void run(InventoryPreClickEvent event, HypixelPlayer clicker) {
                new GUISlayer().open(clicker);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer viewer) {
                return ItemStacks.item(Material.ARROW, 1, Text.of("<a>Back to Slayer"), List.of());
            }
        });

        SlayerDefinition definition = SlayerRegistry.get(type);
        if (definition == null || !definition.enabled()) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.item(Material.GRAY_DYE, 1, type.coloredName(), List.of(
                            Text.of("<7>This slayer is not available yet.")));
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        List<SlayerTierDefinition> tiers = definition.orderedTiers();
        for (int index = 0; index < tiers.size() && index < TIER_SLOTS.length; index++) {
            SlayerTierDefinition tier = tiers.get(index);
            int slot = TIER_SLOTS[index];

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent event, HypixelPlayer clicker) {
                    SkyBlockPlayer player = (SkyBlockPlayer) clicker;
                    SlayerService.StartResult result = SlayerService.startQuest(player, type, tier.tier());
                    if (result.success()) {
                        player.closeInventory();
                        return;
                    }
                    player.sendMessage("<c>{}", result.message());
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    DatapointSlayer.SlayerData data = ((SkyBlockPlayer) viewer).getSkyblockDataHandler()
                            .get(SkyBlockDataHandler.Data.SLAYER, DatapointSlayer.class).getValue();
                    int completions = data.progress(type).completions(tier.tier());
                    return ItemStacks.item(type.menuMaterial(), 1,
                            Text.of("<e>{}", tier.displayName(type)), List.of(
                                    Text.of("<7>Cost: <6>{:,} Coins", tier.cost()),
                                    Text.of("<7>Combat XP: <e>{:,}", tier.requiredCombatXp()),
                                    Text.of("<7>Slayer XP: <d>{:,}", tier.slayerXp()),
                                    Text.of("<7>Boss Level: <c>{}", tier.bossLevel()),
                                    Text.of("<7>Completions: <a>{}", completions),
                                    Text.empty(),
                                    Text.of("<e>Click to start this quest!")));
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
