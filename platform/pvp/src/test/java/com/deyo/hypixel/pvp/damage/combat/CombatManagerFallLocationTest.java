package com.deyo.hypixel.pvp.damage.combat;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CombatManagerFallLocationTest {

    @Test
    void fallingIntoWaterIsReportedWhenNotClimbing() {
        assertEquals("water", CombatManager.resolveFallLocation(null, true));
    }

    @Test
    void genericFallIsReportedWhenNotClimbingOrWet() {
        assertNull(CombatManager.resolveFallLocation(null, false));
    }

    @Test
    void climbableBlocksMapToTheirFallLocations() {
        assertEquals("ladder", CombatManager.resolveFallLocation(Block.LADDER, false));
        assertEquals("ladder", CombatManager.resolveFallLocation(Block.OAK_TRAPDOOR, false));
        assertEquals("vines", CombatManager.resolveFallLocation(Block.VINE, false));
        assertEquals("weeping_vines", CombatManager.resolveFallLocation(Block.WEEPING_VINES, false));
        assertEquals("weeping_vines", CombatManager.resolveFallLocation(Block.WEEPING_VINES_PLANT, false));
        assertEquals("twisting_vines", CombatManager.resolveFallLocation(Block.TWISTING_VINES, false));
        assertEquals("twisting_vines", CombatManager.resolveFallLocation(Block.TWISTING_VINES_PLANT, false));
        assertEquals("scaffolding", CombatManager.resolveFallLocation(Block.SCAFFOLDING, false));
        assertEquals("other_climbable", CombatManager.resolveFallLocation(Block.BAMBOO, false));
    }
}
