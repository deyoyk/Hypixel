package com.deyo.hypixel.type.skyblockgeneric.entity.mob.impl;

import lombok.Builder;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobRegistry;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;

import java.util.List;

public interface RegionPopulator {
    List<Populator> getPopulators();

    static void populateRegion(MobRegistry registry, Populator populator) {
        if (HypixelConst.getInstanceContainer() == null) return;
        SkyBlockRegion region = SkyBlockRegion.getRandomRegionOfType(populator.regionType());

        if (region == null) return;

        Pos randomPosition = region.getRandomPositionForEntity(HypixelConst.getInstanceContainer());

        if (randomPosition == null) return;

        if (!HypixelConst.getInstanceContainer().isChunkLoaded(randomPosition))
            HypixelConst.getInstanceContainer().loadChunk(randomPosition).join();

        SkyBlockMob mob = registry.asMob();
        mob.setInstance(HypixelConst.getInstanceContainer(), randomPosition);
    }

    @Builder
    record Populator(RegionType regionType, int minimumAmountToPopulate) {}
}
