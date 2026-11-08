package com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public interface EventBasedEnchant {
    default ItemStatistics getStatisticsOnDamage(SkyBlockPlayer causer, LivingEntity receiver, int level) {
        return ItemStatistics.empty();
    }
}
