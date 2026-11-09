package com.deyo.hypixel.type.generic.utility;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathUtilityTest {

    private static InstanceContainer instance;

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkLoader(new ChunkLoader() {
            @Override
            public Chunk loadChunk(Instance instance, int chunkX, int chunkZ) {
                return new DynamicChunk(instance, chunkX, chunkZ);
            }

            @Override
            public void saveChunk(Chunk chunk) {
            }
        });
    }

    @Test
    void batchedScanFindsEveryRequestedBlockType() {
        instance.loadChunk(0, 0).join();
        instance.setBlock(0, 64, 0, Block.STONE);
        instance.setBlock(1, 64, 0, Block.DIRT);
        instance.setBlock(-1, 64, 0, Block.STONE);

        List<Pos> found = MathUtility.getNearbyBlocks(
                instance, new Pos(0, 64, 0), 1, List.of(Block.STONE, Block.DIRT));

        assertEquals(3, found.size());
    }

    @Test
    void integerRandomIsUniformlyInclusive() {
        boolean sawMinimum = false;
        boolean sawMaximum = false;

        for (int attempt = 0; attempt < 2000; attempt++) {
            int value = MathUtility.random(1, 3);
            assertTrue(value >= 1 && value <= 3);
            sawMinimum |= value == 1;
            sawMaximum |= value == 3;
        }

        assertTrue(sawMinimum);
        assertTrue(sawMaximum);
    }

    @Test
    void integerRandomWithOneValueReturnsIt() {
        assertEquals(5, MathUtility.random(5, 5));
    }
}
