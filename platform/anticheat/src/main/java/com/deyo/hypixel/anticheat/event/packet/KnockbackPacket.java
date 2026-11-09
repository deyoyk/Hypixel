package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.math.Vel;

import java.util.UUID;

@Getter
public class KnockbackPacket extends DeyoPacket {
    private final Vel velocity;

    public KnockbackPacket(UUID uuid, Vel velocity) {
        super(uuid);
        this.velocity = velocity;
    }
}
