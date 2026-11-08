package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class PickaxeComponent extends SkyBlockItemComponent {
    public PickaxeComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.PICKAXE), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("PICKAXE"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}