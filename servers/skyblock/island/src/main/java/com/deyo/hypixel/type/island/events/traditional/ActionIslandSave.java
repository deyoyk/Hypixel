package com.deyo.hypixel.type.island.events.traditional;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.island.SkyBlockIsland;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionIslandSave implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.PERSIST, order = 20)
    public void run(PlayerDisconnectEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockIsland island = player.getSkyBlockIsland();
        if (island != null) {
            island.runVacantCheck();
        }
    }
}
