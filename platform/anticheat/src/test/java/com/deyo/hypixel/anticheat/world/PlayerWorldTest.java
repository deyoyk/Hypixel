package com.deyo.hypixel.anticheat.world;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PlayerWorldTest {

    private PlayerWorld world;

    @BeforeEach
    void setUp() {
        world = new PlayerWorld();
    }

    @AfterEach
    void tearDown() {
        world.shutdown();
    }

    @Test
    void storesBlocksAcrossPositiveAndNegativeChunks() throws Exception {
        Block positive = new Block(true, false, null);
        Block negative = new Block(false, true, null);

        world.updateBlock(3, 64, 7, positive).get(5, TimeUnit.SECONDS);
        world.updateBlock(-1, 64, -1, negative).get(5, TimeUnit.SECONDS);

        assertSame(positive, world.getBlock(3, 64, 7).get(5, TimeUnit.SECONDS));
        assertSame(negative, world.getBlock(-1, 64, -1).get(5, TimeUnit.SECONDS));
        assertSame(negative, world.getBlockNow(-1, 64, -1));
    }

    @Test
    void synchronousLookupSeesLatestBlockWithoutWaitingOnExecutor() throws Exception {
        Block block = new Block(true, false, null);

        world.updateBlock(-17, 32, -33, block).get(5, TimeUnit.SECONDS);

        assertSame(block, world.getBlockNow(-17, 32, -33));
    }

    @Test
    void unloadedBlocksAreNull() {
        assertNull(world.getBlockNow(0, 0, 0));
        assertNull(world.getBlockNow(-1, 0, -1));
    }
}
