package com.deyo.hypixel.type.skyblockgeneric.mission.missions.blacksmith;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.LocationAssociatedMission;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionTalkToBlacksmith extends SkyBlockMission implements LocationAssociatedMission {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerRegionChangeEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (event.getTo() == null || !event.getTo().equals(RegionType.BLACKSMITH)) {
            return;
        }

        if (data.isCurrentlyActive(this.getClass()) || data.hasCompleted(this.getClass())) {
            return;
        }

        data.startMission(this.getClass());
    }

    @Override
    public String getID() {
        return "talk_to_blacksmith";
    }

    @Override
    public String getName() {
        return "Talk to the Blacksmith";
    }

    @Override
    public HashMap<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionMineCoal.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.BLACKSMITH, RegionType.COAL_MINE);
    }

    @Override
    public Pos getLocation() {
        return new Pos(-28.5, 69, -125.45);
    }
}
