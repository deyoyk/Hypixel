package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class ShovelComponent extends SkyBlockItemComponent {
    public ShovelComponent() {
        addInheritedComponent(new ExtraRarityComponent("SHOVEL"));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}