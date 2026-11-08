package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeNewYearCakeYear extends ItemAttribute<Integer> {
    @Override
    public String getKey() {
        return "new-year-cake-year";
    }

    @Override
    public Integer getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return -1;
    }

    @Override
    public Integer loadFromString(String string) {
        return Integer.valueOf(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
