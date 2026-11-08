package com.deyo.hypixel.type.skyblockgeneric.mission.missions;


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

public class MissionKillZombies extends SkyBlockProgressMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onKilledZombie(PlayerKilledSkyBlockMobEvent event) {
        if (event.getKilledMob().getEntityType() != EntityType.ZOMBIE) return;

        MissionData data = event.getPlayer().getMissionData();
        if (!data.isCurrentlyActive(MissionKillZombies.class) || data.hasCompleted(MissionKillZombies.class)) return;

        MissionData.ActiveMission mission = data.getMission(MissionKillZombies.class).getKey();
        mission.setMissionProgress(mission.getMissionProgress() + 1);
        mission.checkIfMissionEnded(event.getPlayer());
    }

    @Override
    public String getID() {
        return "kill_zombies";
    }

    @Override
    public String getName() {
        return "Kill Zombies";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<b>5 SkyBlock XP", "<3>100 <7>Combat XP"))).forEach(player::sendMessage);
        player.getSkills().increase(player, SkillCategories.COMBAT, 100D);
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMissionCause(getID()));
        player.getMissionData().startMission(MissionTalkToBartender.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.GRAVEYARD);
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
