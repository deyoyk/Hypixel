package com.deyo.hypixel.type.skyblockgeneric.region;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SkyBlockRegenConfigurationTest {

    private static final SkyBlockRegenConfiguration.RegenerationConfig DIORITE =
            new SkyBlockRegenConfiguration.RegenerationConfig(3, Block.POLISHED_DIORITE);
    private static final SkyBlockRegenConfiguration.RegenerationConfig ORIGINAL =
            new SkyBlockRegenConfiguration.RegenerationConfig(97, Block.GRAY_WOOL);

    @Test
    void weightedSelectionUsesHalfOpenRanges() {
        assertEquals(Block.POLISHED_DIORITE, SkyBlockRegenConfiguration.getRandomBlock(0, DIORITE, ORIGINAL));
        assertEquals(Block.POLISHED_DIORITE, SkyBlockRegenConfiguration.getRandomBlock(2, DIORITE, ORIGINAL));
        assertEquals(Block.GRAY_WOOL, SkyBlockRegenConfiguration.getRandomBlock(3, DIORITE, ORIGINAL));
        assertEquals(Block.GRAY_WOOL, SkyBlockRegenConfiguration.getRandomBlock(99, DIORITE, ORIGINAL));
    }

    @Test
    void zeroChanceEntriesAreSkipped() {
        var never = new SkyBlockRegenConfiguration.RegenerationConfig(0, Block.BEDROCK);
        var always = new SkyBlockRegenConfiguration.RegenerationConfig(5, Block.STONE);

        assertEquals(Block.STONE, SkyBlockRegenConfiguration.getRandomBlock(0, never, always));
        assertEquals(Block.STONE, SkyBlockRegenConfiguration.getRandomBlock(4, never, always));
    }

    @Test
    void rollsOutsideTheTotalChanceReturnNull() {
        assertNull(SkyBlockRegenConfiguration.getRandomBlock(100, DIORITE, ORIGINAL));
        assertNull(SkyBlockRegenConfiguration.getRandomBlock(0));
        assertNull(SkyBlockRegenConfiguration.getRandomBlock(0,
                new SkyBlockRegenConfiguration.RegenerationConfig(0, Block.BEDROCK)));
    }
}
