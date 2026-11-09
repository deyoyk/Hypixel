package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.region;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionRegionBlockBreakTest {

    @Test
    void fortuneMultiplierAddsBaseAndSpecificFortune() {
        assertEquals(1.0, ActionRegionBlockBreak.fortuneMultiplier(0, 0));
        assertEquals(1.25, ActionRegionBlockBreak.fortuneMultiplier(10, 15));
    }

    @Test
    void fortuneAmountRoundsDropsUp() {
        assertEquals(1, ActionRegionBlockBreak.fortuneAmount(1, 1.0));
        assertEquals(2, ActionRegionBlockBreak.fortuneAmount(1, 1.01));
        assertEquals(4, ActionRegionBlockBreak.fortuneAmount(3, 1.25));
    }
}
