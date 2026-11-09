package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.function.Function;

@Getter
public class ExtraRarityComponent extends SkyBlockItemComponent {
    private final Function<SkyBlockItem, Text> extraDisplay;

    public ExtraRarityComponent(String extraDisplay) {
        this(ignored -> Text.parseLenient(extraDisplay));
    }

    public ExtraRarityComponent(Function<SkyBlockItem, Text> extraDisplay) {
        this.extraDisplay = extraDisplay;
    }

    public Text getExtraRarityDisplay(SkyBlockItem item) {
        return extraDisplay.apply(item);
    }
}
