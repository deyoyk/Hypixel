package com.deyo.hypixel.type.skyblockgeneric.mission.missions.blacksmith;


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

public class MissionMineCoal extends SkyBlockProgressMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        if (event.getMaterial().equals(Material.COAL_ORE)) {
            MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
            mission.setMissionProgress(mission.getMissionProgress() + 1);
            mission.checkIfMissionEnded(event.getPlayer());
        }
    }

    @Override
    public String getID() {
        return "mission_mine_coal";
    }

    @Override
    public String getName() {
        return "Mine Coal";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<3>100 <7>Mining Experience", "<b>5 SkyBlock XP"))).forEach(player::sendMessage);
        player.getSkills().increase(player, SkillCategories.MINING, 100D);
        player.getMissionData().startMission(MissionTalkToBlacksmithAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.BLACKSMITH, RegionType.COAL_MINE);
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
