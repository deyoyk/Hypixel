package com.deyo.hypixel.type.hub.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.hub.gui.elizabeth.CommunityShopItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUIBitsSubCategorys extends HypixelInventoryGUI {

    private final int[] displaySlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private final List<CommunityShopItem> items;
    private final String guiName;
    private final HypixelInventoryGUI previousGUI;

    public GUIBitsSubCategorys(List<CommunityShopItem> items, String guiName, HypixelInventoryGUI previousGUI) {
        super("Bits Shop - {}", InventoryType.CHEST_5_ROW, guiName);
        this.items = items;
        this.guiName = guiName;
        this.previousGUI = previousGUI;
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStacks.filler(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(40, previousGUI));
        int index = 0;
        for (int slot : displaySlots) {
            if (index < items.size()) {
                items.forEach(shopItem -> {
                    ItemType item = shopItem.getItemType();
                    int price = shopItem.getPrice();
                    int amount = shopItem.getAmount();
                    set(new GUIClickableItem(slot) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                            if (player.getBits() >= price) {
                                SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                                ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                                itemStack.amount(amount);
                                SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());
                                if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS)) {
                                    player.addAndUpdateItem(finalItem);
                                    player.removeBits(price);
                                    new GUIBitsSubCategorys(items, guiName, previousGUI).open(player);
                                } else {
                                    new GUIBitsConfirmBuy(finalItem, price).open(player);
                                }
                            } else {
                                player.sendMessage("<c>You don't have enough Bits to buy that!");
                            }
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockItem skyBlockItem = new SkyBlockItem(item);
                            ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                            itemStack.amount(amount);
                            return ItemStacks.appendLore(itemStack, """
                                    \s
                                    <7>Cost
                                    <b>{:,} Bits
                                    <r>\s
                                    <e>Click to trade!""", price);
                        }
                    });
                });

            }
            index++;
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}