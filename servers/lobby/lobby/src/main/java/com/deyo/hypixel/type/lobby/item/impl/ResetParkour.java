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

public class ResetParkour extends LobbyItem {

	public ResetParkour() {
		super("reset_parkour");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStacks.named(Material.OAK_DOOR, "<c>Reset").build();
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
			LobbyParkourManager parkourManager = lobbyLoader.getParkourManager();
			if (parkourManager == null) return;
			parkourManager.resetPlayer((HypixelPlayer) event.getPlayer());
		}
	}
}
