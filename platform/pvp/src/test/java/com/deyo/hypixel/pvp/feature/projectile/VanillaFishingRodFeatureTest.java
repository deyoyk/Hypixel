package com.deyo.hypixel.pvp.feature.projectile;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VanillaFishingRodFeatureTest {

    private static final double EPSILON = 1.0e-9;

    @Test
    void modernThrowFollowsLookDirectionAtVanillaSpeed() {
        Vec velocity = VanillaFishingRodFeature.calculateThrowVelocity(
                new Pos(0, 0, 0, 0, 0), true);

        assertEquals(0, velocity.x(), EPSILON);
        assertEquals(0, velocity.y(), EPSILON);
        assertEquals(0.6, velocity.z(), EPSILON);
    }

    @Test
    void lookingDownThrowsTheBobberDownwards() {
        Vec velocity = VanillaFishingRodFeature.calculateThrowVelocity(
                new Pos(0, 0, 0, 0, 90), true);

        assertEquals(0, velocity.x(), EPSILON);
        assertEquals(-0.6, velocity.y(), EPSILON);
        assertEquals(0, velocity.z(), EPSILON);
    }

    @Test
    void legacyThrowUsesLegacySpeed() {
        Vec velocity = VanillaFishingRodFeature.calculateThrowVelocity(
                new Pos(0, 0, 0, 0, 0), false);

        assertEquals(0, velocity.x(), EPSILON);
        assertEquals(0, velocity.y(), EPSILON);
        assertEquals(1.5, velocity.z(), EPSILON);
    }
}
