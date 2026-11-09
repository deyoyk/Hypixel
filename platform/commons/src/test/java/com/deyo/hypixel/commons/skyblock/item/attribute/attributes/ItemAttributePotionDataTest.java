package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributePotionData.PotionData;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemAttributePotionDataTest {

    @Test
    void brewerLevelAddsOnePercentPotionDurationPerLevel() {
        PotionData potion = new PotionData("SPEED", 1, 180);

        assertEquals(180_000L, potion.getFinalDurationMs(0));
        assertEquals(181_800L, potion.getFinalDurationMs(1));
        assertEquals(270_000L, potion.getFinalDurationMs(50));
        assertEquals(270_000L, potion.getFinalDurationMs(100));
        assertEquals(180_000L, potion.getFinalDurationMs(-10));
    }

    @Test
    void splashPotionsUseHalfDurationUnlessExtended() {
        PotionData splash = new PotionData("SPEED", 1, 180, true, false);
        PotionData extendedSplash = new PotionData("SPEED", 1, 180, true, true);

        assertEquals(90_000L, splash.getFinalDurationMs(0));
        assertEquals(180_000L, extendedSplash.getFinalDurationMs(0));
    }

    @Test
    void brewerIsAPercentageStatistic() {
        assertTrue(ItemStatistic.BREWER.getIsPercentage());
    }
}
