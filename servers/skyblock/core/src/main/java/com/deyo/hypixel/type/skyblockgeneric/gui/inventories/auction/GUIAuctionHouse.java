package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.RefreshingGUI;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIAuctionHouse extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIAuctionHouse() {
        super(Text.key("gui_auction.house.title"), InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionHouseStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStacks.item(Material.PAPER, 1,
                        Text.key("gui_auction.house.stats_button"),
                        Text.keyLines("gui_auction.house.stats_button.lore"));
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionBrowser().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStacks.item(Material.GOLD_BLOCK, 1,
                        Text.key("gui_auction.house.browser_button"),
                        Text.keyLines("gui_auction.house.browser_button.lore"));
            }
        });

        if (((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIAuctionCreateItem(GUIAuctionHouse.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStacks.item(Material.GOLDEN_HORSE_ARMOR, 1,
                            Text.key("gui_auction.house.create_button"),
                            Text.keyLines("gui_auction.house.create_button.lore"));
                }
            });
        } else {
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIManageAuctions().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStacks.item(Material.GOLDEN_HORSE_ARMOR, 1,
                        Text.key("gui_auction.house.manage_button"),
                        Text.keyLines("gui_auction.house.manage_button.lore",
                            player.getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().size()));
                }
            });
        }

        if (!((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIViewBids().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStacks.item(Material.GOLDEN_CARROT, 1,
                            Text.key("gui_auction.house.bids_button"),
                            Text.keyLines("gui_auction.house.bids_button.lore"));
                }
            });
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage(Text.key("gui_auction.house.offline_message"));
                player.closeInventory();
            }
        });
    }

    @Override
    public int refreshRate() {
        return 20;
    }
}
