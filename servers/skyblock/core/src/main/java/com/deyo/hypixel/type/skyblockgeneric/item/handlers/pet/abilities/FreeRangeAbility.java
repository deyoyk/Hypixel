package com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abilities;

import com.deyo.hypixel.commons.skyblock.item.Rarity;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;

import static com.deyo.hypixel.commons.StringUtility.decimalify;

public final class FreeRangeAbility implements PetAbility {
    private static final RarityValue<Double> PER_LEVEL = new RarityValue<>(0.5, 0.75, 1.0, 1.0, 1.0, 1.0, 0.0);

    @Override
    public String getName() {
        return "Free Range";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double ff = PER_LEVEL.getForRarity(rarity) * level;

        return Arrays.asList(
                "<7>Grants <6>+" + decimalify(ff, 1) + "<stat:farming_fortune><7>while",
                "<7>on <b>Public Islands<7>."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        if (!isPublicIsland(player)) return ItemStatistics.empty();
        return farmingFortuneFor(pet);
    }

    private boolean isPublicIsland(SkyBlockPlayer player) {
        return player.getRegion() != null && player.getRegion().getType() != RegionType.PRIVATE_ISLAND;
    }

    private ItemStatistics farmingFortuneFor(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double ff = PER_LEVEL.getForRarity(rarity) * level;

        return ItemStatistics.builder()
                .withBase(ItemStatistic.FARMING_FORTUNE, ff)
                .build();
    }
}
