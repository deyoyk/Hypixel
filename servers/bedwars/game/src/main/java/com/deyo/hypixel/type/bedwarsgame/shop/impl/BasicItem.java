package com.deyo.hypixel.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.ShopItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBedWarsHotbar;
import org.jetbrains.annotations.Nullable;

public class BasicItem extends ShopItem {

	public BasicItem(String id, String name, String description, int cost, int amount, Currency currency, Material material) {
		super(id, name, description, cost, amount, currency, material);
	}

	public BasicItem(String id, String name, String description, int cost, int amount, Currency currency, Material material,
	                 @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType) {
		super(id, name, description, cost, amount, currency, material, hotbarItemType);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		giveItem(player, this.getDisplay().withAmount(this.getAmount()));
	}

}
