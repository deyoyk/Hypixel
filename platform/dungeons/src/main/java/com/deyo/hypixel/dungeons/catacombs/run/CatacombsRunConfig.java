package com.deyo.hypixel.dungeons.catacombs.run;

import com.deyo.hypixel.dungeons.catacombs.CatacombsFloorDefinition;
import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassType;

import java.util.Map;
import java.util.UUID;

public record CatacombsRunConfig(
        CatacombsFloorDefinition floor,
        Map<UUID, DungeonClassType> partyClasses,
        int totalRooms,
        int totalSecrets
) {
    public CatacombsRunConfig {
        partyClasses = Map.copyOf(partyClasses);
        if (!floor.rules().allowsPartySize(partyClasses.size())) {
            throw new IllegalArgumentException("Invalid Catacombs party size " + partyClasses.size());
        }
    }
}
