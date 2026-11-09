package com.deyo.hypixel.type.skyblockgeneric.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkyBlockRegenConfigurationTest {

    @Test
    void stageTwoTasksAreRescheduledWithoutDuplicatingTheBlock() {
        SkyBlockRegenConfiguration.MiningTask task = SkyBlockRegenConfiguration.MiningTask.builder()
                .position(new Pos(0, 64, 0))
                .stage(2)
                .reviveBlock(Block.STONE)
                .initialReviveBlock(Block.DIRT)
                .build();

        SkyBlockRegenConfiguration.reschedule(task, 5_000L, 1_000L);

        assertEquals(1, task.getStage());
        assertEquals(6_000L, task.getRegenerationTime());
        assertSame(Block.STONE, task.getIntermediaryBlock());
        assertSame(Block.DIRT, task.getReviveBlock());
    }

    @Test
    void tasksAreKeyedByTheirBlockPosition() {
        TestConfiguration configuration = new TestConfiguration();
        Pos position = new Pos(1, 64, -3);
        SkyBlockRegenConfiguration.MiningTask first = SkyBlockRegenConfiguration.MiningTask.builder()
                .position(position)
                .stage(1)
                .initialReviveBlock(Block.DIRT)
                .build();
        SkyBlockRegenConfiguration.MiningTask second = SkyBlockRegenConfiguration.MiningTask.builder()
                .position(position)
                .stage(2)
                .initialReviveBlock(Block.DIRT)
                .build();

        configuration.putTask(first);
        configuration.putTask(second);

        assertEquals(1, configuration.activeMiningTasks.size());
        assertSame(second, configuration.findMiningTask(position).orElseThrow());
        assertEquals(Optional.of(Block.DIRT), configuration.getInitialBlockFromPos(position));

        configuration.removeExistingTask(position);

        assertTrue(configuration.findMiningTask(position).isEmpty());
    }

    private static final class TestConfiguration extends SkyBlockRegenConfiguration {
        @Override
        public SkyBlockRegenConfiguration.MiningTask handleStageOne(SkyBlockRegenConfiguration.MiningTask task, Pos brokenBlock) {
            return task;
        }

        @Override
        public SkyBlockRegenConfiguration.MiningTask handleStageTwo(SkyBlockRegenConfiguration.MiningTask task, Pos brokenBlock) {
            return task;
        }

        @Override
        public List<Material> getMineableBlocks(Instance instance, Point point) {
            return List.of();
        }

        @Override
        public long getRegenerationTime() {
            return 1_000L;
        }
    }
}
