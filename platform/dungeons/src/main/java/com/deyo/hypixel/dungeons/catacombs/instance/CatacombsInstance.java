package com.deyo.hypixel.dungeons.catacombs.instance;

import com.deyo.hypixel.dungeons.CatacombsDungeon;
import com.deyo.hypixel.dungeons.catacombs.CatacombsFloorDefinition;
import com.deyo.hypixel.dungeons.catacombs.boss.state.BossFightController;
import com.deyo.hypixel.dungeons.catacombs.kit.DungeonClassKit;
import com.deyo.hypixel.dungeons.catacombs.run.CatacombsRunState;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public record CatacombsInstance(
        UUID id,
        CatacombsFloorDefinition floor,
        CatacombsDungeon dungeon,
        CatacombsRunState runState,
        BossFightController bossFight,
        Map<UUID, DungeonClassKit> kits,
        Map<Integer, DungeonRoomEncounter> encounters,
        Path renderedMap
) {
    public CatacombsInstance {
        kits = Map.copyOf(kits);
        encounters = Map.copyOf(encounters);
    }
}
