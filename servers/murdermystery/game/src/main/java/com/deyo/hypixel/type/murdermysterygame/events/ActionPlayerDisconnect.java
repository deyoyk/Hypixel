package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.murdermysterygame.MurderMysteryGameScoreboard;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
	public void run(PlayerDisconnectEvent event) {
		MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();

		Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
		if (game != null) {
			game.disconnect(player);
		}

		MurderMysteryGameScoreboard.removeCache(player);
	}
}
