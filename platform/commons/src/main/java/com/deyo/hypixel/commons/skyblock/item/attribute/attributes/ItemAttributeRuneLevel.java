package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeRuneLevel extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "rune_level";
    }

    @Override
    public Integer getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 1;
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public String saveIntoString() {
        return getValue().toString();
    }
}
