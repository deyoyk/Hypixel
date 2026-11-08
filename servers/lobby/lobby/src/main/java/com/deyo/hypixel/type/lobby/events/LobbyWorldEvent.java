package com.deyo.hypixel.type.lobby.events;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class LobbyWorldEvent implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        event.setCancelled(true);
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        event.setCancelled(true);
    }
}
