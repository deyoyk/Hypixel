package com.deyo.hypixel.type.skyblockgeneric.mission.missions;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionTalkToBanker extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive("talk_to_banker") || data.hasCompleted("talk_to_banker")) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(MissionTalkToBanker.class);
    }

    @Override
    public String getID() {
        return "talk_to_banker";
    }

    @Override
    public String getName() {
        return "Talk to the Banker";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionDepositCoinsInBank.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}
