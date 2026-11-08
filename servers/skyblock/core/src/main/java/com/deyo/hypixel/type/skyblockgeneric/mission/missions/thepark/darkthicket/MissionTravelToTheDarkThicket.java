package com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.darkthicket;


import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.skyblockgeneric.mission.LocationAssociatedMission;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.SkyBlockMission;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.Set;

public class MissionTravelToTheDarkThicket extends SkyBlockMission implements LocationAssociatedMission {

	@Override
	public Pos getLocation() {
		return new Pos(-365, 89, -19);
	}

	@Override
	public String getID() {
		return "travel_to_the_dark_thicket";
	}

	@Override
	public String getName() {
		return "Travel to the Dark Thicket";
	}

	@Override
	public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
		mission.getNewObjectiveText().forEach(player::sendMessage);
		return Map.of();
	}

	@Override
	public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
		player.getMissionData().startMission(MissionFindTheCampfire.class);
	}

	@Override
	public Set<RegionType> getValidRegions() {
		return Set.of(RegionType.SPRUCE_WOODS, RegionType.DARK_THICKET);
	}
}
