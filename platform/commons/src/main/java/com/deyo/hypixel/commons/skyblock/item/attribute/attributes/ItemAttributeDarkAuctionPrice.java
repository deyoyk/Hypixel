package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeDarkAuctionPrice extends ItemAttribute<Long> {
    @Override
    public String getKey() {
        return "dark_auction_price";
    }

    @Override
    public Long getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 0L;
    }

    @Override
    public Long loadFromString(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public String saveIntoString() {
        return String.valueOf(getValue());
    }
}
