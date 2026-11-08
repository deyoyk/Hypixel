package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.auctions.AuctionFetchItemProtocol;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.RefreshingGUI;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.utility.PaginationList;
import com.deyo.hypixel.type.skyblockgeneric.auction.AuctionItemLoreHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIManageAuctions extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIManageAuctions() {
        super(Text.key("gui_auction.manage.title"), InventoryType.CHEST_3_ROW);

        fill(ItemStacks.named(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getGoBackItem(22, new GUIAuctionHouse()));
        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionCreateItem(GUIManageAuctions.this).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStacks.item(Material.GOLDEN_HORSE_ARMOR, 1,
                        Text.key("gui_auction.manage.create_button"),
                        Text.keyLines("gui_auction.manage.create_button.lore"));
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        setItems();
    }

    public void setItems() {
        List<UUID> auctions = ((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
        List<CompletableFuture<AuctionFetchItemProtocol.AuctionFetchItemResponse>> futures = new ArrayList<>(auctions.size());
        PaginationList<AuctionItem> auctionItems = new PaginationList<>(7);

        auctions.forEach(uuid -> {
            AuctionFetchItemProtocol.AuctionFetchItemMessage message =
                    new AuctionFetchItemProtocol.AuctionFetchItemMessage(uuid);
            CompletableFuture<AuctionFetchItemProtocol.AuctionFetchItemResponse> future =
                    new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message);

            future.thenAccept(response -> {
                AuctionItem item = response.item();
                synchronized (auctionItems) {
                    auctionItems.add(item);
                }
            }).exceptionally(ex -> {
                Logger.error(ex, "Failed to manage auction");
                return null;
            });
            futures.add(future);
        });

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allDone.thenRun(() -> {
            auctionItems.sort((o1, o2) -> Long.compare(o2.getEndTime(), o1.getEndTime()));

            List<AuctionItem> auctionItemsPage = auctionItems.getPage(1);

            for (int i = 0; i < 7; i++) {
                int slot = i + 10;

                if (i >= auctionItems.size()) {
                    set(new GUIItem(slot) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            return ItemStack.builder(Material.AIR);
                        }
                    });
                    continue;
                }

                AuctionItem item = auctionItemsPage.get(i);
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        new GUIAuctionViewItem(item.getUuid(), GUIManageAuctions.this).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStacks.item(item.getItem().material(), item.getItem().amount(),
                                ItemStacks.nameText(new NonPlayerItemUpdater(item.getItem()).getUpdatedItem()),
                                new AuctionItemLoreHandler(item).getLoreTexts(player));
                    }
                });
            }
        });
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

    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage(Text.key("gui_auction.manage.offline_message"));
                player.closeInventory();
                return;
            }
            setItems();
        });
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
