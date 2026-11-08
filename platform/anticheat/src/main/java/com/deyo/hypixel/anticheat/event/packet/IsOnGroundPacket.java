package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

@Getter
public class IsOnGroundPacket extends DeyoPacket {
    private final boolean onGround;

    public IsOnGroundPacket(DeyoPlayer player, boolean onGround) {
        super(player);
        this.onGround = onGround;
    }

}
