package com.deyo.hypixel.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.event.value.ValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class RegenerationValueUpdateEvent extends ValueUpdateEvent {
    public RegenerationValueUpdateEvent(SkyBlockPlayer player, Object value) {
        super(player, value);
    }
}
