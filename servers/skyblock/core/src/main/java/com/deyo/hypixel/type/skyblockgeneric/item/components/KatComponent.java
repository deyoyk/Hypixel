package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class KatComponent extends SkyBlockItemComponent {
    @Getter
    private final int reducedDays;

    public KatComponent(int reducedDays) {
        this.reducedDays = reducedDays;
        addInheritedComponent(new TrackedUniqueComponent());
    }
}