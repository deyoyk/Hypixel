package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

@Getter
public class RequestPingPacket extends DeyoPacket {
    private final int requestId;

    public RequestPingPacket(DeyoPlayer player, int requestId) {
        super(player);

        this.requestId = requestId;
    }
}
