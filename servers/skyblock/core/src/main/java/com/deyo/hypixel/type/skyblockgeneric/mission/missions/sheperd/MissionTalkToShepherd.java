package com.deyo.hypixel.type.skyblockgeneric.mission.missions.sheperd;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionTalkToShepherd extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onVillagerSpokenTo(NPCInteractEvent event) {
        MissionData data = ((SkyBlockPlayer) event.getPlayer()).getMissionData();

        // Check if this is the Shepherd NPC
        if (!event.getNpc().getClass().getSimpleName().contains("Shepherd")) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.setSkyBlockPlayer((SkyBlockPlayer) event.getPlayer());
        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_shepherd";
    }

    @Override
    public String getName() {
        return "Talk to the Shepherd";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        // Start the next mission in the chain
        player.getMissionData().startMission(MissionShearSheep.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.SHEPHERD_KEEP);
    }
}
