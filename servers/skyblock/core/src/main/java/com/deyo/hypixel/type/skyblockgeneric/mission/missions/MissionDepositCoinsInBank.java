package com.deyo.hypixel.type.skyblockgeneric.mission.missions;


import com.deyo.hypixel.type.generic.data.datapoints.DatapointDouble;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class MissionDepositCoinsInBank extends SkyBlockMission {

    @Override
    public String getID() {
        return "deposit_coins_in_bank";
    }

    @Override
    public String getName() {
        return "Deposit coins in the Bank";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("<6>10 <7>Coins"))).forEach(player::sendMessage);
        player.getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue() + 10
        );
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}
