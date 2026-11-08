package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeFishingExpertiseKills extends ItemAttribute<Long> {
    @Override
    public String getKey() {
        return "fishing_expertise_kills";
    }

    @Override
    public Long getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 0L;
    }

    @Override
    public Long loadFromString(String string) {
        return Long.parseLong(string);
    }

    @Override
    public String saveIntoString() {
        return String.valueOf(getValue());
    }
}
