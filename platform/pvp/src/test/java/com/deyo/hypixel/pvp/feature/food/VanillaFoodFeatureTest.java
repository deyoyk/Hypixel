package com.deyo.hypixel.pvp.feature.food;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaFoodFeatureTest {

    @Test
    void removeEffectsCollectsMatchesBeforeRemovingThem() {
        List<TimedPotion> active = List.of(
                new TimedPotion(new Potion(PotionEffect.SPEED, 0, 100), 0),
                new TimedPotion(new Potion(PotionEffect.SLOWNESS, 0, 100), 0));

        assertEquals(List.of(PotionEffect.SPEED),
                VanillaFoodFeature.effectsToRemove(active, effect -> effect == PotionEffect.SPEED));
    }

    @Test
    void predicateMatchingNothingRemovesNothing() {
        assertTrue(VanillaFoodFeature.effectsToRemove(
                List.of(new TimedPotion(new Potion(PotionEffect.SPEED, 0, 100), 0)),
                effect -> false).isEmpty());
    }
}
