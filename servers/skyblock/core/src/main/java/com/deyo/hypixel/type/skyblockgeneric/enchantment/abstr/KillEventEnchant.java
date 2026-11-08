package com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr;

import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public interface KillEventEnchant {
    default void onMobKilled(SkyBlockPlayer player, SkyBlockMob killedMob, int level) {
    }
}
