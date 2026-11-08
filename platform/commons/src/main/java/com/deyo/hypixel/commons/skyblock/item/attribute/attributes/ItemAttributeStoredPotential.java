package com.deyo.hypixel.commons.skyblock.item.attribute.attributes;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeStoredPotential extends ItemAttribute<Integer> {
	@Override
	public String getKey() {
		return "stored_potential";
	}

	@Override
	public Integer getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
		return 0;
	}

	@Override
	public Integer loadFromString(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public String saveIntoString() {
		return String.valueOf(getValue());
	}

}