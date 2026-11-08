package com.deyo.hypixel.type.skyblockgeneric.fishing;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingBaitComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingRodPartComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public record FishingContext(
    SkyBlockPlayer player,
    SkyBlockItem rod,
    FishingMedium medium,
    @Nullable FishingBaitComponent bait,
    @Nullable FishingRodPartComponent hook,
    @Nullable FishingRodPartComponent line,
    @Nullable FishingRodPartComponent sinker,
    @Nullable String regionId,
    Pos hookPosition,
    boolean hotspotActive,
    ItemStatistics hotspotBuffs,
    long castDurationMs
) {
}
