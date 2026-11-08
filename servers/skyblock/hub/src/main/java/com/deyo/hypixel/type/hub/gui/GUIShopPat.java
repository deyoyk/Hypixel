package com.deyo.hypixel.type.hub.gui;

import net.minestom.server.item.Material;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopPat extends ShopView {
    public GUIShopPat() {
        super("Pat", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.FLINT), 10, new CoinShopPrice(60)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAVEL), 15, new CoinShopPrice(65)));
    }
}
