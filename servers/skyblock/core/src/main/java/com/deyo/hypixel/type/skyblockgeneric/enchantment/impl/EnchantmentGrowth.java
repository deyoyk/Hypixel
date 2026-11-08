package com.deyo.hypixel.type.skyblockgeneric.enchantment.impl;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.collection.CustomCollectionAward;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.Ench;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.EnchFromTable;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentGrowth implements Ench, EnchFromTable {
    public static final int[] MULTIPLIERS = new int[]{15, 30, 45, 60, 75, 90, 105};

    @Override
    public String getDescription(int level) {
        return "Grants <a>+" + MULTIPLIERS[level - 1] + "<stat:health>";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                4, 36,
                5, 45,
                6, 91,
                7, 179
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.GROWTH_DISCOUNT)) {
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.ARMOR);
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        double increase = MULTIPLIERS[level - 1];
        return ItemStatistics.builder().withBase(ItemStatistic.HEALTH, increase).build();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30,
                4, 40,
                5, 50
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.GROWTH_DISCOUNT)) {
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 8;
    }
}
