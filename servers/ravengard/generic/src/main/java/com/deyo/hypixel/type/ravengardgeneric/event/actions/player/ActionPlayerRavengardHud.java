package com.deyo.hypixel.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.ravengardgeneric.hud.RavengardHud;

public class ActionPlayerRavengardHud implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.POST_SPAWN)
    public void onSpawn(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) {
            return;
        }
        RavengardHud.attach((HypixelPlayer) event.getPlayer());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        RavengardHud.detach((HypixelPlayer) event.getPlayer());
    }
}
