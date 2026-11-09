package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.world.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhaseFlagTest {

    @Test
    void liquidsAndMissingBlocksAreNotSolid() {
        assertFalse(PhaseFlag.isSolid(null));
        assertFalse(PhaseFlag.isSolid(new Block(true, false, null)));
        assertFalse(PhaseFlag.isSolid(new Block(false, true, null)));
        assertFalse(PhaseFlag.isSolid(new Block(true, true, null)));
    }

    @Test
    void nonLiquidBlocksAreSolid() {
        assertTrue(PhaseFlag.isSolid(new Block(false, false, null)));
    }
}
