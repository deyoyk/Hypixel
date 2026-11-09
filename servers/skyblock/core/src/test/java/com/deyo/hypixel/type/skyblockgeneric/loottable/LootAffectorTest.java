package com.deyo.hypixel.type.skyblockgeneric.loottable;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LootAffectorTest {

    @Test
    void magicFindUsesTheProvidedSnapshot() {
        ItemStatistics statistics = ItemStatistics.builder()
                .withBase(ItemStatistic.MAGIC_FIND, 50D)
                .build();

        assertEquals(1.5, LootAffector.MAGIC_FIND.apply(null, 1.0, null, statistics));
    }

    @Test
    void petLuckUsesTheProvidedSnapshot() {
        ItemStatistics statistics = ItemStatistics.builder()
                .withBase(ItemStatistic.PET_LUCK, 25D)
                .build();

        assertEquals(1.25, LootAffector.PET_LUCK.apply(null, 1.0, null, statistics));
    }
}
