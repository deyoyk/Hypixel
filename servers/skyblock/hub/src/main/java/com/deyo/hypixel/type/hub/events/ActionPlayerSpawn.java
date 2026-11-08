package com.deyo.hypixel.type.hub.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.hub.util.HubMap;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.SPAWN)
    public void run(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        HubMap hubMap = new HubMap();
        hubMap.sendMapData(player);
    }
}
