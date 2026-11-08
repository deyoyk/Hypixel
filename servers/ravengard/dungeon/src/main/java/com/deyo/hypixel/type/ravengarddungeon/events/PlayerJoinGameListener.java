package com.deyo.hypixel.type.ravengarddungeon.events;

import com.deyo.hypixel.type.game.game.event.PlayerPostJoinGameEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.ravengarddungeon.game.RavengardDungeonGame;
import com.deyo.hypixel.type.ravengarddungeon.user.RavengardDungeonPlayer;

public class PlayerJoinGameListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerJoinGame(PlayerPostJoinGameEvent event) {
        RavengardDungeonPlayer player = (RavengardDungeonPlayer) event.player();
        int playerIndex = event.currentPlayerCount() - 1;
        player.setInstance(event.getGame().getInstance(),
                RavengardDungeonGame.SPAWN_POINTS.get(playerIndex % RavengardDungeonGame.SPAWN_POINTS.size()));
    }
}
