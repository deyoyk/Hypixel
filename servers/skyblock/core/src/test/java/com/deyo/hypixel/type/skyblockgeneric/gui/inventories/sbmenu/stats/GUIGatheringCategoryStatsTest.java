package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGatheringCategoryStatsTest {

    @Test
    void visibleStatsFilterAgainstASingleSnapshot() {
        ItemStatistics values = ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 10D)
                .build();

        assertEquals(List.of(ItemStatistic.HEALTH),
                GUIGatheringCategoryStats.visibleStats(
                        GUIGatheringCategoryStats.Category.COMBAT, false, values));
        assertTrue(GUIGatheringCategoryStats.visibleStats(
                GUIGatheringCategoryStats.Category.COMBAT, false, ItemStatistics.empty()).isEmpty());
        assertEquals(GUIGatheringCategoryStats.Category.COMBAT.getStatistics().size(),
                GUIGatheringCategoryStats.visibleStats(
                        GUIGatheringCategoryStats.Category.COMBAT, true, ItemStatistics.empty()).size());
    }
}
