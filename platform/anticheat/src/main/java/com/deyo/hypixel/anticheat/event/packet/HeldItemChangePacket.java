package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HeldItemChangePacket extends DeyoPacket {
    private final int slot;

    public HeldItemChangePacket(UUID uuid, int slot) {
        super(uuid);
        this.slot = slot;
    }
}
