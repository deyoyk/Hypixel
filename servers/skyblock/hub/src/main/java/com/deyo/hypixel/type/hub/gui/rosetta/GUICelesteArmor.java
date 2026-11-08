package com.deyo.hypixel.type.hub.gui.rosetta;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUICelesteArmor extends ShopView {
    public GUICelesteArmor() {
        super("Celeste Armor", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_HELMET), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_CHESTPLATE), 1, new CoinShopPrice(8000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_LEGGINGS), 1, new CoinShopPrice(7000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_BOOTS), 1, new CoinShopPrice(4000)));
    }
}
