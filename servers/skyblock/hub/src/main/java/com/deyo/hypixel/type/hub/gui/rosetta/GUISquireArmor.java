package com.deyo.hypixel.type.hub.gui.rosetta;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUISquireArmor extends ShopView {
    public GUISquireArmor() {
        super("Squire Armor", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_SWORD), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_HELMET), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_CHESTPLATE), 1, new CoinShopPrice(8000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_LEGGINGS), 1, new CoinShopPrice(7000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_BOOTS), 1, new CoinShopPrice(4000)));
    }
}
