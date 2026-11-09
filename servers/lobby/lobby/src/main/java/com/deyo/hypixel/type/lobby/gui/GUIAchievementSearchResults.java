package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.achievement.AchievementDefinition;
import com.deyo.hypixel.type.generic.achievement.AchievementRegistry;
import com.deyo.hypixel.type.generic.achievement.PlayerAchievementHandler;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.List;

public class GUIAchievementSearchResults extends HypixelInventoryGUI {
    private static final int PAGE_SIZE = 45;

    private final String query;
    private final int page;

    public GUIAchievementSearchResults(String query, int page) {
        super("Achievement Search", InventoryType.CHEST_6_ROW);
        this.query = query;
        this.page = Math.max(0, page);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerAchievementHandler handler = player.getAchievementHandler();
        List<AchievementDefinition> results = AchievementRegistry.search(query);
        int pages = Math.max(1, (results.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int currentPage = Math.min(page, pages - 1);
        int start = currentPage * PAGE_SIZE;

        if (results.isEmpty()) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.BARRIER, 1,
                            Text.of("<c>No achievements found"),
                            List.of(Text.of("<7>Query: <e>{}", query)));
                }
            });
        } else {
            for (int index = start; index < Math.min(start + PAGE_SIZE, results.size()); index++) {
                AchievementDefinition definition = results.get(index);
                int slot = index - start;
                set(new GUIClickableItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        boolean unlocked = handler.hasAchievement(definition.getId());
                        List<Text> lore = List.of(
                                Text.of("<8>{}", definition.getCategory().getDisplayName()),
                                Text.of("<7>{}", definition.getDescription()),
                                Text.empty(),
                                Text.of("<7>Type: <e>{}", definition.getType().getDisplayName()),
                                Text.of("<7>Points: <e>{}", definition.getTotalPoints()),
                                Text.empty(),
                                Text.of(unlocked ? "<a>Unlocked!" : "<c>Locked"),
                                Text.of("<e>Click to view {} achievements!",
                                        definition.getCategory().getDisplayName()));
                        return ItemStacks.of(definition.getCategory().getMaterial(), 1,
                                Text.of((unlocked ? "<a>" : "<7>") + "{}", definition.getName()), lore);
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                        new GUIGameAchievements(definition.getCategory()).open(player);
                    }
                });
            }
        }

        if (currentPage > 0) {
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.ARROW, 1, Text.of("<a>Previous Page"), List.of());
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUIAchievementSearchResults(query, currentPage - 1).open(player);
                }
            });
        } else {
            set(new GUIItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.GRAY_DYE, 1, Text.of("<7>Previous Page"), List.of());
                }
            });
        }

        set(new GUIClickableItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.ARROW, 1, Text.of("<a>Back to Achievements"), List.of());
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIAchievementsMenu().open(player);
            }
        });

        if (currentPage < pages - 1) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.ARROW, 1, Text.of("<a>Next Page"), List.of());
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUIAchievementSearchResults(query, currentPage + 1).open(player);
                }
            });
        } else {
            set(new GUIItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStacks.item(Material.GRAY_DYE, 1, Text.of("<7>Next Page"), List.of());
                }
            });
        }

        updateItemStacks(getInventory(), player);
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
