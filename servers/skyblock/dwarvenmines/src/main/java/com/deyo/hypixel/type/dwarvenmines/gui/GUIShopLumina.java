package com.deyo.hypixel.type.dwarvenmines.gui;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopLumina extends ShopView {
    public GUIShopLumina() {
        super("Lumina", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.TORCH), 64, new CoinShopPrice(32)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.LANTERN), 1, new CoinShopPrice(10_000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.INFINI_TORCH), 1, new CoinShopPrice(100_000)));
    }
}
