package com.deyo.hypixel.type.hub.gui;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;


public class GUIBakerShop extends ShopView {

    public GUIBakerShop() {
        super("Baker", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.NEW_YEAR_CAKE_BAG), 1, new CoinShopPrice(250_000)));
    }
}

