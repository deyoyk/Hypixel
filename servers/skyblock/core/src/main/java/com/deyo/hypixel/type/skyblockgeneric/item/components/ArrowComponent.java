package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class ArrowComponent extends SkyBlockItemComponent {
    public ArrowComponent() {
        addInheritedComponent(new ExtraRarityComponent("ARROW"));
    }
}
