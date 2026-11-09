package com.deyo.hypixel.pvp.feature.effect;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.event.entity.EntityPotionRemoveEvent;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.pvp.feature.config.FeatureConfiguration;
import com.deyo.hypixel.pvp.potion.effect.CombatPotionEffects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class VanillaEffectFeatureTest {

    private VanillaEffectFeature feature;
    private EntityCreature entity;

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        CombatPotionEffects.registerAll();
    }

    @BeforeEach
    void setUp() {
        feature = new VanillaEffectFeature(new FeatureConfiguration());
        feature.initDependencies();
        entity = new EntityCreature(EntityType.ZOMBIE);
    }

    @Test
    void keepsLongerTrackedDurationWhenShorterPotionIsApplied() {
        addPotion(PotionEffect.SPEED, 200);

        addPotion(PotionEffect.SPEED, 100);

        assertEquals(200, duration(PotionEffect.SPEED));
    }

    @Test
    void extendsTrackedDurationWhenLongerPotionIsApplied() {
        addPotion(PotionEffect.SPEED, 100);

        addPotion(PotionEffect.SPEED, 300);

        assertEquals(300, duration(PotionEffect.SPEED));
    }

    @Test
    void infinitePotionDurationWinsOverFiniteDuration() {
        addPotion(PotionEffect.SPEED, 100);

        addPotion(PotionEffect.SPEED, Potion.INFINITE_DURATION);

        assertEquals(Integer.MAX_VALUE, duration(PotionEffect.SPEED));
    }

    @Test
    void removingPotionClearsTrackedDuration() {
        addPotion(PotionEffect.SPEED, 200);

        feature.onPotionRemove(new EntityPotionRemoveEvent(entity,
                new Potion(PotionEffect.SPEED, 0, 200)));

        Map<PotionEffect, Integer> durations = entity.getTag(VanillaEffectFeature.DURATION_LEFT);
        assertFalse(durations.containsKey(PotionEffect.SPEED));
        assertNull(entity.getTag(VanillaEffectFeature.DURATION_LEFT).get(PotionEffect.SLOWNESS));
    }

    private void addPotion(PotionEffect effect, int duration) {
        feature.onPotionAdd(new EntityPotionAddEvent(entity, new Potion(effect, 0, duration)));
    }

    private int duration(PotionEffect effect) {
        return entity.getTag(VanillaEffectFeature.DURATION_LEFT).get(effect);
    }
}
