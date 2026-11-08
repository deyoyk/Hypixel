package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class PowerStoneComponent extends SkyBlockItemComponent {
    public PowerStoneComponent() {
        addInheritedComponent(new ExtraRarityComponent("POWER STONE"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}