package com.deyo.hypixel.type.skyblockgeneric.item.set.sets;

import com.deyo.hypixel.commons.ServerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinerArmorSetTest {

    @Test
    void onlyMiningIslandServerTypesReceiveTheBonus() {
        assertTrue(MinerArmorSet.isMiningIsland(ServerType.SKYBLOCK_GOLD_MINE));
        assertTrue(MinerArmorSet.isMiningIsland(ServerType.SKYBLOCK_DEEP_CAVERNS));
        assertTrue(MinerArmorSet.isMiningIsland(ServerType.SKYBLOCK_DWARVEN_MINES));
        assertFalse(MinerArmorSet.isMiningIsland(ServerType.SKYBLOCK_HUB));
        assertFalse(MinerArmorSet.isMiningIsland(null));
    }
}
