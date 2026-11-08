package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class ArmorComponent extends SkyBlockItemComponent {
    public ArmorComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
