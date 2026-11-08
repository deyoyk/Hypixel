package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class BlockDataToolComponent extends SkyBlockItemComponent {

    public BlockDataToolComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }

}
