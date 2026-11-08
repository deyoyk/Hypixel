package com.deyo.hypixel.type.deepcaverns.gui;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopWalter extends ShopView {
	public GUIShopWalter() {
		super("Walter", SINGLE_SLOT);
	}

	@Override
	public void initializeShopItems() {
		attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.SUPERBOOM_TNT), 1, new CoinShopPrice(2500)));
	}
}
