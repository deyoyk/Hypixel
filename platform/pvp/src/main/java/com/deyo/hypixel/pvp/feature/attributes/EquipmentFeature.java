package com.deyo.hypixel.pvp.feature.attributes;

import com.deyo.hypixel.pvp.feature.CombatFeature;

/**
 * Combat feature which handles equipment changes (applies weapon and armor attributes).
 */
public interface EquipmentFeature extends CombatFeature {
	EquipmentFeature NO_OP = new EquipmentFeature() {};
}
