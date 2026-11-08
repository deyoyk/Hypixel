package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class DrillComponent extends SkyBlockItemComponent {
    public DrillComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.DRILL), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("DRILL"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}