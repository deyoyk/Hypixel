package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

@Getter
public class PingResponsePacket extends DeyoPacket {
    private final long requestId;

    public PingResponsePacket(DeyoPlayer player, long requestId) {
        super(player);

        this.requestId = requestId;
    }
}
