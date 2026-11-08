package com.deyo.hypixel.type.skywarsgame.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skywarsgame.SkywarsGameScoreboard;
import com.deyo.hypixel.type.skywarsgame.TypeSkywarsGameLoader;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGame;
import com.deyo.hypixel.type.skywarsgame.user.SkywarsPlayer;

public class ActionPlayerDisconnect implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);

        if (game != null) {
            game.disconnect(player);
        }
        // git is wierd with this file idk
        SkywarsGameScoreboard.removeCache(player);
    }
}
