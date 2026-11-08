package com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public interface DamageEventEnchant {
    default void onDamageDealt(SkyBlockPlayer player, LivingEntity target, double damageDealt, int level) {
    }
}

