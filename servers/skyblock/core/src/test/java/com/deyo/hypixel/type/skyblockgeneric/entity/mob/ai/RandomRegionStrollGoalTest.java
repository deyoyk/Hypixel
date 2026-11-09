package com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNull;

class RandomRegionStrollGoalTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void nearbyPositionsAreComputedLazily() throws Exception {
        RandomRegionStrollGoal goal = new RandomRegionStrollGoal(
                new EntityCreature(EntityType.ZOMBIE), 2, RegionType.VILLAGE);

        Field field = RandomRegionStrollGoal.class.getDeclaredField("closePositions");
        field.setAccessible(true);

        assertNull(field.get(goal));
    }
}
