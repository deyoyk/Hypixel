package com.deyo.hypixel.pvp.feature.projectile;

import com.deyo.hypixel.pvp.feature.config.FeatureConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VanillaBowFeatureTest {

    @Test
    void flameLastsFiveSeconds() {
        assertEquals(100, VanillaBowFeature.FLAME_FIRE_TICKS);
    }

    @Test
    void bowPowerFollowsVanillaChargeCurveAndCapsAtOne() {
        VanillaBowFeature feature = new VanillaBowFeature(new FeatureConfiguration());

        assertEquals(0.0, feature.getBowPower(0));
        assertEquals(1.0, feature.getBowPower(20), 1.0e-9);
        assertEquals(1.0, feature.getBowPower(100), 1.0e-9);
    }
}
