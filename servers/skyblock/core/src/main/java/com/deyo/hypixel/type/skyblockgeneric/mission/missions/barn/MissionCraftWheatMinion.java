package com.deyo.hypixel.type.skyblockgeneric.mission.missions.barn;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.ItemCraftEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class MissionCraftWheatMinion extends SkyBlockMission {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onCraftEvent(ItemCraftEvent event) {
        MissionData data = event.getPlayer().getMissionData();

        if (data.isCurrentlyActive(MissionCraftWheatMinion.class) || data.hasCompleted(MissionCraftWheatMinion.class)) {
            return;
        }

        if (event.getCraftedItem().getAttributeHandler().getPotentialType() != ItemType.WHEAT_MINION) {
            return;
        }

        data.setSkyBlockPlayer(event.getPlayer());
        data.startMission(MissionCraftWheatMinion.class);
    }

    @Override
    public String getID() {
        return "craft_minion";
    }

    @Override
    public String getName() {
        return "Craft a Wheat Minion";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return Map.of();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        player.getMissionData().startMission(MissionTalkToFarmhandAgain.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.THE_BARN);
    }
}
