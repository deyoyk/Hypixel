package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class EnchantableComponent extends SkyBlockItemComponent {
    private final List<EnchantItemGroups> enchantGroups;
    private final boolean showEnchantLores;

    public EnchantableComponent(List<EnchantItemGroups> enchants, boolean showEnchantLores) {
        this.enchantGroups = enchants;
        this.showEnchantLores = showEnchantLores;
    }

    public List<EnchantItemGroups> getEnchantItemGroups() {
        return enchantGroups;
    }

    public boolean showEnchantLores() {
        return showEnchantLores;
    }
}