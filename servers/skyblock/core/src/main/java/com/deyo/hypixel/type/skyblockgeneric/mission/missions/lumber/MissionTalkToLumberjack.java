package com.deyo.hypixel.type.skyblockgeneric.mission.missions.lumber;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionTalkToLumberjack extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.FOREST)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_lumberjack";
    }

    @Override
    public String getName() {
        return "Talk to the Lumber Jack";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionBreakOaklog.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FOREST);
    }
}

