package com.deyo.hypixel.pvp.feature.damage;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaDamageFeatureTest {

    @Test
    void hurtPitchStaysWithinVanillaBounds() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int attempt = 0; attempt < 1000; attempt++) {
            float pitch = VanillaDamageFeature.randomHurtPitch(random);
            assertTrue(pitch >= 0.8f && pitch <= 1.2f);
        }
    }
}
