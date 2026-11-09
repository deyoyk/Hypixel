package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhaseFlagTest {

    @Test
    void liquidsAirAndMissingBlocksAreNotSolid() {
        assertFalse(PhaseFlag.isSolid(null));
        assertFalse(PhaseFlag.isSolid(Block.AIR));
        assertFalse(PhaseFlag.isSolid(Block.WATER));
        assertFalse(PhaseFlag.isSolid(Block.LAVA));
        assertFalse(PhaseFlag.isSolid(Block.TORCH));
    }

    @Test
    void solidBlocksAreSolid() {
        assertTrue(PhaseFlag.isSolid(Block.STONE));
    }
}
