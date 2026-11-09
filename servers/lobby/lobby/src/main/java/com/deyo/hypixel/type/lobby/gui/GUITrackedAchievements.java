package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.achievement.AchievementData;
import com.deyo.hypixel.type.generic.achievement.AchievementDefinition;
import com.deyo.hypixel.type.generic.achievement.AchievementRegistry;
import com.deyo.hypixel.type.generic.achievement.PlayerAchievementHandler;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GUITrackedAchievements extends HypixelInventoryGUI {
    private static final int[] TRACKED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    public GUITrackedAchievements() {
        super("Tracked Achievements", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(31, new GUIAchievementsMenu()));
        set(GUIClickableItem.getCloseItem(35));

        HypixelPlayer player = e.player();
        PlayerAchievementHandler handler = player.getAchievementHandler();
        List<AchievementDefinition> tracked = trackedAchievements(handler.getAchievementData());

        if (tracked.isEmpty()) {
            set(new GUIItem(13) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.item(Material.BARRIER, 1, """
                            <c>No tracked achievements
                            <7>Track a tiered achievement to see it here.""");
                }
            });
        } else {
            for (int index = 0; index < tracked.size() && index < TRACKED_SLOTS.length; index++) {
                AchievementDefinition definition = tracked.get(index);
                int slot = TRACKED_SLOTS[index];

                set(new GUIClickableItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer viewer) {
                        int tier = handler.getAchievementTier(definition.getId());
                        int progress = handler.getProgress(definition.getId());
                        return ItemStacks.of(definition.getCategory().getMaterial(), 1, """
                                <a>{}
                                <7>Category: <f>{}
                                <7>Tier: <e>{}
                                <7>Progress: <a>{:,}

                                <e>Click to open this category!""",
                                definition.getName(),
                                definition.getCategory().getDisplayName(),
                                tier,
                                progress);
                    }

                    @Override
                    public void run(InventoryPreClickEvent click, HypixelPlayer clicker) {
                        new GUITieredAchievements(definition.getCategory()).open(clicker);
                    }
                });
            }
        }

        updateItemStacks(getInventory(), player);
    }

    static List<AchievementDefinition> trackedAchievements(AchievementData data) {
        return trackedAchievements(data, AchievementRegistry::get);
    }

    static List<AchievementDefinition> trackedAchievements(
            AchievementData data, Function<String, AchievementDefinition> lookup) {
        List<AchievementDefinition> tracked = new ArrayList<>();
        for (AchievementCategory category : AchievementCategory.values()) {
            String id = data.getTrackedAchievement(category);
            if (id == null) continue;

            AchievementDefinition definition = lookup.apply(id);
            if (definition != null) {
                tracked.add(definition);
            }
        }
        return tracked;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
