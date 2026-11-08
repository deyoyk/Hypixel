package com.deyo.hypixel.type.island.events.traditional;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.MissionBreakLog;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionStartIslandMission implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.POST_DATA)
    public void run(AsyncPlayerConfigurationEvent event) {
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        if (data.isCurrentlyActive("break_log")) return;
        if (data.hasCompleted("break_log")) return;

        data.startMission(MissionBreakLog.class);
    }
}
