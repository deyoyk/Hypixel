package com.deyo.hypixel.type.thepark.events;

import net.minestom.server.entity.Entity;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.birchpark.MissionTravelToThePark;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionFindTheCampfire;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.darkthicket.MissionTravelToTheDarkThicket;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.jungle.MissionTalkToMolbertAgainAgainAgain;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.savanna.MissionTravelToTheSavannaWoodland;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.thepark.spruce.MissionTravelToTheSpruceWoods;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.thepark.TypeTheParkLoader;
import com.deyo.hypixel.type.thepark.npcs.NPCWorkerXavier;

public class ActionContinueParkMission implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerRegionChangeEvent event) {
        if (event.getTo() == null) return;

		SkyBlockPlayer player = event.getPlayer();
        MissionData data = event.getPlayer().getMissionData();
		switch (event.getTo()) {
			case BIRCH_PARK -> {
				if (data.isCurrentlyActive(MissionTravelToThePark.class)) {
					data.endMission(MissionTravelToThePark.class);
				}
			}
			case SPRUCE_WOODS -> {
				if (data.isCurrentlyActive(MissionTravelToTheSpruceWoods.class)) {
					data.endMission(MissionTravelToTheSpruceWoods.class);
				}
			}
			case DARK_THICKET -> {
				if (data.isCurrentlyActive(MissionTravelToTheDarkThicket.class)) {
					data.endMission(MissionTravelToTheDarkThicket.class);
				}
			}
			case TRIALS_OF_FIRE -> {
				if (data.isCurrentlyActive(MissionFindTheCampfire.class)) {
					data.endMission(MissionFindTheCampfire.class);
				}
			}
			case SAVANNA_WOODLAND -> {
				if (data.isCurrentlyActive(MissionTravelToTheSavannaWoodland.class)) {
					NPCWorkerXavier xavier = HypixelNPC.getRegisteredNPCs().stream()
							.filter(npc -> npc instanceof NPCWorkerXavier)
							.map(npc -> (NPCWorkerXavier) npc)
							.findFirst()
							.orElse(null);
					if (xavier == null) {
						return;
					}
					xavier.setDialogue(player, "intro").thenRun(() -> {
						data.endMission(MissionTravelToTheSavannaWoodland.class);
					});
				}
			}
			case JUNGLE_ISLAND -> {
				if (data.isCurrentlyActive(MissionTalkToMolbertAgainAgainAgain.class)) {
					TypeTheParkLoader.entities.forEach(Entity::updateViewableRule);
				}
			}
			case null, default -> {
			}
		}

    }
}
