package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JesusFlagTest {

    @Test
    void waterAndLavaAreLiquids() {
        assertTrue(JesusFlag.isLiquid(Block.WATER));
        assertTrue(JesusFlag.isLiquid(Block.LAVA));
    }

    @Test
    void solidAndMissingBlocksAreNotLiquids() {
        assertFalse(JesusFlag.isLiquid(Block.STONE));
        assertFalse(JesusFlag.isLiquid(Block.AIR));
        assertFalse(JesusFlag.isLiquid(null));
    }
}
