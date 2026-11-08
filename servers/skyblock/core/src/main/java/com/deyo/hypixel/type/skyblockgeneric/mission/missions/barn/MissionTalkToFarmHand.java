package com.deyo.hypixel.type.skyblockgeneric.mission.missions.barn;


import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionTalkToFarmHand extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onRegionChange(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.THE_BARN)) {
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
        return "talk_to_farmhand";
    }

    @Override
    public String getName() {
        return "Talk to the Farmhand";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<b>5 SkyBlock XP", "<3>100 <7>Farming Experience"))).forEach(player::sendMessage);
        player.getSkills().increase(player, SkillCategories.FARMING, 100D);
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMissionCause(getID()));
        player.getMissionData().startMission(MissionCraftWheatMinion.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }
}
