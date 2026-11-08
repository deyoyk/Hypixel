package com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abstr;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(SkyBlockItem instance);

    default ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        return ItemStatistics.empty();
    }

    default void onEvent(PetEvent event) {
    }
}