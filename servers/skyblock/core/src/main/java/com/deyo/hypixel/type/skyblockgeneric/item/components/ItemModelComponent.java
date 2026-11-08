package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class ItemModelComponent extends SkyBlockItemComponent {
    @Getter
    private final String itemModel;

    public ItemModelComponent(String itemModel) {
        this.itemModel = itemModel;
    }
}
