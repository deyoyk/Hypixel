package com.deyo.hypixel.anticheat.flag.flags;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimerFlagTest {

    @Test
    void averageIntervalUsesTheWholeSampleWindow() {
        assertEquals(50.0, TimerFlag.averageInterval(1000, 20));
        assertEquals(25.0, TimerFlag.averageInterval(500, 20));
        assertEquals(0.0, TimerFlag.averageInterval(0, 0));
    }

    @Test
    void tooFastRequiresLessThanSeventyPercentOfANormalTick() {
        assertTrue(TimerFlag.isTooFast(34.9));
        assertFalse(TimerFlag.isTooFast(35.0));
        assertFalse(TimerFlag.isTooFast(50.0));
    }
}
