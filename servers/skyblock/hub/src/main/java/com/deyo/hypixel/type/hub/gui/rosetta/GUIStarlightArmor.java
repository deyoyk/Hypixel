package com.deyo.hypixel.type.hub.gui.rosetta;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIStarlightArmor extends ShopView {
    public GUIStarlightArmor() {
        super("Starlight Armor", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_HELMET), 1, new CoinShopPrice(35000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_CHESTPLATE), 1, new CoinShopPrice(70000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_LEGGINGS), 1, new CoinShopPrice(45000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.STARLIGHT_BOOTS), 1, new CoinShopPrice(30000)));
    }
}
