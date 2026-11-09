package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.experience.LevelReward;
import com.deyo.hypixel.type.generic.experience.LevelRewardRegistry;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUIVeteranRewards extends HypixelInventoryGUI {
    private static final int LEVELS_PER_PAGE = 28;
    private static final int[] REWARD_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private int page = 0;

    public GUIVeteranRewards() {
        super("Veteran Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        int playerLevel = player.getExperienceHandler().getLevel();
        List<LevelReward> rewards = veteranRewards(LevelRewardRegistry.getAllRewards());
        int totalPages = Math.max(1, (rewards.size() + LEVELS_PER_PAGE - 1) / LEVELS_PER_PAGE);
        page = Math.min(page, totalPages - 1);

        fill(FILLER_ITEM);

        int startIndex = page * LEVELS_PER_PAGE;
        for (int i = 0; i < REWARD_SLOTS.length; i++) {
            int index = startIndex + i;
            if (index >= rewards.size()) break;
            set(createRewardItem(REWARD_SLOTS[i], rewards.get(index), playerLevel));
        }

        if (page > 0) {
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.ARROW, "<a>Previous Page <e>{}", page);
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    page--;
                    open(player);
                }
            });
        }

        if (page < totalPages - 1) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.ARROW, "<a>Next Page <e>{}", page + 2);
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    page++;
                    open(player);
                }
            });
        }

        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.ARROW, """
                        <a>Go Back
                        <7>To Hypixel Leveling""");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIHypixelLeveling().open(player);
            }
        });
        set(GUIClickableItem.getCloseItem(49));
        updateItemStacks(getInventory(), player);
    }

    static List<LevelReward> veteranRewards(List<LevelReward> rewards) {
        return rewards.stream()
                .filter(reward -> reward.getLevel() >= 100)
                .sorted(Comparator.comparingInt(LevelReward::getLevel))
                .toList();
    }

    private GUIItem createRewardItem(int slot, LevelReward reward, int playerLevel) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean claimed = playerLevel >= reward.getLevel();
                List<Text> lore = new ArrayList<>();
                lore.add(Text.empty());
                if (reward.hasCoins()) {
                    lore.add(Text.of("<8>+<6>{:,} <7>Arcade Coins", reward.getCoins()));
                }
                if (reward.hasDust()) {
                    if (reward.getMysteryDust() > 0) {
                        lore.add(Text.of("<8>+<b>{} Mystery Dust", reward.getMysteryDust()));
                    }
                    if (reward.getDust() > 0) {
                        lore.add(Text.of("<8>+<b>{} Dust", reward.getDust()));
                    }
                }
                if (reward.hasBooster()) {
                    lore.add(Text.of("<8>+<6>{} <7>Personal Booster <7>(<b>{}<7>)",
                            reward.getBoosterType(), reward.getBoosterDurationDisplay()));
                }
                if (reward.hasSpecialRewards()) {
                    for (String special : reward.getSpecialRewards()) {
                        lore.add(Text.of("<8>+<7>{}", GUIHypixelLeveling.formatSpecialReward(special)));
                    }
                }
                lore.add(Text.empty());
                lore.add(claimed
                        ? Text.of("<a>You have already claimed this reward!")
                        : Text.of("<e>Reach Level {} to claim!", reward.getLevel()));

                return ItemStacks.item(
                        claimed ? Material.MINECART : Material.CHEST_MINECART,
                        1,
                        Text.of((claimed ? "<c>" : "<a>") + "Hypixel Level Reward {}", reward.getLevel()),
                        lore
                );
            }
        };
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
