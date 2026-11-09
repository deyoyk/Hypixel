package com.deyo.hypixel.type.skyblockgeneric.region.mining;

import com.deyo.hypixel.type.skyblockgeneric.region.mining.handler.InstantBreakHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class MineableBlockTest {

    @Test
    void farmingBlocksUseTheInstantBreakHandler() {
        List<MineableBlock> farmingBlocks = List.of(
                MineableBlock.WHEAT,
                MineableBlock.SUGAR_CANE,
                MineableBlock.CACTUS,
                MineableBlock.BROWN_MUSHROOM,
                MineableBlock.RED_MUSHROOM,
                MineableBlock.POTATO,
                MineableBlock.CARROT,
                MineableBlock.NETHER_WART,
                MineableBlock.PUMPKIN,
                MineableBlock.CARVED_PUMPKIN,
                MineableBlock.MELON
        );

        for (MineableBlock block : farmingBlocks) {
            assertSame(InstantBreakHandler.INSTANCE, block.getMiningHandler());
        }
    }
}
