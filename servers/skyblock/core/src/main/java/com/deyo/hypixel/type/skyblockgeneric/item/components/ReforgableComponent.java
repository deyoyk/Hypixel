package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

@Getter
public class ReforgableComponent extends SkyBlockItemComponent {
    private final ReforgeType reforgeType;

    public ReforgableComponent(ReforgeType reforgeType) {
        this.reforgeType = reforgeType;
    }
}