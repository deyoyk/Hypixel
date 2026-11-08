package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeShardId extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "attribute_shard_id";
    }

    @Override
    public String getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return "none";
    }

    @Override
    public String loadFromString(String string) {
        return string == null || string.isBlank() ? "none" : string;
    }

    @Override
    public String saveIntoString() {
        return getValue() == null || getValue().isBlank() ? "none" : getValue();
    }
}
