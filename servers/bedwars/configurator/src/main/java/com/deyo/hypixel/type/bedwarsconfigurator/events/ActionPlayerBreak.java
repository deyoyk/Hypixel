package com.deyo.hypixel.type.bedwarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPlayerBreak implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER , requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockBreakEvent event) {
        event.setCancelled(true);
    }
}

