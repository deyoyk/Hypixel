package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.shop;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopAlda extends ShopView {
    public GUIShopAlda() {
        super(Text.key("gui_shop.alda.title"), SINGLE_SLOT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ABIPHONE_BASIC), 1, new CoinShopPrice(100000)));
    }
}
