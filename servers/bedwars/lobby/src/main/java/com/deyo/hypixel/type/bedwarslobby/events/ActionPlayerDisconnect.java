package com.deyo.hypixel.type.bedwarslobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.bedwarslobby.BedWarsLobbyScoreboard;
import com.deyo.hypixel.type.bedwarslobby.gui.cosmetics.ShopkeeperPreviewController;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		ShopkeeperPreviewController.stopPreview(player, null, null);
		BedWarsLobbyScoreboard.removeCache(player);
	}
}

