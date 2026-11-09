package com.deyo.hypixel.type.skyblockgeneric.slayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlayerServiceTest {

    @Test
    void slashedPricingHalvesTheQuestCost() {
        assertEquals(2_000, SlayerService.applySlashedPricing(2_000, false));
        assertEquals(1_000, SlayerService.applySlashedPricing(2_000, true));
        assertEquals(1, SlayerService.applySlashedPricing(1, true));
    }

    @Test
    void slayerXpBuffAddsTwentyFivePercent() {
        assertEquals(100, SlayerService.applySlayerXpBuff(100, false));
        assertEquals(125, SlayerService.applySlayerXpBuff(100, true));
        assertEquals(6, SlayerService.applySlayerXpBuff(5, true));
    }
}
