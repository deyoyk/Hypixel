package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.world.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JesusFlagTest {

    @Test
    void waterAndLavaAreLiquids() {
        assertTrue(JesusFlag.isLiquid(new Block(true, false, null)));
        assertTrue(JesusFlag.isLiquid(new Block(false, true, null)));
        assertTrue(JesusFlag.isLiquid(new Block(true, true, null)));
    }

    @Test
    void solidAndMissingBlocksAreNotLiquids() {
        assertFalse(JesusFlag.isLiquid(new Block(false, false, null)));
        assertFalse(JesusFlag.isLiquid(null));
    }
}
