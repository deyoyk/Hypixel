package com.deyo.hypixel.type.skyblockgeneric.event.value.events;

import com.deyo.hypixel.type.skyblockgeneric.event.value.ValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class FallDamageValueUpdateEvent extends ValueUpdateEvent {
    public FallDamageValueUpdateEvent(SkyBlockPlayer player, Object value) {
        super(player, value);
    }
}
