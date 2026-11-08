package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class DecorationHeadComponent extends SkyBlockItemComponent {
    public DecorationHeadComponent(String texture) {
        addInheritedComponent(new SkullHeadComponent((item) -> texture));
        addInheritedComponent(new PlaceableComponent("DECORATION"));
    }
}
