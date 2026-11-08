package com.deyo.hypixel.dungeons.catacombs.kit;

import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassType;

import java.util.List;

public record DungeonClassKit(
        String id,
        String displayName,
        DungeonClassType type,
        List<DungeonKitItem> items
) {
    public DungeonClassKit {
        items = List.copyOf(items);
    }
}
