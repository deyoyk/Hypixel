package com.deyo.hypixel.type.hub.gui;

import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopWeaponsmith extends ShopView {

    public GUIShopWeaponsmith() {
        super("Weaponsmith", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.UNDEAD_SWORD), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.END_SWORD), 1, new CoinShopPrice(150)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SPIDER_SWORD), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.DIAMOND_SWORD), 1, new CoinShopPrice(60)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.BOW), 1, new CoinShopPrice(25)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.FLINT_ARROW), 1, new CoinShopPrice(3)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WITHER_BOW), 1, new CoinShopPrice(250)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ARTISANAL_SHORTBOW), 1, new CoinShopPrice(600)));
    }
}
