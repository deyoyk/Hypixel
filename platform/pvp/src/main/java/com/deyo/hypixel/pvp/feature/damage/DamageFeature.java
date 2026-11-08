package com.deyo.hypixel.pvp.feature.damage;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles entities being damaged.
 */
public interface DamageFeature extends CombatFeature {
	DamageFeature NO_OP = new DamageFeature() {};
}
