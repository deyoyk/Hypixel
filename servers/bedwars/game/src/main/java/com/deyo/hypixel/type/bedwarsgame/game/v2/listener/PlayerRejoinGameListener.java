package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.event.PlayerRejoinGameEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class PlayerRejoinGameListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerGameRejoin(PlayerRejoinGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        BedWarsGame game = player.getGame();

        game.onPlayerRejoin(player, event.disconnectedPlayerData());
    }

}
