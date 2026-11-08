package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

@Getter
public class AxeComponent extends SkyBlockItemComponent {
    private final int axeStrength;

    /**
     * Create an axe component with specified strength.
     * @param axeStrength The axe's breaking speed (higher = faster). Used to calculate break time.
     */
    public AxeComponent(int axeStrength) {
        this.axeStrength = axeStrength;
        addInheritedComponent(new ExtraRarityComponent("AXE"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.AXES));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }

}
