package com.deyo.hypixel.type.skyblockgeneric.mission.missions.sheperd;


import net.minestom.server.entity.EntityType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockProgressMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionShearSheep extends SkyBlockProgressMission {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onKilledSheep(PlayerKilledSkyBlockMobEvent event) {
        if (event.getKilledMob().getEntityType() != EntityType.SHEEP) return;

        MissionData data = event.getPlayer().getMissionData();
        if (!data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) return;

        MissionData.ActiveMission mission = data.getMission(this.getClass()).getKey();
        mission.setMissionProgress(mission.getMissionProgress() + 1);
        mission.checkIfMissionEnded(event.getPlayer());
    }

    @Override
    public String getID() {
        return "mission_shear_sheep";
    }

    @Override
    public String getName() {
        return "Shear Sheep";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<b>20 Farming XP", "<6>100 <7>Coins"))).forEach(player::sendMessage);
        player.getSkills().increase(player, SkillCategories.FARMING, 20D);
        player.addCoins(100);
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMissionCause(getID()));
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.SHEPHERD_KEEP);
    }

    @Override
    public Double getAttachedSkyBlockXP() {
        return 5D;
    }

    @Override
    public int getMaxProgress() {
        return 5;
    }
}
