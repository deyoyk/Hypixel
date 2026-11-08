package com.deyo.hypixel.type.skyblockgeneric.mission.missions.farmer;


import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockProgressMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionCollectWheat extends SkyBlockProgressMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        if (event.getMaterial().equals(Material.WHEAT)) {
            MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
            mission.setMissionProgress(mission.getMissionProgress() + 1);
            mission.checkIfMissionEnded(event.getPlayer());
        }
    }

    @Override
    public String getID() {
        return "mission_collect_wheat";
    }

    @Override
    public String getName() {
        return "Collect Wheat";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<b>20 Farming XP"))).forEach(player::sendMessage);
        player.getSkills().increase(player, SkillCategories.FARMING, 20D);
        player.getMissionData().startMission(MissionTalkToFarmerAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.FARM);
    }

    @Override
    public Double getAttachedSkyBlockXP() {
        return 5D;
    }

    @Override
    public int getMaxProgress() {
        return 10;
    }
}
