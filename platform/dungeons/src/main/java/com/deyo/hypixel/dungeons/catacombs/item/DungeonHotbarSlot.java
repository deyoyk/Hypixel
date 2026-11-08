package com.deyo.hypixel.dungeons.catacombs.item;

import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassAbility;

public record DungeonHotbarSlot(
        int slot,
        DungeonHotbarAction action,
        DungeonClassAbility ability
) {}
