package com.deyo.hypixel.type.bedwarsgame.shop.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.ShopItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class Wool extends ShopItem {

	public Wool() {
		super("wool", "Wool", "Great for bridging across islands.\nTurns into your team's color.", 4, 16, Currency.IRON, Material.WHITE_WOOL,
			DatapointBedWarsHotbar.HotbarItemType.BLOCKS);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		giveItem(player, ItemStack.builder(player.getTeamKey().woolMaterial())
				.amount(16)
				.build());
	}

}
