package com.deyo.hypixel.pvp.feature.projectile;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles crossbow shooting.
 */
public interface CrossbowFeature extends CombatFeature {
	CrossbowFeature NO_OP = new CrossbowFeature() {};
}
