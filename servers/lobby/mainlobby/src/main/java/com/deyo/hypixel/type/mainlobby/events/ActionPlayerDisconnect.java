package com.deyo.hypixel.type.mainlobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.mainlobby.MainLobbyScoreboard;

public class ActionPlayerDisconnect implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        MainLobbyScoreboard.removeCache(player);
    }
}

