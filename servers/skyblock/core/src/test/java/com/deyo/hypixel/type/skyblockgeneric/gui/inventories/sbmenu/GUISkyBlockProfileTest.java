package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.commons.text.Text;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUISkyBlockProfileTest {

    @Test
    void statisticLinesUseTheProvidedSnapshot() {
        ItemStatistics values = ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 12.5D)
                .build();
        List<Text> lore = new ArrayList<>();

        GUISkyBlockProfile.addStatisticLines(lore,
                List.of(ItemStatistic.HEALTH, ItemStatistic.STRENGTH), values);

        assertEquals(2, lore.size());
        assertTrue(lore.getFirst().serialize().contains("12.5"));
    }
}
