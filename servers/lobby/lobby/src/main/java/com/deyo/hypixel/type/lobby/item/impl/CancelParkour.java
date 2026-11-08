package com.deyo.hypixel.type.lobby.item.impl;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.item.LobbyItem;
import com.deyo.hypixel.type.lobby.parkour.LobbyParkourManager;

public class CancelParkour extends LobbyItem {

	public CancelParkour() {
		super("cancel_parkour");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStacks.named(Material.RED_BED, "<c>Cancel").build();
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
			LobbyParkourManager parkourManager = lobbyLoader.getParkourManager();
			if (parkourManager == null) return;
			parkourManager.cancelParkour((HypixelPlayer) event.getPlayer());
		}
	}
}
