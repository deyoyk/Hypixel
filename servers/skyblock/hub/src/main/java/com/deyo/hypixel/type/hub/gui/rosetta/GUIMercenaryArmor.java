package com.deyo.hypixel.type.hub.gui.rosetta;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIMercenaryArmor extends ShopView {
    public GUIMercenaryArmor() {
        super("Mercenary Armor", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_AXE), 1, new CoinShopPrice(30000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_HELMET), 1, new CoinShopPrice(35000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_CHESTPLATE), 1, new CoinShopPrice(70000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_LEGGINGS), 1, new CoinShopPrice(45000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_BOOTS), 1, new CoinShopPrice(30000)));
    }
}
