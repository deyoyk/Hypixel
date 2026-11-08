package com.deyo.hypixel.type.skyblockgeneric.enchantment.impl;

import net.minestom.server.entity.LivingEntity;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.collection.CustomCollectionAward;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.Ench;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.EnchFromTable;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.EventBasedEnchant;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentImpaling implements Ench, EnchFromTable, EventBasedEnchant {

    public static final double[] DAMAGE_MULTIPLIERS = new double[]{5.0, 10.0, 15.0, 20.0, 30.0};

    @Override
    public String getDescription(int level) {
        return "Increases damage dealt to " + MobType.AQUATIC.getFullDisplayName() + "<7> mobs by <a>" + DAMAGE_MULTIPLIERS[level - 1] + "%<7>.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                4, 25,
                5, 30
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.IMPALING_DISCOUNT)) {
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.SWORD, EnchantItemGroups.FISHING_WEAPON,
                EnchantItemGroups.GAUNTLET, EnchantItemGroups.LONG_SWORD,
                EnchantItemGroups.BOW, EnchantItemGroups.FISHING_ROD);
    }

    @Override
    public ItemStatistics getStatisticsOnDamage(SkyBlockPlayer causer, LivingEntity receiver, int level) {
        if (receiver instanceof SkyBlockMob skyBlockMob && skyBlockMob.getMobTypes().contains(MobType.AQUATIC)) {
            return ItemStatistics.builder().withBase(ItemStatistic.DAMAGE, DAMAGE_MULTIPLIERS[level - 1]).build();
        }

        return ItemStatistics.empty();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 10,
                2, 15,
                3, 20,
                4, 25,
                5, 30
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.IMPALING_DISCOUNT)) {
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 8;
    }
}