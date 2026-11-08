package com.deyo.hypixel.type.replayviewer.event;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.replayviewer.TypeReplayViewerLoader;

public class PlayerLeaveEvent implements HypixelEventClass {
	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		TypeReplayViewerLoader.getSession(event.getPlayer().getUuid()).ifPresent(session -> {
			session.removeViewer(event.getPlayer());
		});
	}
}
