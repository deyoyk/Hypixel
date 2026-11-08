package com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.birchpark;

import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionClaimTheTrousers extends SkyBlockMission {

	@Override
	public String getID() {
		return "claim_charlie_trousers";
	}

	@Override
	public String getName() {
		return "Claim the trousers from Charlie!";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionTalkToCharlieAgain.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.BIRCH_PARK);
	}
}
