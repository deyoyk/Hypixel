package com.deyo.hypixel.type.prototypelobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.prototypelobby.util.PrototypeLobbyMap;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.SPAWN)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        PrototypeLobbyMap prototypeLobbyMap = new PrototypeLobbyMap();
        prototypeLobbyMap.sendMapData(player);
    }
}
