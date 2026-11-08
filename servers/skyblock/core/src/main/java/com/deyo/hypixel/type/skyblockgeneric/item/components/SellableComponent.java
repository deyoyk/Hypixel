package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class SellableComponent extends SkyBlockItemComponent {
    @Getter
    private final double sellValue;

    public SellableComponent(double sellValue) {
        this.sellValue = sellValue;
    }
}