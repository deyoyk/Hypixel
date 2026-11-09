package com.deyo.hypixel.type.skyblockgeneric.user.statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerStatisticsRuntimeTest {

    @Test
    void runtimeValuesIncludeAbsorptionAndRoundForDisplay() {
        PlayerStatistics.RuntimeValues values = PlayerStatistics.runtimeValues(
                10.4f, 2f, 50.6f, 20.6f, 12.4, 120.5f);

        assertEquals(12, values.hearts());
        assertEquals(21, values.maxHearts());
        assertEquals(12.4, values.defense());
        assertEquals(51, values.mana());
        assertEquals(121, values.maxMana());
        assertEquals(2f, values.absorption());
    }

    @Test
    void healthRegenerationUsesTheCurrentMaxHealthAndBonus() {
        assertEquals(2.5f, PlayerStatistics.healthRegenerationAmount(100f, 0));
        assertEquals(3.75f, PlayerStatistics.healthRegenerationAmount(100f, 0.5));
        assertEquals(2.0f, PlayerStatistics.healthRegenerationAmount(50.9f, 0));
    }
}
