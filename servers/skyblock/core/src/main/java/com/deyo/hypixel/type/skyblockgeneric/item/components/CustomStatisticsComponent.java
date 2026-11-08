package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.customstatistics.CustomStatisticsConfig;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.customstatistics.CustomStatisticsRegistry;

@Getter
public class CustomStatisticsComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public CustomStatisticsComponent(String handlerId) {
        this.handlerId = handlerId;
    }

    public CustomStatisticsConfig getConfig() {
        return CustomStatisticsRegistry.getHandler(handlerId);
    }
}
