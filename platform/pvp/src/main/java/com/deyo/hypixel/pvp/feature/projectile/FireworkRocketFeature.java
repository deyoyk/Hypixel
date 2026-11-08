package com.deyo.hypixel.pvp.feature.projectile;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles using fireworks.
 */
public interface FireworkRocketFeature extends CombatFeature {
    FireworkRocketFeature NO_OP = new FireworkRocketFeature() {
    };
}