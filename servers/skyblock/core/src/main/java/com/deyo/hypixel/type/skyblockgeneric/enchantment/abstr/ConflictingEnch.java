package com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr;

import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;

import java.util.List;

public interface ConflictingEnch {
    List<EnchantmentType> getConflictingEnchantments();
}
