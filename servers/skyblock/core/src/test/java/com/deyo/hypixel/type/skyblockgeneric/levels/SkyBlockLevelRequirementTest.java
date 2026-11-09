package com.deyo.hypixel.type.skyblockgeneric.levels;

import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.levels.unlocks.SkyBlockLevelStatisticUnlock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkyBlockLevelRequirementTest {

    @Test
    void cumulativeStatisticUnlocksIncludeEveryLevelUpToTheTarget() {
        SkyBlockLevelRequirement levelOne = requirement(1, health(1));
        SkyBlockLevelRequirement levelTwo = requirement(2, health(2));

        Map<Integer, ItemStatistics> cumulative = SkyBlockLevelRequirement.buildCumulativeStatisticUnlocks(
                Map.of(1, levelOne, 2, levelTwo));

        assertEquals(0.0, cumulative.get(0).getOverall(ItemStatistic.HEALTH), 1.0e-9);
        assertEquals(1.0, cumulative.get(1).getOverall(ItemStatistic.HEALTH), 1.0e-9);
        assertEquals(3.0, cumulative.get(2).getOverall(ItemStatistic.HEALTH), 1.0e-9);
    }

    @Test
    void missingLevelsCarryThePreviousCumulativeValueForward() {
        SkyBlockLevelRequirement levelOne = requirement(1, health(2));
        SkyBlockLevelRequirement levelThree = requirement(3, health(5));

        Map<Integer, ItemStatistics> cumulative = SkyBlockLevelRequirement.buildCumulativeStatisticUnlocks(
                Map.of(1, levelOne, 3, levelThree));

        assertEquals(2.0, cumulative.get(1).getOverall(ItemStatistic.HEALTH), 1.0e-9);
        assertEquals(2.0, cumulative.get(2).getOverall(ItemStatistic.HEALTH), 1.0e-9);
        assertEquals(7.0, cumulative.get(3).getOverall(ItemStatistic.HEALTH), 1.0e-9);
    }

    private static SkyBlockLevelRequirement requirement(int level, ItemStatistics statistics) {
        return new SkyBlockLevelRequirement(
                level, 0, false,
                List.of(new SkyBlockLevelStatisticUnlock(statistics)),
                "", "", Material.STONE);
    }

    private static ItemStatistics health(double amount) {
        return ItemStatistics.builder().withBase(ItemStatistic.HEALTH, amount).build();
    }
}
