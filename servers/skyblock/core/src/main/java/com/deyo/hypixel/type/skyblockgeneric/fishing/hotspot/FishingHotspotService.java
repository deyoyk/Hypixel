package com.deyo.hypixel.type.skyblockgeneric.fishing.hotspot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.fishing.FishingMedium;
import com.deyo.hypixel.type.skyblockgeneric.fishing.registry.FishingRegistry;
import com.deyo.hypixel.type.skyblockgeneric.fishing.rod.FishingRodPartService;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingHotspotService {
    private static final double HOTSPOT_RADIUS_SQUARED = 25.0D;

    public static boolean isBobberInHotspot(SkyBlockPlayer player, FishingMedium medium, Pos bobberPos) {
        ItemStatistics buffs = getActiveHotspotBuffs(player, medium, bobberPos);
        for (ItemStatistic statistic : ItemStatistic.values()) {
            if (buffs.getOverall(statistic) != 0.0D) {
                return true;
            }
        }
        return false;
    }

    public static ItemStatistics getActiveHotspotBuffs(SkyBlockPlayer player, FishingMedium medium, Pos bobberPos) {
        String serverType = HypixelConst.getTypeLoader().getType().name();
        SkyBlockRegion playerRegion = player.getRegion();
        String regionId = playerRegion == null ? null : playerRegion.getType().name();
        double sinkerMultiplier = 1.0D;
        if (player.getItemInMainHand() != null) {
            var heldItem = new com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem(player.getItemInMainHand());
            var sinker = FishingRodPartService.getSinker(heldItem);
            if (sinker != null && sinker.getHotspotBuffMultiplier() > 0) {
                sinkerMultiplier = sinker.getHotspotBuffMultiplier();
            }
        }

        ItemStatistics.Builder builder = ItemStatistics.builder();
        for (HotspotDefinition hotspot : FishingRegistry.getHotspots()) {
            if (hotspot.medium() != medium) {
                continue;
            }
            if (!hotspot.regions().isEmpty() && (regionId == null || !hotspot.regions().contains(regionId))) {
                continue;
            }
            if (!matchesAnyPoint(serverType, bobberPos, hotspot)) {
                continue;
            }

            for (ItemStatistic statistic : ItemStatistic.values()) {
                double value = hotspot.buffs().getOverall(statistic) * sinkerMultiplier;
                if (value != 0.0D) {
                    builder.withBase(statistic, value);
                }
            }
        }
        return builder.build();
    }

    private static boolean matchesAnyPoint(String serverType, Pos bobberPos, HotspotDefinition hotspot) {
        for (HotspotDefinition.SpawnPoint point : hotspot.spawnPoints()) {
            if (point.serverType() != null && !point.serverType().equals(serverType)) {
                continue;
            }
            double dx = bobberPos.x() - point.x();
            double dy = bobberPos.y() - point.y();
            double dz = bobberPos.z() - point.z();
            if ((dx * dx) + (dy * dy) + (dz * dz) <= HOTSPOT_RADIUS_SQUARED) {
                return true;
            }
        }
        return false;
    }
}
