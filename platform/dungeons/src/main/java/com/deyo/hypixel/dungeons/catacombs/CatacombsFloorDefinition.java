package com.deyo.hypixel.dungeons.catacombs;

import com.deyo.hypixel.dungeons.catacombs.boss.CatacombsBossEncounter;
import com.deyo.hypixel.dungeons.catacombs.puzzle.CatacombsPuzzle;
import com.deyo.hypixel.dungeons.catacombs.run.DungeonRunRules;

import java.util.Set;

public record CatacombsFloorDefinition(
        CatacombsFloor floor,
        CatacombsMode mode,
        CatacombsBossEncounter boss,
        DungeonRunRules rules,
        int puzzleRooms,
        int minibossRooms,
        int trapRooms,
        Set<CatacombsPuzzle> possiblePuzzles
) {
    public CatacombsFloorDefinition {
        possiblePuzzles = Set.copyOf(possiblePuzzles);
        if (!floor.supports(mode)) {
            throw new IllegalArgumentException(floor.displayName() + " does not support " + mode);
        }
    }
}
