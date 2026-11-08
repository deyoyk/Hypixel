package com.deyo.hypixel.pvp.feature.potion;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles drinking and throwing potions.
 */
public interface PotionFeature extends CombatFeature {
	PotionFeature NO_OP = new PotionFeature() {};
}
