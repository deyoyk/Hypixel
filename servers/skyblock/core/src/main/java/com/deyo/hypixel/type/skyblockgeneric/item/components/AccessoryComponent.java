package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class AccessoryComponent extends SkyBlockItemComponent {
    public AccessoryComponent() {
        addInheritedComponent(new ExtraRarityComponent("ACCESSORY"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
