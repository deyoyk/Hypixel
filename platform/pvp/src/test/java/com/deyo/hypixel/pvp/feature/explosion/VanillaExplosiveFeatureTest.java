package com.deyo.hypixel.pvp.feature.explosion;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VanillaExplosiveFeatureTest {

    @Test
    void anchorChargesHandleMissingState() {
        assertEquals(0, VanillaExplosiveFeature.anchorCharges(Block.RESPAWN_ANCHOR));
        assertEquals(0, VanillaExplosiveFeature.anchorCharges(Block.STONE));
    }

    @Test
    void anchorChargesReadValidState() {
        assertEquals(2, VanillaExplosiveFeature.anchorCharges(
                Block.RESPAWN_ANCHOR.withProperty("charges", "2")));
    }
}
