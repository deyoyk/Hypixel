package com.deyo.hypixel.type.prototypelobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.prototypelobby.PrototypeLobbyScoreboard;

public class ActionPlayerDisconnect implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void run(PlayerDisconnectEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        PrototypeLobbyScoreboard.removeCache(player);
    }
}

