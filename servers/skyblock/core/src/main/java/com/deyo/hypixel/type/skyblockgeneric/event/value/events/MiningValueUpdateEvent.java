package com.deyo.hypixel.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.event.value.ValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class MiningValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockItem item;

    public MiningValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockItem item) {
        super(player, value);

        this.item = item;
    }
}
