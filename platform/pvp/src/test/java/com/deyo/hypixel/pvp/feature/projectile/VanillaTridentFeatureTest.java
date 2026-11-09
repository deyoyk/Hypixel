package com.deyo.hypixel.pvp.feature.projectile;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaTridentFeatureTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void detectsWallAheadAtFeetHeight() {
        InstanceContainer instance = newInstance();
        instance.setBlock(1, 64, 0, Block.STONE);

        assertTrue(VanillaTridentFeature.isTouchingWall(
                instance, new Pos(0.75, 64, 0.5), new Vec(1, 0, 0), 1.8));
    }

    @Test
    void detectsWallAheadAtHeadHeight() {
        InstanceContainer instance = newInstance();
        instance.setBlock(1, 65, 0, Block.STONE);

        assertTrue(VanillaTridentFeature.isTouchingWall(
                instance, new Pos(0.75, 64, 0.5), new Vec(1, 0, 0), 1.8));
    }

    @Test
    void openAirAheadDoesNotCountAsWall() {
        InstanceContainer instance = newInstance();

        assertFalse(VanillaTridentFeature.isTouchingWall(
                instance, new Pos(0.5, 64, 0.5), new Vec(1, 0, 0), 1.8));
    }

    @Test
    void noHorizontalMovementDoesNotCountAsWall() {
        InstanceContainer instance = newInstance();
        instance.setBlock(1, 64, 0, Block.STONE);

        assertFalse(VanillaTridentFeature.isTouchingWall(
                instance, new Pos(0.75, 64, 0.5), new Vec(0, 0, 0), 1.8));
    }

    @Test
    void riptideTicksTolerateAMissingStartTag() {
        assertEquals(0, VanillaTridentFeature.riptideTicks(100, null));
        assertEquals(20, VanillaTridentFeature.riptideTicks(100, 80L));
        assertEquals(0, VanillaTridentFeature.riptideTicks(100, 120L));
    }

    private static InstanceContainer newInstance() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkLoader(new ChunkLoader() {
            @Override
            public Chunk loadChunk(Instance instance, int chunkX, int chunkZ) {
                return new DynamicChunk(instance, chunkX, chunkZ);
            }

            @Override
            public void saveChunk(Chunk chunk) {
            }
        });
        instance.loadChunk(0, 0).join();
        return instance;
    }
}
