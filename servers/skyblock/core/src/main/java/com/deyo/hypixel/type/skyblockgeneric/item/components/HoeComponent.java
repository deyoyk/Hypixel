package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

/**
 * Component for hoe tools.
 * Placeholder for future farming speed bonuses.
 */
public class HoeComponent extends SkyBlockItemComponent {
    public HoeComponent() {
        addInheritedComponent(new ExtraRarityComponent("HOE"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.HOES));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
