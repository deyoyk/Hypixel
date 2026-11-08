package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeMithrilInfusion extends ItemAttribute<Boolean> {
    @Override
    public String getKey() {
        return "mithril_infusion";
    }

    @Override
    public Boolean getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return false;
    }

    @Override
    public Boolean loadFromString(String string) {
        return Boolean.parseBoolean(string);
    }

    @Override
    public String saveIntoString() {
        return this.value.toString();
    }
}
