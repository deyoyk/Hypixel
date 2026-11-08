package com.deyo.hypixel.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.bedwarsgame.BedWarsGameScoreboard;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.GameState;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@SneakyThrows
	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		final BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		BedWarsGame game = player.getGame();
		if (game != null) {
			if (game.getState() == GameState.IN_PROGRESS) {
				game.handleDisconnect(player);
			} else {
				game.leave(player);
			}
		}
		BedWarsGameScoreboard.removeCache(player);
	}
}

