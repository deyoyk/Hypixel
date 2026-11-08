package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.function.Function;

public class CustomDisplayNameComponent extends SkyBlockItemComponent {
    private final Function<SkyBlockItem, Text> displayNameProvider;

    public CustomDisplayNameComponent(Function<SkyBlockItem, Text> displayNameProvider) {
        this.displayNameProvider = displayNameProvider;
    }

    public Text getDisplayName(SkyBlockItem item) {
        return displayNameProvider.apply(item);
    }
}
