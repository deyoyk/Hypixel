package com.deyo.hypixel.type.skyblockgeneric.enchantment.debuff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VenomousDebuffTest {

    @Test
    void speedReductionScalesWithLevel() {
        assertEquals(0.095, VenomousDebuff.reducedSpeed(0.1, 1, false), 1e-9);
        assertEquals(0.070, VenomousDebuff.reducedSpeed(0.1, 6, false), 1e-9);
    }

    @Test
    void catacombsReducesTheDebuffByEightyPercent() {
        assertEquals(0.094, VenomousDebuff.reducedSpeed(0.1, 6, true), 1e-9);
    }

    @Test
    void levelIsClampedToTheSupportedRange() {
        assertEquals(VenomousDebuff.reducedSpeed(0.1, 1, false),
                VenomousDebuff.reducedSpeed(0.1, 0, false), 1e-9);
        assertEquals(VenomousDebuff.reducedSpeed(0.1, 6, false),
                VenomousDebuff.reducedSpeed(0.1, 100, false), 1e-9);
    }
}
