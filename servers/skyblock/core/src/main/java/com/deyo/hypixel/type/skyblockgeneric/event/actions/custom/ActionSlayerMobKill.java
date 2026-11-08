package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerService;

public class ActionSlayerMobKill implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        SlayerService.handleMobKill(event.getPlayer(), event.getKilledMob());
    }
}
