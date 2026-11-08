package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction.view;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public interface AuctionView {
    void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player);
}
