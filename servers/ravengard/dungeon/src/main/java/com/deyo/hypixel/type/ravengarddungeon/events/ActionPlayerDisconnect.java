package com.deyo.hypixel.type.ravengarddungeon.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.ravengarddungeon.TypeRavengardDungeonLoader;
import com.deyo.hypixel.type.ravengarddungeon.user.RavengardDungeonPlayer;

public class ActionPlayerDisconnect implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void run(PlayerDisconnectEvent event) {
        TypeRavengardDungeonLoader.getGame().disconnect((RavengardDungeonPlayer) event.getPlayer());
    }
}
