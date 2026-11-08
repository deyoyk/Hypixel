package com.deyo.hypixel.pvp.feature.projectile;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles bow shooting.
 */
public interface BowFeature extends CombatFeature {
	BowFeature NO_OP = new BowFeature() {};
}
