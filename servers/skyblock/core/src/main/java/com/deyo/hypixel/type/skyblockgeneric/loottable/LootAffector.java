package com.deyo.hypixel.type.skyblockgeneric.loottable;

import net.minestom.server.entity.LivingEntity;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.impl.EnchantmentLooting;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.impl.EnchantmentLuck;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;

public enum LootAffector {

    MAGIC_FIND {
        @Override
        public double apply(SkyBlockPlayer player, double original, LivingEntity mob, ItemStatistics statistics) {
            return applyStatistic(original, statistics(player, mob, statistics).getOverall(ItemStatistic.MAGIC_FIND));
        }
    },

    PET_LUCK {
        @Override
        public double apply(SkyBlockPlayer player, double original, LivingEntity mob, ItemStatistics statistics) {
            return applyStatistic(original, statistics(player, mob, statistics).getOverall(ItemStatistic.PET_LUCK));
        }
    },

    ENCHANTMENT_LUCK {
        @Override
        public double apply(SkyBlockPlayer player, double original, LivingEntity mob, ItemStatistics statistics) {
            Map<Integer, Integer> enchants = player.getStatistics().getAllOfEnchants(EnchantmentType.LUCK, true);
            int total = 0;
            for (int level : enchants.keySet()) {
                total += EnchantmentLuck.MULTIPLIERS[level - 1] * enchants.get(level);
            }
            return applyStatistic(original, total);
        }
    },

    ENCHANTMENT_LOOTING {
        @Override
        public double apply(SkyBlockPlayer player, double original, LivingEntity mob, ItemStatistics statistics) {
            Map<Integer, Integer> enchants = player.getStatistics().getAllOfEnchants(EnchantmentType.LOOTING, true);
            int total = 0;
            for (int level : enchants.keySet()) {
                total += EnchantmentLooting.MULTIPLIERS[level - 1] * enchants.get(level);
            }
            return applyStatistic(original, total);
        }
    };

    public double apply(SkyBlockPlayer player, double original) {
        return apply(player, original, null, null);
    }

    public double apply(SkyBlockPlayer player, double original, LivingEntity mob) {
        return apply(player, original, mob, null);
    }

    public abstract double apply(SkyBlockPlayer player, double original, LivingEntity mob, ItemStatistics statistics);

    static double applyStatistic(double original, double statistic) {
        return original + (original * statistic * 0.01);
    }

    private static ItemStatistics statistics(SkyBlockPlayer player, LivingEntity mob, ItemStatistics statistics) {
        if (statistics != null) return statistics;
        return mob == null ? player.getStatistics().allStatistics() : player.getStatistics().allStatistics(player, mob);
    }
}
