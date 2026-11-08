package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.Rarity;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRarity extends ItemAttribute<Rarity> {
    @Override
    public String getKey() {
        return "rarity";
    }

    @Override
    public Rarity getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return Rarity.COMMON;
    }

    @Override
    public Rarity loadFromString(String string) {
        return Rarity.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.name();
    }
}
