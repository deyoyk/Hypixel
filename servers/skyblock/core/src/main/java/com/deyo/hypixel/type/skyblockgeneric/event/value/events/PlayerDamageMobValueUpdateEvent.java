package com.deyo.hypixel.type.skyblockgeneric.event.value.events;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.event.value.ValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class PlayerDamageMobValueUpdateEvent extends ValueUpdateEvent {
    private final SkyBlockMob mob;

    public PlayerDamageMobValueUpdateEvent(SkyBlockPlayer player, Object value, SkyBlockMob mob) {
        super(player, value);
        this.mob = mob;
    }
}
