package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.deepcaverns;

import net.minestom.server.MinecraftServer;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.MeleeAttackWithinRegionGoal;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MobRedstonePigmanTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void meleeGoalTargetsPlayersInPigmensDen() {
        MobRedstonePigman mob = new MobRedstonePigman();

        MeleeAttackWithinRegionGoal goal = mob.getGoalSelectors().stream()
                .filter(MeleeAttackWithinRegionGoal.class::isInstance)
                .map(MeleeAttackWithinRegionGoal.class::cast)
                .findFirst()
                .orElseThrow();

        assertEquals(RegionType.PIGMENS_DEN, goal.getType());
    }
}
